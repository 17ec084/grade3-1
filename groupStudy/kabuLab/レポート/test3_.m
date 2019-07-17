function test3_( symsFunc_x, xmin, xmax, freq, alpha, beta, figXmin, figXmax, figYmin, figYmax)
%UNTITLED2 ���̊֐��̊T�v�������ɋL�q
%   �ڍא����������ɋL�q
hold off;

fplot(0,'-','DisplayName','x��');
hold on;
y=linspace(figYmin,figYmax,2);
x=[0 0];
plot(x,y,'-','DisplayName','y��');
x=linspace(figXmin,figXmax,2);
y=tan(alpha).*x;
plot(x,y,'--','DisplayName',strcat(num2str(rad2deg(alpha)),'��(����)������]����x��(��x����)'),'LineWidth',2);
y=tan(pi-alpha).*x;
plot(x,y,'--','DisplayName',strcat(num2str(rad2deg(alpha)),'��(����)������]����y��(��y����)'));
y=-1/tan(alpha+beta).*x;
plot(x,y,'-.','DisplayName',strcat(num2str(rad2deg(alpha+beta)),'��(����+��)������]����y��(��y������)'),'LineWidth',2);






ht=matlabFunction(symsFunc_x);

i=[xmin:freq:xmax];


plot(i,ht(i),'DisplayName','���̊֐��̃O���t','LineWidth',2);


[theta,rho] = cart2pol(i,ht(i));%�������W���ɍ��W
theta=theta+alpha;
[x,y]=pol2cart(theta,rho);%�ɍ��W���������W

plot(x,y,'DisplayName',strcat(num2str(rad2deg(alpha)),'��(����)������]�����O���t'));

xlim([figXmin figXmax]);
ylim([figYmin figYmax]);
%x=(x.*cos(beta)/-tan((pi/2)-beta))+x;
%y=y.*cos(beta);

%�ȉ��A�c�܂���

kakudo=theta-alpha;
[n,m]=pol2cart(kakudo,rho);%�ɍ��W���������W
[atan_m_n,r] = cart2pol(n,m);

atan_m_n=atan_m_n+beta.*atan_m_n./(pi/2);
atan_m_n=atan_m_n+alpha;
[n,m]=pol2cart(atan_m_n,r);

plot(n, m,'DisplayName',strcat(num2str(rad2deg(alpha)),'��(����)������]���A�����',num2str(rad2deg(beta)),'��(����)�����c�܂����O���t'),'LineWidth',3);

legend('show','Location','southoutside');

%legend('���̊֐�',strcat(alpha,'������]�����O���t'),strcat(alpha,'������]���A�����',beta,'�����c�܂����O���t'));

hold off;

end