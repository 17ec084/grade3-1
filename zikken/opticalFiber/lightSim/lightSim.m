function tmp = lightSim( roadImg, sourceImg, sourceCoord )
%LIGHTSIM 光路シミュレータ
%   3年情報通信工学実験「光ファイバ」の吟味課題
%   roadImgは白黒赤の3値から成る1000×1000pxの256色png画像。
%   白は空間、黒はガラス(レンズ)、赤は鏡を表す
%   sourceImgは光源のパターンを表す画像。256色png。
%   sourceCoordは光源の中心の座標

    [mirror, lens, fiber, fiberIn, screen,diffMirror, diffLens, diffFiber] = setRoad(roadImg);
    % 画像roadImgを読み込み、
    % ノイズをキャンセルし、
    % 鏡の形にあわせてlogical配列mirrorをtrueにする。それ以外はfalse。
    % 同様に
    % レンズ→lens
    % 光ファイバ(側面)→fiber
    % 光ファイバ(進入口)→fiberIn
    % スクリーン→screen
    % さらに、傾き情報はdiffMirrorやdiffLensやdiffFiberに記録する。
    
    source = setSource(sourceImg, sourceCoord, mirror, lens, fiber, fiberIn, screen);%未完成
    % 光源の座標をmirror、lens、...のそれと共通にする
    % 返却値sourceは1000×1000×3行列
    
    simulate(source, diffMirror, diffLens, diffFiber, 100);
    % シミュレーション
    % 光源の各点から鏡またはレンズの各点の方向へ光線を直進させる
    % 途中でレンズや鏡または光ファイバにぶつかったら、適切に振舞う。
    % 光路の屈折あるいは反射の最大回数は100とした。
    % 返却値:最後の引数が
    % 0のとき、光線を描画せず、実像を描いたもの
    % 1のとき、描画可能な光線を全て描画して実像を描いたもの                   
    % n(>=2)のとき、描画可能な光線をnつに1つの割合で選んで描画して実像を描いたもの 
    
    tmp=diffMirror;
    %dummy
