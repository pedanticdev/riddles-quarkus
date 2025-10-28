package fish.payara;

import jakarta.validation.constraints.NotEmpty;

public record UserAnswerSubmissionRequest(String userId, String riddleId, @NotEmpty String answer, Long timeSpent) {
}
