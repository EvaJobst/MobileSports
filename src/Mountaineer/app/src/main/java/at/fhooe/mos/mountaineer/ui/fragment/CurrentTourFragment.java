package at.fhooe.mos.mountaineer.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.model.Tour;
import at.fhooe.mos.mountaineer.services.TourDataCollector;
import at.fhooe.mos.mountaineer.ui.TourDataFormatter;

@EFragment
public class CurrentTourFragment extends Fragment {

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

    TourDataFormatter tourDataFormatter = new TourDataFormatter();

    public CurrentTourFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_tour, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TourDataCollector.TourDetailsEvent event) {
        Tour tour = event.getTour();
        tourSteps.setText(tourDataFormatter.getTotalStepsString(tour));
        tourDuration.setText(tourDataFormatter.getDurationString(tour));

        tourStartTime.setText(tourDataFormatter.getStartTimeString(tour));
    }
}
