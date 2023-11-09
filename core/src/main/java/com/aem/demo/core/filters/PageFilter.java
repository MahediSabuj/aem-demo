package com.aem.demo.core.filters;

import com.day.cq.commons.Filter;
import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.wcm.api.Page;

public class PageFilter implements Filter<Page> {
    private static final String HIDE_SUB_PAGES_IN_NAV = "hideSubPagesInNav";

    @Override
    public boolean includes(Page page) {
        HierarchyNodeInheritanceValueMap pageProperties = new HierarchyNodeInheritanceValueMap(page.getParent().getContentResource());
        boolean isHideSubPages = pageProperties.getInherited(HIDE_SUB_PAGES_IN_NAV, false);

        return !page.isHideInNav() && !isHideSubPages;
    }
}
