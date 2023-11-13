package com.aem.demo.core.components.internal.servlets;

import com.aem.demo.core.components.services.LoginService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.request.RequestParameterMap;
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

    @InjectMocks
    private LoginServlet loginServlet;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        Mockito.when(
            loginService.loginUser(Mockito.anyString(), Mockito.anyString())
        ).thenReturn(true);
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
        Assertions.assertEquals("Login Successful", response.getOutputAsString().trim());
    }
}
