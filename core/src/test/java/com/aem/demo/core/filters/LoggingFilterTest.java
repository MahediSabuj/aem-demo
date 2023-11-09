package com.aem.demo.core.filters;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.servlet.MockRequestPathInfo;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

@ExtendWith(AemContextExtension.class)
public class LoggingFilterTest {
    private LoggingFilter loggingFilter = new LoggingFilter();

    private TestLogger logger = TestLoggerFactory.getTestLogger(loggingFilter.getClass());

    @BeforeEach
    public void setup() {
        TestLoggerFactory.clear();
    }

    @Test
    public void testLoggingFilter(AemContext context) throws ServletException, IOException {
        MockSlingHttpServletRequest request = context.request();
        MockSlingHttpServletResponse response = context.response();

        MockRequestPathInfo requestPathInfo = (MockRequestPathInfo) request.getRequestPathInfo();
        requestPathInfo.setResourcePath("/content/aem-demo/us/en");

        loggingFilter.init(Mockito.mock(FilterConfig.class));
        loggingFilter.doFilter(request, response, Mockito.mock(FilterChain.class));
        loggingFilter.destroy();

        List<LoggingEvent> events = logger.getLoggingEvents();
        Assertions.assertEquals(1, events.size());

        LoggingEvent event = events.get(0);
        Assertions.assertEquals(Level.DEBUG, event.getLevel());
    }
}
