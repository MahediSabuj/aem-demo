package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.ResourceResolverService;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Component(service = { ResourceResolverService.class },
  property = {
    Constants.SERVICE_DESCRIPTION + "=Resource Resolver Service"
})
public class ResourceResolverServiceImpl implements ResourceResolverService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Override
    public ResourceResolver getResourceResolver() {
        Map<String, Object> params = new HashMap<>();
        params.put(resolverFactory.SUBSERVICE, "writeService");

        try {
            return resolverFactory.getServiceResourceResolver(params);
        } catch (LoginException ex) {
            LOG.error("Error in getting Resource Resolver for Service User");
        }

        return null;
    }
}
