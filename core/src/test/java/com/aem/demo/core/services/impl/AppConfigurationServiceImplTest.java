package com.aem.demo.core.services.impl;

import com.aem.demo.core.services.AppConfigurationService;
import org.apache.sling.testing.mock.osgi.junit5.OsgiContext;
import org.apache.sling.testing.mock.osgi.junit5.OsgiContextExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(OsgiContextExtension.class)
public class AppConfigurationServiceImplTest {
    private final OsgiContext osgiContext = new OsgiContext();

    @Test
    public void testAppConfigurationService() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("api.baseurl", "https://www.google.com");
        properties.put("client.id", "XXX");
        properties.put("redirect.uri", "ZZZ");

        AppConfigurationService appConfigurationService = osgiContext.registerInjectActivateService(
            new AppConfigurationServiceImpl(), properties);

        Assertions.assertEquals("https://www.google.com", appConfigurationService.getApiBaseUrl());
        Assertions.assertEquals("XXX", appConfigurationService.getClientId());
        Assertions.assertEquals("ZZZ", appConfigurationService.getRedirectUri());
    }
}
