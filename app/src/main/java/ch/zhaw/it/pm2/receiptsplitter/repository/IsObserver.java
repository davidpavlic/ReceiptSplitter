package ch.zhaw.it.pm2.receiptsplitter.repository;

/**
 * This interface represents an observable object.
 * It can have multiple observers that are notified when the state of the observable object changes.
 *
 * @author Suhejl Asani, Ryan Simmonds, Kaspar Streiff, David Pavlic
 * @version 1.0
 */
public interface IsObserver {
    /**
     * Updates the observer.
     */
    void update();
}
