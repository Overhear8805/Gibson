package se.tuxflux.gibson.config;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletProperties;
import org.springframework.stereotype.Component;
import se.tuxflux.gibson.api.boundary.SearchResource;

import javax.ws.rs.ApplicationPath;

@Component
@ApplicationPath("/api")
public class JerseyConfig extends ResourceConfig {

  /**
   * In constructor we can define Jersey Resources & Other Components
   */
  public JerseyConfig() {
    register(SearchResource.class);
    property(ServletProperties.FILTER_FORWARD_ON_404, true);
  }
}