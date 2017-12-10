package at.fhooe.mos.mountaineer.ui;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import at.fhooe.mos.mountaineer.EventSource;
import at.fhooe.mos.mountaineer.ImageChangedEventListener;
import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.TourState;
import at.fhooe.mos.mountaineer.model.tour.Tour;
import at.fhooe.mos.mountaineer.model.tour.TourDataFormatter;
import at.fhooe.mos.mountaineer.persistence.PersistenceManager;
import at.fhooe.mos.mountaineer.services.TourDataCollector;
import at.fhooe.mos.mountaineer.services.TourRecorderService;
import at.fhooe.mos.mountaineer.services.TourRecorderService_;
import at.fhooe.mos.mountaineer.ui.fragment.EditNameDialog;
import at.fhooe.mos.mountaineer.ui.fragment.NewTourFragment_;
import at.fhooe.mos.mountaineer.ui.fragment.SaveTourDialog;
import at.fhooe.mos.mountaineer.ui.fragment.SaveTourFragment_;

@EActivity(R.layout.activity_tour)
@OptionsMenu(R.menu.tour_activity_menu)
public class TourActivity extends AppCompatActivity {
    private final static String TAG = TourActivity.class.getSimpleName();

    //private EventSource<ImageChangedEventListener> imageChangedEventListener;
    public PersistenceManager persistenceManager;
    //private TourState currentState;
    //private String selectedImagePath;

    @ViewById
    protected ImageView tourImage;

    // GENERAL
    @ViewById
    protected TextView tourLocation;
    @ViewById
    protected TextView tourDuration;
    @ViewById
    protected TextView tourStartTime;

    // TRACK
    @ViewById
    protected TextView tourSteps;
    @ViewById
    protected TextView tourSpeed;
    @ViewById
    protected TextView tourDistance;
    @ViewById
    protected TextView tourElevation;

    // HEALTH
    @ViewById
    protected TextView tourHeartRate;
    @ViewById
    protected TextView tourNormalHeartRate;
    @ViewById
    protected TextView tourRespiration;
    @ViewById
    protected TextView tourKcal;

    // WEATHER
    @ViewById
    protected TextView tourTemp;
    @ViewById
    protected TextView tourMinMaxTemp;
    @ViewById
    protected TextView tourRain;
    @ViewById
    protected TextView tourHumidity;
    @ViewById
    protected TextView tourWind;
    @ViewById
    protected TextView tourWeatherDescription;

    @StringRes
    protected String tourActivityMenuSettings;

    @StringRes
    protected String tourActivityMenuStop;

    @StringRes
    protected String tourActivityMenuDoNotSave;

    @OptionsMenuItem
    protected MenuItem tourActivityMenuItem;

    @ViewById
    protected Toolbar toolbar;

    @ViewById
    protected CollapsingToolbarLayout collapse_toolbar;

    @ViewById
    protected FloatingActionButton fabAddPhoto;

    @ViewById
    protected FloatingActionButton fabEditName;

    private static TourDataFormatter tourDataFormatter = TourDataFormatter.getInstance();

    @OptionsItem(R.id.tourActivityMenuItem)
    protected void onOptionsItemClicked() {
        SaveTourDialog dialog = new SaveTourDialog();
        dialog.show(getFragmentManager(), SaveTourDialog.class.getName());
    }

    @Click(R.id.fabEditName)
    public void onFabEditNameClick() {
        EditNameDialog dialog = new EditNameDialog();
        dialog.show(getFragmentManager(), SaveTourDialog.class.getName());
    }

