package at.fhooe.mos.mountaineer;


import at.fhooe.mos.mountaineer.model.Tour;

/**
 * Created by Eva on 17.11.2017.
 */

public interface FirebaseEventListener {
    void onFetchEvent(Tour tour);
}
