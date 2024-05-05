package ch.zhaw.it.pm2.receiptsplitter.utils;

public interface IsObservable {
    void addObserver(IsObserver observer);
    void removeObserver(IsObserver observer);
    void notifyObservers();
}
