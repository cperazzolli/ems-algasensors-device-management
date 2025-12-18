package com.cperazzolli.algasensors.device.management.api.client.impl;

import com.cperazzolli.algasensors.device.management.api.client.SensorMonitoringClientBadGatewayException;
import com.cperazzolli.algasensors.device.management.api.client.SensorMonitoringClient;
import io.hypersistence.tsid.TSID;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class SensorMonitoringClientImpl implements SensorMonitoringClient {

    private final RestClient restClient;

    public SensorMonitoringClientImpl(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl("http://localhost:8081")
                .defaultStatusHandler(HttpStatusCode::isError, ((request, response) -> {
                    throw new SensorMonitoringClientBadGatewayException();
                }))
                .build();
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
}
