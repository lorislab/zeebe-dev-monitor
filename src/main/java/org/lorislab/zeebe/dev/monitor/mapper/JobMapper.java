package org.lorislab.zeebe.dev.monitor.mapper;

import org.lorislab.zeebe.dev.monitor.dto.JobDTO;
import org.lorislab.zeebe.dev.monitor.models.Job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Map;

@Mapper(uses = OffsetDateTimeMapper.class)
public abstract class JobMapper {


    public List<JobDTO> jobs(List<Job> jobs, Map<Long, String> elementIdsForKeys) {
        if (jobs == null) {
            return null;
        }
        return jobs.stream()
                .map(x -> job(x, elementIdsForKeys.getOrDefault(x.elementInstanceKey, "")))
                .toList();
    }

    public abstract List<JobDTO> jobs(List<Job> jobs);

    public abstract JobDTO job(Job job);

    @Mapping(target = "isActivatable", source ="job", qualifiedByName = "isActivatable")
    public abstract JobDTO job(Job job, String elementId);

    @Named("isActivatable")
    boolean isActivatable(Job job) {
        return job.retries > 0 &&
                (job.state == Job.State.CREATED || job.state == Job.State.FAILED ||
                        job.state == Job.State.TIMED_OUT || job.state == Job.State.RETRIES_UPDATED);
    }

}
