package at.fhooe.mos.app.mosproject;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

import java.util.ArrayList;

/**
 * Created by stefan on 09.11.2017.
 */

public class SensorRecorder implements SensorEventListener {

    private ArrayList<SensorEventData> recordedSensorEvents = new ArrayList<>();

    private boolean isRecording = false;

    StepDetector.DebugEventListener debugListener;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        float[] values = sensorEvent.values.clone();

        SensorEventData eventData = new SensorEventData(sensorEvent.sensor.getType(), sensorEvent.accuracy, sensorEvent.timestamp, values);

        if(isRecording){
            recordedSensorEvents.add(eventData);
        }

        if(debugListener != null)
            debugListener.onDebugEvent(sensorEvent.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void registerDebugListener(StepDetector.DebugEventListener debugListener) {
        this.debugListener = debugListener;
    }

    public void startRecording()
    {
        isRecording = true;
    }

    public void stopRecording()
    {
        isRecording = false;
    }

    public ArrayList<SensorEventData> getRecordedData(){
        return recordedSensorEvents;
    }

    public void clearRecordedData(){
        recordedSensorEvents = new ArrayList<>();
    }
}
