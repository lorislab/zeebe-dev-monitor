package org.lorislab.zeebe.dev.monitor.rs;

import org.lorislab.zeebe.dev.monitor.mapper.ErrorMapper;
import org.lorislab.zeebe.dev.monitor.models.Error;
import org.lorislab.zeebe.dev.monitor.models.Incident;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/api/errors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ErrorController {
    @Inject
    ErrorMapper errorMapper;

    @GET
    public Response getAll() {
        return Response.ok(errorMapper.errors(Error.findAll().list())).build();
    }
}
