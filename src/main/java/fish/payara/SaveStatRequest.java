package fish.payara;

public record SaveStatRequest(String userId, int correctAnswers, int currentStreak, String lastUpdated,
                              int currentRiddleIndex) {
}
