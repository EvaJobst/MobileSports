package at.fhooe.mos.mountaineer.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;

import at.fhooe.mos.mountaineer.PersistenceManager;
import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.TourState;
import at.fhooe.mos.mountaineer.services.TourRecorderService_;
import at.fhooe.mos.mountaineer.ui.fragment.CurrentTourFragment_;
import at.fhooe.mos.mountaineer.ui.fragment.NewTourFragment;
import at.fhooe.mos.mountaineer.ui.fragment.NewTourFragment_;
import at.fhooe.mos.mountaineer.ui.fragment.NewTourTitleFragment_;

@EActivity(R.layout.activity_tour)
@OptionsMenu(R.menu.tour_activity_menu)
public class TourActivity extends AppCompatActivity implements NewTourFragment.OnAddTourClickListener {
    private final static String TAG = TourActivity.class.getSimpleName();

    PersistenceManager persistenceManager;
    TourState currentState;

    @OptionsMenuItem
    MenuItem tourActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        persistenceManager = new PersistenceManager(this);
        currentState = persistenceManager.getCurrentState();
        updateFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        updateOptionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem(R.id.tourActivityButton)
    void invokeStateTransition() {
        TourState nextState;

        switch (currentState) {
            case newTour:
                nextState = TourState.newTourTitle;
                break;
            case newTourTitle:
                nextState = TourState.currentTour;
                break;
            case currentTour:
                nextState = TourState.finishTour;
                break;
            case finishTour:
                nextState = TourState.newTour;
                break;
            default:
                nextState = TourState.newTour;
        }

        if(nextState == TourState.newTour ||
                nextState == TourState.currentTour) {
            persistenceManager.setCurrentState(nextState);
        }

        currentState = nextState;
        updateOptionsMenu();
        updateFragment();
        updateTourRecordingStatus();
    }

    void updateFragment() {
        Fragment newFragment = getFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.currentTourFragment, newFragment);
        transaction.commit();
    }

    void updateOptionsMenu() {
        switch (currentState) {
            case newTour:
                tourActivityButton.setVisible(false);
                break;
            case newTourTitle:
                tourActivityButton.setTitle("START");
                tourActivityButton.setVisible(true);
                break;
            case currentTour:
                tourActivityButton.setTitle("STOP");
                break;
            default:
                tourActivityButton.setVisible(false);
        }
    }

    Fragment getFragment() {
        switch (currentState) {
            case newTour:
                return new NewTourFragment_();
            case newTourTitle:
                return new NewTourTitleFragment_();
            case currentTour:
                return new CurrentTourFragment_();
            default:
                return new NewTourFragment_();
        }
    }

    @Override
    public void onAddTourClick() {
        invokeStateTransition();
    }

    /*@UiThread
    void createUI(Intent intent) {
        Bundle bundle = intent.getExtras();

        String name = bundle.getString(KEY_TOUR_NAME);
        tourName.setText(name);

        String imagePath = bundle.getString(KEY_TOUR_IMAGE_PATH);
        // TODO Set image by path
    }*/

    public void updateTourRecordingStatus(){
        switch (currentState) {
            case newTour:
            case newTourTitle:
            break;
            case currentTour:
                startTourRecording();
                break;
            case finishTour:
                stopTourRecording();
                break;
            default:
                Log.e(TAG, "undefined state!");
        }
    }

    public void startTourRecording(){
        TourRecorderService_.intent(getApplication()).start();
    }

    public void stopTourRecording(){
        TourRecorderService_.intent(getApplication()).stop();
    }
}