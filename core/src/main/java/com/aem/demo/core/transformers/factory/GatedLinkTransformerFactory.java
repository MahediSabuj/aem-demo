package com.aem.demo.core.transformers.factory;

import com.aem.demo.core.components.services.FormatterService;
import com.aem.demo.core.transformers.GatedLinkTransformer;
import org.apache.sling.rewriter.Transformer;
import org.apache.sling.rewriter.TransformerFactory;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
  immediate = true,
  service = { TransformerFactory.class },
  property = {
    "pipeline.type=gatedlinkrewriter"
  }
)
public class GatedLinkTransformerFactory implements TransformerFactory {
    @Reference
    private SlingSettingsService settingsService;

    @Reference
    private FormatterService formatterService;

    @Override
    public Transformer createTransformer() {
        return new GatedLinkTransformer(settingsService, formatterService);
    }
}
