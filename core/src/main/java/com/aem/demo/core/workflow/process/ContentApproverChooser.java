package com.aem.demo.core.workflow.process;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.ParticipantStepChooser;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.aem.demo.core.components.services.SiteConfigService;
import com.aem.demo.core.configs.SiteConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = { ParticipantStepChooser.class },
  property = {
    "chooser.label=" + "AEM Demo Content Approver Chooser"
})
public class ContentApproverChooser implements ParticipantStepChooser {
    @Reference
    SiteConfigService configService;

    @Override
    public String getParticipant(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) throws WorkflowException {
        WorkflowData workflowData = workItem.getWorkflowData();
        String payloadType = workflowData.getPayloadType();

        if (StringUtils.equals(payloadType, "JCR_PATH")) {
            try (ResourceResolver resolver = workflowSession.adaptTo(ResourceResolver.class)) {
                String pagePath = workflowData.getPayload().toString();
                if (resolver != null) {
                    Resource resource = resolver.getResource(pagePath);

                    if (resource != null) {
                        SiteConfig config = configService.getSiteConfig(resource);
                        return config.approverGroup();
                    }
                }
            }
        }

        return "aem-demo-content-approver";
    }
}
