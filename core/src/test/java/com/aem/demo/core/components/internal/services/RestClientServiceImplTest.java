package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.models.authentication.AuthorizeModel;
import com.aem.demo.core.models.authentication.TokenModel;
import com.aem.demo.core.models.authentication.UserInfoModel;
import com.aem.demo.core.models.authentication.impl.AuthorizeModelImpl;
import com.aem.demo.core.models.authentication.impl.TokenModelImpl;
import com.aem.demo.core.models.authentication.impl.UserInfoModelImpl;
import com.aem.demo.core.services.AppConfigService;
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
import java.util.HashMap;
import java.util.Map;

@ExtendWith(AemContextExtension.class)
public class RestClientServiceImplTest {
    @Mock
    private HttpClient httpClient;

    @Mock
    private AppConfigService appConfigService;

    @Mock
    private HttpClient.Builder httpClientBuilder;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private RestClientServiceImpl restClientService;

    @BeforeEach
    public void setup() throws IOException, InterruptedException {
        MockitoAnnotations.openMocks(this);

        Mockito.when(appConfigService.getApiBaseUrl()).thenReturn("https://www.google.com");
        Mockito.when(httpClientBuilder.version(HttpClient.Version.HTTP_1_1)).thenReturn(httpClientBuilder);
        Mockito.when(httpClientBuilder.followRedirects(HttpClient.Redirect.NORMAL)).thenReturn(httpClientBuilder);
        Mockito.when(httpClientBuilder.connectTimeout(Duration.ofSeconds(10))).thenReturn(httpClientBuilder);
        Mockito.when(httpClientBuilder.build()).thenReturn(httpClient);

        Mockito.when(
            httpClient.send(Mockito.any(), Mockito.any(HttpResponse.BodyHandlers.ofString().getClass()))
        ).thenReturn(httpResponse);
    }

    @Test
    public void testHttpClientService() {
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn("{" +
                "\"preferred_username\": \"mahedi.sabuj@gmail.com\"," +
                "\"email\": \"sabuj.ict.mbstu@gmail.com\"," +
                "\"given_name\": \"AEM\"," +
                "\"family_name\": \"User\"}");

        try(MockedStatic<HttpClient> mocked = Mockito.mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newBuilder).thenReturn(httpClientBuilder);

            UserInfoModel userInfoModel = restClientService.get("https://www.google.com", null, UserInfoModelImpl.class);
            Assertions.assertNotNull(userInfoModel);
            Assertions.assertEquals("AEM", userInfoModel.getFirstName());
            Assertions.assertEquals("User", userInfoModel.getLastName());
            Assertions.assertEquals("mahedi.sabuj@gmail.com", userInfoModel.getUsername());
            Assertions.assertEquals("sabuj.ict.mbstu@gmail.com", userInfoModel.getEmail());
            Assertions.assertNull(userInfoModel.getCountry());
        }
    }

    @Test
    public void testInvalidStatusCode() {
        Mockito.when(httpResponse.statusCode()).thenReturn(404);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        try(MockedStatic<HttpClient> mocked = Mockito.mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newBuilder).thenReturn(httpClientBuilder);

            TokenModel tokenModel = restClientService.get("https://www.google.com", headers, TokenModelImpl.class);
            Assertions.assertNull(tokenModel);
        }
    }

    @Test
    public void testApiResponseWithInvalidProperty() {
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn("{\"access_token\":\"XXX\",\"code\":\"ZZZ\"}");

        try(MockedStatic<HttpClient> mocked = Mockito.mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newBuilder).thenReturn(httpClientBuilder);

            TokenModel tokenModel = restClientService.get("https://www.google.com", null, TokenModelImpl.class);
            Assertions.assertNotNull(tokenModel);
            Assertions.assertEquals("XXX", tokenModel.getAccessToken());
        }
    }

    @Test
    public void testApiResponseWithInvalidResponse() {
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn("{'code':'XXX'}");

        try(MockedStatic<HttpClient> mocked = Mockito.mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newBuilder).thenReturn(httpClientBuilder);

            AuthorizeModel authorizeModel = restClientService.get("https://www.google.com", null, AuthorizeModelImpl.class);
            Assertions.assertNull(authorizeModel);
        }
    }

    @Test
    public void testPostRequest() {
        Mockito.when(httpResponse.statusCode()).thenReturn(200);
        Mockito.when(httpResponse.body()).thenReturn("{\"code\":\"XXX\"}");

        try(MockedStatic<HttpClient> mocked = Mockito.mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newBuilder).thenReturn(httpClientBuilder);

            AuthorizeModel authorizeModel = restClientService.post("https://www.google.com", "", null, AuthorizeModelImpl.class);
            Assertions.assertNotNull(authorizeModel);
            Assertions.assertEquals("XXX", authorizeModel.getCode());
        }
    }

    @Test
    public void testPostRequestThrowException() throws IOException, InterruptedException {
        Mockito.when(
            httpClient.send(Mockito.any(), Mockito.any(HttpResponse.BodyHandlers.ofString().getClass()))
        ).thenThrow(IOException.class);

        try(MockedStatic<HttpClient> mocked = Mockito.mockStatic(HttpClient.class)) {
            mocked.when(HttpClient::newBuilder).thenReturn(httpClientBuilder);

            AuthorizeModel authorizeModel = restClientService.post("https://www.google.com", "", null, AuthorizeModelImpl.class);
            Assertions.assertNull(authorizeModel);
        }
    }
}
