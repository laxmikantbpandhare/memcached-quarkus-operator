package com.example;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

public class IncrementCounter {

    private Counter increaseSuccessCounter;
    private Counter increaseFailCounter;
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
        increaseSuccessCounter = Counter.builder(name)    // 1 - create a counter using the fluent API.
                .tag("succeeded", type)
                .description(description)
                .register(meterRegistry);

        increaseFailCounter = Counter.builder(name)    // 1 - create a counter using the fluent API.
                .tag("failed", type)
                .description(description)
                .register(meterRegistry);
    }

    public void counterSuccessIncrement(CounterDetails counterDetails){
        increaseSuccessCounter.increment(); // 2 -  Increment the counter
        System.out.println("here"+increaseSuccessCounter);
    }

    public void counterFailIncrement(CounterDetails counterDetails){
        increaseFailCounter.increment(); // 2 -  Increment the counter
        System.out.println("here"+increaseFailCounter);
    }
}
