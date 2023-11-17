package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.LoginService;
import com.aem.demo.core.components.services.RestClientService;
import com.aem.demo.core.models.authentication.AuthorizeModel;
import com.aem.demo.core.models.authentication.TokenModel;
import com.aem.demo.core.models.authentication.UserInfoModel;
import com.aem.demo.core.models.authentication.impl.AuthorizeModelImpl;
import com.aem.demo.core.models.authentication.impl.TokenModelImpl;
import com.aem.demo.core.models.authentication.impl.UserInfoModelImpl;
import com.aem.demo.core.services.AppConfigService;
import com.aem.demo.core.utils.AppConstants;
import com.aem.demo.core.utils.SessionUtils;
import com.day.crx.security.token.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component(service = { LoginService.class },
  property = {
    Constants.SERVICE_DESCRIPTION + "=Login Service"
  }
)
public class LoginServiceImpl implements LoginService {
    private static final String RESPONSE_TYPE = "code_credentials";

    @Reference
    private AppConfigService appConfigService;

    @Reference
    private RestClientService restClientService;

    @Reference
    private SlingRepository slingRepository;

    private String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

    private String getFormDataAsString(Map<String, String> params) {
        return params.entrySet()
            .stream()
            .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
            .collect(Collectors.joining("&"));
    }

    private String getRedirectUri() {
        return appConfigService.getApiBaseUrl()
            .concat(appConfigService.getRedirectUri());
    }

    private String getAuthorizeCode(String username, String password) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Auth-Request-Type", "Named-User");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Authorization", getBasicAuthenticationHeader(username, password));

        Map<String, String> params = new HashMap<>();
        params.put("response_type", RESPONSE_TYPE);
        params.put("client_id", appConfigService.getClientId());
        params.put("redirect_uri", getRedirectUri());

        AuthorizeModel authorizeModel = restClientService.post(
            "/services/oauth2/authorize",
            getFormDataAsString(params),
            headers,
            AuthorizeModelImpl.class
        );

        return authorizeModel.getCode();
    }

    private String getAccessToken(String code) {
        Map<String, String> params = new HashMap<>();
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        params.put("client_id", appConfigService.getClientId());
        params.put("redirect_uri", getRedirectUri());

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        TokenModel tokenModel = restClientService.post(
            "/services/oauth2/token",
            getFormDataAsString(params),
            headers,
            TokenModelImpl.class
        );

        return tokenModel.getAccessToken();
    }

    public String getAccessToken(String username, String password) {
        String code = getAuthorizeCode(username, password);
        return getAccessToken(code);
    }

    public UserInfoModel getUserInfo(String accessToken) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + accessToken);

        return restClientService.post(
            "/services/oauth2/userinfo",
            StringUtils.EMPTY,
            headers,
            UserInfoModelImpl.class
        );
    }

    @Override
    public boolean loginUser(SlingHttpServletRequest request, SlingHttpServletResponse response, String userId) {
        try {
            TokenUtil.createCredentials(
                request, response, slingRepository, userId, true);
        } catch (RepositoryException | IllegalArgumentException ex) {
            return false;
        }

        return true;
    }

    @Override
    public UserInfoModel isLoggedIn(SlingHttpServletRequest request) {
        SessionUtils sessionUtils = new SessionUtils(request);
        Object accessTokenValue = sessionUtils.getAttribute("accessToken");
        String accessToken = accessTokenValue != null ? accessTokenValue.toString() : "";

        if (StringUtils.isNotBlank(accessToken)) {
            Session session = request.getResourceResolver().adaptTo(Session.class);
            if (session != null) {
                String userId = session.getUserID();
                if (userId != null && userId.equals(AppConstants.AEM_SERVICE_USER)) {
                    return (UserInfoModel) sessionUtils.getAttribute("userInfo");
                }
            }
        }

        return null;
    }
}
