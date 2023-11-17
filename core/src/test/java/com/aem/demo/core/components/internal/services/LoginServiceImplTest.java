package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.RestClientService;
import com.aem.demo.core.models.authentication.UserInfoModel;
import com.aem.demo.core.models.authentication.impl.AuthorizeModelImpl;
import com.aem.demo.core.models.authentication.impl.TokenModelImpl;
import com.aem.demo.core.models.authentication.impl.UserInfoModelImpl;
import com.aem.demo.core.services.AppConfigService;
import com.aem.demo.core.utils.AppConstants;
import com.aem.demo.core.utils.SessionUtils;
import com.day.crx.security.token.TokenUtil;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.auth.core.spi.AuthenticationInfo;
import org.apache.sling.jcr.api.SlingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

@ExtendWith(AemContextExtension.class)
public class LoginServiceImplTest {
    private static final String LOGIN_USER_ID = "login-user";

    @Mock
    private SlingRepository slingRepository;

    @Mock
    private Session session;

    @Mock
    private AppConfigService appConfigService;

    @Mock
    private RestClientService restClientService;

    @InjectMocks
    private LoginServiceImpl loginService;

    private UserInfoModelImpl userInfoModel;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(appConfigService.getApiBaseUrl()).thenReturn("https://www.google.com");
        Mockito.when(appConfigService.getClientId()).thenReturn("XXX");
        Mockito.when(appConfigService.getRedirectUri()).thenReturn("https://www.google.com/callback");

        Mockito.when(restClientService.post(
            Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.eq(AuthorizeModelImpl.class))
        ).thenReturn(new AuthorizeModelImpl("XXX"));

        Mockito.when(restClientService.post(
            Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.eq(TokenModelImpl.class))
        ).thenReturn(new TokenModelImpl("ZZZ"));

        userInfoModel = new UserInfoModelImpl(
            "Mahedi", "Sabuj", "sabuj@gmail.com", "sabuj@ms-29.com");

        Mockito.when(restClientService.post(
            Mockito.anyString(), Mockito.anyString(), Mockito.any(), Mockito.eq(UserInfoModelImpl.class))
        ).thenReturn(userInfoModel);
    }

    @Test
    public void testLoginService() {
        String accessToken = loginService.getAccessToken("sabuj@ms-29.com", "XXX");
        Assertions.assertEquals("ZZZ", accessToken);

        UserInfoModel userInfoModel = loginService.getUserInfo(accessToken);
        Assertions.assertNotNull(userInfoModel);
        Assertions.assertEquals("Mahedi", userInfoModel.getFirstName());
    }

    @Test
    public void testLoginUser(AemContext context) {
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
    public void testLoginUserStatus(AemContext context) {
        SlingHttpServletRequest request = context.request();
        context.registerAdapter(ResourceResolver.class, Session.class, session);

        SessionUtils sessionUtils = new SessionUtils(request);
        sessionUtils.setAttribute("accessToken", "XXX");
        sessionUtils.setAttribute("userInfo", userInfoModel);

        Mockito.when(session.getUserID()).thenReturn(AppConstants.AEM_SERVICE_USER);

        UserInfoModel userInfoModel = loginService.isLoggedIn(request);
        Assertions.assertNotNull(userInfoModel);
        Assertions.assertEquals("Mahedi", userInfoModel.getFirstName());
        Assertions.assertEquals("Sabuj", userInfoModel.getLastName());
        Assertions.assertEquals("sabuj@gmail.com", userInfoModel.getEmail());
        Assertions.assertEquals("sabuj@ms-29.com", userInfoModel.getUsername());
    }

    @Test
    public void testNotLoggedIn(AemContext context) {
        SlingHttpServletRequest request = context.request();

        UserInfoModel userInfoModel = loginService.isLoggedIn(request);
        Assertions.assertNull(userInfoModel);
    }

    @Test
    public void testLoginUserException(AemContext context) {
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
