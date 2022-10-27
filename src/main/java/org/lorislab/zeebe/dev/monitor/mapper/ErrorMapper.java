package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.models.Error;
import org.lorislab.zeebe.dev.monitor.ErrorData;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface ErrorMapper {

    List<ErrorData> items(List<Error> items);

    ErrorData item(Error item);
}
