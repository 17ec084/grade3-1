            function [newX,newY,newN] = test(x1, y1, X, Y, n)%未完成
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