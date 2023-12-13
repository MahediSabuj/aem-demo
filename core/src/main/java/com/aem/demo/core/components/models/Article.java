package com.aem.demo.core.components.models;

import org.osgi.annotation.versioning.ConsumerType;

@ConsumerType
public interface Article {
    String getArticleTitle();

    String getArticleDescription();

    String getArticleAuthor();

    String[] getArticleTags();
}
