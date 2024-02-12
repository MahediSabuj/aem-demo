package com.aem.demo.core.components.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface ContentPublishService {
    boolean publishContent(ResourceResolver resourceResolver, String path);
}
