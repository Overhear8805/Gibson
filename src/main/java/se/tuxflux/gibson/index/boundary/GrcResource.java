package se.tuxflux.gibson.index.boundary;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GrcResource {
  private final RestTemplate restTemplate = new RestTemplate();

  public ResponseEntity<String> getTranscript(String fullUrl){
    return restTemplate.getForEntity(fullUrl, String.class);
  }
}
