package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.RestClientService;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;

@Component(service = { RestClientService.class },
  property = {
    Constants.SERVICE_DESCRIPTION + "=Http Service Client Service"
})
public class HttpClientServiceImpl extends RestClientServiceImpl implements RestClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientServiceImpl.class);

    private static final int CONNECTION_TIMEOUT = 10;

    private HttpClient getHttpClient(int... timeout) {
        int connectionTimeout = Arrays.stream(timeout).findFirst().orElse(CONNECTION_TIMEOUT);

        return HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(connectionTimeout))
            .build();
    }

    private String getResponse(String url) throws IOException, InterruptedException {
        HttpClient httpClient = getHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(url))
            .build();

        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if(httpResponse.statusCode() == 200)
            return httpResponse.body();

        return null;
    }

    @Override
    public <T> T get(String url, Class<T> type) {
        try {
            String response = getResponse(url);
            return super.get(response, type);
        } catch (IOException | InterruptedException ex) {
            LOGGER.error(ex.getMessage());
        }

        return null;
    }
}
