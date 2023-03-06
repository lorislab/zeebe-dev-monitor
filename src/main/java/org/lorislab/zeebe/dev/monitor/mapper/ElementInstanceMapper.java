package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.ElementInstanceDTO;
import org.lorislab.zeebe.dev.monitor.models.ElementInstance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface ElementInstanceMapper {

    List<ElementInstanceDTO> items(List<ElementInstance> data);

    ElementInstanceDTO item(ElementInstance data);
}
