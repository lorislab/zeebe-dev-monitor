package org.lorislab.zeebe.dev.monitor.rs;

import org.lorislab.zeebe.dev.monitor.mapper.ErrorMapper;
import org.lorislab.zeebe.dev.monitor.models.Error;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/errors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class ErrorController {
    @Inject
    ErrorMapper errorMapper;

    @GET
    public Response getAll() {
        return Response.ok(errorMapper.errors(Error.list("ORDER BY timestamp DESC"))).build();
    }
}
