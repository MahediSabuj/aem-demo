package com.aem.demo.core.services.impl;

import com.aem.demo.core.configs.AppConfig;
import com.aem.demo.core.services.AppConfigurationService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.metatype.annotations.Designate;

@Component(service = { AppConfigurationService.class }, configurationPolicy = ConfigurationPolicy.REQUIRE)
@Designate(ocd = AppConfig.class)
public class AppConfigurationServiceImpl implements AppConfigurationService {
    private AppConfig appConfig;

    @Activate
    public void activate(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    public String getApiBaseUrl() {
        return appConfig.api_baseurl();
    }

    @Override
    public String getClientId() {
        return appConfig.client_id();
    }

    @Override
    public String getRedirectUri() {
        return appConfig.redirect_uri();
    }
}
