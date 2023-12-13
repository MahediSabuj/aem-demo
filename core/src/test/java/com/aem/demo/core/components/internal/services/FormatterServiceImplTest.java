package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.FormatterService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
public class FormatterServiceImplTest {
    @Test
    public void testFormattedLink(AemContext context) {
        FormatterService formatterService = new FormatterServiceImpl();
        String formatterLink = formatterService.getFormattedLink(
           "/content/aem-demo/us/en", context.resourceResolver());

        Assertions.assertEquals("/content/aem-demo/us/en.html", formatterLink);
    }
}
