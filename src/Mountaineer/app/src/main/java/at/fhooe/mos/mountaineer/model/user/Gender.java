package at.fhooe.mos.mountaineer.model.user;

/**
 * Created by stefan on 29.11.2017.
 */

public enum Gender {
    Male,
    Female,
    NA;

    public static Gender fromShortGenderString(String genderShort) {
        if (genderShort.equals("m"))
            return Male;
        if (genderShort.equals("f"))
            return Female;
        else
            return NA;
    }
}
