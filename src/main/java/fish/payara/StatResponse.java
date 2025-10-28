package fish.payara;

public record StatResponse(int correctAnswers, int currentStreak, int hintsUsed, int currentIndex) {
}
