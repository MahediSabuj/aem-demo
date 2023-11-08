package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.models.ArticleModel;
import com.aem.demo.core.models.impl.ArticleModelImpl;
import com.aem.demo.core.services.AppConfigurationService;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.time.Duration;

@ExtendWith(AemContextExtension.class)
public class RestClientServiceImplTest {
    @Mock
    private HttpClient httpClient;

    @Mock
    private AppConfigurationService appConfigurationService;

    @Mock
    private HttpClient.Builder httpClientBuilder;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private RestClientServiceImpl restClientService;

    @BeforeEach
    public void setup() throws IOException, InterruptedException {
        MockitoAnnotations.openMocks(this);

        Mockito.when(appConfigurationService.getApiDomain()).thenReturn("https://www.google.com");
        Mockito.when(httpClientBuilder.version(HttpClient.Version.HTTP_2)).thenReturn(httpClientBuilder);
        Mockito.when(httpClientBuilder.connectTimeout(Duration.ofSeconds(10))).thenReturn(httpClientBuilder);
        Mockito.when(httpClientBuilder.build()).thenReturn(httpClient);

        Mockito.when(
            httpClient.send(Mockito.any(), Mockito.any(HttpResponse.BodyHandlers.ofString().getClass()))
        ).thenReturn(httpResponse);
    }

    @Test
    public void testHttpClientService() throws IOException, InterruptedException {
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn("{\"articleId\":\"1\",\"articleAuthor\":\"Mahedi Sabuj\"}");

        try(MockedStatic<HttpClient> mocked = Mockito.mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newBuilder).thenReturn(httpClientBuilder);

            ArticleModel articleModel = restClientService.get("https://www.google.com", ArticleModelImpl.class);
            Assertions.assertNotNull(articleModel);
            Assertions.assertEquals("1", articleModel.getArticleId());
            Assertions.assertEquals("Mahedi Sabuj", articleModel.getArticleAuthor());
        }
    }

    @Test
    public void testInvalidStatusCode() {
        Mockito.when(httpResponse.statusCode()).thenReturn(404);

        try(MockedStatic<HttpClient> mocked = Mockito.mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newBuilder).thenReturn(httpClientBuilder);

            ArticleModel articleModel = restClientService.get("https://www.google.com", ArticleModelImpl.class);
            Assertions.assertNull(articleModel);
        }
    }

    @Test
    public void testApiResponseWithInvalidProperty() {
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn("{\"articleTitle\":\"AEM\",\"articleAuthor\":\"Mahedi Sabuj\"}");

        try(MockedStatic<HttpClient> mocked = Mockito.mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newBuilder).thenReturn(httpClientBuilder);

            ArticleModel articleModel = restClientService.get("https://www.google.com", ArticleModelImpl.class);
            Assertions.assertNotNull(articleModel);
            Assertions.assertNull(articleModel.getArticleId());
            Assertions.assertEquals("Mahedi Sabuj", articleModel.getArticleAuthor());
        }
    }

    @Test
    public void testApiResponseWithInvalidResponse() {
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn("{'articleId':'AEM','articleAuthor':'Mahedi Sabuj'}");

        try(MockedStatic<HttpClient> mocked = Mockito.mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newBuilder).thenReturn(httpClientBuilder);

            ArticleModel articleModel = restClientService.get("https://www.google.com", ArticleModelImpl.class);
            Assertions.assertNull(articleModel);
        }
    }
}
