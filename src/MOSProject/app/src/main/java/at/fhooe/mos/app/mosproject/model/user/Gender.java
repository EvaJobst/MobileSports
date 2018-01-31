package at.fhooe.mos.app.mosproject.model.user;

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

    public static Gender fromLongGenderString(String genderLong){
        if (genderLong.equals("Male"))
            return Male;
        else if (genderLong.equals("Female"))
            return Female;
        else
            return NA;
    }
}
