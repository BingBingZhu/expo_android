package com.expo.base;

public class BaseEventMessage<T> {

    public int id;

    public T t;

    public BaseEventMessage(int id, T t) {
        this.id = id;
        this.t = t;
    }
}
