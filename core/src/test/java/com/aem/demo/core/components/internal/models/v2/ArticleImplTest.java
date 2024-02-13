package com.aem.demo.core.components.internal.models.v2;

import com.aem.demo.core.components.models.Article;
import com.aem.demo.core.components.services.ContentFragmentService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

@ExtendWith(AemContextExtension.class)
public class ArticleImplTest {
    @Mock
    ContentFragmentService contentFragmentService;

    @BeforeEach
    public void setup(AemContext context) {
        MockitoAnnotations.openMocks(this);

        context.load().json("/content/aem-demo/article.json", "/content");
        context.load().json("/content/cq:tags/aem-demo/article.json", "/content/cq:tags/aem-demo");

        context.currentResource("/content/article/v2");
        context.registerService(ContentFragmentService.class, contentFragmentService);
    }

    @Test
    public void testArticle(AemContext context) {
        Map<String, Object> fragmentMap = new HashMap<>();
        fragmentMap.put("title", "AEM Community");
        fragmentMap.put("description", "<p>Hello World</p>");
        fragmentMap.put("author", "/content/dam/aem-demo/us/en/authors/mahedi-sabuj");
        fragmentMap.put("tags", new String[]{"aem-demo:activity/biking","aem-demo:activity/hiking"});

        Map<String, Object> authorFragmentMap = new HashMap<>();
        authorFragmentMap.put("name", "Mahedi Sabuj");

        Mockito.when(contentFragmentService.get(Mockito.any(), Mockito.eq("master"))).thenReturn(fragmentMap);
        Mockito.when(contentFragmentService.get(Mockito.any(), Mockito.eq(""))).thenReturn(authorFragmentMap);

        ModelFactory modelFactory = context.getService(ModelFactory.class);
        Article article = modelFactory.createModel(context.request(), ArticleImpl.class);

        Assertions.assertNotNull(article);
        Assertions.assertEquals("AEM Community", article.getTitle());
        Assertions.assertEquals("<p>Hello World</p>", article.getDescription());
        Assertions.assertEquals("Mahedi Sabuj", article.getAuthor());

        String[] articleTags = article.getTags();
        Assertions.assertNotNull(articleTags);
        Assertions.assertEquals(2, articleTags.length);
        Assertions.assertEquals("Biking", articleTags[0]);
        Assertions.assertEquals("Hiking", articleTags[1]);
    }
}
