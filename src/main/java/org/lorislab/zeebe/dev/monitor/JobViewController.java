package org.lorislab.zeebe.dev.monitor;

import io.quarkus.panache.common.Parameters;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.lorislab.zeebe.dev.monitor.mapper.JobMapper;
import org.lorislab.zeebe.dev.monitor.models.Job;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("job")
public class JobViewController {

    @Inject
    Template jobs;

    @Inject
    JobMapper jobMapper;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance getJobs() {
        return jobs.data(
                "jobs", jobMapper.items(Job.find("state not in :state", Parameters.with("state", List.of(Job.State.COMPLETED, Job.State.CANCELED)).map()).list()),
                "count", Job.count("state not in :state", Parameters.with("state",List.of(Job.State.COMPLETED, Job.State.CANCELED)).map())
        );
    }
}
