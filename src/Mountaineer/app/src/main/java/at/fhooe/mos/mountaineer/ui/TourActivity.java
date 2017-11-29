package at.fhooe.mos.mountaineer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
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
    MenuItem tourActivityMenuItem;

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

    @Override
    public void onAddTourClick() {
        invokeStateTransition();
    }

    @OptionsItem(R.id.tourActivityButton)
    void onOptionsItemClicked() {
        if (tourActivityMenuItem.getTitle().toString().toLowerCase().equals(getString(R.string.tour_activity_menu_settings).toLowerCase())) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else {
            invokeStateTransition();
        }
    }

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

        if (nextState == TourState.newTour ||
                nextState == TourState.currentTour) {
            persistenceManager.setCurrentState(nextState);
        }

        currentState = nextState;
        updateOptionsMenu();
        updateFragment();
        updateTourRecordingStatus();
    }

    void updateFragment() {
        Fragment newFragment = getFragmentForCurrentState();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.currentTourFragment, newFragment);
        transaction.commit();
    }

    void updateOptionsMenu() {
        switch (currentState) {
            case newTour:
                tourActivityMenuItem.setTitle(getString(R.string.tour_activity_menu_settings));
                tourActivityMenuItem.setVisible(true);
                break;
            case newTourTitle:
                tourActivityMenuItem.setTitle(getString(R.string.tour_activity_menu_start));
                tourActivityMenuItem.setVisible(true);
                break;
            case currentTour:
                tourActivityMenuItem.setTitle(getString(R.string.tour_activity_menu_stop));
                break;
            default:
                tourActivityMenuItem.setVisible(false);
        }
    }

    private Fragment getFragmentForCurrentState() {
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

    public void updateTourRecordingStatus() {
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

    public void startTourRecording() {
        TourRecorderService_.intent(this).start();
    }

    public void stopTourRecording() {
        TourRecorderService_.intent(this).stop();
    }
}