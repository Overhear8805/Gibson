package se.tuxflux.gibson.index.controller;

import org.junit.Assert;
import org.junit.Test;
import se.tuxflux.gibson.common.model.ParagraphEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class ScrapeControllerITTest {
  List<ParagraphEntity> paragraphEntities;

  public ScrapeControllerITTest() throws IOException {
    ScrapeController scrapeController = new ScrapeController();
    URLConnection conn = new URL("https://www.grc.com/sn/sn-513.txt").openConnection();
    String body;
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
      body = reader.lines().collect(Collectors.joining("\r\n|\r|\n"));
    }

    paragraphEntities = scrapeController.parseTranscript(body);
  }

  @Test
  public void checkCorrectSpeaker() throws Exception {
//    for (ParagraphEntity paragraphEntity : paragraphEntities) {
//      String paragraph = paragraphEntity.getParagraph();
//      String speaker = paragraphEntity.getSpeaker();
//      System.out.println(paragraph.startsWith(speaker));
//    }
  }

  @Test
  public void testTranscriptUrl() {
    Assert.assertTrue(paragraphEntities.stream().allMatch(paragraphEntity -> paragraphEntity.getSourceTranscriptUrl().equals("https://www.grc.com/sn/sn-513.txt")));
  }

  @Test
  public void testAudioUrl() {
    Assert.assertTrue(paragraphEntities.stream().allMatch(paragraphEntity -> paragraphEntity.getSourceAudioUrl().equals("https://media.GRC.com/sn/SN-513.mp3")));
  }
}