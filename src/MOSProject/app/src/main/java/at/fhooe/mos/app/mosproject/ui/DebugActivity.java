package at.fhooe.mos.app.mosproject.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

import at.fhooe.mos.app.mosproject.FetchEventListener;
import at.fhooe.mos.app.mosproject.FirebaseManager;
import at.fhooe.mos.app.mosproject.PersistenceManager;
import at.fhooe.mos.app.mosproject.R;
import at.fhooe.mos.app.mosproject.models.Training;

public class DebugActivity extends AppCompatActivity implements FetchEventListener {
    PersistenceManager persistenceManager;
    FirebaseManager firebaseManager;
    EditText display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button add = findViewById(R.id.add);
        Button fetch = findViewById(R.id.fetch);
        display = findViewById(R.id.display);

        final Training training = new Training(123, "2:55", "17th November 2017", 200, 10, 6, 88, 123, 30);

        persistenceManager = new PersistenceManager(this);
        firebaseManager = new FirebaseManager(persistenceManager);
        firebaseManager.addListener(this);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = firebaseManager.addTraining(training);
                persistenceManager.addTrainingId(key);
            }
        });

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> keys = persistenceManager.getTrainingIds();

                for(String key : keys) {
                    firebaseManager.fetchTraining(key);
                }
            }
        });
    }

    @Override
    public void onFetchEvent(Training training) {
        display.setText(training.toString());
    }
}
