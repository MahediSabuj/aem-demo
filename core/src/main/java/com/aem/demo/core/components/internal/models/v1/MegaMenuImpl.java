package com.aem.demo.core.components.internal.models.v1;

import com.adobe.cq.export.json.ExporterConstants;
import com.aem.demo.core.components.models.MegaMenu;
import com.aem.demo.core.components.models.MegaMenuItem;
import com.aem.demo.core.components.services.ResourceResolverService;
import com.aem.demo.core.filters.PageFilter;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Model(
  adaptables = { SlingHttpServletRequest.class },
  adapters = { MegaMenu.class },
  resourceType = { MegaMenuImpl.RESOURCE_TYPE },
  defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(
  name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
  extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class MegaMenuImpl implements MegaMenu {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    protected static final String RESOURCE_TYPE = "aem-demo/components/megamenu/v1/megamenu";

    @ValueMapValue
    String navigationRoot;

    @ValueMapValue
    @Default(intValues = 3)
    int structureDepth;

    @ValueMapValue
    String id;

    @ScriptVariable
    private Page currentPage;

    @OSGiService
    private ResourceResolverService resolverService;

    private List<MegaMenuItem> items;
    private Page navigationRootPage;

    private List<Page> getRootItems(final Page navigationRootPage) {
        Iterator<Page> childIterator = navigationRootPage.listChildren(new PageFilter());

        return StreamSupport.stream(((Iterable<Page>) () -> childIterator).spliterator() , false)
            .collect(Collectors.toList());
    }

    private List<MegaMenuItem> getItems(final Page subtreeRoot) {
        if (subtreeRoot.getDepth() - this.navigationRootPage.getDepth() < this.structureDepth) {
            Iterator<Page> childIterator = subtreeRoot.listChildren(new PageFilter());

            return StreamSupport.stream(((Iterable<Page>) () -> childIterator).spliterator() , false)
                .map(page -> createNavigationItem(page, getItems(page)))
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private boolean checkSelected(final Page page, boolean current) {
        return current || this.currentPage.getPath().startsWith(page.getPath() + "/");
    }

    private boolean checkCurrent(final Page page) {
        return this.currentPage.equals(page);
    }

    private MegaMenuItem createNavigationItem(final Page page, final List<MegaMenuItem> children) {
        boolean current = checkCurrent(page);
        boolean selected = checkSelected(page, current);

        return new MegaMenuItemImpl(page, selected, current, children);
    }

    @Override
    public List<MegaMenuItem> getItems() {
        if(this.items == null) {
            try {
                ResourceResolver resolver = resolverService.getResourceResolver();
                PageManager pageManager = resolver.adaptTo(PageManager.class);
                this.navigationRootPage = pageManager.getPage(navigationRoot);
                this.items = getRootItems(navigationRootPage)
                    .stream().map(page -> createNavigationItem(page, getItems(page)))
                    .collect(Collectors.toList());
            } catch (LoginException e) {
                LOG.error("Error in Resource Resolver");
            }
        }
        return items != null ? Collections.unmodifiableList(items) : null;
    }

    @Override
    public String getId() {
        return id;
    }
}
