fid=fopen('rr_data.txt');
tline = fgetl(fid);
tlines = [];
while ischar(tline)
    tlines = [tlines, str2num(tline)];
    tline = fgetl(fid);
end
fclose(fid);

hrv = tlines(600:4000);


r1=0;
r2=0;
i=1;
j=1;
dT = 200;
hrvResampled=[];

hrvResampled(j) = hrv(i);
j = j + 1;

for i=2:length(hrv)-1
    c = floor((hrv(i) - r2)/dT);
    hrvResampled(j:j+c-1) = hrv(i);
    j = j + c;
    
    r1 = hrv(i) - r2 - dT * c;
    r2 = dT - r1;
    hrvResampled(j) = (hrv(i)*r1 + hrv(i+1)*r2)/dT;
    
    j = j + 1;
end

%subplot(2,1,1);
%plot(hrv);
%subplot(2,1,2);
%plot(hrvResampled);

respirationRate=[];

N=256;
for i = 1:floor(length(hrvResampled)/N)-1

    X = fft(hrvResampled(N*i:N*i+N), N);
    plot((1:N/2-3)*((1000/dT)/N), abs((X(4:N/2))));

    from = floor(0.2/((1000/dT)/N));
    to = floor(0.7/((1000/dT)/N));
    [pk,MaxFreq] = findpeaks(abs((X(from:to))),'NPeaks',1,'SortStr','descend');

    MaxFreq=(from + MaxFreq)*((1000/dT)/N);
    
    respirationRate = [respirationRate, MaxFreq];
    
end

plot(respirationRate);