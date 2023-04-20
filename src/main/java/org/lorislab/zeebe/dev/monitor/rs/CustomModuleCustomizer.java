package org.lorislab.zeebe.dev.monitor.rs;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.quarkus.jackson.ObjectMapperCustomizer;

import jakarta.inject.Singleton;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

@Singleton
public class CustomModuleCustomizer implements ObjectMapperCustomizer {

    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss.SS").toFormatter();

    public void customize(ObjectMapper mapper) {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(OffsetDateTime.class, new OffsetDateTimeSerializer());
        mapper.registerModule(module);
    }

    public static class OffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {

        @Override
        public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
            gen.writeString(value.format(FORMATTER));
        }
    }
}

