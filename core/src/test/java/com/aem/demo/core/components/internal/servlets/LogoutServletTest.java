package com.aem.demo.core.components.internal.servlets;

import com.aem.demo.core.components.services.FormatterService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

@ExtendWith(AemContextExtension.class)
public class LogoutServletTest {
    @Mock
    private FormatterService formatterService;

    @InjectMocks
    private LogoutServlet logoutServlet;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogoutServlet(AemContext context) throws IOException {
        SlingHttpServletRequest request = context.request();
        SlingHttpServletResponse response = context.response();

        logoutServlet.doGet(request, response);
        Assertions.assertEquals(302, response.getStatus());
    }
}
