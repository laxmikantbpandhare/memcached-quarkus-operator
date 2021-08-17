package com.example;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

public class IncrementCounter {

    private Counter increaseSuccessCounter;
    private final MeterRegistry meterRegistry;
    private String tagType;
    private String tagName;
    private String description;
    private String name;

    IncrementCounter(MeterRegistry meterRegistry, String name, String tagName,String tagType, String description){
        this.meterRegistry = meterRegistry;
        this.name = name;
        this.tagName = tagName;
        this.tagType = tagType;
        this.description = description;
        initCounter();
    }

    private void initCounter() {
        increaseSuccessCounter = Counter.builder(name)    // 1 - create a counter using the fluent API.
                .tag(tagName, tagType)
                .description(description)
                .register(meterRegistry);

//        increaseFailCounter = Counter.builder("Failed Controller Executions")    // 1 - create a counter using the fluent API.
//                .tag("failed", "Total Number of Failed Controller Executions")
//                .description("This counter will count the number of failed reconciliation happened for the operator.")
//                .register(meterRegistry);
    }

    public void counterIncrement(CounterDetails counterDetails){
        increaseSuccessCounter.increment(); // 2 -  Increment the counter
        System.out.println("here"+increaseSuccessCounter);
    }

//    public void counterFailIncrement(CounterDetails counterDetails){
//        increaseFailCounter.increment(); // 2 -  Increment the counter
//        System.out.println("Failed here"+increaseFailCounter);
//    }
}
