package at.fhooe.mos.mountaineer.model.calculations;

import at.fhooe.mos.mountaineer.model.user.Gender;
import at.fhooe.mos.mountaineer.model.user.UserInformation;

/**
 * Created by stefan on 05.12.2017.
 */

public class EnergyExpenditureCalculator {

    public static double calculateEnergyExpenditureEstimation(UserInformation userInfo, double heartRateBpm) {
        double vo2max = calculateVO2MaxEstimation(userInfo.getAge(), userInfo.getGender(), userInfo.getHeight(), userInfo.getBodyMass(), userInfo.getPar());

        return calculateEnergyExpenditureEstimation(userInfo.getGender(), userInfo.getAge(), userInfo.getBodyMass(), vo2max, heartRateBpm);
    }

    public static double calculateEnergyExpenditureEstimation(Gender gender, int ageYears, int bodyMassKg, double vo2maxMlMinKg, double heartRateBpm) {
        if (gender == Gender.NA) {
            throw new IllegalArgumentException("gender must be female or male");
        }
        if (ageYears <= 0) {
            throw new IllegalArgumentException("ageYears must be greater than 0");
        }
        if (bodyMassKg <= 0) {
            throw new IllegalArgumentException("bodyMassKg must be greater than 0");
        }

        double energyExpenditure = -59.3954;

        if (gender == Gender.Male) {
            energyExpenditure += (-36.3781 + (0.271 * ageYears) + (0.394 * bodyMassKg) + (0.404 * vo2maxMlMinKg) + (0.634 * heartRateBpm));
        }

        if (gender == Gender.Female) {
            energyExpenditure += (0.274 * ageYears) + (0.103 * bodyMassKg) + (0.380 * vo2maxMlMinKg) + (0.450 * heartRateBpm);
        }

        return energyExpenditure;
    }

    public static double calculateVO2MaxEstimation(int ageYears, Gender gender, int heightCm, int bodyMassKg, int par) {
        if (ageYears <= 0) {
            throw new IllegalArgumentException("ageYears must be greater than 0");
        }
        if (gender == Gender.NA) {
            throw new IllegalArgumentException("gender must be female or male");
        }
        if (heightCm <= 0) {
            throw new IllegalArgumentException("heightCm must be greater than 0");
        }
        if (bodyMassKg <= 0) {
            throw new IllegalArgumentException("bodyMassKg must be greater than 0");
        }
        if (par < 0) {
            throw new IllegalArgumentException("par must be greater than or equal 0");
        }

        double heightM = (double) heightCm / 100.0;

        double vo2max = (0.133 * ageYears) - (0.005 * ageYears * ageYears) + (1.463 * par) + (9.17 * heightM) - (0.254 * bodyMassKg) + 34.143;

        if (gender == Gender.Male) {
            vo2max += 11.403;
        }

        return vo2max;  //Predicted VO2max  in [ml / min / kg]
    }

    public static void testCalculations() {
        double vo2max = calculateVO2MaxEstimation(37, Gender.Male, 177, 69, 7);

        if (Math.abs(vo2max - 52.5) > 0.1) {
            throw new RuntimeException("vo2max calculation is wrong!");
        }
    }
}
