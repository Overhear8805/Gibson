package se.tuxflux.gibson.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.tuxflux.gibson.api.model.GibsonException;
import se.tuxflux.gibson.common.model.ParagraphEntity;

import java.util.List;

@Component
public class SearchController {

  @Autowired
  ParagraphRepository paragraphRepository;

  public List<ParagraphEntity> getParagraphEntities(String phrase, Integer page) throws GibsonException {
    return paragraphRepository.findByPhrase(phrase, page);
  }

  public Integer getLatestEpisode() throws GibsonException {
    return paragraphRepository.getLatestEpisode();
  }

  public void index(ParagraphEntity paragraphEntity) {
    try {
      paragraphRepository.insertParagraph(paragraphEntity);
    } catch (GibsonException e) {
      e.printStackTrace();
    }
  }
}
