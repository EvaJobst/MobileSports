package at.fhooe.mos.app.mosproject.pedometer;

/**
 * Created by stefan on 10.11.2017.
 */

public class SensorEventData {
    public int type;  //TODO
    public int accuracy;
    public long timestamp;
    public float[] values;

    public SensorEventData(int type, int accuracy, long timestamp, float[] values) {
        this.type = type;
        this.accuracy = accuracy;
        this.timestamp = timestamp;
        this.values = values;
    }

    public int getType() {
        return type;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float[] getValues() {
        return values;
    }
}
