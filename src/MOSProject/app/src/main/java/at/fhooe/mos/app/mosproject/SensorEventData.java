package at.fhooe.mos.app.mosproject;

/**
 * Created by stefan on 10.11.2017.
 */

public class SensorEventData {
    private final int type;
    private final int accuracy;
    private final long timestamp;
    private final float[] values;

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
