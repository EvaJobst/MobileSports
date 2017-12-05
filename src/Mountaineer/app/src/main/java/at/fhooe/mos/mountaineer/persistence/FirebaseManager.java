package at.fhooe.mos.mountaineer.persistence;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import at.fhooe.mos.mountaineer.EventSource;
import at.fhooe.mos.mountaineer.model.Tour;
import at.fhooe.mos.mountaineer.model.TourDetails;


/**
 * Created by Eva on 17.11.2017.
 */

public class FirebaseManager extends EventSource<FirebaseFetchEventListener> {
    private static final String TOURS_REFERENCE = "tours";
    private static final String TOUR_DETAILS_REFERENCE = "tourDetails";

    private DatabaseReference userToursDb;
    private DatabaseReference userTourDetailsDb;

    public FirebaseManager(String userId) {
        userToursDb = FirebaseDatabase.getInstance().getReference(TOURS_REFERENCE + "/" + userId);
        userTourDetailsDb = FirebaseDatabase.getInstance().getReference(TOUR_DETAILS_REFERENCE + "/" + userId);
    }

    public void addTour(final Tour tour, final FirebaseAddEventsListener eventListener) {
        final TourDetails tourDetails = tour.getTourDetails();
        tour.setTourDetails(null);  //temporally remove details to save tour and details separately

        userToursDb.push().setValue(tour, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError == null){
                    String key = databaseReference.getKey();

                    userTourDetailsDb.child(key).setValue(tourDetails, new DatabaseReference.CompletionListener() {
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
                else{
                    eventListener.addFailedEvent();
                }
            }
        });

        tour.setTourDetails(tourDetails);   //add details again
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
