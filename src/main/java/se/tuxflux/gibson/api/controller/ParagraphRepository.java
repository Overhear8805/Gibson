package se.tuxflux.gibson.api.controller;


import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.indices.CreateIndex;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import se.tuxflux.gibson.api.model.GibsonException;
import se.tuxflux.gibson.common.model.ParagraphEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ParagraphRepository {
  public final static String PARAGRAPH_INDEX = "paragraph";
  public final static String GIBSON_TYPE = "gibson";
  public static final int RESULT_SIZE = 30;

  @Autowired
  JestClient jestClient;
  private String lastestEpisode;

  private void setupIndex() {
    try {
      Settings.Builder settings = Settings.settingsBuilder();
      jestClient.execute(new CreateIndex.Builder(PARAGRAPH_INDEX).settings(settings.build().getAsMap()).build());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void insertParagraph(ParagraphEntity paragraphEntity) throws GibsonException {
    setupIndex();
    Index index = new Index.Builder(paragraphEntity).index(PARAGRAPH_INDEX).type(GIBSON_TYPE).build();
    try {
      jestClient.execute(index);
    } catch (IOException e) {
      e.printStackTrace();
      throw new GibsonException(e);
    }
  }

  public List<ParagraphEntity> findByPhrase(String phrase, Integer offset) throws GibsonException {
    List<ParagraphEntity> queryResult;
    try {
      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      searchSourceBuilder.query(QueryBuilders.fuzzyQuery(PARAGRAPH_INDEX, phrase)).size(RESULT_SIZE).from(offset * RESULT_SIZE);

      Search search = new Search.Builder(searchSourceBuilder.toString())
          .addIndex(PARAGRAPH_INDEX).addType(GIBSON_TYPE).build();
      System.out.println(searchSourceBuilder.toString());
      JestResult result = jestClient.execute(search);
      queryResult = result.getSourceAsObjectList(ParagraphEntity.class);
    } catch (IOException e) {
      e.printStackTrace();
      throw new GibsonException(e);
    }
    return queryResult != null ? queryResult : new ArrayList<>();
  }

  /**
   * @return 0 if no result is found, else the highest numerical value stored in the episode index.
   * @throws GibsonException If an error occurs :-)
   */
  public Integer getLatestEpisode() throws GibsonException {
    try {
      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
      searchSourceBuilder.query(QueryBuilders.matchAllQuery()).sort("episode", SortOrder.DESC).size(1);
      Search search = new Search.Builder(searchSourceBuilder.toString()).addIndex(PARAGRAPH_INDEX).build();
      System.out.println(searchSourceBuilder.toString());
      JestResult result = jestClient.execute(search);
      if (result.isSucceeded()) return result.getSourceAsObject(ParagraphEntity.class).getEpisode();
      else return 0;
    } catch (IOException e) {
      e.printStackTrace();
      throw new GibsonException(e);
    }
  }
}