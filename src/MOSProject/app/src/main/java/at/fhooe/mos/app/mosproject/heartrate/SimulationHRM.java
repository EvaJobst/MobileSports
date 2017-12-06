package at.fhooe.mos.app.mosproject.heartrate;

import android.app.Activity;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Eva on 29.11.2017.
 */

public class SimulationHRM implements HRM {
    String fileName = "simulation_data/lichtenberg.txt";
    BufferedReader bufferedReader = null;

    @Override
    public int getHeartRate() {
        String line = "";
        try {
            while((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                return Integer.parseInt(values[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void initialize(Activity activity) {
        try {
            AssetManager assetManager = activity.getAssets();
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(fileName));
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}