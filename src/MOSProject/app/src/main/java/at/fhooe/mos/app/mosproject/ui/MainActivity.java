package at.fhooe.mos.app.mosproject.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.InstanceState;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.StepDetectorService;
import at.fhooe.mos.app.mosproject.stopwatch.Stopwatch;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

@EActivity
public class MainActivity extends AppCompatActivity {

    private Intent stepDetectorServiceIntent;

    @InstanceState
    public boolean isStepDetectorServiceRunning = false;

    private Stopwatch stopwatch = new Stopwatch();

    @BindView(R.id.stepCount)
    TextView stepCountTextView;

    @BindView(R.id.stopwatch)
    TextView stopwatchTextView;

    @BindView(R.id.launchStepDetectorTestActivityButton)
    Button launchStepDetectorTestActivityButton;

    @BindView(R.id.startStopStepDetectorService)
    Button startStopStepDetectorService;

    @OnClick(R.id.openHeartRateSimulation)
    public void onOpenHeartRateSimulationClick() {
        Intent intent = new Intent(MainActivity.this, HeartRateSimulationActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.openHeartRateDevice)
    public void onOpenHeartRateDeviceClick() {
        Intent intent = new Intent(MainActivity.this, ScanHeartRateDeviceActivity.class);
        startActivity(intent);
    }

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

            stopwatch.start();
        } else {
            isStepDetectorServiceRunning = true;

            EventBus.getDefault().register(this);
            startService(stepDetectorServiceIntent);

            stopwatch.stop();
        }

        updateStartStopStepDetectorServiceText();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        stepDetectorServiceIntent = new Intent(MainActivity.this, StepDetectorService.class);

        ButterKnife.bind(this);

        updateStartStopStepDetectorServiceText();
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
        if(isStepDetectorServiceRunning){
            EventBus.getDefault().unregister(this);
        }

        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStepCountEvent(StepDetectorService.StepCountEvent event) {
        stepCountTextView.setText("Steps: " + event.stepCount);
    }

    private void updateStartStopStepDetectorServiceText(){
        if (isStepDetectorServiceRunning) {
            startStopStepDetectorService.setText("Stop Step Detector");
        } else {
            startStopStepDetectorService.setText("Start Step Detector");
        }
    }
}
