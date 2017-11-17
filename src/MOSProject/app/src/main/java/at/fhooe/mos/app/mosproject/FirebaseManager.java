package at.fhooe.mos.app.mosproject;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import at.fhooe.mos.app.mosproject.models.Training;

/**
 * Created by Eva on 17.11.2017.
 */

public class FirebaseManager {
    ArrayList<FetchEventListener> listenerList = new ArrayList<>();
    private String TRAINING_IDS_KEY = "trainingIds";
    private DatabaseReference dataReference = FirebaseDatabase.getInstance().getReference("data");
    private PersistenceManager persistence;

    public FirebaseManager(PersistenceManager persistence) {
        this.persistence = persistence;
    }

    public String addTraining(Training training) {
        String key = dataReference.push().getKey();
        dataReference.child(key).setValue(training);
        return key;
    }

    public void fetchTraining(String id) {
        dataReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Training training = dataSnapshot.getValue(Training.class);
                notifyListener(training);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DATABASE ERROR", databaseError.toString());
            }
        });
    }

    public void addListener(FetchEventListener listener) {
        listenerList.add(listener);
    }

    public void removeListener(FetchEventListener listener) {
        listenerList.remove(listener);
    }

    public void notifyListener(Training training) {
        for(FetchEventListener listener : listenerList) {
            listener.onFetchEvent(training);
        }
    }
}
