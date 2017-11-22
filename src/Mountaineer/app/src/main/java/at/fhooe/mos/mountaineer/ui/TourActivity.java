package at.fhooe.mos.mountaineer.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import at.fhooe.mos.mountaineer.R;

@EActivity(R.layout.activity_tour)
public class TourActivity extends AppCompatActivity {

    String KEY_TOUR_NAME = "keyTourName";
    String KEY_TOUR_IMAGE_PATH = "keyTourImagePath";

    @ViewById ImageView tourImage;
    @ViewById TextView tourName;

    // GENERAL
    @ViewById TextView tourLocation;
    @ViewById TextView tourDuration;
    @ViewById TextView tourStartTime;

    // TRACK
    @ViewById TextView tourSteps;
    @ViewById TextView tourAverageSteps;
    @ViewById TextView tourSpeed;
    @ViewById TextView tourDistance;
    @ViewById TextView tourElevation;

    // HEALTH
    @ViewById TextView tourHeartRate;
    @ViewById TextView tourNormalHeartRate;
    @ViewById TextView tourRespiration;
    @ViewById TextView tourKcal;

    // WEATHER
    @ViewById TextView tourTemp;
    @ViewById TextView tourMinMaxTemp;
    @ViewById TextView tourRain;
    @ViewById TextView tourHumidity;
    @ViewById TextView tourWind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //createUI(getIntent());
    }

    @UiThread
    void createUI(Intent intent) {
        Bundle bundle = intent.getExtras();

        String name = bundle.getString(KEY_TOUR_NAME);
        tourName.setText(name);

        String imagePath = bundle.getString(KEY_TOUR_IMAGE_PATH);
        // TODO Set image by path
    }
}
