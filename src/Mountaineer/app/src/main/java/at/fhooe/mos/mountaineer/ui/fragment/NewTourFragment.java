package at.fhooe.mos.mountaineer.ui.fragment;


import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import at.fhooe.mos.mountaineer.R;

@EFragment(R.layout.fragment_new_tour)
public class NewTourFragment extends Fragment {
    OnAddTourClickListener onAddTourClickListener;

    public interface OnAddTourClickListener {
        void onAddTourClick();
    }

    @ViewById
    FloatingActionButton addTourButton;

    @Click
    void addTourButtonClicked() {
        onAddTourClickListener.onAddTourClick();
    }


    public NewTourFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAddTourClickListener = (OnAddTourClickListener) context;
    }
}
