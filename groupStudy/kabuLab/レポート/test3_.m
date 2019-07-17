function test3_( symsFunc_x, xmin, xmax, freq, alpha, beta, figXmin, figXmax, figYmin, figYmax)
%UNTITLED2 この関数の概要をここに記述
%   詳細説明をここに記述
hold off;

fplot(0,'-','DisplayName','x軸');
hold on;
y=linspace(figYmin,figYmax,2);
x=[0 0];
plot(x,y,'-','DisplayName','y軸');
x=linspace(figXmin,figXmax,2);
y=tan(alpha).*x;
plot(x,y,'--','DisplayName',strcat(num2str(rad2deg(alpha)),'°(＝α)だけ回転したx軸(＝x′軸)'),'LineWidth',2);
y=tan(pi-alpha).*x;
plot(x,y,'--','DisplayName',strcat(num2str(rad2deg(alpha)),'°(＝α)だけ回転したy軸(＝y′軸)'));
y=-1/tan(alpha+beta).*x;
plot(x,y,'-.','DisplayName',strcat(num2str(rad2deg(alpha+beta)),'°(＝α+β)だけ回転したy軸(＝y′′軸)'),'LineWidth',2);






ht=matlabFunction(symsFunc_x);

i=[xmin:freq:xmax];


plot(i,ht(i),'DisplayName','元の関数のグラフ','LineWidth',2);


[theta,rho] = cart2pol(i,ht(i));%直交座標→極座標
theta=theta+alpha;
[x,y]=pol2cart(theta,rho);%極座標→直交座標

plot(x,y,'DisplayName',strcat(num2str(rad2deg(alpha)),'°(＝α)だけ回転したグラフ'));

xlim([figXmin figXmax]);
ylim([figYmin figYmax]);
%x=(x.*cos(beta)/-tan((pi/2)-beta))+x;
%y=y.*cos(beta);

%以下、歪ませる

kakudo=theta-alpha;
[n,m]=pol2cart(kakudo,rho);%極座標→直交座標
[atan_m_n,r] = cart2pol(n,m);

atan_m_n=atan_m_n+beta.*atan_m_n./(pi/2);
atan_m_n=atan_m_n+alpha;
[n,m]=pol2cart(atan_m_n,r);

plot(n, m,'DisplayName',strcat(num2str(rad2deg(alpha)),'°(＝α)だけ回転し、さらに',num2str(rad2deg(beta)),'°(＝β)だけ歪ませたグラフ'),'LineWidth',3);

legend('show','Location','southoutside');

%legend('元の関数',strcat(alpha,'だけ回転したグラフ'),strcat(alpha,'だけ回転し、さらに',beta,'だけ歪ませたグラフ'));

hold off;

end