package com.aem.demo.core.components.internal.models;

import com.aem.demo.core.components.models.MegaMenu;
import com.aem.demo.core.components.models.MegaMenuItem;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Objects;

@ExtendWith(AemContextExtension.class)
public class MegaMenuImplTest {
    private AemContext context = new AemContext();

    @BeforeEach
    public void setup() {
        context.load().json("/com/aem/demo/core/components/megamenu.json", "/content");
        context.currentResource("/content/aem-demo/us/en/mobile/service/jcr:content/root/container/megamenu");
    }

    @Test
    public void testMegaMenu() {
        context.addModelsForPackage("com.aem.demo.core.models");

        ModelFactory modelFactory = context.getService(ModelFactory.class);
        MockSlingHttpServletRequest request = context.request();

        MegaMenu megaMenu = Objects.requireNonNull(modelFactory).createModel(request, MegaMenuImpl.class); //req.adaptTo(Article.class);
        Assertions.assertNotNull(megaMenu);

        Assertions.assertEquals("megamenu-48ea24db16", megaMenu.getId());
        List<MegaMenuItem> megaMenuItems = megaMenu.getItems();
        Assertions.assertNotNull(megaMenuItems);
        Assertions.assertTrue(megaMenuItems.size() > 0);

        MegaMenuItem mobileMenuItem = megaMenuItems.get(0);
        Assertions.assertNull(mobileMenuItem.getLink());
        Assertions.assertTrue(mobileMenuItem.isActive());
        Assertions.assertFalse(mobileMenuItem.isCurrent());
        Assertions.assertEquals("mobile", mobileMenuItem.getName());
        Assertions.assertEquals("Mobile", mobileMenuItem.getTitle());

        // Verify Hide In Navigation
        List<MegaMenuItem> mobileChildItems = mobileMenuItem.getChildren();
        Assertions.assertNotNull(mobileChildItems);
        Assertions.assertEquals(2, mobileChildItems.size());

        // Verify Hide SubPages in Navigation
        MegaMenuItem planSubMenuItem = mobileChildItems.get(1);
        List<MegaMenuItem> PostpaidSubMenuItem = planSubMenuItem.getChildren();
        Assertions.assertNotNull(PostpaidSubMenuItem);
        Assertions.assertEquals(0, PostpaidSubMenuItem.size());

    }
}
