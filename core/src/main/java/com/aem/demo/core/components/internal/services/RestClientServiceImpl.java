package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.RestClientService;
import com.aem.demo.core.services.AppConfigService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

@Component(service = { RestClientService.class },
  property = {
    Constants.SERVICE_DESCRIPTION + "=Rest Client Service"
})
public class RestClientServiceImpl implements RestClientService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private static final int CONNECTION_TIMEOUT = 10;

    @Reference
    AppConfigService appConfigService;

    private HttpClient getHttpClient(int... timeout) {
        int connectionTimeout = Arrays.stream(timeout).findFirst()
            .orElse(CONNECTION_TIMEOUT);

        return HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(connectionTimeout))
            .build();
    }

    private HttpRequest.Builder get() {
        return HttpRequest.newBuilder()
            .GET();
    }

    private HttpRequest.Builder post(String body) {
        return HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(body));
    }

    private String send(HttpRequest.Builder httpRequestBuilder, String url, Map<String, String> headers) {
        HttpClient httpClient = getHttpClient();
        final String API_URL = appConfigService.getApiBaseUrl().concat(url);
        
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpRequestBuilder = httpRequestBuilder.header(entry.getKey(), entry.getValue());
            }
        }

        HttpRequest httpRequest = httpRequestBuilder
            .uri(URI.create(API_URL))
            .build();

        try {
            HttpResponse<String> httpResponse = httpClient.send(
                httpRequest, HttpResponse.BodyHandlers.ofString());

            if(httpResponse.statusCode() == 200)
                return httpResponse.body();
        } catch (IOException | InterruptedException ex) {
            LOGGER.error("Error in Send Request");
        }

        return StringUtils.EMPTY;
    }

    private <T> T map(String response, Class<T> type) {
        if(StringUtils.isNotBlank(response)) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                return objectMapper.readValue(response, type);
            } catch (JsonProcessingException ex) {
                LOGGER.error(ex.getMessage());
            }
        }

        return null;
    }

    @Override
    public <T> T get(String url, Map<String, String> headers, Class<T> type) {
        HttpRequest.Builder httpRequestBuilder = get();
        String response = send(httpRequestBuilder, url, headers);
        return map(response, type);
    }

    @Override
    public <T> T post(String url, String body, Map<String, String> headers, Class<T> type) {
        HttpRequest.Builder httpRequestBuilder = post(body);
        String response = send(httpRequestBuilder, url, headers);
        return map(response, type);
    }
}
