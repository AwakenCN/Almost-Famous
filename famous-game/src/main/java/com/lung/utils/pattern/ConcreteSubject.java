package com.lung.utils.pattern;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author haoyitao
 * @implSpec 观察者模式
 *  观察者模式（Observer Pattern）是一种行为型设计模式，它定义了对象之间的一种一对多的依赖关系。当一个对象的状态发生变化时，所有依赖于它的对象都会得到通知并自动更新。
 *  这种模式由两个核心组件组成：主题（Subject）和观察者（Observer）。主题负责维护一系列观察者，以及发送通知。观察者则定义了一个更新方法，用于接收主题的通知并作出相应的处理。
 * @since 2023/10/8 - 18:06
 * @version 1.0
 */
public class ConcreteSubject implements Subject {
    private List<Observer> observers = new ArrayList<>();
    @Getter
    private int state;

    public void setState(int state) {
        this.state = state;
        notifyObservers();
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}



