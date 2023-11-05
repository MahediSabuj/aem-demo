package com.aem.demo.core.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
  name = "AEM Demo Application Configuration")
public @interface AppConfig {
    @AttributeDefinition(name = "API Domain")
    String api_domain();
}
