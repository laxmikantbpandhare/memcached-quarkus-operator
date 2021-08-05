package com.example;

public class CounterDetails {

    int counter;
    String type;

    public CounterDetails(int counter, String type) {
        this.counter = counter;
        this.type = type;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
