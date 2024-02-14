package com.aem.demo.core.components.internal.models.v2;

import com.adobe.cq.export.json.ExporterConstants;
import com.aem.demo.core.components.models.Article;
import com.aem.demo.core.components.services.contentfragment.ContentFragmentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.*;

@Model(
  adaptables = { SlingHttpServletRequest.class },
  adapters = { Article.class },
  resourceType = { ArticleImpl.RESOURCE_TYPE },
  defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
  name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
  extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ArticleImpl extends com.aem.demo.core.components.internal.models.v1.ArticleImpl implements Article {
    protected static final String RESOURCE_TYPE = "aem-demo/components/article/v2/article";

    @ValueMapValue
    String fragmentPath;

    @ValueMapValue
    String variationName;

    @OSGiService
    ContentFragmentService fragmentService;

    @PostConstruct
    public void init() {
        Map<String, Object> articleFragmentMap = getFragment(fragmentPath, variationName);
        if (articleFragmentMap != null) {
            title = getFragmentElementValue(articleFragmentMap, "title");
            description = getFragmentElementValue(articleFragmentMap, "description");

            String authorFragmentPath = getFragmentElementValue(articleFragmentMap, "author");
            Map<String, Object> authorFragmentMap = getFragment(authorFragmentPath, "");
            if (authorFragmentMap != null) {
                author = getFragmentElementValue(authorFragmentMap, "name");
            }

            if (articleFragmentMap.containsKey("tags")) {
                tags = (String[]) articleFragmentMap.get("tags");
            }
        }
    }

    private String getFragmentElementValue(Map<String, Object> fragmentMap, String element) {
        return fragmentMap.getOrDefault(element, StringUtils.EMPTY).toString();
    }

    private Map<String, Object> getFragment(String fragmentPath, String variationName) {
        if (StringUtils.isNotBlank(fragmentPath)) {
            Resource fragmentResource = resourceResolver.getResource(fragmentPath);
            return fragmentService.get(fragmentResource, variationName);
        }
        return null;
    }
}