end


    function [mirror, lens, fiber, diffMirror, diffLens, diffFiber] = setRoad(roadImg)
       road=imread(roadImg);
        [x,y,col] = size(road);
        if (x~=1000 || y~=1000 || col~=3)
            fprintf("error at lightSim(roadImg,,)\n");
            mirror=0;
            lens=0;
            fiber=0;
            diffMirror=0;
            diffLens=0;
            diffFiber=0;
            return
        end
        
        mirror(1000,1000)=0;
        lens(1000,1000)=0;
        fiber(1000,1000)=0;
        fiberIn(1000,1000)=0;
        screen(1000,1000)=0;
        for x=[1:1000]
            for y=[1:1000]
                if road(x,y,1)>=128
                    % 赤か白かマゼンタ
                    if road(x,y,2)>=128
                        % 白(空間)
                        mirror(x,y)=255;
                        lens(x,y)=255;
                        fiber(x,y)=255;
                        fiberIn(x,y)=255;
                        screen(x,y)=255;
                    else
                        % 赤かマゼンタ
                        if road(x,y,3)>=128
                            % マゼンタ(スクリーン)
                            mirror(x,y)=255;
                            lens(x,y)=255;
                            fiber(x,y)=255;
                            fiberIn(x,y)=255;
                            screen(x,y)=0;
                        else
                            % 赤(鏡)
                            mirror(x,y)=0;
                            lens(x,y)=255;
                            fiber(x,y)=255;
                            fiberIn(x,y)=255;
                            screen(x,y)=255;
                        end
                    end
                else
                    % 黒か青かシアン
                    if road(x,y,3)>=128
                        %青かシアン
                        if road(x,y,2)>=128
                            % シアン(光ファイバ進入口)
                            mirror(x,y)=255;
                            lens(x,y)=255;
                            fiber(x,y)=255;
                            fiberIn(x,y)=0;
                            screen(x,y)=255;
                        else
                            % 青(光ファイバ側面)
                            mirror(x,y)=255;
                            lens(x,y)=255;
                            fiber(x,y)=0;
                            fiberIn(x,y)=255;
                            screen(x,y)=255;
                        end

                    else
                        % 黒(レンズ)
                        mirror(x,y)=255;
                        lens(x,y)=0;
                        fiber(x,y)=255;
                        fiberIn(x,y)=255;
                        screen(x,y)=255;
                    end
                end
            end
        end
        %厳密な白黒赤青シマ6値に変換
    
        mirror=not(edge(mirror,'sobel'));
        lens=not(edge(lens,'sobel'));
        fiber=not(edge(fiber,'sobel'));
        % エッジ検出
        % (輪郭を黒(=0=false)、何もないところを白(=1=true)とするため、
        % 論理を反転させている)

        diffMirror=getDiff(mirror,5);
        diffLens=getDiff(lens,5);
        diffFiber=getDiff(fiber,5);
        % 傾き情報(周辺5座標から傾きを求める)

    
    end
    
        function diff2Dmatrix = getDiff(original2Dmatrix, sampleNum)
            [i,j]=size(original2Dmatrix);

            diff2Dmatrix=NaN(i,j);
            %返却用の配列の大きさをあらかじめ確保

            for x=[sampleNum:i]
                for y=[sampleNum:j]
                    if not(original2Dmatrix(x,y))
                    % 座標(x,y)の上に対象(鏡、レンズなど)の輪郭がある場合    
                        % 最小2乗法により、次のような点から回帰直線の傾きを求める。
                        % 中心(x,y)で半径sampleNumの円を含む最小の正方形内※
                        % かつ(x,y)に連続して対象の輪郭がのっている点 
                        % ※x座標はx-sampleNum+1～x+sampleNum-1
                        %   y座標はy-sampleNum+1～y+sampleNum-1

                        square=original2Dmatrix(x-sampleNum+1:x+sampleNum-1, y-sampleNum+1:y+sampleNum-1);
                                            
                        [sizeX,sizeY]=size(square);
                        X=zeros(1,sizeX*sizeY);
                        Y=zeros(1,sizeX*sizeY);
                        n=0;
                        for g=[1:sizeX]
                            for h=[1:sizeY]
                                if not(square(g,h))
                                    n=n+1;
                                    X(n)=g;
                                    Y(n)=h;
                                end
                            end
                        end

                        % square内で対象の輪郭の載る座標k(X(k),Y(k))を集めた。
                        % そのような座標の個数はnとなる。                        
                        
                        [X,Y,n]=getContinuousPoints(sampleNum, sampleNum, X, Y, n);
                        % 座標(sampleNum, sampleNum)から連続する、
                        % 輪郭の乗った座標、およびその個数nを取得

                        sigmaXX=0;
                        sigmaX=0;
                        sigmaY=0;
                        sigmaXY=0;
                        for k=[1:n]
                            sigmaXX=sigmaXX+(X(k))^2;
                            sigmaX=sigmaX+X(k);
                            sigmaY=sigmaY+Y(k);
                            sigmaXY=sigmaXY+(X(k))*(Y(k));
                        end
                        % 正規方程式に必要な変数をそろえた。
                        
                        bunshi=n*sigmaXY-sigmaX*sigmaY;
                        bunbo=(n*sigmaXX-(sigmaX^2));
                        a=bunshi/bunbo;
                        % 回帰直線の傾き
                        
                        a=-1/a;
                        % 縦と横を誤ってプログラムしてしまったため、補正 
                        
                        if (bunshi==0 && bunbo~=0)
                            a=inf;
                            % -infになっては困る(特別な意味を持たせるので)
                        end
                        
                        if (bunshi==0 && bunbo==0)
                            % a=0/0
                            % a0=0
                            % その条件を以下で求める。
                            % 左の0より、
                            % n*sigmaXX=(sigmaX*sigmaX)
                            % 右の0より、
                            % n*sigmaXY=(sigmaX*sigmaY)
                            % これらが同時に成り立つときである。
                            % sigmaXX=0の場合
                            %     sigmaX=0
                            %     つまり-sigma[～max-1]X=Xmax(明らかに>=0。全て負なのに総和が0にはならない)
                            %     一方仮定より-sigma[～max-1]XX=XXmax=Xmax*Xmax
                            %     2乗値の総和*-1が正にはならないので左辺は負か0
                            %     2乗値が負にはならないので右辺は正か0
                            %     よって、Xmax=0
                            %     帰納的に、Xall=0を示せる。
                            %     このとき、
                            %     n*sigmaXY=(sigmaX*sigmaY)
                            %     n*sigma0Y=0*sigmaY
                            %     0=0つまり必ず。
                            %     よって、Xが一定値かつXは必ず0
                            % sigmaXX≠0の場合
                            %     sigmaXY≠0の場合
                            %         n=(sigmaX*sigmaX)/simgaXX=(sigmaX*sigmaY)/sigmaXY
                            %         sigmaX≠0の場合
                            %             n=(sigmaX*sigmaX)/simgaXXかつ
                            %             sigmaY/sigmaX=sigmaXY/sigmaXXのとき
                            %             Xが一定値の時
                            %                 n=(nX*nX)/nXXつまり必ず。
                            %             Xが一定値ではない時
                            %                 (複雑すぎて表現できない)
                            %         sigmaX=0の場合
                            %             n=(sigmaX*sigmaX)/simgaXX=(sigmaX*sigmaY)/sigmaXY
                            %             はn=0となり、不適
                            %     sigmaXY=0の場合
                            %         n*sigmaXY=(sigmaX*sigmaY)より
                            %         sigmaX*sigmaY=0
                            %         つまり、
                            %         (sigmaX=0またはsigmaY=0)
                            %         かつ
                            %         n*sigmaXX=(sigmaX*sigmaX)
                            %         sigmaXX≠0より、sigmaX*sigmaX≠0
                            %         よってsigmaY=0かつn=(sigmaX*sigmaX)/sigmaXX
                            % 以上のことをまとめると、Xが一定値である場合は必ずa=0/0となるが、
                            % それ以外にもa=0/0となる場合がないといいきれない

                            isConstX=true;
                            for v=[2:n]
                                if X(v)~=X(1)
                                    isConstX=false;
                                    break
                                end                                                                
                            end
                            if isConstX
                                % Xが一定値の場合          
                                a=inf;
                                a=-1/a;
                            else
                                fprintf("最小2乗法に失敗しました。傾き行列に-Infを代入します。");
                            end
                            
                        end
                        diff2Dmatrix(x,y)=a;


