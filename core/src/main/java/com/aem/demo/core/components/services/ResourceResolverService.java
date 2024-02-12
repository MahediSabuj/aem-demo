package com.aem.demo.core.components.services;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;

public interface ResourceResolverService {
    ResourceResolver getResourceResolver();
}
