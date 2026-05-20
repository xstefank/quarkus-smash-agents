package io.xstefank.hitl;

public record SmashingVerification(int id, VerificationStatus status, String smashSummary) {

    public enum VerificationStatus {
        PENDING, APPROVED, REJECTED
    }
}
