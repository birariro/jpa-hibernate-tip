package com.example.jpatipsample.utils.support;

public class Counter {

    private int count;

    public void increase() {
        count++;
    }

    public int getCount() {
        return count;
    }

    public void init() {
        count = 0;
    }
}