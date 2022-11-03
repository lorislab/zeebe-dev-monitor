package org.lorislab.zeebe.dev.monitor;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.protocol.record.value.BpmnElementType;
import io.quarkus.qute.RawString;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.annotations.providers.multipart.PartFilename;
import org.jboss.resteasy.annotations.providers.multipart.PartType;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.lorislab.zeebe.dev.monitor.mapper.ProcessMapper;
import org.lorislab.zeebe.dev.monitor.mapper.InstanceTableMapper;
import org.lorislab.zeebe.dev.monitor.mapper.MessageSubscriptionMapper;
import org.lorislab.zeebe.dev.monitor.mapper.TimerMapper;
import org.lorislab.zeebe.dev.monitor.models.BpmnXmlResource;
import org.lorislab.zeebe.dev.monitor.models.Definition;
import org.lorislab.zeebe.dev.monitor.models.ElementInstance;
import org.lorislab.zeebe.dev.monitor.models.Instance;
import org.lorislab.zeebe.dev.monitor.models.MessageSubscription;
import org.lorislab.zeebe.dev.monitor.models.Timer;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.RedirectionException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("process")
public class ProcessViewController {

    @Inject
    Template definitions;

    @Inject
    Template definition;

    @Inject
    ProcessMapper mapper;

    @Inject
    TimerMapper timerMapper;

    @Inject
    InstanceTableMapper instanceMapper;

    @Inject
    MessageSubscriptionMapper messageSubscriptionMapper;

    @Inject
    ZeebeClient zeebe;

    @Context
    UriInfo info;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getDefinitions() {
        return definitions.data(
                "definitions", mapper.definitions(Definition.findAll().list(), Instance.countEndedInstances(), Instance.countActiveInstances()),
                "count", Definition.count()
        );
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getDefinition(@PathParam("id") long id) {
        Definition def = Definition.findById(id);
        BpmnXmlResource xml = BpmnXmlResource.findById(id);

        List<ElementInstance.ElementInstanceStatistics> elementEntered = ElementInstance.findElementInstanceByKeyAndIntentIn(def.key, ELEMENT_ENTERED, EXCLUDE_ELEMENT_TYPES);

        Map<String, Long> elementCompleted = ElementInstance.findElementInstanceByKeyAndIntentIn(def.key, ELEMENT_COMPLETED, EXCLUDE_ELEMENT_TYPES)
                .stream().collect(Collectors.toMap(ElementInstance.ElementInstanceStatistics::elementId, ElementInstance.ElementInstanceStatistics::count));

        List<ElementInstanceStateData> elementsInstances = elementEntered.stream().map(x -> {
            var ci = elementCompleted.getOrDefault(x.elementId(), 0L);
            return new ElementInstanceStateData(x.elementId(), x.count() - ci, ci);
        }).toList();


        return definition.data("definition", mapper.definition(def,
                        Instance.countActiveInstanceOfProcessDefinitionKey(def.key),
                        Instance.countEndedInstanceOfProcessDefinitionKey(def.key)))
                .data("instances", instanceMapper.items(Instance.findByProcessDefinitionKey(def.key)))
                .data("instances_count", Instance.countOfProcessDefinitionKey(def.key))
                .data("resource", new RawString(new String(xml.resource)))
                .data("timers", timerMapper.items(Timer.findByProcessDefinitionKeyAndProcessInstanceKeyIsNull(def.key)))
                .data("messageSubscriptions", messageSubscriptionMapper.items(MessageSubscription.findByProcessDefinitionKeyAndProcessInstanceKeyIsNull(def.key)))
                .data("instance", new DefinitionInstanceData(BpmnModel.loadBpmnElementInfos(xml), elementsInstances));
    }

    static final List<String> EXCLUDE_ELEMENT_TYPES = List.of(BpmnElementType.MULTI_INSTANCE_BODY.name());

    static final List<ElementInstance.Intent> ELEMENT_ENTERED = List.of(ElementInstance.Intent.ELEMENT_ACTIVATED);


    static final List<ElementInstance.Intent> ELEMENT_COMPLETED = List.of(ElementInstance.Intent.ELEMENT_COMPLETED, ElementInstance.Intent.ELEMENT_TERMINATED);

    @RegisterForReflection
    public record DefinitionInstanceData(List<BpmnElementInfoData> bpmnElementInfos, List<ElementInstanceStateData> elementInstances) {}
    @RegisterForReflection
    public record DefinitionData(long key, String bpmnProcessId, int version, OffsetDateTime timestamp,
                                 long countRunning, long countEnded) {}

}
