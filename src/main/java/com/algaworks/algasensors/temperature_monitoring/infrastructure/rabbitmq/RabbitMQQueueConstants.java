package com.algaworks.algasensors.temperature_monitoring.infrastructure.rabbitmq;

public class RabbitMQQueueConstants {

    public static final String EXCHANGE = "temperature-processing.temperature-received.v1.e";
    public static final String QUEUE = "temperature-monitoring.process-temperature.v1.q";
}
