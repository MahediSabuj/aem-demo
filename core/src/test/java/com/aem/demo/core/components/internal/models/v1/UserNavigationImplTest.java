package com.aem.demo.core.components.internal.models.v1;

import com.aem.demo.core.components.models.UserNavigation;
import com.aem.demo.core.components.services.FormatterService;
import com.aem.demo.core.components.services.LoginService;
import com.aem.demo.core.models.authentication.impl.UserInfoModelImpl;
import com.aem.demo.core.utils.AppConstants;
import com.day.crx.JcrConstants;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.factory.ModelFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@ExtendWith(AemContextExtension.class)
public class UserNavigationImplTest {
    private final static String LOGIN_PAGE = "/content/aem-demo/us/en/login";
    private final static String USER_NAVIGATION = "/root/usernavigation";
    private final static String LOGOUT_SELECTOR = ".logout";

    @Mock
    private FormatterService formatterService;

    @Mock
    private LoginService loginService;

    private String userNavigationPath;

    @BeforeEach
    public void setup(AemContext context) {
        MockitoAnnotations.openMocks(this);

        context.load().json(
           "/com/aem/demo/core/components/internal/models/v1/usernavigation.json",
           "/content");

        userNavigationPath = String.format("%s/%s%s",
            LOGIN_PAGE, JcrConstants.JCR_CONTENT, USER_NAVIGATION);
        context.currentResource(userNavigationPath);

        Mockito.when(loginService.isLoggedIn(Mockito.any()))
            .thenReturn(new UserInfoModelImpl(
               "AEM", "User", "sabuj@gmail.com", "sabuj@ms-29.com"));

        Mockito.when(formatterService.getFormattedLink(
            Mockito.eq(LOGIN_PAGE), Mockito.any())
        ).thenReturn(LOGIN_PAGE + AppConstants.HTML_EXTENSION);

        Mockito.when(formatterService.getFormattedLink(
            Mockito.eq(userNavigationPath + LOGOUT_SELECTOR), Mockito.any()
        )).thenReturn(userNavigationPath + LOGOUT_SELECTOR + AppConstants.HTML_EXTENSION);
    }

    @Test
    public void testUserNavigation(AemContext context) {
        SlingHttpServletRequest request = context.request();
        context.registerService(LoginService.class, loginService);
        context.registerService(FormatterService.class, formatterService);

        ModelFactory modelFactory = context.getService(ModelFactory.class);

        UserNavigation userNavigation = modelFactory.createModel(
            request, UserNavigationImpl.class);

        Assertions.assertNotNull(userNavigation);
        Assertions.assertEquals("Login", userNavigation.getLoginText());
        Assertions.assertEquals("Logout", userNavigation.getLogoutText());
        Assertions.assertEquals("AEM User", userNavigation.getUsername());

        Assertions.assertEquals(
           LOGIN_PAGE + AppConstants.HTML_EXTENSION, userNavigation.getLoginUrl());
        Assertions.assertEquals(
           userNavigationPath + LOGOUT_SELECTOR + AppConstants.HTML_EXTENSION,
            userNavigation.getLogoutUrl());
    }

    @Test
    public void testNotLoggedIn(AemContext context) {
        SlingHttpServletRequest request = context.request();
        context.registerService(LoginService.class, loginService);
        context.registerService(FormatterService.class, formatterService);

        Mockito.when(loginService.isLoggedIn(Mockito.any())).thenReturn(null);

        ModelFactory modelFactory = context.getService(ModelFactory.class);
        UserNavigation userNavigation = modelFactory.createModel(
            request, UserNavigationImpl.class);

        Assertions.assertEquals(StringUtils.EMPTY, userNavigation.getUsername());
    }
}
