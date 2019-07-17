function lastFunc = test( varargin )
%UNTITLED この関数の概要をここに記述
%   詳細説明をここに記述
times = varargin{1};
symsFunc = varargin{2};
if(times ~= 0)
    for x=[1:times]
        symsFunc = diff(symsFunc, varargin{3});
        fprintf("%d階微分は\n",x);
        fprintf("%s",symsFunc);
        fprintf("\nであります。\n\n"); 
        lastFunc = symsFunc;
    end
else
        fprintf("%d階微分は\n",0);
        fprintf("%s",symsFunc);
        fprintf("\nであります。\n\n"); 
        lastFunc = symsFunc;
end
%varargin{2}
% nargin
