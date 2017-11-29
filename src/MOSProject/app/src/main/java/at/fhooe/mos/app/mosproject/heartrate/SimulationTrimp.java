package at.fhooe.mos.app.mosproject.heartrate;

import android.app.Activity;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Eva on 29.11.2017.
 */

public class SimulationTrimp implements Trimp {
    String fileName = "simulation_data/trimps.txt";
    BufferedReader bufferedReader = null;

    public void openFile(Activity activity) {
        try {
            AssetManager assetManager = activity.getAssets();
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(fileName));
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Float> getTrimpData() {
        ArrayList<Float> data = new ArrayList<>();

        String line = "";
        try {
            while((line = bufferedReader.readLine()) != null) {
                String trimp = line.split("\t")[1];
                data.add(Float.parseFloat(trimp));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }
}
