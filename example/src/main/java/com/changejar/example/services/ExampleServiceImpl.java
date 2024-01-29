package com.changejar.example.services;

import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExampleServiceImpl implements ExampleService {
    Logger logger = LoggerFactory.getLogger(ExampleServiceImpl.class);
    private final Counter apiCounter;
    private final AtomicInteger gauge;

    @Autowired
    public ExampleServiceImpl(MeterRegistry statsdMeterRegistry) {
        apiCounter = statsdMeterRegistry.counter("jar.potato.counter");
        gauge = statsdMeterRegistry.gauge("jar.potato.gauge", new AtomicInteger(0));
    }

    @Override
    @Timed(value = "jar.potato.timer")
    public void test() {
        logger.info("TEST INNIT");
        gauge.set(43);
        apiCounter.increment();
    }
}
