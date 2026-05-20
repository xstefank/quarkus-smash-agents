package io.xstefank.hitl;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class SmashVerificationService {

    private final Map<Integer, CompletableFuture<SmashingVerification>> futures = new ConcurrentHashMap<>();
    private final Map<Integer, SmashingVerification> pending = new ConcurrentHashMap<>();

    public CompletableFuture<SmashingVerification> createVerification(String smashSummary) {
        Log.infof("Creating smash verification for: %s", smashSummary);
        int id = smashSummary.hashCode();
        CompletableFuture<SmashingVerification> future = new CompletableFuture<>();
        futures.put(id, future);
        pending.put(id, new SmashingVerification(id, SmashingVerification.VerificationStatus.PENDING, smashSummary));
        return future;
    }

    public SmashingVerification processVerification(SmashingVerification verification) {
        Log.infof("Processing verification: %s", verification);
        if (verification.status() == SmashingVerification.VerificationStatus.PENDING) {
            throw new IllegalStateException("Verification is still pending");
        }
        int id = verification.id();
        pending.remove(id);
        CompletableFuture<SmashingVerification> future = futures.remove(id);
        if (future == null) {
            throw new IllegalStateException("No pending verification found for id: " + id);
        }
        future.complete(verification);
        return verification;
    }

    public List<SmashingVerification> getPendingVerifications() {
        return List.copyOf(pending.values());
    }
}
