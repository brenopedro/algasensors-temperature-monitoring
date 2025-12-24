package com.algaworks.algasensors.temperature_monitoring.domain.service;

import com.algaworks.algasensors.temperature_monitoring.api.model.TemperatureLogData;
import com.algaworks.algasensors.temperature_monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature_monitoring.domain.model.SensorMonitoring;
import com.algaworks.algasensors.temperature_monitoring.domain.model.TemperatureLog;
import com.algaworks.algasensors.temperature_monitoring.domain.model.TemperatureLogId;
import com.algaworks.algasensors.temperature_monitoring.domain.repository.SensorMonitoringRepository;
import com.algaworks.algasensors.temperature_monitoring.domain.repository.TemperatureLogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TemperatureMonitoringService {

    private final TemperatureLogRepository temperatureLogRepository;
    private final SensorMonitoringRepository sensorMonitoringRepository;

    @Transactional
    public void processTemperatureReading(TemperatureLogData temperatureLogData) {
        sensorMonitoringRepository.findById(new SensorId(temperatureLogData.getSensorId()))
                .ifPresentOrElse(sensor -> handleSensorMonitoring(temperatureLogData, sensor),
                        () -> logIgnoredTemperature(temperatureLogData));
    }

    private void logIgnoredTemperature(TemperatureLogData temperatureLogData) {
        log.info("Ignored temperature reading from unknown sensor. Sensor ID: {}, Temperature: {}",
                temperatureLogData.getSensorId(), temperatureLogData.getValue());
    }

    private void handleSensorMonitoring(TemperatureLogData temperatureLogData, SensorMonitoring sensor) {
        if (sensor.isEnabled()) {
            sensor.setLastTemperature(temperatureLogData.getValue());
            sensor.setUpdatedAt(OffsetDateTime.now());
            sensorMonitoringRepository.save(sensor);

            TemperatureLog temperatureLog = TemperatureLog.builder()
                    .id(new TemperatureLogId(temperatureLogData.getId()))
                    .sensorId(new SensorId(temperatureLogData.getSensorId()))
                    .registeredAt(temperatureLogData.getRegisteredAt())
                    .value(temperatureLogData.getValue())
                    .build();

            temperatureLogRepository.save(temperatureLog);
            log.info("Processed temperature reading from sensor ID: {}, Temperature: {}",
                    temperatureLogData.getSensorId(), temperatureLogData.getValue());
        } else {
            logIgnoredTemperature(temperatureLogData);
        }
    }
}
