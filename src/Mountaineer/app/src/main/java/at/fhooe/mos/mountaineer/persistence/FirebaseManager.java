package at.fhooe.mos.mountaineer.persistence;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import at.fhooe.mos.mountaineer.EventSource;
import at.fhooe.mos.mountaineer.model.Tour;


/**
 * Created by Eva on 17.11.2017.
 */

public class FirebaseManager extends EventSource<FirebaseFetchEventListener> {
    private static final String TOURS_REFERENCE = "tours";

    private DatabaseReference userToursDb;

    public FirebaseManager(String userId) {
        userToursDb = FirebaseDatabase.getInstance().getReference(TOURS_REFERENCE + "/" + userId);
    }

    public void addTour(Tour tour, final FirebaseAddEventsListener eventListener) {
        userToursDb.push().setValue(tour, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError == null){
                    eventListener.addSucceededEvent();
                }
                else{
                    eventListener.addFailedEvent();
                }
            }
        });
    }

    public void fetchTour(String id) {
        userToursDb.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Tour tour = dataSnapshot.getValue(Tour.class);

                for (FirebaseFetchEventListener listener : FirebaseManager.super.eventListeners) {
                    listener.onFetchEvent(tour);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DATABASE ERROR", databaseError.toString());
            }
        });
    }
}
