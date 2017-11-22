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

import at.fhooe.mos.mountaineer.R;

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

    public CurrentTourFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_current_tour, container, false);
    }
}
