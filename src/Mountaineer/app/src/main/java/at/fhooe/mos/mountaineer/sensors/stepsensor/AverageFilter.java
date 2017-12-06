package at.fhooe.mos.mountaineer.sensors.stepsensor;

/**
 * Created by stefan on 11.11.2017.
 */

public class AverageFilter {

    private int amount;
    private float[] filterBuffer;

    public AverageFilter(int amount) {
        this.amount = amount;
        this.filterBuffer = new float[amount];
    }

    public float filter(float data) {
        float filteredData = 0;

        for (int i = 0; i < amount - 1; i++) {
            filterBuffer[i] = filterBuffer[i + 1];

            filteredData += filterBuffer[i];
        }

        filterBuffer[amount - 1] = data;

        filteredData += data;

        filteredData = filteredData / amount;

        return filteredData;
    }
}
