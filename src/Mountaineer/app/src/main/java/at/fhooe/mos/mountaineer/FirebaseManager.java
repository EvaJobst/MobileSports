package at.fhooe.mos.mountaineer;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import at.fhooe.mos.mountaineer.model.Tour;


/**
 * Created by Eva on 17.11.2017.
 */

public class FirebaseManager {
    ArrayList<FetchEventListener> listenerList = new ArrayList<>();
    private DatabaseReference dataReference = FirebaseDatabase.getInstance().getReference("data");

    public String addTour(Tour tour) {
        String key = dataReference.push().getKey();
        dataReference.child(key).setValue(tour);
        return key;
    }

    public void fetchTour(String id) {
        dataReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tour tour = dataSnapshot.getValue(Tour.class);
                notifyListener(tour);
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

    public void notifyListener(Tour tour) {
        for (FetchEventListener listener : listenerList) {
            listener.onFetchEvent(tour);
        }
    }
}
