package com.aem.demo.core.components.internal.models;

import com.aem.demo.core.components.models.Article;
import com.aem.demo.core.components.services.RestClientService;
import com.aem.demo.core.models.impl.ArticleModelImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Objects;

@ExtendWith(AemContextExtension.class)
public class ArticleTest {
    final AemContext context = new AemContext();

    @Mock
    private RestClientService restClientService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        context.load().json("/com/aem/demo/core/components/article.json", "/content");
        context.currentResource("/content/article");
    }

    @Test
    public void testArticle() {
        context.addModelsForPackage("com.aem.demo.core.models");
        context.registerService(RestClientService.class, restClientService);

        ModelFactory modelFactory = context.getService(ModelFactory.class);
        MockSlingHttpServletRequest request = context.request();

        Mockito.when(restClientService.get(Mockito.anyString(), Mockito.eq(ArticleModelImpl.class)))
                .thenReturn(new ArticleModelImpl());

        Article article = Objects.requireNonNull(modelFactory).createModel(request, ArticleImpl.class);

        Assertions.assertNotNull(article);
        Assertions.assertEquals("AEM Community", article.getArticleTitle());
        Assertions.assertEquals("<p>Hello World!</p>", article.getArticleDescription());

        String[] articleTags = article.getArticleTags();
        Assertions.assertNotNull(articleTags);
        Assertions.assertEquals(2, articleTags.length);
        Assertions.assertEquals("Biking", articleTags[0]);
        Assertions.assertEquals("Hiking", articleTags[1]);
    }
}
