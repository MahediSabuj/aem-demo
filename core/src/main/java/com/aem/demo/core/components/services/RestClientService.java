package com.aem.demo.core.components.services;

import java.util.Map;

public interface RestClientService {
    <T> T get(String url, Map<String, String> headers, Class<T> type);

    <T> T post(String url, String body, Map<String, String> headers, Class<T> type);
}
