package pl.robertprogramista.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * App Availability Monitor
 */
@Component
public class AvailabilityMonitor {

    private static final Logger logger = LoggerFactory.getLogger(AvailabilityMonitor.class);
    public static final String URI_TO_CHECK = "http://localhost:1410/books";

    /**
     * Checks the availability of the application
     * @throws IOException IO exceptions
     * @throws InterruptedException interrupted exception
     */
    @Scheduled(cron = "${check.availability.cron}")
    public void checkAvailability() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URI_TO_CHECK)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        logger.info("System available: {}", response);
    }
}
