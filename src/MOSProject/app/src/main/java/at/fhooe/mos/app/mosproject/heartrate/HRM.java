package at.fhooe.mos.app.mosproject.heartrate;

import android.app.Activity;

/**
 * Created by Eva on 29.11.2017.
 */

public interface HRM {
    int getHeartRate();
    void initialize(Activity activity);
}
