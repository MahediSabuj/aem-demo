package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.FormatterService;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

@Component(service = FormatterService.class,
  property = {
    Constants.SERVICE_DESCRIPTION + "=Formatter Service Implementation"
})
public class FormatterServiceImpl implements FormatterService {
    @Override
    public String getFormattedLink(String url, ResourceResolver resourceResolver) {
        return resourceResolver.map(url + ".html");
    }
}
