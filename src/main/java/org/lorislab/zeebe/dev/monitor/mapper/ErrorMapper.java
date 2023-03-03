package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.ErrorDTO;
import org.lorislab.zeebe.dev.monitor.models.Error;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface ErrorMapper {
    List<ErrorDTO> errors(List<Error> items);

    ErrorDTO error(Error item);

}
