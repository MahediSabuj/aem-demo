package com.aem.demo.core.components.internal.services.contentfragment;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.aem.demo.core.components.models.Article;
import com.aem.demo.core.components.services.contentfragment.ArticleService;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

@Component(service = { ArticleService.class },
  property = {
    Constants.SERVICE_DESCRIPTION + "=Article Service"
})
public class ArticleServiceImpl implements ArticleService {
    @Override
    public boolean updateContentFragment(ContentFragment fragment, Article article) {
        return false;
    }
}
