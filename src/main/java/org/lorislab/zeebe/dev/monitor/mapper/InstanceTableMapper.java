package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.InstanceTableData;
import org.lorislab.zeebe.dev.monitor.models.Instance;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(uses = OffsetDateTimeMapper.class)
public interface InstanceTableMapper {

    List<InstanceTableData> items(List<Instance> instances);

    InstanceTableData item(Instance instance);
}
