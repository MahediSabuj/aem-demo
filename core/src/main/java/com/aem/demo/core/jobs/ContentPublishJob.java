package com.aem.demo.core.jobs;

import com.aem.demo.core.components.services.ContentPublishService;
import com.aem.demo.core.components.services.ResourceResolverService;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = JobConsumer.class,
  immediate = true,
  property = {
    Constants.SERVICE_DESCRIPTION + "=Page Publish Job",
    JobConsumer.PROPERTY_TOPICS + "=PagePublishJob"
})
public class ContentPublishJob implements JobConsumer {
    @Reference
    ContentPublishService publishService;

    @Reference
    ResourceResolverService resourceResolverService;

    @Override
    public JobResult process(Job job) {
        String path = (String) job.getProperty("path");
        ResourceResolver resolver = resourceResolverService.getResourceResolver();

        if(publishService.publishContent(resolver, path)) {
            return JobResult.OK;
        }

        return JobResult.FAILED;
    }
}
