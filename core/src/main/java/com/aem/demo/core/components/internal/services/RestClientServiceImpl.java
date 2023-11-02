package com.aem.demo.core.components.internal.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class RestClientServiceImpl {
    public <T> T get(String response, Class<T> type) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(response, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
