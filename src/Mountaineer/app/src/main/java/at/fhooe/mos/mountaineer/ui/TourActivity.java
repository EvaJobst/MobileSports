package at.fhooe.mos.mountaineer.ui;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.res.StringRes;

import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.TourState;
import at.fhooe.mos.mountaineer.persistence.PersistenceManager;
import at.fhooe.mos.mountaineer.services.TourRecorderService_;
import at.fhooe.mos.mountaineer.ui.fragment.CurrentTourFragment_;
import at.fhooe.mos.mountaineer.ui.fragment.NewTourFragment_;
import at.fhooe.mos.mountaineer.ui.fragment.SaveTourFragment_;

@EActivity(R.layout.activity_tour)
@OptionsMenu(R.menu.tour_activity_menu)
public class TourActivity extends AppCompatActivity {
    private final static String TAG = TourActivity.class.getSimpleName();

    private PersistenceManager persistenceManager;
    private TourState currentState;

    @StringRes
    protected String tourActivityMenuSettings;

    @StringRes
    protected String tourActivityMenuStop;

    @StringRes
    protected String tourActivityMenuDoNotSave;

    @OptionsMenuItem
    protected MenuItem tourActivityMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        persistenceManager = new PersistenceManager(this);
        currentState = persistenceManager.getCurrentState();
        updateFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkPermissions();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        updateOptionsMenu();
        return super.onCreateOptionsMenu(menu);
    }

    public void doStateTransition() {
        transitionToNextState();
    }

    @OptionsItem(R.id.tourActivityMenuItem)
    protected void onOptionsItemClicked() {
        if (tourActivityMenuItem.getTitle().toString().equals(tourActivityMenuSettings)) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        } else {
            transitionToNextState();
        }
    }

    private void transitionToNextState() {
        if (currentState == TourState.newTour) {
            if (checkPermissions() == false || checkBLEEnabled() == false || checkLocationEnabled() == false) {
                return;
            }
        }

        switch (currentState) {
            case newTour:
                currentState = TourState.currentTour;
                break;
            case currentTour:
                currentState = TourState.saveTour;
                break;
            case saveTour:
                currentState = TourState.newTour;
                break;
            default:
                Log.e(TAG, "undefined state!");
                currentState = TourState.newTour;
        }

        if (currentState == TourState.newTour || currentState == TourState.currentTour) {
            persistenceManager.setCurrentState(currentState);
        } else if (currentState == TourState.saveTour) {
            persistenceManager.setCurrentState(TourState.newTour);
        }

        updateOptionsMenu();
        updateFragment();
        updateTourRecordingStatus();
    }

    private void updateFragment() {
        Fragment newFragment = getFragmentForCurrentState();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.currentTourFragment, newFragment);
        transaction.commit();
    }

    private void updateOptionsMenu() {
        switch (currentState) {
            case newTour:
                tourActivityMenuItem.setTitle(tourActivityMenuSettings);
                tourActivityMenuItem.setVisible(true);
                break;
            case currentTour:
                tourActivityMenuItem.setTitle(tourActivityMenuStop);
                tourActivityMenuItem.setVisible(true);
                break;
            case saveTour:
                tourActivityMenuItem.setTitle(tourActivityMenuDoNotSave);
                tourActivityMenuItem.setVisible(true);
                break;
            default:
                tourActivityMenuItem.setVisible(false);
        }
    }

    private Fragment getFragmentForCurrentState() {
        switch (currentState) {
            case newTour:
                return new NewTourFragment_();
            case saveTour:
                return new SaveTourFragment_();
            case currentTour:
                return new CurrentTourFragment_();
            default:
                Log.e(TAG, "undefined state!");
                return null;
        }
    }

    public void updateTourRecordingStatus() {
        switch (currentState) {
            case newTour:
                break;
            case currentTour:
                startTourRecording();
                break;
            case saveTour:
                stopTourRecording();
                break;
            default:
                Log.e(TAG, "undefined state!");
        }
    }

    public boolean checkPermissions() {
        String[] requiredPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
        };

        boolean permissionMissing = false;

        for (String permission : requiredPermissions) {
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                permissionMissing = true;
                break;
            }
        }
        requestPermissions(requiredPermissions, 2);

        if (permissionMissing) {
            ActivityCompat.requestPermissions(this, requiredPermissions, 1);

            return false;
        }

        return true;
    }

    protected boolean checkLocationEnabled() {
        LocationManager locationMgmt =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationMgmt.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Location Service Not Available!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    protected boolean checkBLEEnabled() {
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "BLE Service Not Available", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void startTourRecording() {
        TourRecorderService_.intent(this).start();
    }

    private void stopTourRecording() {
        TourRecorderService_.intent(this).stop();
    }

}