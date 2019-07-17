function test3( symsFunc_x, xmin, xmax, freq, alpha, beta, figXmin, figXmax, figYmin, figYmax)
%UNTITLED2 この関数の概要をここに記述
%   詳細説明をここに記述

ht=matlabFunction(symsFunc_x);

i=[xmin:freq:xmax];


plot(i,ht(i));
hold on;

[theta,rho] = cart2pol(i,ht(i));%直交座標→極座標
theta=theta+alpha;
[x,y]=pol2cart(theta,rho);%極座標→直交座標

plot(x,y);

xlim([figXmin figXmax]);
ylim([figYmin figYmax]);
x=(x.*cos(beta)/-tan((pi/2)-beta))+x;
y=y.*cos(beta);
%[theta,rho] = cart2pol(x,y);%直交座標→極座標
%theta=theta-beta;
%[x,y]=pol2cart(theta,rho);%極座標→直交座標
plot(x, y);
hold off;
xlim([figXmin figXmax]);
ylim([figYmin figYmax]);
end