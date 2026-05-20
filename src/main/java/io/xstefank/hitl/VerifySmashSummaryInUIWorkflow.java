package io.xstefank.hitl;

import dev.langchain4j.agentic.declarative.HumanInTheLoop;
import io.quarkus.arc.Arc;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface VerifySmashSummaryInUIWorkflow {

    @HumanInTheLoop(outputKey = "smashSummary", description = "Human verification of the smash summary before adding to history.")
    static String verifySmashSummary(String smashSummary) {
        CompletableFuture<SmashingVerification> future = Arc.container()
                .select(SmashVerificationService.class).get()
                .createVerification(smashSummary);
        try {
            SmashingVerification verification = future.get(30, TimeUnit.MINUTES);
            if (verification.status() == SmashingVerification.VerificationStatus.REJECTED) {
                throw new RuntimeException("Smash summary rejected by human reviewer");
            }
            return smashSummary;
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to verify smash summary", e);
        }
    }
}
