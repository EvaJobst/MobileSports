%tdfread('FourSteps.txt', 'tab');

Acc_i = sqrt(Acc_X.^2 + Acc_Y.^2 + Acc_Z.^2 );

Acc_i = Acc_i(1:2500);

w = 15;
T1 = 2;
T2 = 1;

Acc_v = [];

for i=1+w:length(Acc_i)-w
    
    Acc_j = 1/(2*w+1) * sum(Acc_i(i-w:i+w));
    Acc_v(i) = 1/(2*w+1) * sum((Acc_i(i-w:i+w) - Acc_j).^2);
end  
    
Acc_v = Acc_v.';

Acc_v = sqrt(Acc_v);

B1 = [];
B2 = [];

for i=1:length(Acc_v)
    
    if Acc_v(i) > T1
        B1(i) = T1;
    else
        B1(i) = 0;
    end
    
    if Acc_v(i) < T2
        B2(i) = T2;
    else
        B2(i) = 0;
    end

end 

steps = [];
for i=1+1:length(Acc_v)-w
    
    if B1(i-1) > B1(i) && max(B2(i:i+w)) == T2
            steps(i)=0;
        
    else
        steps(i)=-10;
    end
end 

hold on
%plot(Acc_i, 'black');
plot(Acc_v, 'blue');

plot(B1, 'green');
plot(B2, 'magenta');
plot(steps, 'red*');

legend('\sigma_a', 'B1', 'B2', 'steps')
hold off
