package fish.payara;

import java.util.List;

public record Riddle(String id, List<String> hints, String question, String answer) {
}
