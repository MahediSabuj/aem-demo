package com.aem.demo.core.components.internal;

import com.adobe.cq.wcm.core.components.commons.link.Link;
import com.adobe.cq.wcm.core.components.commons.link.LinkManager;
import com.aem.demo.core.components.models.MegaMenuItem;
import com.day.cq.wcm.api.Page;

import java.util.List;

public class MegaMenuItemImpl implements MegaMenuItem {
    protected Page page;
    protected Link<Page> link;
    protected List<MegaMenuItem> children;
    protected boolean active;
    protected boolean current;

    public MegaMenuItemImpl(Page page, boolean active, boolean current, List<MegaMenuItem> children, LinkManager linkManager) {
        this.active = active;
        this.current = current;
        this.page = page;
        this.children = children;
        this.link = linkManager.get(page).build();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isCurrent() {
        return current;
    }

    @Override
    public String getName() {
        return page.getName();
    }

    @Override
    public String getTitle() {
        return page.getTitle();
    }

    @Override
    public Link<Page> getLink() {
        return link;
    }

    @Override
    public List<MegaMenuItem> getChildren() {
        return children;
    }
}
