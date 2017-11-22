package at.fhooe.mos.mountaineer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.model.Tour;

@EActivity(R.layout.activity_tour_details)

public class TourDetailsActivity extends AppCompatActivity {

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
    }

    @UiThread
    void createUI(Tour tour) {
        tourName.setText(tour.getName());
        // Todo Set image by path

        // GENERAL
        tourLocation.setText(tour.getLocation());
        tourDuration.setText(tour.getDuration());
        // TODO tourStartTime.setText();

        // TRACK
        tourSteps.setText(tour.getTotalSteps());
        tourAverageSteps.setText(tour.getAverageSteps());
        tourSpeed.setText(tour.getAverageSpeed());
        tourDistance.setText(tour.getDistance());
        tourElevation.setText(tour.getElevation());

        // HEALTH
       tourHeartRate.setText(tour.getAverageHeartRate());
       tourNormalHeartRate.setText(tour.getNormalHeartRate());
       tourRespiration.setText(tour.getAverageRespiration());
       tourKcal.setText(tour.getBurnedKcal());

       // WEATHER
        // TODO tourTemp.setText();
        tourMinMaxTemp.setText(tour.getMaxTemp() + "/" + tour.getMinTemp());
        tourRain.setText(tour.getRain());
        tourHumidity.setText(tour.getHumidity());
        tourWind.setText(tour.getWind());
    }
}
