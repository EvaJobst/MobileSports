package at.fhooe.mos.app.mosproject.heartrate;

import java.util.ArrayList;

/**
 * Created by Eva on 30.11.2017.
 */

public class HRCalculation {
    public ArrayList<Float> getFatigue(ArrayList<Float> trimps) {
        ArrayList<Float> fatigue = new ArrayList<>();

        for(int i = 0; i < trimps.size(); i++) {
            if(fatigue.isEmpty()) {
                fatigue.add(trimps.get(i));
            }

            else {
                float value = (float) (fatigue.get(i-1) * Math.exp(-1/11) + trimps.get(i));
                fatigue.add(value);
            }
        }

        return fatigue;
    }

    public ArrayList<Float> getPerformance(ArrayList<Float> fitness, ArrayList<Float> fatigue, ArrayList<Float> trimps) {
        ArrayList<Float> performance = new ArrayList<>();

        for(int i = 0; i < trimps.size(); i++) {
            float value = fitness.get(i) - fatigue.get(i) * 2;
            performance.add(value);
        }

        return performance;
    }

    public ArrayList<Float> getFitness(ArrayList<Float> trimps) {
        ArrayList<Float> fitness = new ArrayList<>();

        for(int i = 0; i < trimps.size(); i++) {
            if(fitness.isEmpty()) {
                fitness.add(1000f);
            }

            else {
                float value = (float) (fitness.get(i-1) * Math.exp(-1/40) + trimps.get(i));
                fitness.add(value);
            }
        }

        return fitness;
    }

    public int getPercentHRmax(String maxHRInput, String ageInput, String currentMaxHR) {
        double hrMax;
        if(!maxHRInput.isEmpty()) {
            hrMax = Double.parseDouble(maxHRInput);
        }

        else if(!ageInput.isEmpty()) {
            String age = ageInput;
            hrMax = 208 - (0.7 * Double.parseDouble(age));
        }

        else {
            return 0;
        }

        double percent = (100 * Integer.parseInt(currentMaxHR)) / hrMax;
        return (int) percent;
    }

    public int getAverageHR(ArrayList<Integer> heartRateValues) {
        int sum = 0;
        for(int value : heartRateValues) {
            sum += value;
        }

        return sum/heartRateValues.size();
    }

    public float getTrimp(int duration, float currentHR, float restHR, float maxHR, float averageHR, char gender) {
        double b;

        if(gender == 'm') {
            b = 1.92;
        }

        else {
            b = 1.67;
        }

        double x = (currentHR - restHR) / (maxHR - restHR);
        double y = Math.exp(b*x);
        return (float) (duration * averageHR * y);
    }
}
