package at.fhooe.mos.mountaineer.sensors.heartrate;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import at.fhooe.mos.mountaineer.EventSource;

/**
 * Created by stefan on 06.12.2017.
 */

public class SimulatedHeartRateSensor extends EventSource<HeartRateSensorEventListener> implements HeartRateSensor {
    private static final String fileName = "simulation_data/lichtenberg.txt";
    private BufferedReader bufferedReader;

    private Handler handler;
    private PeriodicRunnable periodicRunnable;
    private int nextRunInMs;

    @Override
    public void setup(Context context) {
        openFile(context);

        handler = new Handler();
        periodicRunnable = new PeriodicRunnable();
        nextRunInMs = 10_000;

        handler.postDelayed(periodicRunnable, nextRunInMs);
    }

    @Override
    public void destroy() {
        handler.removeCallbacks(periodicRunnable);

        try {
            if (bufferedReader != null)
                bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFile(Context context) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(fileName));
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getHeartRate() {
        String line = "";

        if (bufferedReader == null)
            return 0;

        try {
            if ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                return Integer.parseInt(values[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private class PeriodicRunnable implements Runnable {

        @Override
        public void run() {
            for (HeartRateSensorEventListener listener : getEventListeners()) {
                listener.onHeatRateEvent(getHeartRate());
            }

            handler.postDelayed(this, nextRunInMs);
        }
    }
}
