function test2( times, y, x ,varargin)
%UNTITLED2 この関数の概要をここに記述
%   詳細説明をここに記述
i=0;
if nargin == 3
    while true
        fplot(test(mod(i,times), y, x))
        pause();
        i=i+1;
    end
else
if nargin == 5
    while true
        fplot(test(mod(i,times), y, x),[varargin{1},varargin{2}])
        pause();
        i=i+1;
    end
else
    fprintf("error the number of arg for test2 is wrong\n");
end    
end

