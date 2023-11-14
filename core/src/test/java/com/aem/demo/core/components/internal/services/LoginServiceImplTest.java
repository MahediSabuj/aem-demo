package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.RestClientService;
import com.aem.demo.core.models.authentication.UserInfoModel;
import com.aem.demo.core.models.authentication.impl.AuthorizeModelImpl;
import com.aem.demo.core.models.authentication.impl.TokenModelImpl;
import com.aem.demo.core.models.authentication.impl.UserInfoModelImpl;
import com.aem.demo.core.services.AppConfigurationService;
import com.day.crx.security.token.TokenUtil;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.auth.core.spi.AuthenticationInfo;
import org.apache.sling.jcr.api.SlingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import javax.jcr.RepositoryException;

@ExtendWith(AemContextExtension.class)
public class LoginServiceImplTest {
    private final AemContext context = new AemContext();

    private static final String LOGIN_USER_ID = "login-user";

    @Mock
    private AppConfigurationService appConfigurationService;

    @Mock
    private RestClientService restClientService;

    @Mock
    private SlingRepository slingRepository;

    @InjectMocks
    private LoginServiceImpl loginService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(appConfigurationService.getApiBaseUrl()).thenReturn("https://www.google.com");
        Mockito.when(appConfigurationService.getClientId()).thenReturn("XXX");
        Mockito.when(appConfigurationService.getRedirectUri()).thenReturn("https://www.google.com/callback");

        Mockito.when(restClientService.post(
            Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.eq(AuthorizeModelImpl.class))
        ).thenReturn(new AuthorizeModelImpl("XXX"));

        Mockito.when(restClientService.post(
            Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.eq(TokenModelImpl.class))
        ).thenReturn(new TokenModelImpl("ZZZ"));

        Mockito.when(restClientService.post(
            Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.eq(UserInfoModelImpl.class))
        ).thenReturn(new UserInfoModelImpl(
            "Mahedi", "Sabuj", "sabuj@gmail.com", "sabuj@ms-29.com")
        );
    }

    @Test
    public void testLoginService() {
        String accessToken = loginService.getAccessToken("sabuj", "XXX");
        Assertions.assertEquals("ZZZ", accessToken);

        UserInfoModel userInfoModel = loginService.getUserInfo(accessToken);
        Assertions.assertNotNull(userInfoModel);
        Assertions.assertEquals("Mahedi", userInfoModel.getFirstName());
    }

    @Test
    public void testLoginUser() {
        try (MockedStatic<TokenUtil> mockedStatic = Mockito.mockStatic(TokenUtil.class)){
            mockedStatic.when(() -> TokenUtil.createCredentials(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.anyBoolean()
            )).thenReturn(new AuthenticationInfo("TOKEN", LOGIN_USER_ID));

            boolean isLoggedIn = loginService.loginUser(
                context.request(), context.response(), LOGIN_USER_ID);

            Assertions.assertTrue(isLoggedIn);
        }
    }

    @Test
    public void testLoginUserException() {
        try (MockedStatic<TokenUtil> mockedStatic = Mockito.mockStatic(TokenUtil.class)){
            mockedStatic.when(() -> TokenUtil.createCredentials(
                    Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyString(), Mockito.anyBoolean()
            )).thenThrow(RepositoryException.class);

            boolean isLoggedIn = loginService.loginUser(
                context.request(), context.response(), LOGIN_USER_ID);

            Assertions.assertFalse(isLoggedIn);
        }
    }
}
