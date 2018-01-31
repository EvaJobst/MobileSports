package at.fhooe.mos.app.mosproject.ui;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.heartrate.EnergyExpenditureCalculator;
import at.fhooe.mos.app.mosproject.heartrate.HRCalculation;
import at.fhooe.mos.app.mosproject.heartrate.HRM;
import at.fhooe.mos.app.mosproject.heartrate.SensorFactory;
import at.fhooe.mos.app.mosproject.heartrate.SimulationFactory;
import at.fhooe.mos.app.mosproject.heartrate.SimulationTrimp;
import at.fhooe.mos.app.mosproject.heartrate.Trimp;
import at.fhooe.mos.app.mosproject.model.user.UserInformation;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HeartRateSimulationActivity extends AppCompatActivity {
    HRCalculation hrCalculation = new HRCalculation();
    UserInformation userInformation;
    Handler handler = new Handler();
    ArrayList<Integer> heartRateValues = new ArrayList<>();
    ArrayList<Float> trimps = new ArrayList<>();
    HRM heartRateMonitor;
    boolean isRunning = false;
    String inputHrMaxPercent;
    //String age;
    double calories = 0;

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

    @BindView(R.id.kCal)
    TextView kCal;

    @BindView(R.id.startStopSimulation)
    Button startSensorSimulator;

    @BindView(R.id.trimpDataChart)
    LineChart trimpDataChart;

    @OnClick(R.id.startStopSimulation)
    public void onStartSensorSimulationClick() {
        isRunning = !isRunning;

        if(isRunning) {
            startSensorSimulator.setText("Stop Simulation");

            heartRateMonitor.initialize(this);

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

        userInformation = new Gson().fromJson(getIntent().getExtras().get("user").toString(), UserInformation.class);
        //age = String.valueOf(userInformation.getAge());
        inputHrMaxPercent = getIntent().getExtras().get("inputHrMaxPercent").toString();

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

        double heartRate = heartRateValues.get(heartRateValues.size()-1);
        double newCalories = EnergyExpenditureCalculator.calculateEnergyExpenditureEstimation(userInformation, heartRate) / 60;
        calories = calories + newCalories;
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        kCal.setText(decimalFormat.format(calories));

        maxHRPercent.setText(String.valueOf(hrCalculation.getPercentHRmax(
                inputHrMaxPercent,
                String.valueOf(userInformation.getAge()),
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
        kCal.setText("0");
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
