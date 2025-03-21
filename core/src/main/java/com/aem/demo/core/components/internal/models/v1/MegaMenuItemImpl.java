package com.aem.demo.core.components.internal.models.v1;

import com.aem.demo.core.components.models.MegaMenuItem;
import com.day.cq.wcm.api.Page;

import java.util.ArrayList;
import java.util.List;

public class MegaMenuItemImpl implements MegaMenuItem {
    protected Page page;
    protected List<MegaMenuItem> children;
    protected boolean active;
    protected boolean current;

    public MegaMenuItemImpl(Page page, boolean active, boolean current, List<MegaMenuItem> children) {
        this.active = active;
        this.current = current;
        this.page = page;
        this.children = children;
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
    public String getLink() {
        return page.getPath();
    }

    @Override
    public List<MegaMenuItem> getChildren() {
        return new ArrayList<>(children);
    }
}
