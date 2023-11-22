package com.aem.demo.core.components.models;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.day.cq.wcm.api.Page;
import org.osgi.annotation.versioning.ConsumerType;

import java.util.List;

@ConsumerType
public interface MegaMenuItem {
    boolean isActive();

    boolean isCurrent();

    String getName();

    String getTitle();

    String getLink();

    List<MegaMenuItem> getChildren();
}
