package com.aem.demo.core.components.services.contentfragment;

import com.adobe.cq.dam.cfm.ContentFragment;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.util.HashMap;

public interface ContentFragmentService {
    HashMap<String, Object> get(Resource resource, String variationName);

    ContentFragment create(ResourceResolver resolver, String cfmPath, String assetPath, String title);

    boolean update(ContentFragment fragment, HashMap<String, Object> properties);
}
