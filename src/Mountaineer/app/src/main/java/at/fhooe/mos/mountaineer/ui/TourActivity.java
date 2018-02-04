package at.fhooe.mos.mountaineer.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    ArrayList<Entry> entries = new ArrayList<>();
    private Boolean isSaving = false;
    private Boolean isLive;
    private Animation heartBeatAnimation;

    private final static String TAG = TourActivity.class.getSimpleName();
    public static final int REQUEST_CODE_PICK_IMAGE = 2;

    public PersistenceManager persistenceManager;
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
    protected TextView tourCadence;
    @ViewById
    protected View helperViewDark;

    // HEALTH
    @ViewById
    protected TextView tourHeartRate;
    @ViewById
    protected TextView tourRestingHeartRate;
    @ViewById
    protected TextView tourKcal;
    @ViewById
    protected ImageView tourIcHeart;
    @ViewById
    protected ImageView tourIcHeartShadow;

    // WEATHER
    @ViewById
    protected TextView tourTemp;
    @ViewById
    protected TextView tourMinMaxTemp;
    @ViewById
    protected TextView tourRain;
    @ViewById
    protected TextView tourSunset;
    @ViewById
    protected TextView tourWind;
    @ViewById
    protected TextView tourWeatherDescription;
    @ViewById
    protected ImageView tourIcWeather;
    @ViewById
    protected ImageView tourIcWeatherShadow;
    @ViewById
    protected LineChart tourChart;

    @StringRes
    protected String tourActivityMenuSettings;

    @StringRes
    protected String tourActivityMenuStop;

    @StringRes
    protected String tourActivityMenuDoNotSave;

    @ViewById
    protected Toolbar toolbar;

    @ViewById
    protected CollapsingToolbarLayout collapseToolbar;

    @ViewById
    protected FloatingActionButton fabAddPhoto;

    @ViewById
    protected FloatingActionButton fabEditName;

    protected MenuItem tourActivityMenuItem;

    private static TourDataFormatter tourDataFormatter = TourDataFormatter.getInstance();

    @OptionsItem(R.id.tourActivityMenuItem)
    protected void onOptionsItemClicked() {
        if(isLive) {
            getSaveDialog().show();
        }
    }

    public AlertDialog getSaveDialog() {
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Do you want to save the current tour?")
                .setTitle(tourDataFormatter.getName(Tour.getEmptyTour(), temporaryTourName))
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isSaving = true;
                        stopTourRecording();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isSaving = false;
                        stopTourRecording();
                        Intent j = new Intent(TourActivity.this, MainActivity_.class);
                        startActivity(j);
                        finish();
                    }
                });

        // 3. Get the AlertDialog from create()
        return builder.create();
    }

    @Click(R.id.fabEditName)
    public void onFabEditNameClick() {
        EditNameDialog dialog = new EditNameDialog();
        dialog.registerListener(new EditNameDialog.NameChangedEventListener() {
            @Override
            public void onNameChangedEvent(String name) {
                temporaryTourName = name;
                collapseToolbar.setTitle(name);
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
    }


    @Override
    protected void onStart() {
        super.onStart();

        setSupportActionBar(toolbar);
        heartBeatAnimation = AnimationUtils.loadAnimation(this, R.anim.pulse);
        tourIcHeart.setAnimation(heartBeatAnimation);
        tourIcHeartShadow.setAnimation(heartBeatAnimation);
        tourChart.clear();

        tourChart.setNoDataText("");
        tourChart.setViewPortOffsets(0f, 0f, 0f, 0f);

        tourChart.getAxisLeft().setDrawLabels(false);
        tourChart.getAxisLeft().setDrawGridLines(false);
        tourChart.getAxisLeft().setDrawAxisLine(false);
        tourChart.getAxisLeft().setDrawTopYLabelEntry(false);
        tourChart.getAxisLeft().setDrawZeroLine(false);
        tourChart.getAxisLeft().setDrawLimitLinesBehindData(false);

        tourChart.getAxisRight().setDrawLabels(false);
        tourChart.getAxisRight().setDrawGridLines(false);
        tourChart.getAxisRight().setDrawAxisLine(false);
        tourChart.getAxisRight().setDrawTopYLabelEntry(false);
        tourChart.getAxisRight().setDrawZeroLine(false);
        tourChart.getAxisRight().setDrawLimitLinesBehindData(false);

        tourChart.getXAxis().setDrawLabels(false);
        tourChart.getXAxis().setDrawGridLines(false);
        tourChart.getXAxis().setDrawAxisLine(false);
        tourChart.getXAxis().setDrawLimitLinesBehindData(false);

        tourChart.setDescription(null);    // Hide the description
        tourChart.getLegend().setEnabled(false);   // Hide the legend
        tourChart.setDrawGridBackground(false);
        tourChart.setDrawMarkers(false);

        Intent i = getIntent();

        if(i.getExtras() != null && i.getExtras().containsKey("tour")) {
            // Hide views
            fabAddPhoto.setVisibility(View.INVISIBLE);
            fabEditName.setVisibility(View.INVISIBLE);

            // Set Tour
            isLive = false;
            String tourJson = i.getExtras().getString("tour");
            Tour tour = new Gson().fromJson(tourJson, Tour.class);
            updateUI(tour);
        }

        else {
            isLive = true;
            if(!EventBus.getDefault().isRegistered(this)) {
                temporaryTourImagePath = null;
                temporaryTourName = null;
                updateUI(Tour.getEmptyTour());

                EventBus.getDefault().post(new TourDataCollector.ControlEvent(true));
                EventBus.getDefault().register(this);
                startTourRecording();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(isLive) {
            EventBus.getDefault().post(new TourDataCollector.ControlEvent(false));
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onBackPressed() {
        if(isLive) {
            getSaveDialog().show();
        }

        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_IMAGE && data != null) {
            Uri selectedImageUri = data.getData();
            String path = getPath(selectedImageUri);

            if (path != null) {
                temporaryTourImagePath = path;
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TourDataCollector.TourDataUpdateEvent event) {
        updateUI(event.getTour());
    }

    @Subscribe
    public void onMessageEvent(TourDataCollector.FinalTourDataEvent event) {
        Tour tour = event.getTour();

        if (temporaryTourName != null) {
            tour.setName(temporaryTourName);
        }

        if (temporaryTourImagePath != null) {
            tour.setImagePath(temporaryTourImagePath);
        }

        if (isSaving) {
            save(tour);
        }

        isSaving = false;
    }

    private void updateUI(Tour tour) {
        // GENERAL
        collapseToolbar.setTitle(tourDataFormatter.getName(tour, temporaryTourName));
        tourImage.setImageBitmap(tourDataFormatter.getImage(tour, temporaryTourImagePath, this));

        tourStartTime.setText(tourDataFormatter.getStartTime(tour));
        tourDuration.setText(tourDataFormatter.getDuration(tour));
        tourLocation.setText(tourDataFormatter.getLocation(tour));

        // TRACK
        tourSteps.setText(tourDataFormatter.getTotalSteps(tour));
        tourSpeed.setText(tourDataFormatter.getSpeed(tour));
        tourDistance.setText(tourDataFormatter.getDistance(tour));
        tourCadence.setText(tourDataFormatter.getCadence(tour));
        updateChart(tour.getCadence(), tour.getDuration());

        // WEATHER
        tourTemp.setText(tourDataFormatter.getTemp(tour));
        tourMinMaxTemp.setText(tourDataFormatter.getMinMaxTemp(tour));
        tourWind.setText(tourDataFormatter.getWind(tour));
        tourWeatherDescription.setText(tourDataFormatter.getDescription(tour));
        tourRain.setText(tourDataFormatter.getRain(tour));
        tourSunset.setText(tourDataFormatter.getSunset(tour));
        tourIcWeather.setImageResource(tourDataFormatter.getWeatherIcon(tour));
        tourIcWeatherShadow.setImageResource(tourDataFormatter.getWeatherIconShadow(tour, this));

        // HEALTH
        tourHeartRate.setText(tourDataFormatter.getCurrentHeartRate(tour));
        tourRestingHeartRate.setText(tourDataFormatter.getRestingHeartRate(persistenceManager.getRestingHeartRate()));
        //tourRespiration.setText(tourDataFormatter.getRespiration(tour));
        tourKcal.setText(tourDataFormatter.getBurnedCalories(tour));

        double minForOneHeartBeat = (1 / tour.getCurrentHeartRate());
        double sekForOneHeartBeat = minForOneHeartBeat * 60;
        double msForOneHeartBeat = sekForOneHeartBeat * 1000;
        long duration = (long) (msForOneHeartBeat / 2);

        if(duration != heartBeatAnimation.getDuration()) {
            heartBeatAnimation.setDuration(duration);
            //heartBeatAnimation.start();
            tourIcHeart.startAnimation(heartBeatAnimation);
            tourIcHeartShadow.startAnimation(heartBeatAnimation);
        }
    }

    public Entry getMinEntryOfSection(List<Entry> section) {
        Entry min = section.get(0);

        for(Entry entry : section) {
            if(entry.getY() < min.getY()) {
                min = entry;
            }
        }

        return min;
    }

    public Entry getMaxEntryOfSection(List<Entry> section) {
        Entry max = section.get(0);

        for(Entry entry : section) {
            if(entry.getY() > max.getY()) {
                max = entry;
            }
        }

        return max;
    }

    public void sortEntriesAscending() {
        Collections.sort(entries, new Comparator<Entry>() {
            @Override
            public int compare(Entry entry, Entry other) {
                return Float.compare(entry.getX(), other.getX());
            }
        });
    }

    public void updateChart(int cadence, long duration) {
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        if(entries.isEmpty()) {
            entries.add(new Entry(duration, cadence));
        }

        else if(entries.get(entries.size()-1).getY() != cadence) {
            entries.add(new Entry(duration, cadence));
        }

        if(cadence != 0) {
            helperViewDark.setVisibility(View.VISIBLE);
        }

        if(entries.size() == 10) {
            ArrayList<Entry> temp = new ArrayList<>();

            for (int i = 0; i < entries.size(); i++) {
                if(i != 0 && (i+1) % 5 == 0) {
                    Entry min = getMinEntryOfSection(entries.subList(i-4, i+1));
                    Entry max = getMaxEntryOfSection(entries.subList(i-4, i+1));
                    temp.add(min);
                    temp.add(max);
                }
            }

            entries = temp;
            sortEntriesAscending();
        }

        final LineDataSet dataSet = new LineDataSet(entries, "");
        dataSet.setDrawCircles(false);
        dataSet.setDrawFilled(true);
        dataSet.setColor(Color.WHITE, 255);
        dataSet.setLineWidth(2);
        dataSet.setFillColor(getResources().getColor(R.color.colorPrimaryDark));
        dataSet.setFillAlpha(255);
        dataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        dataSet.setCubicIntensity(0.5f);
        dataSets.add(dataSet);

        LineData data = new LineData(dataSets);
        data.setDrawValues(false);

        tourChart.clear();
        tourChart.setData(data);
    }

    private void save(final Tour tour) {
        String userId = persistenceManager.getUserId();
        FirebaseManager firebaseManager = new FirebaseManager(userId);
        firebaseManager.addTour(tour, new FirebaseAddEventsListener() {

            @Override
            public void addSucceededEvent() {
                Toast.makeText(TourActivity.this, "Tour Saved!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void addFailedEvent() {
                Toast.makeText(TourActivity.this, "Tour not saved!", Toast.LENGTH_SHORT).show();
            }
        });

        Intent j = new Intent(TourActivity.this, MainActivity_.class);
        startActivity(j);
        finish();
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
            //cursor.close();
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
}