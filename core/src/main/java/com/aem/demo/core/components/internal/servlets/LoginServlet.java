package com.aem.demo.core.components.internal.servlets;

import com.aem.demo.core.components.services.LoginService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

@Component(service = { Servlet.class })
@SlingServletResourceTypes(
  resourceTypes = "aem-demo/components/login/v1/login",
  selectors = "login",
  extensions = "html",
  methods = HttpConstants.METHOD_POST)
@ServiceDescription("Login Servlet")
public class LoginServlet extends SlingAllMethodsServlet {
    @Reference
    private LoginService loginService;

    private String getParameter(RequestParameterMap map, String param) {
        return Objects.requireNonNull(map.getValue(param)).getString();
    }

    @Override
    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        RequestParameterMap map = request.getRequestParameterMap();
        String username = getParameter(map, "username");
        String password = getParameter(map, "password");

        String loginResponse = "Invalid Credentials";

        if(loginService.loginUser(username, password)) {
            loginResponse = "Login Successful";
        }

        try(PrintWriter printWriter = response.getWriter()) {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            printWriter.println(loginResponse);
        }
    }
}
