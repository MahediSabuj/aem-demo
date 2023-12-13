package com.aem.demo.core.components.internal.models.v1;

import com.aem.demo.core.components.models.MegaMenu;
import com.aem.demo.core.components.models.MegaMenuItem;
import com.aem.demo.core.components.services.ResourceResolverService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.models.factory.ModelFactory;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Objects;

@ExtendWith(AemContextExtension.class)
public class MegaMenuImplTest {
    @Mock
    private ResourceResolverService resolverService;

    @BeforeEach
    public void setup(AemContext context) {
        MockitoAnnotations.openMocks(this);

        context.load().json("/content/aem-demo/megamenu.json", "/content");
        context.currentResource("/content/aem-demo/us/en/mobile/service/jcr:content/root/container/megamenu");

        context.addModelsForPackage("com.aem.demo.core.models");
        context.registerService(ResourceResolverService.class, resolverService);
    }

    @Test
    public void testMegaMenu(AemContext context) throws LoginException {
        Mockito.when(resolverService.getResourceResolver()).thenReturn(context.resourceResolver());

        ModelFactory modelFactory = context.getService(ModelFactory.class);
        MockSlingHttpServletRequest request = context.request();

        MegaMenu megaMenu = Objects.requireNonNull(modelFactory).createModel(request, MegaMenuImpl.class); //req.adaptTo(Article.class);
        Assertions.assertNotNull(megaMenu);

        Assertions.assertEquals("megamenu-48ea24db16", megaMenu.getId());
        List<MegaMenuItem> megaMenuItems = megaMenu.getItems();
        Assertions.assertNotNull(megaMenuItems);
        Assertions.assertTrue(megaMenuItems.size() > 0);

        MegaMenuItem mobileMenuItem = megaMenuItems.get(0);
        Assertions.assertNotNull(mobileMenuItem.getLink());
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

    @Test
    public void testLoginException(AemContext context) throws LoginException {
        Mockito.when(resolverService.getResourceResolver()).thenThrow(LoginException.class);

        ModelFactory modelFactory = context.getService(ModelFactory.class);
        MockSlingHttpServletRequest request = context.request();

        MegaMenu megaMenu = Objects.requireNonNull(modelFactory).createModel(request, MegaMenuImpl.class); //req.adaptTo(Article.class);
        Assertions.assertNotNull(megaMenu);

        List<MegaMenuItem> megaMenuItems = megaMenu.getItems();
        Assertions.assertNull(megaMenuItems);
    }
}