%{
% 動作確認ツール    
if not(exist('f1'))
    f1=figure();
    f1Pos=[653  139  607  486];
    f1.Position=f1Pos;
end
stopCond=false;%pauseする条件
stopTime=0.2; % 次の画面へ切り替えるまでの時間[s]
figure(f1);
imshow(square) % 任意の輪郭近辺を表示
f1.Position=f1Pos;
if(stopCond)pause();end
pause(stopTime);
copiedSqu= false(9,9);
for xx=[1:9]
    for yy=[1:9]
        have=0;
        for ii=[1:n]
            if (X(ii)==xx && Y(ii)==yy)
                have=1;
            end
        end
        copiedSqu(xx, yy)=not(have);
    end
end
figure(f1);
imshow(copiedSqu) % 中心から連続した輪郭のみを抽出
f1.Position=f1Pos;

if(stopCond)pause();end
pause(stopTime);

if isinf(a)
    xxxx=[0,0];
    yyyy=[0,1];
else
    xxxx = 0:1;
    yyyy = a.*xxxx;
end
figure(f1);
plot(xxxx,yyyy) % 傾きのプレビュー
daspect([1 1 1]);
pause(0.2);
f1.Position=f1Pos;

if(stopCond)pause();end
pause(stopTime);
copiedMat=zeros(1000,1000,3);
copiedMat(:,:,1)=original2Dmatrix;
copiedMat(:,:,2)=original2Dmatrix;
copiedMat(:,:,3)=original2Dmatrix;
copiedMat=uint8(copiedMat.*255);
for xxx=[x-sampleNum+1:x+sampleNum-1]
    for yyy=[y-sampleNum+1:y+sampleNum-1]
        copiedMat(xxx,yyy,1)=1;
    end
end
figure(f1);
imshow(copiedMat) % squareの箇所をどの付近なのか表示
f1.Position=f1Pos;

if(stopCond)pause();end
pause(stopTime);
%}
                        
                    end
                    % なければ何もしない
                end
            end

        end
        
            function [newX,newY,newN] = getContinuousPoints(x1, y1, X, Y, n)
                continuingX(n)=0;
                continuingY(n)=0;
                continuingX(1)=x1;
                continuingY(1)=y1;
                j=0;
                l=1;
                I=(-1)^0.5;
                while j<=l
                    j=j+1;
                    for k=[j:l]
                        for m=[+1+I, +1, +1-I, -I, -1-I, -1, -1+I, +I] % 8方向試す
                            if (continuingX(k)+real(m)~=0 || continuingY(k)+imag(m)~=0)
                                if have(continuingX(k)+real(m), continuingY(k)+imag(m))
                                    if not(haveContinuing(continuingX(k)+real(m), continuingY(k)+imag(m)))
                                        l=l+1;
                                        continuingX(l)=continuingX(k)+real(m);
                                        continuingY(l)=continuingY(k)+imag(m);
                                    end
                                end
                            end
                        end 
                    end
                end
                newX=continuingX;
                newY=continuingY;
                newN=l;
                                
                function bool2=have(x,y)
                % 座標(x,y)の上に輪郭があるかを調べる
                    for i=[1:n]
                        if (X(i)==x && Y(i)==y)
                            bool2 = 1;
                            return
                        end
                    end
                    bool2 = 0;
                end
                
                function bool2=haveContinuing(x,y)
                % continuingに(x,y)があるかを調べる
                    for i=[1:l]
                        if (continuingX(i)==x && continuingY(i)==y)
                            bool2 = 1;
                            return
                        end
                    end
                    bool2 = 0;
                end
            end
           
    function source = setSource(sourceImg, sourceCoord, mirror, lens, fiber, fiberIn, screen)
        source(1000,1000,3)=0;    
        
        source=printSource(sourceImg, sourceCoord,source);
        % 光源を書き込む
        
        source=printMirror(mirror,source);
        % 鏡を書き込む
        
        source=printLens(lens,source);
        % レンズを書き込む

        source=printFiber(fiber,source);
        % 光ファイバ(側面)を書き込む  

        source=printFiberIn(fiberIn,source);
        % 光ファイバ(進入口)を書き込む      
        
        source=printScreen(screen,source);
        % スクリーンを書き込む
        
    end
    
        function source=printSource(sourceImg, sourceCoord, source)
            img=imread(sourceImg);
            [sx,sy]=size(img);
            cx=sourceCoord(1);
            cy=sourceCoord(2);
        
            for i=[1:sx]
                for j=[1:sy]
                    if not(isSpecialColor(i,j,img))
                        source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,1)=img(i,j,1);
                        source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,2)=img(i,j,2);
                        source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,3)=img(i,j,3);
                    else
                        if isColor(i,j,"red",img) || isColor(i,j,"magenta",img)
                            % 赤成分を254に
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,1)=254;
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,2)=img(i,j,2);
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,3)=img(i,j,3);
                        elseif isColor(i,j,"cyan",img) || isColor(i,j,"green",img)
                            % 緑成分を254に
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,1)=img(i,j,1);
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,2)=254;
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,3)=img(i,j,3); 
                        elseif isColor(i,j,"blue",img)
                        % 青成分を254に
                        source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,1)=img(i,j,1);
                        source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,2)=img(i,j,2);
                        source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,3)=254; 
                        else
                            %{
                            % 赤成分を1に...しなくていい(黒→空間とみなせばいいだけ)
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,1)=1;
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,2)=img(i,j,2);
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,3)=img(i,j,3);  
                            %}
                        end
                    end
                end
            end
        end
    
            function bool=isSpecialColor(i,j,img)
                bool=isColor(i,j,"blue",img) || isColor(i,j,"cyan",img) || isColor(i,j,"red",img) || isColor(i,j,"green",img) || isColor(i,j,"black",img) || isColor(i,j,"magenta",img);
            end
                function bool=isColor(i,j,color,img)
                    if color=="blue"
                        bool=img(i,j,1)==0 && img(i,j,2)==0 && img(i,j,3)==255;   
                    elseif color=="cyan"
                        bool=img(i,j,1)==0 && img(i,j,2)==255 && img(i,j,3)==255;
                    elseif color=="red"
                        bool=img(i,j,1)==255 && img(i,j,2)==0 && img(i,j,3)==0;
                    elseif color=="green"
                        bool=img(i,j,1)==0 && img(i,j,2)==255 && img(i,j,3)==0;
                    elseif color=="brack"
                        bool=img(i,j,1)==0 && img(i,j,2)==0 && img(i,j,3)==0;
                    elseif color=="magenta"
                        bool=img(i,j,1)==255 && img(i,j,2)==0 && img(i,j,3)==255;
                    end
                end

        function source=printMirror(mirror, source)
            [i,j]=size(mirror);
            for x=[1:i]
                for y=[1:j]
                    if not(mirror(x,y))
                        % 鏡の輪郭があった場合
                        if existSource(x,y,source)
                            %既に他のものがその座標においてあった場合
                            fprintf("error at printMirror(mirror, source): 同じ座標に2つ以上のものが重なっています。\n");
                            source=false;
                            return
                        end
                        source(x,y,1)=255;
                        source(x,y,2)=0;
                        source(x,y,3)=0;
                    end
                end
            end
        end
        
            function bool=existSource(x,y,source)
                bool=source(x,y,1)+source(x,y,2)+source(x,y,3);
            end
            
        function source=printLens(lens, source)
            [i,j]=size(lens);
            for x=[1:i]
                for y=[1:j]
                    if not(lens(x,y))
                        if existSource(x,y,source)
                            fprintf("error at printLens(lens, source): 同じ座標に2つ以上のものが重なっています。\n");
                            source=false;
                            return
                        end
                        source(x,y,1)=0;
                        source(x,y,2)=255;
                        source(x,y,3)=0;
                    end
                end
            end
        end
        
        function source=printFiber(fiber, source)
            [i,j]=size(fiber);
            for x=[1:i]
                for y=[1:j]
                    if not(fiber(x,y))
                        if existSource(x,y,source)
                            fprintf("error at printFiber(fiber, source): 同じ座標に2つ以上のものが重なっています。\n");
                            source=false;
                            return
                        end
                        source(x,y,1)=0;
                        source(x,y,2)=0;
                        source(x,y,3)=255;
                    end
                end
            end
        end        

        function source=printFiberIn(fiberIn, source)
            [i,j]=size(fiberIn);
            for x=[1:i]
                for y=[1:j]
                    if not(fiberIn(x,y))
                        if existSource(x,y,source)
                            fprintf("error at printFiberIn(fiberIn, source): 同じ座標に2つ以上のものが重なっています。\n");
                            source=false;
                            return
                        end
                        source(x,y,1)=0;
                        source(x,y,2)=255;
                        source(x,y,3)=255;
                    end
                end
            end
        end

        function source=printScreen(screen, source)
            [i,j]=size(screen);
            for x=[1:i]
                for y=[1:j]
                    if not(screen(x,y))
                        if existSource(x,y,source)
                            fprintf("error at printScreen(screen, source): 同じ座標に2つ以上のものが重なっています。\n");
                            source=false;
                            return
                        end
                        source(x,y,1)=255;
                        source(x,y,2)=0;
                        source(x,y,3)=255;
                    end
                end
            end
        end

    function simulate(world, diffMirror, diffLens, diffFiber, life)
        sx=0;
        sy=0;
        ox=0;
        oy=0;
        while sx~=-1
            [sx,sy,ox,oy]=select2points(sx, sy, ox, oy, world);
            if (ox==-1||ox==-2)
                while (ox==-1 || ox==-2)
                    [sx,sy,ox,oy]=select2points(sx, sy, ox, oy, world);
                    % sは光源の点、oは光源以外の点。これら2点を選ぶ
                    % oxやsxは正の整数の他、
                    % ox=0(初回)、
                    % ox=-1or-2(鏡、レンズ切り替え)、
                    % ox=-2(光源の点の切り替え)、
                    % sx=-1(終了)
                    % を返しうる。
                end
            end
            
            [nowX,nowY,life]=goFromSource(sx,sy,ox,oy,world,life);
            % まっすぐ進む(光源から。オブジェクトの方向へ)

            
        end
    end
    
        function [sx,sy,ox,oy]=select2points(lastSx, lastSy, lastOx, lastOy, world)
            if (lastOx==-2 || lastOx==0)
                % その光源の点に対して、光源以外の点がすべて処理された(-2)
                % あるいは今はじめた場合(0)
                [sx,sy]=selectSource(lastSx, lastSy, world);
                % 新たに光源を選ぶ
            end
            
            if (lastOx==-2 || lastOx==0)
                % その光源の点に対して、光源以外の点がすべて処理された(-2)
                % あるいは今はじめた場合(0)
                [ox,oy]=selectObject("mirror", lastOx, lastOy, world);
                % 「光源以外の点」の種類を鏡とする
            elseif lastOx==-1
                % その光源の点に対して、鏡の点がすべて処理された場合(-1)
                [ox,oy]=selectObject("lens", lastOx, lastOy, world);
                % レンズの点に対しても同じことを繰り返す
            else
                [ox,oy]=selectObject("same", lastOx, lastOy, world);
                % 直前が鏡なら鏡の点、レンズならレンズの点を探す
            end

        end
            function [X,Y]=selectSource(lastX, lastY, world)
                [i,j]=size(world);
                for x=[lastX,i]
                    for y=[1,j]
                        if (x==lastX && y<=lastY)
                            continue
                        end
                        if not(isSpecialColor(x,y,world))
                            % 座標(x,y)が光源なら
                            X=x;
                            Y=y;
                            return
                        end
                    end
                end    
                X=-1;
                Y=-1;
            end
            
            function [X,Y]=selectObject(type, lastX, lastY, world)
                [i,j]=size(world);
                for x=[lastX,i]
                    for y=[1,j]
                        if (x==lastX && y<=lastY)
                            continue
                        end
                        if (isCorrectColor())
                            % 座標(x,y)が鏡、レンズの内適切なほうなら
                            X=x;
                            Y=y;
                            return
                        end
                    end
                end 
                if isColor(x,y,"green",world)
                    X=-2;
                    Y=-2;
                else
                    X=-1;
                    Y=-1;
                end
                function bool=isCorrectColor()
                    if type=="mirror"
                        bool=isColor(x,y,"red",world);
                    elseif type=="lens"
                        bool=isColor(x,y,"green",world);
                    elseif type=="same"
                        if isColor(lastX,lastY,"red",world)
                            bool=isColor(x,y,"red",world);
                        elseif isColor(lastX,lastY,"green",world)
                            bool=isColor(x,y,"green",world);
                        end
                    end 
                end
            end
            
        function [nowPoint(1),nowPoint(2),life]=goFromSource(sx,sy,ox,oy,world,life)
            direction=(oy-sy)/(ox-sx);
            % 方向
            
            degree=(ox-sx)+(oy-sy)*(-1^0.5);
            degree=-1/degree;
            % 角度
            
            nowPoint=[sx,sy];
            % 現在の点
            
            [sizeX,sizeY,sizeC]=size(world);
                        
            while not(isTouch(nowPoint, world))
                % 何かにぶつからない間
                x=nowPoint(1);
                y=nowPoint(2);
                if sx~=ox
                    if sx<ox % sx→ox
                        nowPoint(1)=x+1;
                        % x方向にインクリメント
                    elseif ox<sx % ox←sx
                        nowPoint(1)=x+1;
                    end
                        nowPoint(2)=(direction-sx)*x+sy;
                        % y方向は誤差の累積を防ぐ              
                else
                    % 真上あるいは真下へ
                    if sy<oy % sy↑oy
                        nowPoint(2)=y+1;
                    else % oy↓sy
                        nowPoint(2)=y-1;
                    end
                end
                if (sizeX<nowPoint(1)||sizeY<nowPoint(2))
                    life=0;
                    break
                end
            end
            if life~=0
                % 何にぶつかったかによってlifeを変える
            end
            

        end
            
            function bool=isTouch(nowPoint, world)
                % worldの座標nowPointに何かあるか
                x=uint8(nowPoint(1));
                y=uint8(nowPoint(2));
                bool=existSource(x,y,world);
            end    