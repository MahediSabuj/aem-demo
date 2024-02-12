package com.aem.demo.core.components.internal.services;

import com.aem.demo.core.components.services.ContentPublishService;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;

@Component(service = ContentPublishService.class,
  property = {
    Constants.SERVICE_DESCRIPTION + "=Content Publish Service"
})
public class ContentPublishServiceImpl implements ContentPublishService {
    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Reference
    Replicator replicator;

    private boolean activateResource(Session session, String path) {
        try {
            replicator.checkPermission(session, ReplicationActionType.ACTIVATE, path);
            replicator.replicate(session, ReplicationActionType.ACTIVATE, path);
        } catch (ReplicationException ex) {
            LOG.error("Could no activate pages to publish: {}", ex.getMessage());
            return false;
        }

        return true;
    }

    @Override
    public boolean publishContent(ResourceResolver resourceResolver, String path) {
        Session session = resourceResolver.adaptTo(Session.class);
        return activateResource(session, path);
    }
}
