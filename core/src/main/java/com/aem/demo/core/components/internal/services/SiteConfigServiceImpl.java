package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.SiteConfigService;
import com.aem.demo.core.configs.SiteConfig;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

@Component(service = { SiteConfigService.class },
  property = {
    Constants.SERVICE_DESCRIPTION + "=Site Config Service"
})
public class SiteConfigServiceImpl implements SiteConfigService {
    @Override
    public SiteConfig getSiteConfig(Resource resource) {
        ConfigurationBuilder configurationBuilder = resource.adaptTo(ConfigurationBuilder.class);
        return configurationBuilder != null ? configurationBuilder.as(SiteConfig.class) : null;
    }
}
