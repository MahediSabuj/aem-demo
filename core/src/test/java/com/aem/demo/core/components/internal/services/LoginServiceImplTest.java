package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.LoginService;
import com.aem.demo.core.components.services.RestClientService;
import com.aem.demo.core.models.authentication.impl.AuthorizeModelImpl;
import com.aem.demo.core.models.authentication.impl.TokenModelImpl;
import com.aem.demo.core.models.authentication.impl.UserInfoModelImpl;
import com.aem.demo.core.services.AppConfigurationService;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@ExtendWith(AemContextExtension.class)
public class LoginServiceImplTest {
    @Mock
    private AppConfigurationService appConfigurationService;

    @Mock
    private RestClientService restClientService;

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
        boolean isLoggedIn = loginService.loginUser("sabuj", "10029");
        Assertions.assertTrue(isLoggedIn);
    }
}
