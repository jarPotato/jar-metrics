package com.changejar.metrics.listeners;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaListeners {
    Logger logger = LoggerFactory.getLogger(KafkaListeners.class);

    @Value("${statsd.host:localhost}")
    private String statsdHost;

    @Value("${statsd.port:8125}")
    private int statsdPort;

    @KafkaListener(topics = "${changejar.metrics-topic}", groupId = "jar-metrics")
    public void processMetrics(String message) {
        sendStatsdLine(message);
    }

    /**
     * Send line to statsd_exporter
     *
     * @param statsdLine
     */
    private void sendStatsdLine(String statsdLine) {
        logger.info("RECEIVED: {}", statsdLine);
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] data = statsdLine.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet =
                    new DatagramPacket(
                            data, data.length, InetAddress.getByName(statsdHost), statsdPort);
            socket.send(packet);
        } catch (IOException e) {
            logger.error("ERROR SENDING MESSAGE", e);
        }
    }
}
