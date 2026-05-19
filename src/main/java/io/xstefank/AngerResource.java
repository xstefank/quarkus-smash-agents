package io.xstefank;

import io.xstefank.agents.AngerEvalAgent;
import io.xstefank.agents.AngerEvalWorkflow;
import io.xstefank.model.SmashingResponse;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

@Path("/anger")
public class AngerResource {

    private static final Logger LOG = Logger.getLogger(AngerResource.class);

    @Inject
    AngerEvalWorkflow angerEvalWorkflow;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_HTML)
    public String submitAnger(@RestForm String angerText, @RestForm FileUpload angerFile) {
        LOG.infof("Received anger text: %s", angerText);
        SmashingResponse smashingResponse = angerEvalWorkflow.evaluateAngerAndHulkOut(angerText);
        LOG.infof("Smashing response: %s", smashingResponse);
        return smashingResponse.html();
    }

    @Inject
    AngerEvalAgent angerEvalAgent;

    @GET
    @Path("/agent")
    public short agent() {
        return angerEvalAgent.angerEvaluation("Super angry");
    }


}
