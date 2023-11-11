package com.aem.demo.core.components.services;

public interface RestClientService {
    <T> T get(String url, Class<T> type);

    <T> T post(String url, String body, Class<T> type);
}
