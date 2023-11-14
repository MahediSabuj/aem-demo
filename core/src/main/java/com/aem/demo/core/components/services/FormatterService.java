package com.aem.demo.core.components.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface FormatterService {
    String getFormattedLink(String url, ResourceResolver resourceResolver);
}
