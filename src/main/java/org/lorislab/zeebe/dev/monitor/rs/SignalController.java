package org.lorislab.zeebe.dev.monitor.rs;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.BroadcastSignalResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lorislab.zeebe.dev.monitor.dto.BroadcastSignalDTO;
import org.lorislab.zeebe.dev.monitor.mapper.SignalMapper;
import org.lorislab.zeebe.dev.monitor.models.Signal;

@Path("/api/signal")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SignalController {

    @Inject
    ZeebeClient client;

    @Inject
    SignalMapper mapper;

    @GET
    public Response getAll() {
        return Response.ok(mapper.signals(Signal.list("ORDER BY timestamp DESC"))).build();
    }

    @POST
    public Response broadcastSignal(BroadcastSignalDTO dto) {
        BroadcastSignalResponse event = client.newBroadcastSignalCommand()
                .signalName(dto.name())
                .variables(dto.variables())
                .send().join();
        return Response.ok(event.getKey()).build();
    }


}
