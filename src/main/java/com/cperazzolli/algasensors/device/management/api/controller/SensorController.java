package com.cperazzolli.algasensors.device.management.api.controller;

import com.cperazzolli.algasensors.device.management.api.model.SensorInput;
import com.cperazzolli.algasensors.device.management.common.IDGenerator;
import com.cperazzolli.algasensors.device.management.domain.model.Sensor;
import com.cperazzolli.algasensors.device.management.domain.model.SensorId;
import com.cperazzolli.algasensors.device.management.domain.repository.SensorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository sensorRepository;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Sensor create(@RequestBody SensorInput input) {
        Sensor sensor = Sensor.builder()
                .id(new SensorId(IDGenerator.generateId()))
                .name(input.getName())
                .ip(input.getIp())
                .protocol(input.getProtocol())
                .location(input.getLocation())
                .model(input.getModel())
                .enabled(false)
                .build();

        return sensorRepository.saveAndFlush(sensor);
    }
}
