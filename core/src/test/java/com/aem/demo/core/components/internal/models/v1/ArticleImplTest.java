package com.aem.demo.core.components.internal.models.v1;

import com.aem.demo.core.components.models.Article;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Objects;

@ExtendWith(AemContextExtension.class)
public class ArticleImplTest {
    @BeforeEach
    public void setup(AemContext context) {
        context.load().json("/content/aem-demo/article.json", "/content");
        context.load().json("/content/cq:tags/aem-demo/article.json", "/content/cq:tags/aem-demo");
        context.currentResource("/content/article/v1");
    }

    @Test
    public void testArticle(AemContext context) {
        context.addModelsForPackage("com.aem.demo.core.models");

        ModelFactory modelFactory = context.getService(ModelFactory.class);
        MockSlingHttpServletRequest request = context.request();

        Article article = Objects.requireNonNull(modelFactory).createModel(request, ArticleImpl.class);

        Assertions.assertNotNull(article);
        Assertions.assertEquals("AEM Community", article.getArticleTitle());
        Assertions.assertEquals("<p>Hello World!</p>", article.getArticleDescription());
        Assertions.assertEquals("Mahedi Sabuj", article.getArticleAuthor());

        String[] articleTags = article.getArticleTags();
        Assertions.assertNotNull(articleTags);
        Assertions.assertEquals(2, articleTags.length);
        Assertions.assertEquals("Biking", articleTags[0]);
        Assertions.assertEquals("Hiking", articleTags[1]);
    }
}
