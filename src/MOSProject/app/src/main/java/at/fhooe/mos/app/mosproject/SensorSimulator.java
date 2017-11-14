package at.fhooe.mos.app.mosproject;

import android.database.Observable;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stefan on 10.11.2017.
 */

public class SensorSimulator {
    private ArrayList<SimulatedSensorEventListener> sensorEventListeners = new ArrayList<>();
    private SimulationFinishedEvent simulationFinishedEvent;
    private int nextSensorEventIndex = 0;
    private int maxSensorEventIndex = 0;
    private boolean isRunning = false;
    private List<SensorEventData> sensorEvents = new ArrayList<>();

    Handler handler = new Handler();

    public void registerSimulationFinishedEvent(SimulationFinishedEvent simulationFinishedEvent) {
        this.simulationFinishedEvent = simulationFinishedEvent;
    }

    public void registerListener(SimulatedSensorEventListener listener)
    {
        sensorEventListeners.add(listener);
    }

    public void setSensorEvents(List<SensorEventData> sensorEvents)
    {
        this.sensorEvents = sensorEvents;
    }

    public void start()
    {
        isRunning = true;
        nextSensorEventIndex = 0;
        maxSensorEventIndex = sensorEvents.size() - 1;
        runNextSimulationStep();
    }

    public void stop()
    {
        isRunning = false;
    }

    private void runNextSimulationStep()
    {
        if(isRunning && nextSensorEventIndex < maxSensorEventIndex)
        {
            SensorEventData sensorEventData = sensorEvents.get(nextSensorEventIndex);
            for (SimulatedSensorEventListener listener : sensorEventListeners) {
                listener.onSensorChanged(sensorEventData);
            }

            long timeBetweenEvents = sensorEvents.get(nextSensorEventIndex+1).getTimestamp() - sensorEventData.getTimestamp();

            timeBetweenEvents = timeBetweenEvents / 1000000L;

            nextSensorEventIndex++;

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    runNextSimulationStep();
                }
            }, timeBetweenEvents);
        }
        else{
            if(simulationFinishedEvent != null)
                simulationFinishedEvent.onSimulationFinished();
        }
    }
}
