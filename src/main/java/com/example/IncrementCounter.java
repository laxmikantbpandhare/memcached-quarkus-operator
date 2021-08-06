package com.example;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

public class IncrementCounter {

    private Counter increaseTheCounter;
    private final MeterRegistry meterRegistry;
    private String type;
    private String description;
    private String name;

    IncrementCounter(MeterRegistry meterRegistry, String name, String type, String description){
        this.meterRegistry = meterRegistry;
        this.name = name;
        this.type = type;
        this.description = description;
        initCounter();
    }


    private void initCounter() {
        increaseTheCounter = Counter.builder(name)    // 1 - create a counter using the fluent API.
                .tag("Counter type", type)
                .description(description)
                .register(meterRegistry);
    }

    public void counterIncrement(CounterDetails counterDetails){
        increaseTheCounter.increment(); // 2 -  Increment the counter.
        System.out.println("here"+increaseTheCounter);
    }
}
