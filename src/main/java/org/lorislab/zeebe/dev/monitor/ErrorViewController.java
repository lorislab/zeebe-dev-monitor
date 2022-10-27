package org.lorislab.zeebe.dev.monitor;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.lorislab.zeebe.dev.monitor.mapper.ErrorMapper;
import org.lorislab.zeebe.dev.monitor.models.Error;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("error")
public class ErrorViewController {

    @Inject
    Template errors;

    @Inject
    ErrorMapper errorMapper;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getIncidents() {
        return errors.data(
                "errors", errorMapper.items(Error.findAll().list()),
                "count", Error.count()
        );
    }


}
