package at.fhooe.mos.mountaineer;

/**
 * Created by stefan on 05.12.2017.
 */

public interface EventSourceInterface<T> {
    void registerListener(T listener);
    void removeListener(T listener);
}