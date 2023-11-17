package com.aem.demo.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface UserNavigation {
    String getLoginText();

    String getLoginUrl();

    String getLogoutText();

    String getLogoutUrl();

    String getUsername();
}
