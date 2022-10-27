package org.lorislab.zeebe.dev.monitor.graal;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;
import io.camunda.zeebe.model.bpmn.impl.BpmnParser;
import org.camunda.bpm.model.xml.ModelParseException;
import org.camunda.bpm.model.xml.impl.instance.DomDocumentImpl;
import org.camunda.bpm.model.xml.impl.util.DomUtil;
import org.camunda.bpm.model.xml.impl.util.ReflectUtil;
import org.camunda.bpm.model.xml.instance.DomDocument;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

import static io.camunda.zeebe.model.bpmn.impl.BpmnModelConstants.BPMN_20_SCHEMA_LOCATION;

@TargetClass(DomUtil.class)
public final class DomUtil_Target {


    @Substitute
    public static DomDocument parseInputStream(DocumentBuilderFactory documentBuilderFactory, InputStream inputStream) {

        // Replace wrong URL for the BPMN20.xsd
        String tmp = (String) documentBuilderFactory.getAttribute(TargetUtil.JAXP_SCHEMA_SOURCE);
        if (!tmp.startsWith("resource:/")) {
            documentBuilderFactory.setAttribute(TargetUtil.JAXP_SCHEMA_SOURCE,
                    ReflectUtil.getResource(BPMN_20_SCHEMA_LOCATION, BpmnParser.class.getClassLoader()).toString()
            );
        }

        try {
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            documentBuilder.setErrorHandler(new DomUtil.DomErrorHandler());
            return new DomDocumentImpl(documentBuilder.parse(inputStream));
        } catch (ParserConfigurationException e) {
            throw new ModelParseException("ParserConfigurationException while parsing input stream", e);

        } catch (SAXException e) {
            throw new ModelParseException("SAXException while parsing input stream", e);

        } catch (IOException e) {
            throw new ModelParseException("IOException while parsing input stream", e);

        }
    }
}
