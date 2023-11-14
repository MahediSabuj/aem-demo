package com.aem.demo.core.utils;

import org.apache.sling.api.SlingHttpServletRequest;

import javax.servlet.http.HttpSession;

public class SessionUtils {
    private final HttpSession httpSession;

    public SessionUtils(SlingHttpServletRequest request) {
        this.httpSession = request.getSession();
    }

    public void setAttribute(String key, Object value) {
        httpSession.setAttribute(key, value);
    }
}
