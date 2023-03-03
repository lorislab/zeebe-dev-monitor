package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.InstanceTableItemDTO;
import org.lorislab.zeebe.dev.monitor.models.Instance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface InstanceTableMapper {

    List<InstanceTableItemDTO> tableItems(List<Instance> instances);

    InstanceTableItemDTO tableItem(Instance instances);

}
