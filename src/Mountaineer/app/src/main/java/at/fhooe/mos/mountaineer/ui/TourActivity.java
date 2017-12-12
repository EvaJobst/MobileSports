package at.fhooe.mos.mountaineer.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.model.tour.Tour;
import at.fhooe.mos.mountaineer.model.tour.TourDataFormatter;
import at.fhooe.mos.mountaineer.persistence.FirebaseAddEventsListener;
import at.fhooe.mos.mountaineer.persistence.FirebaseManager;
import at.fhooe.mos.mountaineer.persistence.PersistenceManager;
import at.fhooe.mos.mountaineer.services.TourDataCollector;
import at.fhooe.mos.mountaineer.services.TourRecorderService_;
import at.fhooe.mos.mountaineer.ui.fragment.EditNameDialog;
import at.fhooe.mos.mountaineer.ui.fragment.SaveTourDialog;

@EActivity(R.layout.activity_tour)
@OptionsMenu(R.menu.tour_activity_menu)
public class TourActivity extends AppCompatActivity {
    private final static String TAG = TourActivity.class.getSimpleName();
    public static final int REQUEST_CODE_PICK_IMAGE = 2;

    //private EventSource<ImageChangedEventListener> imageChangedEventListener;
    public PersistenceManager persistenceManager;
    //private TourState currentState;
    //private String selectedImagePath;

    private Tour tour;
    private String temporaryTourName;
    private String temporaryTourImagePath;

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
        stopTourRecording();


    }

    @Click(R.id.fabEditName)
    public void onFabEditNameClick() {
        EditNameDialog dialog = new EditNameDialog();
        dialog.registerListener(new EditNameDialog.NameChangedEventListener() {
            @Override
            public void onNameChangedEvent(String name) {
                temporaryTourName = name;
                collapse_toolbar.setTitle(temporaryTourName);
            }
        });
        dialog.show(getFragmentManager(), SaveTourDialog.class.getName());
    }

    @Click(R.id.fabAddPhoto)
    public void onFabAddPhotoClick() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_CODE_PICK_IMAGE);
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

        updateUI(Tour.getEmptyTour());

        EventBus.getDefault().post(new TourDataCollector.ControlEvent(true));
        EventBus.getDefault().register(this);
        startTourRecording();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().post(new TourDataCollector.ControlEvent(false));
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
            Uri selectedImageUri = data.getData();
            String path = getPath(selectedImageUri);

            if (path != null) {
                temporaryTourImagePath = path;

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                Bitmap image = BitmapFactory.decodeFile(temporaryTourImagePath, options);

                tourImage.setImageBitmap(image);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TourDataCollector.TourDataUpdateEvent event) {
        Tour tour = event.getTour();
        updateUI(tour);
    }

    @Subscribe
    public void onMessageEvent(TourDataCollector.FinalTourDataEvent event) {
        tour = event.getTour();

        if (temporaryTourName != null) {
            tour.setName(temporaryTourName);
        }

        if (temporaryTourImagePath != null) {
            tour.setImagePath(temporaryTourImagePath);
        }

        askAndSave();
    }

    private void updateUI(Tour tour) {
        // GENERAL
        //collapse_toolbar.setTitle(tourDataFormatter.getName(tour));
        tourStartTime.setText(tourDataFormatter.getStartTime(tour));
        tourDuration.setText(tourDataFormatter.getDuration(tour));
        tourLocation.setText(tourDataFormatter.getLocation(tour));
        //tourImage.setImageBitmap(tourDataFormatter.getImage(tour, this));

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

    }

    private void askAndSave() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Do you want to save the current tour?")
                .setTitle("My First tour")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String userId = persistenceManager.getUserId();
                        FirebaseManager firebaseManager = new FirebaseManager(userId);
                        firebaseManager.addTour(tour, new FirebaseAddEventsListener() {

                            @Override
                            public void addSucceededEvent() {
                                Toast.makeText(TourActivity.this, "Tour Saved!", Toast.LENGTH_SHORT).show();

                                //tourActivity.doStateTransition();
                            }

                            @Override
                            public void addFailedEvent() {
                                Toast.makeText(TourActivity.this, "Tour not saved!", Toast.LENGTH_SHORT).show();
                            }
                        });

                        Intent j = new Intent(TourActivity.this, MainActivity_.class);
                        startActivity(j);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //stopTourRecording();
                        Intent j = new Intent(TourActivity.this, MainActivity_.class);
                        startActivity(j);
                    }
                });

        // 3. Get the AlertDialog from create()
        builder.create().show();
    }

    private String getPath(Uri uri) {
        if (uri == null) {
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