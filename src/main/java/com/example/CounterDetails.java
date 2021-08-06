package com.example;

public class CounterDetails {

    int counter;
    String name;
    String type;
    String description;

    public CounterDetails(int counter, String name, String type, String description) {
        this.counter = counter;
        this.name = name;
        this.type = type;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
