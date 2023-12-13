package com.aem.demo.core.configs;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
  name = "AEM Demo Application Configuration")
public @interface AppConfig {
    @AttributeDefinition(name = "API Base URL")
    String api_baseurl();

    @AttributeDefinition(name = "Salesforce Connected App CLient ID")
    String client_id();

    @AttributeDefinition(name = "Salesforce Connected App Callback URL")
    String redirect_uri();
}
