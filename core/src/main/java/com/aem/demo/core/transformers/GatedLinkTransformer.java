package com.aem.demo.core.transformers;

import com.aem.demo.core.components.services.FormatterService;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.rewriter.DefaultTransformer;
import org.apache.sling.rewriter.ProcessingComponentConfiguration;
import org.apache.sling.rewriter.ProcessingContext;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.Optional;

public class GatedLinkTransformer extends DefaultTransformer {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private SlingHttpServletRequest request;
    private SlingSettingsService settingsService;
    private FormatterService formatterService;

    public GatedLinkTransformer(SlingSettingsService settingsService, FormatterService formatterService) {
        this.settingsService = settingsService;
        this.formatterService = formatterService;
    }

    @Override
    public void init(ProcessingContext context, ProcessingComponentConfiguration config) {
        this.request = context.getRequest();
    }

    private boolean isGated(ResourceResolver resolver, String href) {
        Session session = resolver.adaptTo(Session.class);

        try {
            return session != null && !session.hasPermission(href, Session.ACTION_READ);
        } catch (RepositoryException e) {
            LOG.error("Error in Session.READ permission for {}", href);
        }

        return false;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        ResourceResolver resolver = request.getResourceResolver();
        AttributesImpl attributesImpl = new AttributesImpl(attributes);

        String href = Optional.ofNullable(attributes.getValue("href")).orElse(StringUtils.EMPTY);
        if (StringUtils.isNotBlank(href)) {
            if (isGated(resolver, href)) {
                attributesImpl.addAttribute(
                    uri, "data-link", "data-link", "CDATA", "gated");
            }

            href = formatterService.getFormattedLink(href, resolver);
            if (settingsService.getRunModes().contains("author")) {
                href += "?wcmmode=disabled";
            }
            attributesImpl.setAttribute(
                attributes.getIndex("href"), uri, "href", "href", "CDATA", href);
        }

        super.startElement(uri, localName, qName, attributesImpl);
    }
}
