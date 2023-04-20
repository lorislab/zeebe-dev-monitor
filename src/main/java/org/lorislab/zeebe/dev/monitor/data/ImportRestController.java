package org.lorislab.zeebe.dev.monitor.data;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.protocol.jackson.ZeebeProtocolModule;
import io.camunda.zeebe.protocol.record.Record;


import io.camunda.zeebe.protocol.record.RecordType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("records")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ImportRestController {

    private static final Logger log = LoggerFactory.getLogger(ImportRestController.class);

    private static final String SERVER_HEADER = "Server";

    private static final String SERVER_INFO = "zpt-debug/1.1";

    private static final Map<Integer, Long> positions = new HashMap<>();

    private static final ObjectMapper MAPPER =
            new ObjectMapper().registerModule(new ZeebeProtocolModule());

    @Inject
    ImportDataService importDataService;

    @ConfigProperty(name = "zeebe.dev.monitor.acknowledge", defaultValue = "true")
    boolean acknowledge;

    @POST
    public Response records(byte[] body) {

        if (body == null || body.length == 0) {
            return response(Response.Status.BAD_REQUEST, "must send a list of records as body");
        }

        final List<Record<?>> records;
        try {
            records = MAPPER.readValue(body, new TypeReference<List<Record<?>>>() {});
        } catch (final IOException e) {
            log.warn("Failed to deserialize exported records", e);
            return response(Response.Status.BAD_REQUEST, "failed to deserialize records, see receiver logs for more");
        }

        if (records == null || records.isEmpty()) {
            return response(Response.Status.NO_CONTENT, "no records given");
        }

        for (final Record<?> record : records) {
//            log.info("Add record {}}/{} ==> {}", record.getValueType(), record.getPosition(), record.getRecordType());
            if (record.getRecordType() == RecordType.EVENT) {
                switch (record.getValueType()) {
                    case PROCESS_INSTANCE -> importDataService.importProcessInstance(value(record));
                    case DEPLOYMENT -> importDataService.importWorkflowProcess(value(record));
                    case TIMER -> importDataService.importTimer(value(record));
                    case PROCESS_MESSAGE_SUBSCRIPTION -> importDataService.importMessageSubscription(value(record));
                    case MESSAGE_START_EVENT_SUBSCRIPTION -> importDataService.importMessageStartEventSubscription(value(record));
                    case ERROR -> importDataService.importError(value(record));
                    case INCIDENT -> importDataService.importIncident(value(record));
                    case JOB -> importDataService.importJob(value(record));
                    case MESSAGE -> importDataService.importMessage(value(record));
                    case VARIABLE -> importDataService.importVariable(value(record));
                }
            }
            if (acknowledge) {
                acknowledge(record.getPartitionId(), record.getPosition());
            }
        }

        final int partitionId = records.get(0).getPartitionId();
        return createSuccessfulResponse(partitionId);
    }

    private static <T> T value(Record<?> record) {
        @SuppressWarnings("unchecked")
        T result = (T) record;
        return result;
    }

    void acknowledge(final int partitionId, final long position) {
        positions.merge(partitionId, position, Math::max);
    }

    private Response createSuccessfulResponse(final int partitionId) {
        final Long position = positions.get(partitionId);
        if (position == null) {
            return response(Response.Status.NO_CONTENT, "no acknowledged position for partition " + partitionId);
        }

        try {
            final byte[] responseBody = MAPPER.writeValueAsBytes(Collections.singletonMap("position", position));
            return response(Response.Status.OK, responseBody);
        } catch (Exception ex) {
            log.warn("Failed to serialize response", ex);
            return response(Response.Status.BAD_REQUEST, "Failed to serialize response");
        }
    }


    private static Response response(Response.Status status, Object entity) {
        return Response.status(status).entity(entity)
                .header(SERVER_HEADER, SERVER_INFO).build();
    }

    private static Response response(Response.Status status, String reasonPhrase) {
        return Response.status(status.getStatusCode(), reasonPhrase)
                .header(SERVER_HEADER, SERVER_INFO).build();
    }
}
