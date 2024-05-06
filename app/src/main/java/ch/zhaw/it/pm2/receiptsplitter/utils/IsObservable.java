package ch.zhaw.it.pm2.receiptsplitter.utils;

public interface IsObservable {
    /**
     * Adds an observer to the list of observers.
     * @param observer the observer to add
     */
    void addObserver(IsObserver observer);

    /**
     * Notifies all observers.
     */
    void notifyObservers();
}
