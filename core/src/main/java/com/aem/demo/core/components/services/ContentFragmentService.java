package com.aem.demo.core.components.services;

import org.apache.sling.api.resource.Resource;

import java.util.Map;

public interface ContentFragmentService {
    Map<String, Object> get(Resource resource, String variationName);
}
