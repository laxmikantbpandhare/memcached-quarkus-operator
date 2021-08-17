package com.example;

public class CounterDetails {

    int counter;
    String name;
    String tagName;
    String tagType;
    String description;

    public CounterDetails(int counter, String name, String tagName, String tagType, String description) {
        this.counter = counter;
        this.name = name;
        this.tagName = tagName;
        this.tagType = tagType;
        this.description = description;
    }


    public String getTagType() {
        return tagType;
    }

    public void setTagType(String tagType) {
        this.tagType = tagType;
    }
    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
