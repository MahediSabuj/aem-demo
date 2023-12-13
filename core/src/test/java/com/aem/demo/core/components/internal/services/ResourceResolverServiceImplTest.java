package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.ResourceResolverService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.testing.resourceresolver.MockResourceResolverFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
public class ResourceResolverServiceImplTest {
    @BeforeEach
    public void setup(AemContext context) {
        context.registerService(ResourceResolverFactory.class, new MockResourceResolverFactory());
    }

    @Test
    public void testGetResourceResolver(AemContext context) throws LoginException {
        ResourceResolverService resolverService = context.registerInjectActivateService(
            new ResourceResolverServiceImpl());

        Assertions.assertNotNull(resolverService);
        ResourceResolver resolver = resolverService.getResourceResolver();
        Assertions.assertNotNull(resolver);
    }
}
