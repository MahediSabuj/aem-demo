package com.aem.demo.core.components.internal.models;

import com.aem.demo.core.components.models.Article;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
public class ArticleTest {
    AemContext context = new AemContext();

    @BeforeEach
    public void setup() {
        context.load().json("/com/aem/demo/core/components/article.json", "/content");
        context.currentResource("/content/article");
    }

    @Test
    public void testArticle() {
        MockSlingHttpServletRequest req = context.request();
        Article article = req.adaptTo(Article.class);

        Assertions.assertNotNull(article);
        Assertions.assertEquals("Code Challenge", article.getArticleTitle());
        Assertions.assertEquals("<p>Hello World!</p>", article.getArticleDescription());
        Assertions.assertEquals("we-retail:activity/biking", article.getArticleTags());
    }
}
