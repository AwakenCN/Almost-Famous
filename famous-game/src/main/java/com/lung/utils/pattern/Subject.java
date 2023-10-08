package com.lung.utils.pattern;

public interface Subject {

    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers();
}
