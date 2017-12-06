package at.fhooe.mos.mountaineer.model;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by stefan on 06.12.2017.
 */

public class TrimpSimulatedData {
    String fileName = "simulation_data/trimps.txt";
    BufferedReader bufferedReader = null;

    public TrimpSimulatedData(Context context) {
        openFile(context);
    }

    public ArrayList<Float> getTrimpData() {
        ArrayList<Float> data = new ArrayList<>();

        String line = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                String trimp = line.split("\t")[1];
                data.add(Float.parseFloat(trimp));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
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
}
