package at.fhooe.mos.app.mosproject.ui;

import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.heartrate.HRM;
import at.fhooe.mos.app.mosproject.heartrate.SensorFactory;
import at.fhooe.mos.app.mosproject.heartrate.SimulationFactory;
import at.fhooe.mos.app.mosproject.heartrate.SimulationHRM;
import at.fhooe.mos.app.mosproject.heartrate.SimulationTrimp;
import at.fhooe.mos.app.mosproject.heartrate.Trimp;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HeartRateSimulationActivity extends AppCompatActivity {
    Handler handler = new Handler();
    ArrayList<Integer> heartRateValues = new ArrayList<>();
    ArrayList<Float> trimps = new ArrayList<>();
    SensorFactory sensorFactory;
    HRM heartRateMonitor;
    boolean isRunning = false;

    @BindView(R.id.currentHR)
    TextView currentHR;

    @BindView(R.id.averageHR)
    TextView averageHR;

    @BindView(R.id.minHR)
    TextView minHR;

    @BindView(R.id.maxHR)
    TextView maxHR;

    @BindView(R.id.maxHRPercent)
    TextView maxHRPercent;

    @BindView(R.id.startStopSimulation)
    Button startSensorSimulator;

    @BindView(R.id.ageInput)
    TextInputLayout ageInput;

    @BindView(R.id.maxHRInput)
    TextInputLayout maxHRInput;

    @BindView(R.id.trimpDataChart)
    LineChart trimpDataChart;

    @OnClick(R.id.startStopSimulation)
    public void onStartSensorSimulationClick() {
        isRunning = !isRunning;

        if(isRunning) {
            startSensorSimulator.setText("Stop Simulation");

            if(heartRateMonitor instanceof SimulationHRM) {
                ((SimulationHRM)heartRateMonitor).openFile(this);
            }

            simulate();
        }

        else {
            resetUI();
            startSensorSimulator.setText("Start Simulation");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_simulation);
        ButterKnife.bind(this);

        sensorFactory = new SimulationFactory();

        heartRateMonitor = sensorFactory.createHRM();

        Trimp trimp = sensorFactory.createTrimp();

        if (trimp instanceof SimulationTrimp) {
            ((SimulationTrimp)trimp).openFile(this);
        }

        trimps = trimp.getTrimpData();
        drawTrimpDataChart();
    }

    public void simulate() {
        if (isRunning) {
            String heartRate = heartRateMonitor.getHeartRate();
            String[] values = heartRate.split(",");
            heartRateValues.add(Integer.parseInt(values[1]));
            updateUI();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    simulate();
                }
            }, 1000);
        }
    }

    public ArrayList<Float> getFitness() {
        ArrayList<Float> fitness = new ArrayList<>();

        for(int i = 0; i < trimps.size(); i++) {
            if(fitness.isEmpty()) {
                fitness.add(1000f);
            }

            else {
                float value = (float) (fitness.get(i-1) * Math.exp(-1/40) + trimps.get(i));
                fitness.add(value);
            }
        }

        return fitness;
    }

    public ArrayList<Float> getFatigue() {
        ArrayList<Float> fatigue = new ArrayList<>();

        for(int i = 0; i < trimps.size(); i++) {
            if(fatigue.isEmpty()) {
                fatigue.add(trimps.get(i));
            }

            else {
                float value = (float) (fatigue.get(i-1) * Math.exp(-1/11) + trimps.get(i));
                fatigue.add(value);
            }
        }

        return fatigue;
    }

    public ArrayList<Float> getPerformance(ArrayList<Float> fitness, ArrayList<Float> fatigue) {
        ArrayList<Float> performance = new ArrayList<>();

        for(int i = 0; i < trimps.size(); i++) {
            float value = fitness.get(i) - fatigue.get(i) * 2;
            performance.add(value);
        }

        return performance;
    }

    public void updateUI() {
        currentHR.setText(String.valueOf(heartRateValues.get(heartRateValues.size()-1)));
        minHR.setText(String.valueOf(Collections.min(heartRateValues)));
        maxHR.setText(String.valueOf(Collections.max(heartRateValues)));
        averageHR.setText(String.valueOf(getAverageHR()));
        maxHRPercent.setText(String.valueOf(getPercentHRmax()));
    }

    public void resetUI() {
        heartRateValues = new ArrayList<>();
        currentHR.setText("0");
        minHR.setText("0");
        maxHR.setText("0");
        averageHR.setText("0");
        maxHRPercent.setText("0");
    }

    public int getAverageHR() {
        int sum = 0;
        for(int value : heartRateValues) {
            sum += value;
        }

        return sum/heartRateValues.size();
    }

    public int getPercentHRmax() {
        double hrMax;
        if(!maxHRInput.getEditText().getText().toString().isEmpty()) {
            hrMax = Double.parseDouble(maxHRInput.getEditText().getText().toString());
        }

        else if(!ageInput.getEditText().getText().toString().isEmpty()) {
            String age = ageInput.getEditText().getText().toString();
            hrMax = 208 - (0.7 * Double.parseDouble(age));
        }

        else {
            return 0;
        }

        double percent = (100 * Integer.parseInt(maxHR.getText().toString())) / hrMax;
        return (int) percent;
    }

    private void drawTrimpDataChart(){
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        if(trimps.size() == 0)
            return;

        ArrayList<Float> fitness = getFitness();
        ArrayList<Float> fatigue = getFatigue();
        ArrayList<Float> performance = getPerformance(fitness, fatigue);

        ArrayList<Entry> fitnessEntries = new ArrayList<>();
        ArrayList<Entry> fatigueEntries = new ArrayList<>();
        ArrayList<Entry> performanceEntries = new ArrayList<>();

        for(int x = 0; x < trimps.size(); x++) {
            fitnessEntries.add(new Entry(x, fitness.get(x)));
            fatigueEntries.add(new Entry(x, fatigue.get(x)));
            performanceEntries.add(new Entry(x, performance.get(x)));
        }

        LineDataSet dataSetFitness = new LineDataSet(fitnessEntries, "fitness");
        LineDataSet dataSetFatigue = new LineDataSet(fatigueEntries, "fatigue");
        LineDataSet dataSetPerformance = new LineDataSet(performanceEntries, "performance");

        dataSetFitness = ChartVisualization.trimpSimulationData(dataSetFitness, "fitness");
        dataSetFatigue = ChartVisualization.trimpSimulationData(dataSetFatigue, "fatigue");
        dataSetPerformance = ChartVisualization.trimpSimulationData(dataSetPerformance, "performance");

        dataSets.add(dataSetFitness);
        dataSets.add(dataSetFatigue);
        dataSets.add(dataSetPerformance);
        LineData data = new LineData(dataSets);

        trimpDataChart.clear();
        trimpDataChart.setData(data);
    }
}
