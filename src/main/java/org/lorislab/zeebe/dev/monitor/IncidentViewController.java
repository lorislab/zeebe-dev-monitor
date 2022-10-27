package org.lorislab.zeebe.dev.monitor;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.lorislab.zeebe.dev.monitor.mapper.IncidentTableMapper;
import org.lorislab.zeebe.dev.monitor.models.Incident;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.OffsetDateTime;

@Path("incident")
public class IncidentViewController {

    @Inject
    Template incidents;

    @Inject
    IncidentTableMapper incidentMapper;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIncidents() {
        return incidents.data(
                "incidents", incidentMapper.items(Incident.find("resolved is null").list()),
                "count", Incident.count("resolved is null")
        );
    }

    @RegisterForReflection
    public record IncidentTableData(long key, String bpmnProcessId, long processInstanceKey, long processDefinitionKey,
                                    Incident.ErrorType errorType, String errorMessage, IncidentState state,
                                    OffsetDateTime created, OffsetDateTime resolved) {}
}
