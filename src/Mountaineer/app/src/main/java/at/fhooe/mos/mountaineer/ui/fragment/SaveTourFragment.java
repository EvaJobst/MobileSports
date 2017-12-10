package at.fhooe.mos.mountaineer.ui.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.model.tour.Tour;
import at.fhooe.mos.mountaineer.persistence.FirebaseAddEventsListener;
import at.fhooe.mos.mountaineer.persistence.FirebaseManager;
import at.fhooe.mos.mountaineer.persistence.PersistenceManager;
import at.fhooe.mos.mountaineer.services.TourDataCollector;
import at.fhooe.mos.mountaineer.ui.TourActivity;

@EFragment(R.layout.fragment_save_tour)
public class SaveTourFragment extends Fragment {
    private int requestCodeGallery = 123;


    private TourActivity tourActivity;

    private PersistenceManager persistenceManager;

    private Tour tour;

    @ViewById
    protected TextInputEditText addTitleToTour;

    @ViewById
    protected FloatingActionButton addGalleryImageButton;

    @ViewById
    protected Button saveTourButton;

    @Override
    public void onStart() {
        super.onStart();

        persistenceManager = new PersistenceManager(getContext());
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        tourActivity = (TourActivity) context;
    }

    @Click
    protected void saveTourButtonClicked() {
        saveTourButton.setText("Saving ...");

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
                saveTourButton.setText("Save");
                Toast.makeText(getContext(), "Tour not saved!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Click
    protected void addGalleryImageButtonClicked() {

    }



    @Subscribe
    public void onMessageEvent(TourDataCollector.FinalTourDataEvent event) {
        this.tour = event.getTour();
    }



    public SaveTourFragment() {
        // Required empty public constructor
    }
}
