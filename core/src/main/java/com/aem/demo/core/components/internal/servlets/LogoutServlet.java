package com.aem.demo.core.components.internal.servlets;

import com.aem.demo.core.components.services.FormatterService;
import com.aem.demo.core.utils.SessionUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import java.io.IOException;

@Component(service = Servlet.class)
@SlingServletResourceTypes(
  resourceTypes = "aem-demo/components/usernavigation",
  selectors = "logout",
  extensions = "html",
  methods = HttpConstants.METHOD_GET
)
public class LogoutServlet extends SlingAllMethodsServlet {
    @Reference
    private FormatterService formatterService;

    @Override
    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        SessionUtils sessionUtils = new SessionUtils(request);
        sessionUtils.invalidate();

        String URL = formatterService.getFormattedLink(
            "/content/aem-demo/us/en", request.getResourceResolver());
        response.sendRedirect(URL);
    }
}
