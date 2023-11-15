package com.aem.demo.core.utils;

import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SessionUtils {
    private final HttpSession httpSession;

    public SessionUtils(SlingHttpServletRequest request) {
        this.httpSession = request.getSession();
    }

    public void setAttribute(String key, Object value) {
        httpSession.setAttribute(key, value);
    }

    public Object getAttribute(String key) {
        return Optional.ofNullable(httpSession)
            .map(session -> session.getAttribute(key))
            .orElse(null);
    }
}
