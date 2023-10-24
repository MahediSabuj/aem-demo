package com.aem.demo.core.components.internal;

import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.aem.demo.core.components.models.MegaMenu;
import com.aem.demo.core.components.models.MegaMenuItem;
import com.aem.demo.core.filters.PageFilter;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

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
  extensions = ExporterConstants.SLING_MODEL_EXTENSION
)
public class MegaMenuImpl implements MegaMenu {
    protected static final String RESOURCE_TYPE = "aem-demo/components/megamenu";

    @ValueMapValue
    String navigationRoot;

    @ValueMapValue
    @Default(intValues = 3)
    int structureDepth;

    @ValueMapValue
    @Default(booleanValues = false)
    boolean disableShadowing;

    @ValueMapValue
    String id;

    @Self
    private LinkManager linkManager;

    @ScriptVariable
    private Page currentPage;

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

        return new MegaMenuItemImpl(page, selected, current, children, linkManager);
    }

    @Override
    public List<MegaMenuItem> getItems() {
        if(this.items == null) {
            this.navigationRootPage = currentPage.getPageManager().getPage(navigationRoot);
            this.items = getRootItems(navigationRootPage)
                .stream().map(page -> createNavigationItem(page, getItems(page)))
                .collect(Collectors.toList());
        }
        return Collections.unmodifiableList(items);
    }

    @Override
    public String getId() {
        return id;
    }
}
