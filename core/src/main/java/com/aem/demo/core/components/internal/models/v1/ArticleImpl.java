package com.aem.demo.core.components.internal.models.v1;

import com.adobe.cq.export.json.ExporterConstants;
import com.aem.demo.core.components.models.Article;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.Date;

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

    @Getter
    @ValueMapValue
    protected String title;

    @Getter
    @ValueMapValue
    protected String description;

    @Getter
    @ValueMapValue
    protected String author;

    @ValueMapValue
    protected String[] tags;

    @Getter
    @ValueMapValue
    protected Date publishDate;

    @SlingObject
    protected ResourceResolver resourceResolver;

    @Override
    public String[] getTags() {
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);

        String[] tagList = new String[tags.length];
        for (int index = 0; index < tags.length; index++) {
            Tag tag = tagManager.resolve(tags[index]);
            if(tag != null) {
                tagList[index] = tag.getTitle();
            }
        }
        return tagList;
    }
}
