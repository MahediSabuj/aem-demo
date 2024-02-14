package com.aem.demo.core.components.internal.services.contentfragment;

import com.adobe.cq.dam.cfm.*;
import com.aem.demo.core.components.services.contentfragment.ContentFragmentService;
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

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Iterator;

@Component(service = { ContentFragmentService.class },
  property = {
    Constants.SERVICE_DESCRIPTION + "=Content Fragment Service"
})
public class ContentFragmentServiceImpl implements ContentFragmentService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private static final String MASTER_VERSION = "master";

    @Override
    public HashMap<String, Object> get(Resource resource, String variationName) {
        HashMap<String, Object> fragmentMap = new HashMap<>();
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
    public ContentFragment create(ResourceResolver resolver, String cfmPath, String assetPath, String title) {
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

    @Override
    public boolean update(ContentFragment fragment, HashMap<String, Object> properties) {
        if (fragment != null) {
            final Iterator<ContentElement> elementIterator = fragment.getElements();

            try {
                while (elementIterator.hasNext()) {
                    final ContentElement element = elementIterator.next();
                    String name = element.getName();
                    FragmentData fragmentData = element.getValue();

                    if (properties.containsKey(name)) {
                        fragmentData.setValue(properties.get(name));
                        element.setValue(fragmentData);
                    }
                }
            } catch (ContentFragmentException ex) {
                LOG.error("Failed to Update Content Fragment: {}", ex.getMessage());
                return false;
            }
        }

        return true;
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
