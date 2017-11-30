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
import at.fhooe.mos.app.mosproject.heartrate.HRCalculation;
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
    HRCalculation hrCalculation = new HRCalculation();
    Handler handler = new Handler();
    ArrayList<Integer> heartRateValues = new ArrayList<>();
    ArrayList<Float> trimps = new ArrayList<>();
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

        SensorFactory sensorFactory;
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
            int heartRate = heartRateMonitor.getHeartRate();
            heartRateValues.add(heartRate);
            updateUI();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    simulate();
                }
            }, 1000);
        }
    }

    public void updateUI() {
        currentHR.setText(String.valueOf(heartRateValues.get(heartRateValues.size()-1)));
        minHR.setText(String.valueOf(Collections.min(heartRateValues)));
        maxHR.setText(String.valueOf(Collections.max(heartRateValues)));
        averageHR.setText(String.valueOf(hrCalculation.getAverageHR(heartRateValues)));

        maxHRPercent.setText(String.valueOf(hrCalculation.getPercentHRmax(
                maxHRInput.getEditText().getText().toString(),
                ageInput.getEditText().getText().toString(),
                maxHR.getText().toString()
        )));
    }

    public void resetUI() {
        heartRateValues = new ArrayList<>();
        currentHR.setText("0");
        minHR.setText("0");
        maxHR.setText("0");
        averageHR.setText("0");
        maxHRPercent.setText("0");
    }

    private void drawTrimpDataChart(){
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        if(trimps.size() == 0)
            return;

        ArrayList<Float> fitness = hrCalculation.getFitness(trimps);
        ArrayList<Float> fatigue = hrCalculation.getFatigue(trimps);
        ArrayList<Float> performance = hrCalculation.getPerformance(fitness, fatigue, trimps);

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
