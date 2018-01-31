package at.fhooe.mos.app.mosproject.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import at.fhooe.mos.app.mosproject.FetchEventListener;
import at.fhooe.mos.app.mosproject.FirebaseManager;
import at.fhooe.mos.app.mosproject.PersistenceManager;
import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.model.Training;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DebugActivity extends AppCompatActivity implements FetchEventListener {
    PersistenceManager persistenceManager;
    FirebaseManager firebaseManager;

    @BindView(R.id.display)
    EditText display;

    @BindView(R.id.add)
    Button add;

    @BindView(R.id.fetch)
    Button fetch;

    @OnClick(R.id.fetch)
    public void onFetchClick() {
        ArrayList<String> keys = persistenceManager.getTrainingIds();

        for(String key : keys) {
            firebaseManager.fetchTraining(key);
        }
    }

    Training training = new Training(123, "2:55", "17th November 2017", 200, 10, 6, 88, 123, 30);

    @OnClick(R.id.add)
    public void onAddClick() {
        String key = firebaseManager.addTraining(training);
        persistenceManager.addTrainingId(key);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        persistenceManager = new PersistenceManager(this);
        firebaseManager = new FirebaseManager(persistenceManager);
        firebaseManager.addListener(this);
    }

    @Override
    public void onFetchEvent(Training training) {
        display.setText(training.toString());
    }
}
