package ch.zhaw.it.pm2.receiptsplitter.repository;

public interface IsObservable {
    void addObserver(IsObserver observer);
    void removeObserver(IsObserver observer);
    void notifyObservers();
}
