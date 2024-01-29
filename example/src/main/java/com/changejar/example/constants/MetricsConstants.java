package com.changejar.example.constants;

import java.util.HashSet;
import java.util.List;

public final class MetricsConstants {
    public static final String METRICS_PREFIX = "changejar.application.{0}";

    public static final HashSet<String> METRICS_BLACKLIST =
            new HashSet<>(
                    List.of(
                            "jvm.",
                            "spring.",
                            "logback.",
                            "tomcat.",
                            "process.",
                            "hikaricp.",
                            "system.",
                            "jdbc.",
                            "http."));
}