    @Click(R.id.fabAddPhoto)
    public void onFabAddPhotoClick() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 123);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        persistenceManager = new PersistenceManager(this);
        //currentState = persistenceManager.getCurrentState();
        //updateFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setSupportActionBar(toolbar);

        if(checkPermissions() &&
                checkBLEEnabled() &&
                checkLocationEnabled()) {

            updateUI(Tour.getEmptyTour());

            EventBus.getDefault().post(new TourDataCollector.ControlEvent(true));
            EventBus.getDefault().register(this);
            startTourRecording();

            //imageChangedEventListener = new EventSource<ImageChangedEventListener>() {};
            //imageChangedEventListener.registerListener(TourRecorderService_.tourDataCollector);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().post(new TourDataCollector.ControlEvent(false));
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TourDataCollector.TourDataUpdateEvent event) {
        Tour tour = event.getTour();
        updateUI(tour);
    }

    private void updateUI(Tour tour) {
        // GENERAL
        collapse_toolbar.setTitle(tourDataFormatter.getName(tour));
        tourStartTime.setText(tourDataFormatter.getStartTime(tour));
        tourDuration.setText(tourDataFormatter.getDuration(tour));
        tourLocation.setText(tourDataFormatter.getLocation(tour));

        // TRACK
        tourSteps.setText(tourDataFormatter.getTotalSteps(tour));
        tourSpeed.setText(tourDataFormatter.getSpeed(tour));
        tourDistance.setText(tourDataFormatter.getDistance(tour));
        tourElevation.setText(tourDataFormatter.getElevation(tour));

        // WEATHER
        tourTemp.setText(tourDataFormatter.getTemp(tour));
        tourMinMaxTemp.setText(tourDataFormatter.getMinMaxTemp(tour));
        tourHumidity.setText(tourDataFormatter.getHumidity(tour));
        tourWind.setText(tourDataFormatter.getWind(tour));
        tourWeatherDescription.setText(tourDataFormatter.getDescription(tour));
        tourRain.setText(tourDataFormatter.getRain(tour));

        // HEALTH
        tourHeartRate.setText(tourDataFormatter.getCurrentHeartRate(tour));
        tourNormalHeartRate.setText(tourDataFormatter.getNormalHeartRate(tour));
        tourRespiration.setText(tourDataFormatter.getRespiration(tour));
        tourKcal.setText(tourDataFormatter.getBurnedCalories(tour));
        tourImage.setImageBitmap(tourDataFormatter.getImage(tour, this));
    }

    public boolean checkPermissions() {
        String[] requiredPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        boolean permissionMissing = false;

        for (String permission : requiredPermissions) {
            if(ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
                permissionMissing = true;
                break;
            }
        }

        if (permissionMissing) {
            ActivityCompat.requestPermissions(this, requiredPermissions, 1);
            return false;
        }

        return true;
    }

    protected boolean checkLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please activate the GPS Feature!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, MainActivity_.class);
            startActivity(i);

            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1) {
            for (int result : grantResults) {
                if(result != PackageManager.PERMISSION_GRANTED) {
                    Intent i = new Intent(this, MainActivity_.class);
                    startActivity(i);
                }
            }
        }
    }

    protected boolean checkBLEEnabled() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Toast.makeText(this, "Please activate the Bluetooth Feature!", Toast.LENGTH_SHORT).show();

            Intent i = new Intent(this, MainActivity_.class);
            startActivity(i);

            return false;
        }

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && data != null) {
            Uri selectedImageUri = data.getData();
            String path = getPath(selectedImageUri);
            Toast.makeText(this, "Image selected!", Toast.LENGTH_SHORT).show();

            /*for (ImageChangedEventListener listener : imageChangedEventListener.getEventListeners()) {
                listener.onImageChangedEvent(path);
            }*/
        }
    }

    private String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        // this is our fallback here
        return uri.getPath();
    }

    private void startTourRecording() {
        TourRecorderService_.intent(this).start();
    }

    private void stopTourRecording() {
        TourRecorderService_.intent(this).stop();
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        updateOptionsMenu();
        return super.onCreateOptionsMenu(menu);
    }*/

    /*public void doStateTransition() {
        transitionToNextState();
    }*/

    /*private void transitionToNextState() {
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

        //updateOptionsMenu();
        //updateFragment();
        updateTourRecordingStatus();
    }*/

    /*private void updateFragment() {
        Fragment newFragment = getFragmentForCurrentState();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //transaction.replace(R.id.currentTourFragment, newFragment);
        transaction.commit();
    }*/

    /*private void updateOptionsMenu() {
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
    }*/

    /*private Fragment getFragmentForCurrentState() {
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
    }*/

    /*public void updateTourRecordingStatus() {
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
    }*/
}