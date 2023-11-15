package com.aem.demo.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface UserNavigation {
    String loginText();

    String loginUrl();

    String logoutText();

    String logoutUrl();

    String getUsername();
}
