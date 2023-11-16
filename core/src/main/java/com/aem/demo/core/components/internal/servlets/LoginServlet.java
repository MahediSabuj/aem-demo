package com.aem.demo.core.components.internal.servlets;

import com.aem.demo.core.components.services.FormatterService;
import com.aem.demo.core.components.services.LoginService;
import com.aem.demo.core.models.authentication.UserInfoModel;
import com.aem.demo.core.utils.ConstantUtils;
import com.aem.demo.core.utils.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;

import javax.servlet.Servlet;
import java.io.IOException;
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

    @Reference
    private FormatterService formatterService;

    private String getParameter(RequestParameterMap map, String param) {
        return Objects.requireNonNull(map.getValue(param)).getString();
    }

    @Override
    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        RequestParameterMap map = request.getRequestParameterMap();
        String username = getParameter(map, "username");
        String password = getParameter(map, "password");

        String accessToken = loginService.getAccessToken(username, password);
        if (StringUtils.isNotBlank(accessToken)) {
            UserInfoModel userInfoModel = loginService.getUserInfo(accessToken);
            if (StringUtils.isNotBlank(userInfoModel.getUsername())) {
                if (loginService.loginUser(request, response, ConstantUtils.AEM_SERVICE_USER)) {
                    SessionUtils session = new SessionUtils(request);
                    session.setAttribute("accessToken", accessToken);
                    session.setAttribute("userInfo", userInfoModel);
                }
            }
        }

        String URL = formatterService.getFormattedLink(
            "/content/aem-demo/us/en", request.getResourceResolver());
        response.sendRedirect(URL);
    }
}
