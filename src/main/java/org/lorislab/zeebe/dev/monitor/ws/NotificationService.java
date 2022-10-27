package org.lorislab.zeebe.dev.monitor.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint(value = "/ws/notification", encoders = NotificationService.NotificationEventEncoder.class)
@ApplicationScoped
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private static final Set<Session> SESSIONS = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        SESSIONS.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        SESSIONS.remove(session);
    }

    public void sendEvent(InstanceEvent data) {
        sendEvent(new NotificationEvent(NotificationEventType.PROCESS_INSTANCE, data));
    }

    public void sendEvent(ClusterEvent data) {
        sendEvent(new NotificationEvent(NotificationEventType.CLUSTER, data));
    }

    private void sendEvent(NotificationEvent data) {
        SESSIONS.forEach(s -> s.getAsyncRemote().sendObject(data, result -> {
            if (result.getException() != null) {
                log.info("Unable to send cluster event message: " + result.getException());
            }
        }));
    }

    @RegisterForReflection
    public record ClusterEvent(String message, ClusterEventType type) {}

    @RegisterForReflection
    public enum ClusterEventType {
        ERROR;
    }

    @RegisterForReflection
    public record NotificationEvent(NotificationEventType type, Object data) {}

    @RegisterForReflection
    public enum NotificationEventType {
        PROCESS_INSTANCE, CLUSTER;
    }

    @RegisterForReflection
    public record InstanceEvent(long processInstanceKey, long processDefinitionKey, ProcessInstanceEventType type) {}

    @RegisterForReflection
    public enum ProcessInstanceEventType {
        UPDATED, CREATED, REMOVED;
    }

    @RegisterForReflection
    public static class NotificationEventEncoder implements Encoder.Text<NotificationService.NotificationEvent> {

        private static final ObjectMapper MAPPER = new ObjectMapper();

        @Override
        public String encode(NotificationService.NotificationEvent object) throws EncodeException {
            try {
                return MAPPER.writeValueAsString(object);
            } catch (Exception ex) {
                throw new EncodeException(object, "Error encode event object", ex);
            }
        }

        @Override
        public void init(EndpointConfig config) {
            // ignore
        }

        @Override
        public void destroy() {
            // ignore
        }
    }

}
