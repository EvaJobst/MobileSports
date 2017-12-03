package at.fhooe.mos.mountaineer.ui.fragment;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.ui.TourActivity;

@EFragment(R.layout.fragment_new_tour)
public class NewTourFragment extends Fragment {
    private TourActivity tourActivity;

    @ViewById
    protected FloatingActionButton addTourButton;

    @Click
    protected void addTourButtonClicked() {
        tourActivity.doStateTransition();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tourActivity = (TourActivity) context;
    }

    public NewTourFragment() {
        // Required empty public constructor
    }
}
