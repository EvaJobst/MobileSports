package at.fhooe.mos.app.mosproject.pedometer;

import java.util.Calendar;

import at.fhooe.mos.app.mosproject.model.user.Gender;

/**
 * Created by Eva on 30.01.2018.
 */

public class StepCalculation {
    public static double getDistanceInMeter(int strideLength, int stepCount) {
        int strideDistance = stepCount * strideLength;
        return strideDistance / 100; // cm -> m
    }

    public static double getSpeedInKmH(long millis, int strideLength, int stepCount) {;
        double strideDistance = stepCount * (strideLength / 100);
        double seconds = millis / 1000;
        double speedFromSteps = (strideDistance / seconds);
        return speedFromSteps * 3.6; // m/s -> km/h
    }

    public static double getCadenceInMin(int stepCount, long millis) {
        double seconds = millis / 1000;
        double cadence =  stepCount / seconds;
        return cadence * 60;
    }

    public static double getKcalFromWeight(int weight, int strideLength, int stepCount) {
        int strideDistance = stepCount * (strideLength / 100);
        double energyExpenditureFromSteps = weight * 9.81 * strideDistance;
        return energyExpenditureFromSteps * 0.00023885;
    }

    public static int getStrideLengthInCm(Gender gender, int age, int height, int weight) {
        if(gender.equals(Gender.Female)) {
            return (int)(-0.001 * age + 1.058 * height - 0.002 * weight - 0.129) * 100;
        }

        else  { // Male
            return (int)(-0.002 * age + 0.760 * height - Math.pow(0.001, weight) + 0.327) * 100;
        }
    }
}
