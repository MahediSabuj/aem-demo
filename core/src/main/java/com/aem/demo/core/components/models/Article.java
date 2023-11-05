package com.aem.demo.core.components.models;

import com.aem.demo.core.models.ArticleModel;

public interface Article extends ArticleModel {
    String getArticleTitle();

    String getArticleDescription();

    String[] getArticleTags();
}
