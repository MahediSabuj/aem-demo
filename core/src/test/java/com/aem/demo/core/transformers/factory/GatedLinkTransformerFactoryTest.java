package com.aem.demo.core.transformers.factory;

import com.aem.demo.core.components.internal.services.FormatterServiceImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.rewriter.Transformer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import java.io.IOException;

@ExtendWith(AemContextExtension.class)
public class GatedLinkTransformerFactoryTest {
    @Mock
    ProcessingContext processingContext;

    @Mock
    ContentHandler contentHandler;

    @BeforeEach
    public void setup(AemContext context) {
        MockitoAnnotations.openMocks(this);
        context.registerInjectActivateService(new FormatterServiceImpl());

        Mockito.when(processingContext.getRequest()).thenReturn(context.request());
    }

    @Test
    public void testTransformer(AemContext context) throws IOException, SAXException {
        GatedLinkTransformerFactory factory = context.registerInjectActivateService(new GatedLinkTransformerFactory());
        Transformer transformer = factory.createTransformer();
        transformer.init(processingContext, null);
        transformer.setContentHandler(contentHandler);

        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute(null, "href", "href", "CDATA", "/content/aem-demo/us/en/mobile");

        transformer.startElement(null, "a", null, attributes);

        ArgumentCaptor<Attributes> argumentCaptor = ArgumentCaptor.forClass(AttributesImpl.class);
        Mockito.verify(contentHandler, Mockito.atLeastOnce()).startElement(Mockito.isNull(), Mockito.eq("a"), Mockito.isNull(), argumentCaptor.capture());

        Attributes out = argumentCaptor.getValue();
        Assertions.assertEquals("/content/aem-demo/us/en/mobile.html", out.getValue(0));
    }
}
