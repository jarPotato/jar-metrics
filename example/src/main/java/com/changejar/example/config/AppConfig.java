package com.changejar.example.config;

import static com.changejar.example.utils.MetricsUtils.processMetrics;

import com.changejar.example.constants.MetricsConstants;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.statsd.StatsdConfig;
import io.micrometer.statsd.StatsdMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class AppConfig {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${metrics-topic}")
    private String metricsTopic;

    @Autowired
    public AppConfig(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Bean
    public MeterRegistry getStatsDRegistry() {
        return StatsdMeterRegistry.builder(StatsdConfig.DEFAULT)
                .clock(Clock.SYSTEM)
                .lineSink(
                        line -> {
                            if (MetricsConstants.METRICS_BLACKLIST.stream()
                                    .anyMatch(line::startsWith)) {
                                return;
                            }
                            kafkaTemplate.send(metricsTopic, processMetrics(line));
                        })
                .build();
    }
}
