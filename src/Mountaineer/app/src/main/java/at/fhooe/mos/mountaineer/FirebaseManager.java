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
    private static final String TOURS_REFERENCE = "tours";

    private ArrayList<FirebaseEventListener> firebaseEventListeners = new ArrayList<>();
    private DatabaseReference userToursDb;

    public FirebaseManager(String userId) {
        userToursDb = FirebaseDatabase.getInstance().getReference(TOURS_REFERENCE + "/" + userId);
    }

    public String addTour(Tour tour) {
        String key = userToursDb.push().getKey();
        userToursDb.child(key).setValue(tour);
        return key;
    }

    public void fetchTour(String id) {
        userToursDb.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tour tour = dataSnapshot.getValue(Tour.class);

                for (FirebaseEventListener listener : firebaseEventListeners) {
                    listener.onFetchEvent(tour);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DATABASE ERROR", databaseError.toString());
            }
        });
    }

    public void addListener(FirebaseEventListener listener) {
        firebaseEventListeners.add(listener);
    }

    public void removeListener(FirebaseEventListener listener) {
        firebaseEventListeners.remove(listener);
    }
}
