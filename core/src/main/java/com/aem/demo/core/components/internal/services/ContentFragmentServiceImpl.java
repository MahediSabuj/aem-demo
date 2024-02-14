package com.aem.demo.core.components.internal.services;

import com.adobe.cq.dam.cfm.*;
import com.aem.demo.core.components.services.ContentFragmentService;
import com.aem.demo.core.components.services.ResourceResolverService;
import com.day.cq.commons.jcr.JcrUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(service = { ContentFragmentService.class },
  property = {
    Constants.SERVICE_DESCRIPTION + "=Content Fragment Service"
})
public class ContentFragmentServiceImpl implements ContentFragmentService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Reference
    private ContentFragmentManager fragmentManager;

    @Reference
    ResourceResolverService resolverService;

    private static final String MASTER_VERSION = "master";

    @Override
    public Map<String, Object> get(Resource resource, String variationName) {
        Map<String, Object> fragmentMap = new HashMap<>();
        if (resource != null) {
            ContentFragment fragment = resource.adaptTo(ContentFragment.class);
            if (fragment != null) {
                final Iterator<ContentElement> elementIterator = fragment.getElements();
                while (elementIterator.hasNext()) {
                    final ContentElement element = elementIterator.next();
                    fragmentMap.put(element.getName(), getValue(variationName, element).getValue());
                }
            }
        }
        return fragmentMap;
    }

    @Override
    public ContentFragment create(String cfmPath, String assetPath, String title) {
        ResourceResolver resolver = resolverService.getResourceResolver();
        Resource cfmResource = resolver.getResource(cfmPath);

        if (cfmResource != null) {
            FragmentTemplate template = cfmResource.adaptTo(FragmentTemplate.class);

            if (template != null) {
                try {
                    String name = JcrUtil.createValidName(title);
                    Resource parentRsc = resolver.getResource(assetPath);
                    if (parentRsc != null) {
                        return template.createFragment(parentRsc, name, title);
                    }
                } catch (ContentFragmentException ex) {
                    LOG.error("Failed to Create New Content Fragment: {}", ex.getMessage());
                }
            }
        }

        return null;
    }

    private FragmentData getValue(String variationName, ContentElement element) {
        if (StringUtils.isNotEmpty(variationName) && !MASTER_VERSION.equals(variationName)) {
            ContentVariation variation = element.getVariation(variationName);
            if (variation != null) {
                return variation.getValue();
            }
        }
        return element.getValue();
    }
}
