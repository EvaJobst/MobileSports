package at.fhooe.mos.mountaineer.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import at.fhooe.mos.mountaineer.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewTourImageFragment extends Fragment {


    public NewTourImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_tour_image, container, false);
    }

}
