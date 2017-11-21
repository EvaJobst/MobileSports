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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.StepDetectorService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private Intent stepDetectorServiceIntent;

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

            stopService(stepDetectorServiceIntent);
            EventBus.getDefault().unregister(this);

            startStopStepDetectorService.setText("Start Step Detector");
        } else {
            isStepDetectorServiceRunning = true;

            EventBus.getDefault().register(this);
            startService(stepDetectorServiceIntent);

            startStopStepDetectorService.setText("Stop Step Detector");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        stepDetectorServiceIntent = new Intent(MainActivity.this, StepDetectorService.class);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isStepDetectorServiceRunning){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isStepDetectorServiceRunning){
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStepCountEvent(StepDetectorService.StepCountEvent event) {
        stepCountTextView.setText("Steps: " + event.stepCount);
    }

}
