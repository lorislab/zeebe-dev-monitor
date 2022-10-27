package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.ProcessViewController;
import org.lorislab.zeebe.dev.monitor.models.Definition;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper(uses = OffsetDateTimeMapper.class)
public abstract class ProcessMapper {


    public abstract ProcessViewController.DefinitionData definition(Definition data, Long countRunning, Long countEnded);

    public List<ProcessViewController.DefinitionData> definitions(List<Definition> data, Map<Long, Long> endNotNull, Map<Long, Long> endNull) {
        if (data == null) {
            return null;
        }
        ArrayList<ProcessViewController.DefinitionData> result = new ArrayList<>(data.size());
        if (data.isEmpty()) {
            return result;
        }

        data.forEach(x -> {
            result.add(
                    definition(x,
                            endNotNull.getOrDefault(x.key, 0L),
                            endNull.getOrDefault(x.key, 0L)
                    )
            );

        });
        return result;
    }
}
