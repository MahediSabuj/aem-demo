package com.aem.demo.core.components.internal.models.v2;

import com.adobe.cq.export.json.ExporterConstants;
import com.aem.demo.core.components.models.Article;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
  adaptables = { SlingHttpServletRequest.class },
  adapters = { Article.class },
  resourceType = { ArticleImpl.RESOURCE_TYPE },
  defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
  name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
  extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class ArticleImpl implements Article {
    protected static final String RESOURCE_TYPE = "aem-demo/components/article/v2/article";

    @ValueMapValue
    String fragmentPath;

    @ValueMapValue
    String variationName;

    @Override
    public String getArticleTitle() {
        return null;
    }

    @Override
    public String getArticleDescription() {
        return null;
    }

    @Override
    public String getArticleAuthor() {
        return null;
    }

    @Override
    public String[] getArticleTags() {
        return new String[0];
    }
}
