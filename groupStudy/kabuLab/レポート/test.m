function lastFunc = test( varargin )
%UNTITLED ���̊֐��̊T�v�������ɋL�q
%   �ڍא����������ɋL�q
times = varargin{1};
symsFunc = varargin{2};
if(times ~= 0)
    for x=[1:times]
        symsFunc = diff(symsFunc, varargin{3});
        fprintf("%d�K������\n",x);
        fprintf("%s",symsFunc);
        fprintf("\n�ł���܂��B\n\n"); 
        lastFunc = symsFunc;
    end
else
        fprintf("%d�K������\n",0);
        fprintf("%s",symsFunc);
        fprintf("\n�ł���܂��B\n\n"); 
        lastFunc = symsFunc;
end
%varargin{2}
% nargin
