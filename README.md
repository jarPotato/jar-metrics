# Jar Metrics

---

### Setup Client Side
1. Add dependencies:
    ```groovy
    implementation 'org.springframework.boot:spring-boot-starter-aop' // This enables usage of @Timed Annotation
    implementation 'io.micrometer:micrometer-registry-statsd:latest.release'
   ```
2. Add StatsdMeterRegistry Config
   ```java
   @Configuration
   public class AppConfig {
      private final KafkaTemplate<String, String> kafkaTemplate;
      private final NewTopic jarmetrics;
      
       @Autowired
       public PocAppConfig(KafkaTemplate<String, String> kafkaTemplate, NewTopic jarmetrics) {
           this.kafkaTemplate = kafkaTemplate;
           this.jarmetrics = jarmetrics;
       }
   
       @Bean("statds_meter_registry")
       public MeterRegistry getStatsDRegistry() {
           return StatsdMeterRegistry.builder(StatsdConfig.DEFAULT).clock(Clock.SYSTEM).lineSink(line -> kafkaTemplate.send(jarmetrics.name(),line)).build();
       }
   }
   ```
   
### Setup Consumer
1. Start docker compose project
   ```bash
   docker compose up -d
   ``` 
### Usage
```java
@Service
public class TestServiceImpl implements TestService {
    Logger logger = LoggerFactory.getLogger(TestServiceImpl.class);
    private final Counter apiCounter;
    private final AtomicInteger gauge;

    @Autowired
    public TestServiceImpl(@Qualifier("statds_meter_registry") MeterRegistry statsdMeterRegistry) {
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
```

### 