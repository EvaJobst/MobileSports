package at.fhooe.mos.mountaineer.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import at.fhooe.mos.mountaineer.R;

@EFragment
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_tour, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onAddTourClickListener = (OnAddTourClickListener) context;
    }
}
