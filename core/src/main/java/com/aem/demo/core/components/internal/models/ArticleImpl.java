package com.aem.demo.core.components.internal.models;

import com.adobe.cq.export.json.ExporterConstants;
import com.aem.demo.core.components.models.Article;
import com.aem.demo.core.components.services.RestClientService;
import com.aem.demo.core.models.ArticleModel;
import com.aem.demo.core.models.internals.ArticleModelImpl;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;

@Model(
  adaptables = { SlingHttpServletRequest.class },
  adapters = { Article.class },
  resourceType = { ArticleImpl.RESOURCE_TYPE },
  defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
  name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
  extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class ArticleImpl extends ArticleModelImpl implements Article {
    protected static final String RESOURCE_TYPE = "aem-demo/components/article";

    @OSGiService
    RestClientService restClientService;

    @ValueMapValue
    String articleTitle;

    @ValueMapValue
    String articleDescription;

    @ValueMapValue
    String[] articleTags;

    @SlingObject
    ResourceResolver resourceResolver;

    @PostConstruct
    protected void init() {
        ArticleModel articleModel = restClientService.get(
            "https://6544189f5a0b4b04436c0e3c.mockapi.io/api/v1/articles/1",
            ArticleModelImpl.class);

        articleId = articleModel.getArticleId();
        articleAuthor = articleModel.getArticleAuthor();
    }

    @Override
    public String getArticleTitle() {
        return articleTitle;
    }

    @Override
    public String getArticleDescription() {
        return articleDescription;
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
