package com.aem.demo.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

import java.util.List;

@ConsumerType
public interface MegaMenu {
    String getId();

    List<MegaMenuItem> getItems();
}
