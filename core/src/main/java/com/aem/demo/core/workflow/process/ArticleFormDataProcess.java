package com.aem.demo.core.workflow.process;

import com.adobe.cq.dam.cfm.ContentFragment;
import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.aem.demo.core.components.services.contentfragment.ContentFragmentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@Component(service = { WorkflowProcess.class },
  property = {
    "process.label=" + "Process Article Form Data"
})
public class ArticleFormDataProcess implements WorkflowProcess {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    private final String FORM_BODY = "Article";
    private final String CFM_PATH = "/conf/aem-demo/settings/dam/cfm/models/article";
    private final String ASSET_PATH = "/content/dam/aem-demo/content-fragments/article";

    @Reference
    private ContentFragmentService fragmentService;

    @Override
    public void execute(WorkItem workItem, WorkflowSession workflowSession, MetaDataMap metaDataMap) {
        String formBody = workItem.getWorkflow().getWorkflowData().getMetaDataMap().get(FORM_BODY).toString();

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        try {
            TypeReference<HashMap<String, Object>> typeRef = new TypeReference<>() {};
            HashMap<String, Object> properties = mapper.readValue(formBody, typeRef);
            ContentFragment fragment = fragmentService.create(CFM_PATH, ASSET_PATH, properties.getOrDefault("title", "").toString());

            if (fragment != null) {
                fragmentService.update(fragment, properties);
            }

        } catch (JsonProcessingException ex) {
            LOG.error("Failed to Convert into Article: {}", ex.getMessage());
        }
    }
}
