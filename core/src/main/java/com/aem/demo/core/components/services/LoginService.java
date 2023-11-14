package com.aem.demo.core.components.services;

import com.aem.demo.core.models.authentication.UserInfoModel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;

public interface LoginService {
    String getAccessToken(String username, String password);

    UserInfoModel getUserInfo(String accessToken);

    boolean loginUser(SlingHttpServletRequest request, SlingHttpServletResponse response, String userId);
}
