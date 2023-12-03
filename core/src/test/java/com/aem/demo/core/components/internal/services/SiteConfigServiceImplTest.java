package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.SiteConfigService;
import com.aem.demo.core.configs.SiteConfig;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.caconfig.ContextPlugins;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.apache.sling.testing.mock.sling.junit5.SlingContext;
import org.apache.sling.testing.mock.sling.junit5.SlingContextBuilder;
import org.apache.sling.testing.mock.sling.junit5.SlingContextExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(SlingContextExtension.class)
public class SiteConfigServiceImplTest {
    private static final String CONFIG_NAME = "siteConfig";

    private SlingContext slingContext = new SlingContextBuilder()
            .plugin(ContextPlugins.CACONFIG).build();

    private Resource contextResource;

    @BeforeEach
    public void setup() {
        slingContext.create().resource("/content/aem-demo", "sling:configRef", "/conf/aem-demo");
        contextResource = slingContext.create().resource("/content/aem-demo/us/en");

        MockContextAwareConfig.registerAnnotationClasses(slingContext, SiteConfig.class);
        MockContextAwareConfig.writeConfiguration(slingContext, contextResource.getPath(), SiteConfig.class,
            "contentPath", "/content/aem-demo",
            "siteDomain", "http://local.aemdemo.com",
            "approverGroup", "aem-demo-approver-group");
    }

    @Test
    public void testGetSiteConfig() {
        SiteConfigService config = new SiteConfigServiceImpl();
        SiteConfig siteConfig = config.getSiteConfig(contextResource);

        Assertions.assertNotNull(siteConfig);
        Assertions.assertEquals("/content/aem-demo", siteConfig.contentPath());
        Assertions.assertEquals("http://local.aemdemo.com", siteConfig.siteDomain());
        Assertions.assertEquals("aem-demo-approver-group", siteConfig.approverGroup());
    }
}
