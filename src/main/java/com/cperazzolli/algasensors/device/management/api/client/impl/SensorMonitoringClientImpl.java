package com.cperazzolli.algasensors.device.management.api.client.impl;

import com.cperazzolli.algasensors.device.management.api.client.RestClientFactory;
import com.cperazzolli.algasensors.device.management.api.client.SensorMonitoringClient;
import com.cperazzolli.algasensors.device.management.api.model.SensorMonitoringOutput;
import io.hypersistence.tsid.TSID;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


//@Component
public class SensorMonitoringClientImpl implements SensorMonitoringClient {

    private final RestClient restClient;

    public SensorMonitoringClientImpl(RestClientFactory restClientFactory) {
        this.restClient = restClientFactory.temperatureMonitoringRestClient();
    }

    @Override
    public void enableMonitoring(TSID sensorId) {
          restClient.put()
                  .uri("/api/v1/sensors/{sensorId}/monitoring/enable",sensorId)
                  .retrieve()
                  .toBodilessEntity();
    }

    @Override
    public void disableMonitoring(TSID sensorId) {
        restClient.delete()
                .uri("/api/v1/sensors/{sensorId}/monitoring/enable",sensorId)
                .retrieve()
                .toBodilessEntity();
    }

    @Override
    public SensorMonitoringOutput getSensorMonitoring(TSID sensorId) {
        return restClient.get()
                .uri("/api/v1/sensors/{sensorId}/monitoring",sensorId)
                .retrieve()
                .body(SensorMonitoringOutput.class);
    }
}
