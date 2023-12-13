package com.aem.demo.core.components.internal.models.v1;

import com.adobe.cq.export.json.ExporterConstants;
import com.aem.demo.core.components.models.Article;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
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
    protected static final String RESOURCE_TYPE = "aem-demo/components/article/v1/article";

    @ValueMapValue
    protected String articleTitle;

    @ValueMapValue
    protected String articleDescription;

    @ValueMapValue
    protected String articleAuthor;

    @ValueMapValue
    protected String[] articleTags;

    @SlingObject
    protected ResourceResolver resourceResolver;

    @Override
    public String getArticleTitle() {
        return articleTitle;
    }

    @Override
    public String getArticleDescription() {
        return articleDescription;
    }

    @Override
    public String getArticleAuthor() {
        return articleAuthor;
    }

    @Override
    public String[] getArticleTags() {
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

        String[] tagList = new String[articleTags.length];
        for (int index = 0; index < articleTags.length; index++) {
            Tag tag = tagManager.resolve(articleTags[index]);
            if(tag != null) {
                tagList[index] = tag.getTitle();
            }
        }
        return tagList;
    }
}
