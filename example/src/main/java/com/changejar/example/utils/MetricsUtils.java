package com.changejar.example.utils;

import com.changejar.example.constants.MetricsConstants;
import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class MetricsUtils {

    /**
     *
     *
     * <ol>
     *   <li>Datadog flavour by default exports timer in seconds and uses the type "ms". This
     *       function modifies the value in seconds and converts it to milliseconds.
     *   <li>Prefix all the exported metrics with defined METRICS_PREFIX
     * </ol>
     *
     * @param line line to be exported to statsd
     * @return modified line
     */
    public static String processMetrics(String line) {
        Pattern pattern = Pattern.compile(":([\\d.]+)\\|ms\\|");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String match = matcher.group(1);
            double seconds = Double.parseDouble(matcher.group(1));
            long milliseconds = (long) (seconds * 1000);
            line = line.replaceFirst(match, String.valueOf(milliseconds));
        }

        return MessageFormat.format(MetricsConstants.METRICS_PREFIX, line);
    }
}
