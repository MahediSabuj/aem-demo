package com.aem.demo.core.components.internal.services;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.cq.dam.cfm.ContentVariation;
import com.adobe.cq.dam.cfm.FragmentData;
import com.aem.demo.core.components.services.ContentFragmentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component(service = { ContentFragmentService.class },
  property = {
    Constants.SERVICE_DESCRIPTION + "=Content Fragment Service"
})
public class ContentFragmentServiceImpl implements ContentFragmentService {
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
