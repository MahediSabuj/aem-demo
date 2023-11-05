package com.aem.demo.core.models.impl;

import com.aem.demo.core.models.ArticleModel;

public class ArticleModelImpl implements ArticleModel {
    protected String articleId;
    protected String articleAuthor;

    @Override
    public String getArticleId() {
        return articleId;
    }

    @Override
    public String getArticleAuthor() {
        return articleAuthor;
    }
}
