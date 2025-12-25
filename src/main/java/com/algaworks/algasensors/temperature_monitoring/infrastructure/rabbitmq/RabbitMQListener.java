package com.algaworks.algasensors.temperature_monitoring.infrastructure.rabbitmq;

import com.algaworks.algasensors.temperature_monitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.temperature_monitoring.domain.service.TemperatureMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.algaworks.algasensors.temperature_monitoring.infrastructure.rabbitmq.RabbitMQQueueConstants.QUEUE_PROCESS_ALERTING;
import static com.algaworks.algasensors.temperature_monitoring.infrastructure.rabbitmq.RabbitMQQueueConstants.QUEUE_PROCESS_TEMPERATURE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    private final TemperatureMonitoringService temperatureMonitoringService;

    @SneakyThrows
    @RabbitListener(queues = QUEUE_PROCESS_TEMPERATURE, concurrency = "2-3")
    public void handleProcessTemperature(@Payload TemperatureLogData data,
                       @Headers Map<String, Object> headers) {

        log.info("Received data: {}", data);
        log.info("Received headers: {}", headers);

        temperatureMonitoringService.processTemperatureReading(data);
    }

    @SneakyThrows
    @RabbitListener(queues = QUEUE_PROCESS_ALERTING, concurrency = "2-3")
    public void handleAlerting(@Payload TemperatureLogData data) {

        log.info("Alerting: Sensor ID: {}, Temperature: {}",
                data.getSensorId(), data.getValue());

    }
}
