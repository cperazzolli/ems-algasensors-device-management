package com.cperazzolli.algasensors.device.management.api.controller;

import com.cperazzolli.algasensors.device.management.api.client.SensorMonitoringClient;
import com.cperazzolli.algasensors.device.management.api.model.SensorDetailOutput;
import com.cperazzolli.algasensors.device.management.api.model.SensorInput;
import com.cperazzolli.algasensors.device.management.api.model.SensorMonitoringOutput;
import com.cperazzolli.algasensors.device.management.api.model.SensorOutput;
import com.cperazzolli.algasensors.device.management.common.IDGenerator;
import com.cperazzolli.algasensors.device.management.domain.model.Sensor;
import com.cperazzolli.algasensors.device.management.domain.model.SensorId;
import com.cperazzolli.algasensors.device.management.domain.repository.SensorRepository;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorRepository sensorRepository;
    private final SensorMonitoringClient sensorMonitoringClient;

    @GetMapping
    public Page<SensorOutput> getAll(@PageableDefault Pageable page) {
        Page<Sensor> pageAble = sensorRepository.findAll(page);
        return pageAble.map(this::convertToModel);
    }

    @GetMapping("{sensorId}")
    public SensorOutput getOne(@PathVariable("sensorId") TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return convertToModel(sensor);
    }

    @GetMapping("{sensorId}/detail")
    public SensorDetailOutput getOneWithDetail(@PathVariable("sensorId") TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        SensorMonitoringOutput sensorMonitoringOutput = sensorMonitoringClient.getSensorMonitoring(sensorId);
        SensorOutput sensorOutput = convertToModel(sensor);
        return SensorDetailOutput.builder()
                .monitoring(sensorMonitoringOutput)
                .sensor(sensorOutput)
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SensorOutput create(@RequestBody SensorInput input) {
        Sensor sensor = Sensor.builder()
                .id(new SensorId(IDGenerator.generateId()))
                .name(input.getName())
                .ip(input.getIp())
                .protocol(input.getProtocol())
                .location(input.getLocation())
                .model(input.getModel())
                .enabled(false)
                .build();
        sensor = sensorRepository.saveAndFlush(sensor);
        return convertToModel(sensor);
    }


    @PutMapping("{sensorId}")
    @ResponseStatus(HttpStatus.OK)
    public void update(@PathVariable("sensorId") TSID sensorId, @RequestBody SensorInput input) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sensor.setName(input.getName());
        sensor.setIp(input.getIp());
        sensor.setModel(input.getModel());
        sensor.setLocation(input.getLocation());
        sensor.setProtocol(input.getProtocol());
        sensorRepository.saveAndFlush(sensor);
    }

    @PutMapping("{sensorId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable("sensorId") TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sensor.setEnabled(true);
        sensorRepository.saveAndFlush(sensor);
        sensorMonitoringClient.enableMonitoring(sensorId);
    }

    @DeleteMapping("{sensorId}/enable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable("sensorId") TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sensor.setEnabled(false);
        sensorRepository.saveAndFlush(sensor);
        sensorMonitoringClient.disableMonitoring(sensorId);
    }

    @DeleteMapping("{sensorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("sensorId") TSID sensorId) {
        Sensor sensor = sensorRepository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        sensorRepository.delete(sensor);
        sensorMonitoringClient.disableMonitoring(sensorId);
    }

    private SensorOutput convertToModel(Sensor sensor) {
        return SensorOutput.builder()
                .id(sensor.getId().getValue())
                .name(sensor.getName())
                .ip(sensor.getIp())
                .protocol(sensor.getProtocol())
                .location(sensor.getLocation())
                .model(sensor.getModel())
                .enabled(sensor.getEnabled())
                .build();
    }
}
