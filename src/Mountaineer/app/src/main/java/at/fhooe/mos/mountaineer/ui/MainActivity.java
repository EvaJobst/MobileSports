package at.fhooe.mos.mountaineer.ui;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import at.fhooe.mos.mountaineer.R;
import at.fhooe.mos.mountaineer.TourPreviewAdapter;
import at.fhooe.mos.mountaineer.model.tour.Tour;
import at.fhooe.mos.mountaineer.persistence.FirebaseFetchEventListener;
import at.fhooe.mos.mountaineer.persistence.FirebaseManager;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.main_activity_menu)
public class MainActivity extends AppCompatActivity implements FirebaseFetchEventListener {
    public static final int PERMISSION_REQUEST_CODE = 1;
    TourPreviewAdapter tourPreviewAdapter;
    FirebaseManager firebaseManager;

    @ViewById
    Toolbar mainToolbar;

    @OptionsMenuItem
    protected MenuItem mainActivityMenuItem;

    @ViewById
    FloatingActionButton addTourButton;

    @ViewById
    RecyclerView previewRecyclerView;

    @Click(R.id.addTourButton)
    public void onAddTourButtonClick() {
        if (checkPermissions()) {
            startTourActivity();
            finish();   //end this activity so the user can not use the back button to go back
        }
    }

    @OptionsItem(R.id.mainActivityMenuItem)
    protected void onOptionsItemClicked() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setSupportActionBar(mainToolbar);

        firebaseManager = new FirebaseManager("user1");
        firebaseManager.fetchTours(this);
    }

    private void startTourActivity() {
        if (!checkBLEEnabled()) {
            Toast.makeText(this, "Please activate the Bluetooth Feature!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!checkLocationEnabled()) {
            Toast.makeText(this, "Please activate the GPS Feature!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i = new Intent(this, TourActivity_.class);
        startActivity(i);
    }

    private boolean checkPermissions() {
        String[] requiredPermissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        boolean permissionMissing = false;

        for (String permission : requiredPermissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionMissing = true;
                break;
            }
        }

        if (permissionMissing) {
            ActivityCompat.requestPermissions(this, requiredPermissions, PERMISSION_REQUEST_CODE);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                startTourActivity();
            } else {
                Toast.makeText(this, "Permission not granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        }

        return true;
    }

    private boolean checkBLEEnabled() {
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            return false;
        }

        return true;
    }

    @Override
    public void onFetchEvent(Tour tour) {}

    @Override
    public void onFetchEvent(ArrayList<Tour> tours) {
        tourPreviewAdapter = new TourPreviewAdapter(this, tours);
        previewRecyclerView.setItemAnimator(new DefaultItemAnimator());
        previewRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        previewRecyclerView.setAdapter(tourPreviewAdapter);
    }
}
