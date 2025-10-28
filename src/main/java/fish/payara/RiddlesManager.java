package fish.payara;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class RiddlesManager {

    public static final ConcurrentHashMap<String, RiddleCacheWrapper> RIDDLE_MAP = new ConcurrentHashMap<>();

    private static final Logger LOG = Logger.getLogger(RiddlesManager.class.getName());

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    @Inject
    @ConfigProperty(name = "OPEN_API_KEY")
    private String openAiKey;
    @Inject
    @ConfigProperty(name = "api.url", defaultValue = "https://api.openai.com/v1/chat/completions")
    private String apiUrl;

    @PostConstruct
    void init() {
        scheduler.scheduleAtFixedRate(this::cleanUpCache, 5, 5, TimeUnit.MINUTES);
    }

    public RiddleResponse fetchRiddle() {
        var riddles = Set.copyOf(fetchRiddles());
        return new RiddleResponse(riddles);

    }

    private List<Riddle> fetchRiddles() {
        String requestBody = RiddlesUtil.generateRequestBody();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + openAiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = null;

        try {
            response = RiddlesUtil.HTTP_CLIENT.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            LOG.log(Level.SEVERE, "Error sending request to OpenAI", e);
        }
        if (response != null && response.statusCode() == Response.Status.OK.getStatusCode()) {
            LOG.log(Level.INFO, "Response status code: {0}", response.statusCode());
            LOG.log(Level.INFO, "Response body: {0}", response.body());
            List<Riddle> riddles = RiddlesUtil.processResponse(response.body());
            LOG.log(Level.INFO, "Processed Riddles: {0}", riddles.toString());
            return riddles;
        } else {
            if (response == null) {
                LOG.log(Level.WARNING, "Request failed, no response was received.");
            } else {
                LOG.log(Level.WARNING, "Received non-OK response. Status: {0}, Body: {1}",
                        new Object[]{response.statusCode(), response.body()});
            }
        }

        return List.of();
    }

    private void cleanUpCache() {
        LOG.log(Level.INFO, "Cleaning up Riddles: {0}", RIDDLE_MAP.size());
        Instant cutoffTime = Instant.now().minus(Duration.ofMinutes(15));

        RIDDLE_MAP.entrySet().removeIf(entry -> {
            RiddleCacheWrapper wrapper = entry.getValue();
            return wrapper.lastUpdated().isBefore(cutoffTime);
        });
    }


}
