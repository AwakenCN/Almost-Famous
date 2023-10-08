package com.lung.utils.pattern;

// 具体观察者类，实现了观察者接口，将收到的通知打印出来
public class ConcreteObserver implements Observer {
    private ConcreteSubject subject;

    public ConcreteObserver(ConcreteSubject subject) {
        this.subject = subject;
        subject.attach(this);
    }

    @Override
    public void update() {
        System.out.println("Subject state changed: " + subject.getState());
    }
}
