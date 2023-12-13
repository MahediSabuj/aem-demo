package com.aem.demo.core.components.services;

import com.aem.demo.core.configs.SiteConfig;
import org.apache.sling.api.resource.Resource;

public interface SiteConfigService {
    SiteConfig getSiteConfig(Resource resource);
}
