package com.cperazzolli.algasensors.device.management.api.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RestClientFactory {

    private final RestClient.Builder restClientBuilder;



    public RestClient temperatureMonitoringRestClient() {
        return restClientBuilder.baseUrl("http://localhost:8081")
                .requestFactory(genereteClientHttpRequestFactory())
                .defaultStatusHandler(HttpStatusCode::isError, ((request, response) -> {
                    throw new SensorMonitoringClientBadGatewayException();
                }))
                .build();
    }

    private ClientHttpRequestFactory genereteClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofSeconds(5));
        factory.setConnectTimeout(Duration.ofSeconds(3));
        return factory;
    }
}
