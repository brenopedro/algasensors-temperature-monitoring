package com.algaworks.algasensors.temperature_monitoring.infrastructure.rabbitmq;

import com.algaworks.algasensors.temperature_monitoring.api.model.TemperatureLogData;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.algaworks.algasensors.temperature_monitoring.infrastructure.rabbitmq.RabbitMQQueueConstants.QUEUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    @SneakyThrows
    @RabbitListener(queues = QUEUE)
    public void handle(@Payload TemperatureLogData data,
                       @Headers Map<String, Object> headers) {

        log.info("Received data: {}", data);
        log.info("Received headers: {}", headers);
    }
}
