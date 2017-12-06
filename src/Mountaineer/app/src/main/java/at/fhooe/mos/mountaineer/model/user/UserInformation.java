package at.fhooe.mos.mountaineer.model.user;

/**
 * Created by stefan on 06.12.2017.
 */

public class UserInformation {
    private String id;
    private Gender gender;
    private int age;
    private int height;
    private int bodyMass;
    private int par;
    private int restingHearRate;

    public UserInformation(String id, Gender gender, int age, int height, int bodyMass, int par, int restingHearRate) {
        this.id = id;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.bodyMass = bodyMass;
        this.par = par;
        this.restingHearRate = restingHearRate;
    }

    public boolean isCompleteAndValid() {
        if (id == null || id.equals(""))
            return false;

        if (gender == Gender.NA)
            return false;

        if (age <= 0)
            return false;

        if (height <= 0)
            return false;

        if (bodyMass <= 0)
            return false;

        if (par < 0 || par > 10)
            return false;

        if (restingHearRate <= 0)
            return false;

        return true;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBodyMass() {
        return bodyMass;
    }

    public void setBodyMass(int bodyMass) {
        this.bodyMass = bodyMass;
    }

    public int getPar() {
        return par;
    }

    public void setPar(int par) {
        this.par = par;
    }

    public int getRestingHearRate() {
        return restingHearRate;
    }

    public void setRestingHearRate(int restingHearRate) {
        this.restingHearRate = restingHearRate;
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                "id='" + id + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                ", height=" + height +
                ", bodyMass=" + bodyMass +
                ", par=" + par +
                ", restingHearRate=" + restingHearRate +
                '}';
    }
}
