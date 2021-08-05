package com.example;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

public class IncrementCounter {

    private static Counter increaseTheCounter;
    private final MeterRegistry meterRegistry;
    String type;

    IncrementCounter(MeterRegistry meterRegistry, String type){
        this.meterRegistry = meterRegistry;
        this.type = type;
        initCounter();
    }


    private void initCounter() {
        increaseTheCounter = Counter.builder("counter.types")    // 2- create a counter using the fluent API
                .tag("type", type)
                .description("The number of reconciliation ever processed by Operator")
                .register(meterRegistry);
    }

    public static void counterIncrement(CounterDetails counterDetails){
        increaseTheCounter.increment();
        System.out.println("here"+increaseTheCounter);
    }
}
