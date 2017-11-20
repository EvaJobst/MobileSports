package at.fhooe.mos.app.mosproject.ui;

import android.graphics.Color;

import com.github.mikephil.charting.data.LineDataSet;

/**
 * Created by Eva on 17.11.2017.
 */

public class ChartVisualization {
    public static LineDataSet simulationData(LineDataSet dataSet, int idx) {
        dataSet.setDrawCircles(false);
        int color;

        switch(idx) {
            case 0: color = Color.rgb(255, 0, 0); break;
            case 1: color = Color.rgb(0, 255, 0); break;
            case 2: color = Color.rgb(0, 0, 255); break;
            default: color = Color.rgb(255, 255, 255);
        }

        dataSet.setColor(color);

        return dataSet;
    }

    public static LineDataSet resultsData(LineDataSet dataSet, int idx) {
        int color;

        if(idx == 4) {
            dataSet.setLineWidth(0);
            dataSet.setDrawCircles(true);
        }
        else {
            dataSet.setDrawCircles(false);
        }

        switch (idx) {
            case 0: color = Color.rgb(255, 0, 0); break;
            case 1: color = Color.argb(100, 0, 255, 0); break;
            case 2: color = Color.argb(75, 0, 0, 255); break;
            case 3: color = Color.argb(75, 200, 100, 0); break;
            case 4: color = Color.rgb(255, 255, 255); break;
            default: color = Color.rgb(255, 255, 255);
        }

        dataSet.setColor(color);

        return dataSet;
    }
}
