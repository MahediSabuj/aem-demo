package com.aem.demo.core.components.internal.servlets;

import com.aem.demo.core.components.services.FormatterService;
import com.aem.demo.core.components.services.LoginService;
import com.aem.demo.core.models.authentication.impl.UserInfoModelImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(AemContextExtension.class)
public class LoginServletTest {
    @Mock
    private LoginService loginService;

    @Mock
    private FormatterService formatterService;

    @InjectMocks
    private LoginServlet loginServlet;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(
            loginService.getAccessToken(Mockito.anyString(), Mockito.anyString())
        ).thenReturn("XXXX");

        Mockito.when(
            loginService.getUserInfo(Mockito.anyString())
        ).thenReturn(new UserInfoModelImpl(
            "AEM", "User", "sabuj@gmail.com", "sabuj@ms-29.com"));

        Mockito.when(
            loginService.loginUser(Mockito.any(), Mockito.any(), Mockito.anyString())
        ).thenReturn(true);

        Mockito.when(formatterService.getFormattedLink(
            Mockito.anyString(), Mockito.any())
        ).thenReturn("/content/aem-demo/us/en.html");
    }

    @Test
    public void testPost(AemContext context) throws IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        Map<String, Object> params = new HashMap<>();
        params.put("username", "sabuj@ms-29.com");
        params.put("password", "XXXXX");
        request.setParameterMap(params);

        loginServlet.doPost(request, response);
        Assertions.assertEquals(302, response.getStatus());
    }
}
