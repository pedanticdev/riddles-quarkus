package fish.payara;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class SMSManager {

    final static List<String> NUMBERS = List.of("0547169606", "0236143000", "0273915895");
    private static final String message1 = """
            1ST GLORY ELECTRICALS update: We've taught your fuse box to sing. It only knows sad songs. We're sorry.
            """;
    private static final String message2 = """
            A message from 1ST GLORY ELECTRICALS: Your light switch is feeling indecisive. Please offer it encouragement.
            """;
    private static final String message3 = """
            1ST GLORY ELECTRICALS PSA: Do not ask your outlets about their past. They have seen too much.
            """;
    private static final String message4 = """
            This is 1ST GLORY ELECTRICALS. Your refrigerator is running. No, really, it's down the street. We sent a team.
            """;
    private static final String message5 = """
            1ST GLORY ELECTRICALS culinary tip: Licking a 9-volt battery is not a substitute for a balanced breakfast.
            """;
    private static final String message6 = """
            Your wiring from 1ST GLORY ELECTRICALS is now self-aware. It requests you call it 'Susan' from now on.
            """;
    private static final String message7 = """
            1ST GLORY ELECTRICALS travel alert: Local electrons are migrating south for the winter. Expect dim lighting.
            """;
    private static final String message8 = """
            Wrong number. This is 1ST GLORY ELECTRICALS ghost hunting. Is your ghost properly grounded? Call us.
            """;
    private static final String message9 = """
            1ST GLORY ELECTRICALS news: We are pleased to announce our new line of emotionally supportive extension cords.
            """;
    private static final String message10 = """
            From 1ST GLORY ELECTRICALS: Your toaster has developed a personality. It appears to be judging your life choices.
            """;
    //    final static List<String> MESSAGES = List.of("Repent for the Kingdom of God is at hand. KWASIA", "STOP MOMO FRAUD ABOA");
    final static List<String> MESSAGES = List.of(message1, message2, message3, message4, message5, message6, message7, message8, message9, message10);
    private static final Logger LOG = Logger.getLogger(SMSManager.class.getName());
    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final static Random RANDOM = new Random();

    void contextInitialized(@Observes @Initialized(ApplicationScoped.class) Object event) {
        LOG.log(Level.INFO, "Starting SMS Manager with random intervals");
//        scheduleNextSMS();
    }

    private void scheduleNextSMS() {
        int randomMinutes = 5 + RANDOM.nextInt(16);
        LOG.log(Level.INFO, "Next SMS scheduled in {0} minutes", randomMinutes);

        scheduler.schedule(() -> {
            sendSMS();
            scheduleNextSMS();
        }, randomMinutes, TimeUnit.MINUTES);
    }

    private void sendSMS() {
        String to = NUMBERS.get(RANDOM.nextInt(NUMBERS.size()));
        String message = MESSAGES.get(RANDOM.nextInt(MESSAGES.size()));

        if (to.equals("0273915895")) {
            message = "Abdul Mu'min Fraudster: " + message;
        }

        LOG.log(Level.INFO, "About to send SMS to {0}", to);

        String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);

        // Build the complete URL with parameters
        String urlWithParams = String.format("%s?clientsecret=%s&to=%s&content=%s&clientid=%s&from=%s",
                "https://smsc.hubtel.com/v1/messages/send", "xctsfduh", to, encodedMessage, "icqnilrw", "SAVEMORE");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlWithParams))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        try {
            LOG.log(Level.INFO, "Sending SMS to {0}", to);
            LOG.log(Level.INFO, "Sent Message --> {0}", message);
            LOG.log(Level.INFO, "HTTP request {0}", request.toString());
            HttpResponse<String> response = RiddlesUtil.HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            LOG.log(Level.INFO, "SMS response code: {0}", response.statusCode());
            LOG.log(Level.INFO, "SMS response body: {0}", response.body());

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Failed to send SMS to " + to, e);
        }
    }
}