package com.aem.demo.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

import java.util.Date;

@ConsumerType
public interface Article {
    String getTitle();

    String getDescription();

    String getAuthor();

    String[] getTags();

    Date getPublishDate();
}
