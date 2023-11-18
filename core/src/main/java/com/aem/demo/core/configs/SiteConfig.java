package com.aem.demo.core.configs;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "AEM Demo Site Configuration")
public @interface SiteConfig {
    @Property(label = "Content Path")
    String contentPath();

    @Property(label = "Site Domain")
    String siteDomain();

    @Property(label = "Content Approver Group")
    String approverGroup();
}
