function test3( symsFunc_x, xmin, xmax, freq, alpha, beta, figXmin, figXmax, figYmin, figYmax)
%UNTITLED2 ���̊֐��̊T�v�������ɋL�q
%   �ڍא����������ɋL�q

ht=matlabFunction(symsFunc_x);

i=[xmin:freq:xmax];


plot(i,ht(i));
hold on;

[theta,rho] = cart2pol(i,ht(i));%�������W���ɍ��W
theta=theta+alpha;
[x,y]=pol2cart(theta,rho);%�ɍ��W���������W

plot(x,y);

xlim([figXmin figXmax]);
ylim([figYmin figYmax]);
x=(x.*cos(beta)/-tan((pi/2)-beta))+x;
y=y.*cos(beta);
%[theta,rho] = cart2pol(x,y);%�������W���ɍ��W
%theta=theta-beta;
%[x,y]=pol2cart(theta,rho);%�ɍ��W���������W
plot(x, y);
hold off;
xlim([figXmin figXmax]);
ylim([figYmin figYmax]);
end