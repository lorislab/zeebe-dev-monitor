package org.lorislab.zeebe.dev.monitor;

import io.camunda.zeebe.model.bpmn.Bpmn;
import io.camunda.zeebe.model.bpmn.BpmnModelInstance;
import io.camunda.zeebe.model.bpmn.instance.CatchEvent;
import io.camunda.zeebe.model.bpmn.instance.ErrorEventDefinition;
import io.camunda.zeebe.model.bpmn.instance.SequenceFlow;
import io.camunda.zeebe.model.bpmn.instance.ServiceTask;
import io.camunda.zeebe.model.bpmn.instance.TimerEventDefinition;
import io.camunda.zeebe.model.bpmn.instance.zeebe.ZeebeTaskDefinition;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.lorislab.zeebe.dev.monitor.models.BpmnXmlResource;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BpmnModel {
    public static BpmnModelInstance loadModel(BpmnXmlResource xml) {
        return Bpmn.readModelFromStream(new ByteArrayInputStream(xml.resource));
    }

    public static List<BpmnElementInfoData> loadBpmnElementInfos(final BpmnXmlResource bpmnXmlResource) {
        return loadBpmnElementInfos(loadModel(bpmnXmlResource));
    }

    public static List<BpmnElementInfoData> loadBpmnElementInfos(final BpmnModelInstance bpmn) {
        final List<BpmnElementInfoData> infos = new ArrayList<>();

        bpmn.getModelElementsByType(ServiceTask.class)
                .forEach(x -> infos.add( new BpmnElementInfoData(x.getId(), "job-type: " + x.getSingleExtensionElement(ZeebeTaskDefinition.class).getType())));

        bpmn.getModelElementsByType(SequenceFlow.class)
                .forEach(x -> {
                    var conditionExpression = x.getConditionExpression();
                    if (conditionExpression != null && !conditionExpression.getTextContent().isEmpty()) {
                        infos.add(new BpmnElementInfoData(x.getId(), "condition: " + conditionExpression.getTextContent()));
                    }
                });

        bpmn.getModelElementsByType(CatchEvent.class)
                .forEach(catchEvent -> catchEvent.getEventDefinitions()
                        .forEach(
                                x -> {
                                    if (x instanceof final ErrorEventDefinition errorEventDef) {
                                        infos.add(new BpmnElementInfoData(catchEvent.getId(), "errorCode: " + errorEventDef.getError().getErrorCode()));
                                    }
                                    if (x instanceof final TimerEventDefinition timerEventDefinition) {
                                        Optional.<ModelElementInstance>ofNullable(timerEventDefinition.getTimeCycle())
                                                .or(() -> Optional.ofNullable(timerEventDefinition.getTimeDate()))
                                                .or(() -> Optional.ofNullable(timerEventDefinition.getTimeDuration()))
                                                .map(ModelElementInstance::getTextContent)
                                                .ifPresent(timer -> infos.add(new BpmnElementInfoData(catchEvent.getId(),"timer: " + timer)));
                                    }
                                }));

        return infos;
    }
}
