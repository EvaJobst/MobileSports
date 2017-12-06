package at.fhooe.mos.mountaineer.persistence;


import at.fhooe.mos.mountaineer.model.tour.Tour;

/**
 * Created by Eva on 17.11.2017.
 */

public interface FirebaseFetchEventListener {
    void onFetchEvent(Tour tour);
}
