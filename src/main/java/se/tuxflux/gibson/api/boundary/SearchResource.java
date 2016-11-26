package se.tuxflux.gibson.api.boundary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.tuxflux.gibson.api.controller.SearchController;
import se.tuxflux.gibson.api.model.GibsonException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Path("/")
public class SearchResource {

  @Autowired
  SearchController searchController;

  @GET
  @Path("/search/{phrase}/{page}")
  @Produces("application/json")
  public Response get(@PathParam("phrase") String phrase, @PathParam("page") String page) {
    try {
      if (null == page) page = "0";
      return Response.ok(searchController.getParagraphEntities(phrase, Integer.valueOf(page)))
          .header("Access-Control-Allow-Origin", "*")
          .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
          .allow("OPTIONS").build();
    } catch (GibsonException e) {
      e.printStackTrace();
      return Response.serverError().build();
    }
  }
}
