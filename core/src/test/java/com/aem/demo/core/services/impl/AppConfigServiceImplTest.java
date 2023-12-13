package com.aem.demo.core.services.impl;

import com.aem.demo.core.services.AppConfigService;
import org.apache.sling.testing.mock.osgi.junit5.OsgiContext;
import org.apache.sling.testing.mock.osgi.junit5.OsgiContextExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(OsgiContextExtension.class)
public class AppConfigServiceImplTest {
    @Test
    public void testAppConfigService(OsgiContext osgiContext) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("api.baseurl", "https://www.google.com");
        properties.put("client.id", "XXX");
        properties.put("redirect.uri", "ZZZ");

        AppConfigService appConfigService = osgiContext.registerInjectActivateService(
            new AppConfigServiceImpl(), properties);

        Assertions.assertEquals("https://www.google.com", appConfigService.getApiBaseUrl());
        Assertions.assertEquals("XXX", appConfigService.getClientId());
        Assertions.assertEquals("ZZZ", appConfigService.getRedirectUri());
    }
}
