package com.aem.demo.core.components.internal.models.v1;

import com.adobe.cq.export.json.ExporterConstants;
import com.aem.demo.core.components.models.UserNavigation;
import com.aem.demo.core.components.services.FormatterService;
import com.aem.demo.core.components.services.LoginService;
import com.aem.demo.core.models.authentication.UserInfoModel;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.*;

import javax.annotation.PostConstruct;
import javax.inject.Named;

@Model(
  adaptables = { SlingHttpServletRequest.class },
  adapters = { UserNavigation.class },
  resourceType = { UserNavigationImpl.RESOURCE_TYPE },
  defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
  name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
  extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class UserNavigationImpl implements UserNavigation {
    protected static final String RESOURCE_TYPE = "aem-demo/components/usernavigation";

    @ValueMapValue
    @Named("loginPage")
    String loginPagePath;

    @ValueMapValue
    String logoutText;

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private ResourceResolver resourceResolver;

    @SlingObject
    private Resource resource;

    @ScriptVariable
    private PageManager pageManager;

    @OSGiService
    private FormatterService formatterService;

    @OSGiService
    private LoginService loginService;

    private String loginText;
    private String loginUrl;

    @PostConstruct
    public void init() {
        final Page loginPage = pageManager.getContainingPage(loginPagePath);
        if (loginPage != null) {
            loginText = loginPage.getTitle();
            loginUrl = formatterService.getFormattedLink(loginPage.getPath(), resourceResolver);
        }
    }

    @Override
    public String loginText() {
        return loginText;
    }

    @Override
    public String loginUrl() {
        return loginUrl;
    }

    @Override
    public String logoutText() {
        return logoutText;
    }

    @Override
    public String logoutUrl() {
        final String URL = String.format("%s.logout.html", resource.getPath());
        return formatterService.getFormattedLink(URL, resourceResolver);
    }

    @Override
    public String getUsername() {
        UserInfoModel userInfoModel = loginService.isLoggedIn(request);

        if (userInfoModel != null && StringUtils.isNotBlank(userInfoModel.getUsername())) {
            return String.format("%s %s", userInfoModel.getFirstName(), userInfoModel.getLastName());
        }

        return StringUtils.EMPTY;
    }
}
