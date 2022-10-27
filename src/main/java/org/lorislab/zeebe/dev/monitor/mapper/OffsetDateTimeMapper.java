package org.lorislab.zeebe.dev.monitor.mapper;

import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper
public abstract class OffsetDateTimeMapper {

    public OffsetDateTime map(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return OffsetDateTime.of(dateTime, ZoneOffset.systemDefault().getRules().getOffset(dateTime));
    }

    public LocalDateTime map(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) {
            return null;
        }
        return LocalDateTime.ofInstant(offsetDateTime.toInstant(), ZoneOffset.systemDefault());
    }

}
