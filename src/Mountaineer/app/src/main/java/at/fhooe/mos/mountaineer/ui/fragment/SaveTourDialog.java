package at.fhooe.mos.mountaineer.ui.fragment;


import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.model.tour.Tour;
import at.fhooe.mos.mountaineer.persistence.FirebaseAddEventsListener;
import at.fhooe.mos.mountaineer.persistence.FirebaseManager;
import at.fhooe.mos.mountaineer.persistence.PersistenceManager;
import at.fhooe.mos.mountaineer.services.TourDataCollector;
import at.fhooe.mos.mountaineer.ui.MainActivity_;
import at.fhooe.mos.mountaineer.ui.TourActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SaveTourDialog extends DialogFragment {
    private Tour tour;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Do you want to save the current tour?")
                .setTitle("My First tour")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PersistenceManager persistenceManager = ((TourActivity)getActivity()).persistenceManager;

                        String userId = persistenceManager.getUserId();
                        FirebaseManager firebaseManager = new FirebaseManager(userId);
                        firebaseManager.addTour(tour, new FirebaseAddEventsListener() {

                            @Override
                            public void addSucceededEvent() {
                                Toast.makeText(getContext(), "Tour Saved!", Toast.LENGTH_SHORT).show();

                                //tourActivity.doStateTransition();
                            }

                            @Override
                            public void addFailedEvent() {
                                Toast.makeText(getContext(), "Tour not saved!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent j = new Intent(getContext(), MainActivity_.class);
                        startActivity(j);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //stopTourRecording();
                        Intent j = new Intent(getContext(), MainActivity_.class);
                        startActivity(j);
                    }
                });

        // 3. Get the AlertDialog from create()
        return builder.create();
    }

    @Subscribe
    public void onMessageEvent(TourDataCollector.FinalTourDataEvent event) {
        this.tour = event.getTour();
    }
}
