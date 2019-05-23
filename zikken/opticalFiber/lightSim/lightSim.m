function tmp = lightSim( roadImg, sourceImg, sourceCoord )
%LIGHTSIM ���H�V�~�����[�^
%   3�N���ʐM�H�w�����u���t�@�C�o�v�̋ᖡ�ۑ�
%   roadImg�͔����Ԃ�3�l���琬��1000�~1000px��256�Fpng�摜�B
%   ���͋�ԁA���̓K���X(�����Y)�A�Ԃ͋���\��
%   sourceImg�͌����̃p�^�[����\���摜�B256�Fpng�B
%   sourceCoord�͌����̒��S�̍��W

    [mirror, lens, fiber, fiberIn, screen,diffMirror, diffLens, diffFiber] = setRoad(roadImg);
    % �摜roadImg��ǂݍ��݁A
    % �m�C�Y���L�����Z�����A
    % ���̌`�ɂ��킹��logical�z��mirror��true�ɂ���B����ȊO��false�B
    % ���l��
    % �����Y��lens
    % ���t�@�C�o(����)��fiber
    % ���t�@�C�o(�i����)��fiberIn
    % �X�N���[����screen
    % ����ɁA�X������diffMirror��diffLens��diffFiber�ɋL�^����B
    
    source = setSource(sourceImg, sourceCoord, mirror, lens, fiber, fiberIn, screen);%������
    % �����̍��W��mirror�Alens�A...�̂���Ƌ��ʂɂ���
    % �ԋp�lsource��1000�~1000�~3�s��
    
    simulate(source, diffMirror, diffLens, diffFiber, 100);
    % �V�~�����[�V����
    % �����̊e�_���狾�܂��̓����Y�̊e�_�̕����֌����𒼐i������
    % �r���Ń����Y�⋾�܂��͌��t�@�C�o�ɂԂ�������A�K�؂ɐU�����B
    % ���H�̋��܂��邢�͔��˂̍ő�񐔂�100�Ƃ����B
    % �ԋp�l:�Ō�̈�����
    % 0�̂Ƃ��A������`�悹���A������`��������
    % 1�̂Ƃ��A�`��\�Ȍ�����S�ĕ`�悵�Ď�����`��������                   
    % n(>=2)�̂Ƃ��A�`��\�Ȍ�����n��1�̊����őI��ŕ`�悵�Ď�����`�������� 
    
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
                    % �Ԃ������}�[���^
                    if road(x,y,2)>=128
                        % ��(���)
                        mirror(x,y)=255;
                        lens(x,y)=255;
                        fiber(x,y)=255;
                        fiberIn(x,y)=255;
                        screen(x,y)=255;
                    else
                        % �Ԃ��}�[���^
                        if road(x,y,3)>=128
                            % �}�[���^(�X�N���[��)
                            mirror(x,y)=255;
                            lens(x,y)=255;
                            fiber(x,y)=255;
                            fiberIn(x,y)=255;
                            screen(x,y)=0;
                        else
                            % ��(��)
                            mirror(x,y)=0;
                            lens(x,y)=255;
                            fiber(x,y)=255;
                            fiberIn(x,y)=255;
                            screen(x,y)=255;
                        end
                    end
                else
                    % �������V�A��
                    if road(x,y,3)>=128
                        %���V�A��
                        if road(x,y,2)>=128
                            % �V�A��(���t�@�C�o�i����)
                            mirror(x,y)=255;
                            lens(x,y)=255;
                            fiber(x,y)=255;
                            fiberIn(x,y)=0;
                            screen(x,y)=255;
                        else
                            % ��(���t�@�C�o����)
                            mirror(x,y)=255;
                            lens(x,y)=255;
                            fiber(x,y)=0;
                            fiberIn(x,y)=255;
                            screen(x,y)=255;
                        end

                    else
                        % ��(�����Y)
                        mirror(x,y)=255;
                        lens(x,y)=0;
                        fiber(x,y)=255;
                        fiberIn(x,y)=255;
                        screen(x,y)=255;
                    end
                end
            end
        end
        %�����Ȕ����ԐV�}6�l�ɕϊ�
    
        mirror=not(edge(mirror,'sobel'));
        lens=not(edge(lens,'sobel'));
        fiber=not(edge(fiber,'sobel'));
        % �G�b�W���o
        % (�֊s����(=0=false)�A�����Ȃ��Ƃ����(=1=true)�Ƃ��邽�߁A
        % �_���𔽓]�����Ă���)

        diffMirror=getDiff(mirror,5);
        diffLens=getDiff(lens,5);
        diffFiber=getDiff(fiber,5);
        % �X�����(����5���W����X�������߂�)

    
    end
    
        function diff2Dmatrix = getDiff(original2Dmatrix, sampleNum)
            [i,j]=size(original2Dmatrix);

            diff2Dmatrix=NaN(i,j);
            %�ԋp�p�̔z��̑傫�������炩���ߊm��

            for x=[sampleNum:i]
                for y=[sampleNum:j]
                    if not(original2Dmatrix(x,y))
                    % ���W(x,y)�̏�ɑΏ�(���A�����Y�Ȃ�)�̗֊s������ꍇ    
                        % �ŏ�2��@�ɂ��A���̂悤�ȓ_�����A�����̌X�������߂�B
                        % ���S(x,y)�Ŕ��asampleNum�̉~���܂ލŏ��̐����`����
                        % ����(x,y)�ɘA�����đΏۂ̗֊s���̂��Ă���_ 
                        % ��x���W��x-sampleNum+1�`x+sampleNum-1
                        %   y���W��y-sampleNum+1�`y+sampleNum-1

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

                        % square���őΏۂ̗֊s�̍ڂ���Wk(X(k),Y(k))���W�߂��B
                        % ���̂悤�ȍ��W�̌���n�ƂȂ�B                        
                        
                        [X,Y,n]=getContinuousPoints(sampleNum, sampleNum, X, Y, n);
                        % ���W(sampleNum, sampleNum)����A������A
                        % �֊s�̏�������W�A����т��̌�n���擾

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
                        % ���K�������ɕK�v�ȕϐ������낦���B
                        
                        bunshi=n*sigmaXY-sigmaX*sigmaY;
                        bunbo=(n*sigmaXX-(sigmaX^2));
                        a=bunshi/bunbo;
                        % ��A�����̌X��
                        
                        a=-1/a;
                        % �c�Ɖ�������ăv���O�������Ă��܂������߁A�␳ 
                        
                        if (bunshi==0 && bunbo~=0)
                            a=inf;
                            % -inf�ɂȂ��Ă͍���(���ʂȈӖ�����������̂�)
                        end
                        
                        if (bunshi==0 && bunbo==0)
                            % a=0/0
                            % a0=0
                            % ���̏������ȉ��ŋ��߂�B
                            % ����0���A
                            % n*sigmaXX=(sigmaX*sigmaX)
                            % �E��0���A
                            % n*sigmaXY=(sigmaX*sigmaY)
                            % ����炪�����ɐ��藧�Ƃ��ł���B
                            % sigmaXX=0�̏ꍇ
                            %     sigmaX=0
                            %     �܂�-sigma[�`max-1]X=Xmax(���炩��>=0�B�S�ĕ��Ȃ̂ɑ��a��0�ɂ͂Ȃ�Ȃ�)
                            %     ���������-sigma[�`max-1]XX=XXmax=Xmax*Xmax
                            %     2��l�̑��a*-1�����ɂ͂Ȃ�Ȃ��̂ō��ӂ͕���0
                            %     2��l�����ɂ͂Ȃ�Ȃ��̂ŉE�ӂ͐���0
                            %     ����āAXmax=0
                            %     �A�[�I�ɁAXall=0��������B
                            %     ���̂Ƃ��A
                            %     n*sigmaXY=(sigmaX*sigmaY)
                            %     n*sigma0Y=0*sigmaY
                            %     0=0�܂�K���B
                            %     ����āAX�����l����X�͕K��0
                            % sigmaXX��0�̏ꍇ
                            %     sigmaXY��0�̏ꍇ
                            %         n=(sigmaX*sigmaX)/simgaXX=(sigmaX*sigmaY)/sigmaXY
                            %         sigmaX��0�̏ꍇ
                            %             n=(sigmaX*sigmaX)/simgaXX����
                            %             sigmaY/sigmaX=sigmaXY/sigmaXX�̂Ƃ�
                            %             X�����l�̎�
                            %                 n=(nX*nX)/nXX�܂�K���B
                            %             X�����l�ł͂Ȃ���
                            %                 (���G�����ĕ\���ł��Ȃ�)
                            %         sigmaX=0�̏ꍇ
                            %             n=(sigmaX*sigmaX)/simgaXX=(sigmaX*sigmaY)/sigmaXY
                            %             ��n=0�ƂȂ�A�s�K
                            %     sigmaXY=0�̏ꍇ
                            %         n*sigmaXY=(sigmaX*sigmaY)���
                            %         sigmaX*sigmaY=0
                            %         �܂�A
                            %         (sigmaX=0�܂���sigmaY=0)
                            %         ����
                            %         n*sigmaXX=(sigmaX*sigmaX)
                            %         sigmaXX��0���AsigmaX*sigmaX��0
                            %         �����sigmaY=0����n=(sigmaX*sigmaX)/sigmaXX
                            % �ȏ�̂��Ƃ��܂Ƃ߂�ƁAX�����l�ł���ꍇ�͕K��a=0/0�ƂȂ邪�A
                            % ����ȊO�ɂ�a=0/0�ƂȂ�ꍇ���Ȃ��Ƃ�������Ȃ�

                            isConstX=true;
                            for v=[2:n]
                                if X(v)~=X(1)
                                    isConstX=false;
                                    break
                                end                                                                
                            end
                            if isConstX
                                % X�����l�̏ꍇ          
                                a=inf;
                                a=-1/a;
                            else
                                fprintf("�ŏ�2��@�Ɏ��s���܂����B�X���s���-Inf�������܂��B");
                            end
                            
                        end
                        diff2Dmatrix(x,y)=a;


%{
% ����m�F�c�[��    
if not(exist('f1'))
    f1=figure();
    f1Pos=[653  139  607  486];
    f1.Position=f1Pos;
end
stopCond=false;%pause�������
stopTime=0.2; % ���̉�ʂ֐؂�ւ���܂ł̎���[s]
figure(f1);
imshow(square) % �C�ӂ̗֊s�ߕӂ�\��
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
imshow(copiedSqu) % ���S����A�������֊s�݂̂𒊏o
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
plot(xxxx,yyyy) % �X���̃v���r���[
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
imshow(copiedMat) % square�̉ӏ����ǂ̕t�߂Ȃ̂��\��
f1.Position=f1Pos;

if(stopCond)pause();end
pause(stopTime);
%}
                        
                    end
                    % �Ȃ���Ή������Ȃ�
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
                        for m=[+1+I, +1, +1-I, -I, -1-I, -1, -1+I, +I] % 8��������
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
                % ���W(x,y)�̏�ɗ֊s�����邩�𒲂ׂ�
                    for i=[1:n]
                        if (X(i)==x && Y(i)==y)
                            bool2 = 1;
                            return
                        end
                    end
                    bool2 = 0;
                end
                
                function bool2=haveContinuing(x,y)
                % continuing��(x,y)�����邩�𒲂ׂ�
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
        % ��������������
        
        source=printMirror(mirror,source);
        % ������������
        
        source=printLens(lens,source);
        % �����Y����������

        source=printFiber(fiber,source);
        % ���t�@�C�o(����)����������  

        source=printFiberIn(fiberIn,source);
        % ���t�@�C�o(�i����)����������      
        
        source=printScreen(screen,source);
        % �X�N���[������������
        
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
                            % �Ԑ�����254��
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,1)=254;
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,2)=img(i,j,2);
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,3)=img(i,j,3);
                        elseif isColor(i,j,"cyan",img) || isColor(i,j,"green",img)
                            % �ΐ�����254��
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,1)=img(i,j,1);
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,2)=254;
                            source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,3)=img(i,j,3); 
                        elseif isColor(i,j,"blue",img)
                        % ������254��
                        source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,1)=img(i,j,1);
                        source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,2)=img(i,j,2);
                        source(cx-((sx+1)/2)+i, cy-((sy+1)/2)+j,3)=254; 
                        else
                            %{
                            % �Ԑ�����1��...���Ȃ��Ă���(������ԂƂ݂Ȃ��΂�������)
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
                        % ���̗֊s���������ꍇ
                        if existSource(x,y,source)
                            %���ɑ��̂��̂����̍��W�ɂ����Ă������ꍇ
                            fprintf("error at printMirror(mirror, source): �������W��2�ȏ�̂��̂��d�Ȃ��Ă��܂��B\n");
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
                            fprintf("error at printLens(lens, source): �������W��2�ȏ�̂��̂��d�Ȃ��Ă��܂��B\n");
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
                            fprintf("error at printFiber(fiber, source): �������W��2�ȏ�̂��̂��d�Ȃ��Ă��܂��B\n");
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
                            fprintf("error at printFiberIn(fiberIn, source): �������W��2�ȏ�̂��̂��d�Ȃ��Ă��܂��B\n");
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
                            fprintf("error at printScreen(screen, source): �������W��2�ȏ�̂��̂��d�Ȃ��Ă��܂��B\n");
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
                    % s�͌����̓_�Ao�͌����ȊO�̓_�B�����2�_��I��
                    % ox��sx�͐��̐����̑��A
                    % ox=0(����)�A
                    % ox=-1or-2(���A�����Y�؂�ւ�)�A
                    % ox=-2(�����̓_�̐؂�ւ�)�A
                    % sx=-1(�I��)
                    % ��Ԃ�����B
                end
            end
            
            [nowX,nowY,life]=goFromSource(sx,sy,ox,oy,world,life);
            % �܂������i��(��������B�I�u�W�F�N�g�̕�����)

            
        end
    end
    
        function [sx,sy,ox,oy]=select2points(lastSx, lastSy, lastOx, lastOy, world)
            if (lastOx==-2 || lastOx==0)
                % ���̌����̓_�ɑ΂��āA�����ȊO�̓_�����ׂď������ꂽ(-2)
                % ���邢�͍��͂��߂��ꍇ(0)
                [sx,sy]=selectSource(lastSx, lastSy, world);
                % �V���Ɍ�����I��
            end
            
            if (lastOx==-2 || lastOx==0)
                % ���̌����̓_�ɑ΂��āA�����ȊO�̓_�����ׂď������ꂽ(-2)
                % ���邢�͍��͂��߂��ꍇ(0)
                [ox,oy]=selectObject("mirror", lastOx, lastOy, world);
                % �u�����ȊO�̓_�v�̎�ނ����Ƃ���
            elseif lastOx==-1
                % ���̌����̓_�ɑ΂��āA���̓_�����ׂď������ꂽ�ꍇ(-1)
                [ox,oy]=selectObject("lens", lastOx, lastOy, world);
                % �����Y�̓_�ɑ΂��Ă��������Ƃ��J��Ԃ�
            else
                [ox,oy]=selectObject("same", lastOx, lastOy, world);
                % ���O�����Ȃ狾�̓_�A�����Y�Ȃ烌���Y�̓_��T��
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
                            % ���W(x,y)�������Ȃ�
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
                            % ���W(x,y)�����A�����Y�̓��K�؂Ȃق��Ȃ�
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
            % ����
            
            degree=(ox-sx)+(oy-sy)*(-1^0.5);
            degree=-1/degree;
            % �p�x
            
            nowPoint=[sx,sy];
            % ���݂̓_
            
            [sizeX,sizeY,sizeC]=size(world);
                        
            while not(isTouch(nowPoint, world))
                % �����ɂԂ���Ȃ���
                x=nowPoint(1);
                y=nowPoint(2);
                if sx~=ox
                    if sx<ox % sx��ox
                        nowPoint(1)=x+1;
                        % x�����ɃC���N�������g
                    elseif ox<sx % ox��sx
                        nowPoint(1)=x+1;
                    end
                        nowPoint(2)=(direction-sx)*x+sy;
                        % y�����͌덷�̗ݐς�h��              
                else
                    % �^�゠�邢�͐^����
                    if sy<oy % sy��oy
                        nowPoint(2)=y+1;
                    else % oy��sy
                        nowPoint(2)=y-1;
                    end
                end
                if (sizeX<nowPoint(1)||sizeY<nowPoint(2))
                    life=0;
                    break
                end
            end
            if life~=0
                % ���ɂԂ��������ɂ����life��ς���
            end
            

        end
            
            function bool=isTouch(nowPoint, world)
                % world�̍��WnowPoint�ɉ������邩
                x=uint8(nowPoint(1));
                y=uint8(nowPoint(2));
                bool=existSource(x,y,world);
            end    