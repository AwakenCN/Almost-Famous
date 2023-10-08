package com.lung.utils.pattern;

public class ObserverPatternExample {
    public static void main(String[] args) {
        ConcreteSubject subject = new ConcreteSubject();
        ConcreteObserver observer1 = new ConcreteObserver(subject);
        ConcreteObserver observer2 = new ConcreteObserver(subject);

        subject.setState(1); // 主题状态变化，观察者会收到通知并作出响应
        subject.setState(2);

        subject.detach(observer2); // 移除一个观察者

        subject.setState(3); // 主题状态变化，只有一个观察者会收到通知
    }
}
