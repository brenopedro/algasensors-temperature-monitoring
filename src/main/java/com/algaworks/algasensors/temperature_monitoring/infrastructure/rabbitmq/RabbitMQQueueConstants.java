package com.algaworks.algasensors.temperature_monitoring.infrastructure.rabbitmq;

public class RabbitMQQueueConstants {

    public static final String EXCHANGE = "temperature-processing.temperature-received.v1.e";
    public static final String QUEUE_PROCESS_TEMPERATURE = "temperature-monitoring.process-temperature.v1.q";
    public static final String DEAD_LETTER_QUEUE_PROCESS_TEMPERATURE = "temperature-monitoring.process-temperature.v1.dlq";
    public static final String QUEUE_PROCESS_ALERTING = "temperature-monitoring.alerting.v1.q";
}
