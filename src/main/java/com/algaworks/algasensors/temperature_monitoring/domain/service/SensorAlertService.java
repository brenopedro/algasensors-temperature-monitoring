package com.algaworks.algasensors.temperature_monitoring.domain.service;

import com.algaworks.algasensors.temperature_monitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.temperature_monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature_monitoring.domain.repository.SensorAlertRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SensorAlertService {

    private final SensorAlertRepository sensorAlertRepository;

    @Transactional
    public void handleAlert(@Payload TemperatureLogData temperatureLogData) {
        sensorAlertRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(alert -> {
                if (alert.getMaxTemperature() != null &&
                        temperatureLogData.getValue().compareTo(alert.getMaxTemperature()) >= 0) {
                    log.info("Alert Max Temp: Sensor ID: {}, Temperature: {}",
                            temperatureLogData.getSensorId(), temperatureLogData.getValue());
                } else if (alert.getMinTemperature() != null &&
                        temperatureLogData.getValue().compareTo(alert.getMinTemperature()) <= 0) {
                    log.info("Alert Min Temp: Sensor ID: {}, Temperature: {}",
                            temperatureLogData.getSensorId(), temperatureLogData.getValue());
                } else {
                    logIgnore(temperatureLogData);
                }
        }, () -> {
                    logIgnore(temperatureLogData);
                });
    }

    private static void logIgnore(TemperatureLogData temperatureLogData) {
        log.info("Alert ignored: Sensor ID: {}, Temperature: {}",
                temperatureLogData.getSensorId(), temperatureLogData.getValue());
    }
}
