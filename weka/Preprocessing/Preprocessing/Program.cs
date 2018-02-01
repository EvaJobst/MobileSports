using System;
using System.Collections.Generic;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Preprocessing
{
    class Program
    {
        static void Main(string[] args)
        {
            NumberFormatInfo nfi = new NumberFormatInfo();
            nfi.NumberDecimalSeparator = ".";

            using (var reader = new StreamReader(@"WISDM_ar_v1.1_raw.txt"))
            using (var writer = new StreamWriter("WISDM_ar_v1.1.arff"))
            {
                writer.Write($"" +
                             $"@RELATION WISDM\r\n\r\n" +
                             $"@ATTRIBUTE xAvg \tREAL\r\n" +
                             $"@ATTRIBUTE yAvg \tREAL\r\n" +
                             $"@ATTRIBUTE zAvg \tREAL\r\n" +
                             $"@ATTRIBUTE xStd \tREAL\r\n" +
                             $"@ATTRIBUTE yStd \tREAL\r\n" +
                             $"@ATTRIBUTE zStd \tREAL\r\n" +
                             $"@ATTRIBUTE class \t{{Jogging,Walking,Upstairs,Downstairs,Sitting,Standing}}\r\n\r\n" +
                             $"@DATA\r\n");

                while (!reader.EndOfStream)
                {
                    String type = "x";
                    List<double> x = new List<double>();
                    List<double> y = new List<double>();
                    List<double> z = new List<double>();

                    for (int i = 0; i <= 10; i++)
                    {
                        if (reader.EndOfStream)
                            break;

                        var line = reader.ReadLine().Trim(';');
                        var values = line.Split(',');

                        if(values.Length < 6)
                            continue;

                        type = values[1];
                        x.Add(double.Parse(values[3].Replace(".", ",")));
                        y.Add(double.Parse(values[4].Replace(".", ",")));
                        z.Add(double.Parse(values[5].Replace(".", ",")));
                    }

                    var xAvg = x.Average();
                    var yAvg = y.Average();
                    var zAvg = z.Average();

                    var xStd = CalculateStdDev(x);
                    var yStd = CalculateStdDev(y);
                    var zStd = CalculateStdDev(z);

                    writer.Write(
                        xAvg.ToString(nfi) + "," + yAvg.ToString(nfi) + "," + zAvg.ToString(nfi) + "," + 
                        xStd.ToString(nfi) + "," + yStd.ToString(nfi) + "," + zStd.ToString(nfi) + "," + type + "\r\n");
                }
            }

        }

        private static double CalculateStdDev(IEnumerable<double> values)
        {
            double ret = 0;
            if (values.Count() > 0)
            {
                //Compute the Average      
                double avg = values.Average();
                //Perform the Sum of (value-avg)_2_2      
                double sum = values.Sum(d => Math.Pow(d - avg, 2));
                //Put it all together      
                ret = Math.Sqrt((sum) / (values.Count() - 1));
            }
            return ret;
        }
    }
}
