package at.fhooe.mos.app.mosproject.ui;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.StepDetectorService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements StepDetectorService.Listener {

    private StepDetectorService stepDetectorService;
    private Intent stepDetectorServiceIntent;
    private ServiceConnection stepDetectorServiceConnection;

    private boolean isStepDetectorServiceRunning = false;

    @BindView(R.id.stepCount)
    TextView stepCountTextView;

    @BindView(R.id.launchStepDetectorTestActivityButton)
    Button launchStepDetectorTestActivityButton;

    @BindView(R.id.startStopStepDetectorService)
    Button startStopStepDetectorService;

    @OnClick(R.id.launchStepDetectorTestActivityButton)
    public void onStepDetectorTestClick() {
        Intent intent = new Intent(MainActivity.this, StepDetectorTestActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.startStopStepDetectorService)
    public void onStepDetectorServiceClick() {
        if (isStepDetectorServiceRunning) {
            isStepDetectorServiceRunning = false;

            unbindStepDetectorService();
            stopService(stepDetectorServiceIntent);

            startStopStepDetectorService.setText("Start Step Detector");
        } else {
            isStepDetectorServiceRunning = true;

            startService(stepDetectorServiceIntent);
            bindStepDetectorService();

            startStopStepDetectorService.setText("Stop Step Detector");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        stepDetectorServiceIntent = new Intent(MainActivity.this, StepDetectorService.class);
        stepDetectorServiceConnection = new StepDetectorServiceConnection();

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isStepDetectorServiceRunning){
            bindStepDetectorService();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isStepDetectorServiceRunning){
            if(stepDetectorService != null){
                stepDetectorService.removeListener();
            }

            unbindStepDetectorService();
        }
    }

    @Override
    public void onNewStepCountValue(int stepCount) {
        stepCountTextView.setText("Steps: " + stepCount);
    }

    private void bindStepDetectorService() {
        bindService(stepDetectorServiceIntent, stepDetectorServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindStepDetectorService() {
        unbindService(stepDetectorServiceConnection);
        stepDetectorService = null;
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private class StepDetectorServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            StepDetectorService.LocalBinder binder = (StepDetectorService.LocalBinder) service;

            stepDetectorService = binder.getServiceInstance(); //Get instance of your service!
            stepDetectorService.registerListener(MainActivity.this); //Activity register
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }
}
