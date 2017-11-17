package at.fhooe.mos.app.mosproject.ui;

import android.graphics.Color;

import com.github.mikephil.charting.data.LineDataSet;

/**
 * Created by Eva on 17.11.2017.
 */

public class ChartVisualization {
    public static LineDataSet chart(LineDataSet dataSet, int idx) {
        dataSet.setColor(getRGB(idx));
        dataSet.setDrawCircles(false);
        return dataSet;
    }

    public static LineDataSet details(LineDataSet dataSet, int idx) {
        if(idx == 7) {
            dataSet.setLineWidth(0);
            dataSet.setDrawCircles(true);
        }
        else {
            dataSet.setDrawCircles(false);
        }

        dataSet.setColor(getRGB(idx+3));
        return dataSet;
    }

    public static int getRGB(int idx) {
        int color;
        switch (idx) {
            case 0: color = Color.rgb(255, 0, 0); break;
            case 1: color = Color.rgb(0, 255, 0); break;
            case 2: color = Color.rgb(0, 0, 255); break;
            case 3: color = Color.rgb(255, 0, 0); break;
            case 4: color = Color.argb(100, 0, 255, 0); break;
            case 5: color = Color.argb(75, 0, 0, 255); break;
            case 6: color = Color.argb(75, 200, 100, 0); break;
            case 7: color = Color.rgb(255, 255, 255); break;
            default: color = Color.rgb(0, 0, 0);
        }

        return color;
    }
}
