package se.tuxflux.gibson.index.boundary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import se.tuxflux.gibson.api.controller.SearchController;
import se.tuxflux.gibson.api.model.GibsonException;
import se.tuxflux.gibson.index.controller.ScrapeController;

@Component
public class IndexScheduler {
  private final Logger log = LoggerFactory.getLogger(IndexScheduler.class);

  @Autowired
  GrcResource grcResource;

  @Autowired
  ScrapeController scrapeController;

  @Autowired
  SearchController searchController;

  @Scheduled(cron = "30 27 21 * * *")
  public void checkForNewEpisode() {
    try {
      ResponseEntity responseEntity = null;
      do {
        Integer lastEpisode = searchController.getLatestEpisode();
        lastEpisode++;
        String url = String.format("https://www.grc.com/sn/sn-%03d.txt", lastEpisode);

        responseEntity = grcResource.getTranscript(url);
        log.info("Checking for new episode. {} gave status code {}", url, responseEntity.getStatusCode().value());
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
          String rawBody = responseEntity.getBody().toString();
          scrapeController.parseTranscript(rawBody).forEach(searchController::index);
          log.info("Indexing done");
        }
      } while (responseEntity.getStatusCode().is2xxSuccessful());
    } catch (GibsonException e) {
      e.printStackTrace();
    }
  }
}
