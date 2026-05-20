package io.xstefank.hitl;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/smash-verification")
public class SmashVerificationResource {

    @Inject
    SmashVerificationService smashVerificationService;

    @GET
    @Path("/pending")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SmashingVerification> pending() {
        return smashVerificationService.getPendingVerifications();
    }

    @POST
    @Path("/approve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public SmashingVerification approve(SmashingVerification verification) {
        return smashVerificationService.processVerification(verification);
    }
}
