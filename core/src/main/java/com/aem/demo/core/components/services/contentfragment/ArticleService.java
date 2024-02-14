package com.aem.demo.core.components.services.contentfragment;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.aem.demo.core.components.models.Article;

public interface ArticleService {
    boolean updateContentFragment(ContentFragment fragment, Article article);
}
