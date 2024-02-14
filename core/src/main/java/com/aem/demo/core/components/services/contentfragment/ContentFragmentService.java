package com.aem.demo.core.components.services.contentfragment;

import com.adobe.cq.dam.cfm.ContentFragment;
import org.apache.sling.api.resource.Resource;

import java.util.Map;

public interface ContentFragmentService {
    Map<String, Object> get(Resource resource, String variationName);

    ContentFragment create(String cfmPath, String assetPath, String title);
}
