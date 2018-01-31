package at.fhooe.mos.app.mosproject.model.user;

/**
 * Created by stefan on 06.12.2017.
 */

public class UserInformation {
    private Gender gender;
    private int age;
    private int height;
    private int bodyMass;
    private int par;
    private int strideLength;
    private int restingHeartRate;

    public UserInformation(Gender gender, int age, int height, int bodyMass, int par, int strideLength, int restingHeartRate) {
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.bodyMass = bodyMass;
        this.par = par;
        this.restingHeartRate = restingHeartRate;
        this.strideLength = strideLength;
    }

    public UserInformation() {
    }

    public boolean isCompleteAndValid() {
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

        if (strideLength <= 0 || strideLength > 200)
            return false;

        if (restingHeartRate <= 0)
            return false;

        return true;
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

    public int getStrideLength() {
        return strideLength;
    }

    public void setStrideLength(int strideLength) {
        this.strideLength = strideLength;
    }

    public int getRestingHeartRate() {
        return restingHeartRate;
    }

    public void setRestingHeartRate(int restingHeartRate) {
        this.restingHeartRate = restingHeartRate;
    }

    @Override
    public String toString() {
        return "UserInformation{" +
                " gender=" + gender +
                ", age=" + age +
                ", height=" + height +
                ", bodyMass=" + bodyMass +
                ", par=" + par +
                ", strideLength=" + strideLength +
                ", restingHeartRate=" + restingHeartRate +
                '}';
    }
}
