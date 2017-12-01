package at.fhooe.mos.mountaineer.ui.fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.model.Tour;
import at.fhooe.mos.mountaineer.sensors.location.Location;
import at.fhooe.mos.mountaineer.services.TourDataCollector;
import at.fhooe.mos.mountaineer.model.TourDataFormatter;

@EFragment
public class CurrentTourFragment extends Fragment {

    @ViewById
    protected ImageView tourImage;
    @ViewById
    protected TextView tourName;

    // GENERAL
    @ViewById
    protected TextView tourLocation;
    @ViewById
    protected TextView tourDuration;
    @ViewById
    protected TextView tourStartTime;

    // TRACK
    @ViewById
    protected TextView tourSteps;
    @ViewById
    protected TextView tourAverageSteps;
    @ViewById
    protected TextView tourSpeed;
    @ViewById
    protected TextView tourDistance;
    @ViewById
    protected TextView tourElevation;

    // HEALTH
    @ViewById
    protected TextView tourHeartRate;
    @ViewById
    protected TextView tourNormalHeartRate;
    @ViewById
    protected TextView tourRespiration;
    @ViewById
    protected TextView tourKcal;

    // WEATHER
    @ViewById
    protected TextView tourTemp;
    @ViewById
    protected TextView tourMinMaxTemp;
    @ViewById
    protected TextView tourRain;
    @ViewById
    protected TextView tourHumidity;
    @ViewById
    protected TextView tourWind;

    private static TourDataFormatter tourDataFormatter = TourDataFormatter.getInstance();

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

        EventBus.getDefault().post(new TourDataCollector.ControlEvent(true));
        EventBus.getDefault().register(this);
        checkPermissions();
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().post(new TourDataCollector.ControlEvent(false));
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TourDataCollector.TourDataUpdateEvent event) {
        Tour tour = event.getTour();
        tourSteps.setText(tourDataFormatter.getTotalSteps(tour));
        tourDuration.setText(tourDataFormatter.getDuration(tour));

        tourStartTime.setText(tourDataFormatter.getStartTime(tour));
        tourLocation.setText(tour.getLocation());
        tourTemp.setText(tourDataFormatter.getTemp(tour));
        tourMinMaxTemp.setText(tourDataFormatter.getMinMaxTemp(tour));
        tourHumidity.setText(tourDataFormatter.getHumidity(tour));
        tourWind.setText(tourDataFormatter.getWind(tour));
    }

    public void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
    }
}
