package se.tuxflux.gibson.index.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.tuxflux.gibson.common.model.ParagraphEntity;
import se.tuxflux.gibson.index.model.TranscriptHeaderEntity;

import java.util.*;

@Component
public class ScrapeController {
  private final Logger log = LoggerFactory.getLogger(ScrapeController.class);

  public static final String EPISODE = "EPISODE";
  public static final String DATE = "DATE";
  public static final String TITLE = "TITLE";
  public static final String HOSTS = "HOSTS";
  public static final String SOURCE = "SOURCE";
  public static final String SPEAKERS = "SPEAKERS";
  public static final String SOURCE_FILE = "SOURCE FILE";
  private static final int NAME_REASONABLE_MAX_LENGTH = 25;

  public List<ParagraphEntity> parseTranscript(String rawBody) {
    String[] rawBodyLines = rawBody.split("\r\n|\r|\n");
    TranscriptHeaderEntity transcriptHeaderEntity = parseHeader(rawBodyLines);
    log.info("Parsed header: {}", transcriptHeaderEntity);
    return parseTranscript(transcriptHeaderEntity, rawBodyLines);
  }

  public List<ParagraphEntity> parseTranscript(TranscriptHeaderEntity transcriptHeader, String[] rawBodyLines) {
    List<ParagraphEntity> transcripts = new ArrayList<>(rawBodyLines.length / 2);
    int startIndex = getFirstHostIndex(transcriptHeader, rawBodyLines);
    String speaker = null;
    for (int i = startIndex; i < rawBodyLines.length; i++) {
      String line = rawBodyLines[i];
      if (line.isEmpty() || line.contains("|")) continue;

      String potentialHostname = line.split(":")[0];
      boolean lineStartsWithHostName = checkIsHostName(potentialHostname);
      if (lineStartsWithHostName) speaker = potentialHostname;

      transcripts.add(new ParagraphEntity(UUID.randomUUID().toString(), line, speaker, "UNKNOWN_APROX_TIME", i, transcriptHeader));
    }

    log.info("Finished parsing episode {}, found {} paragraphs", transcriptHeader.getEpisode(), transcripts.size());
    return transcripts;
  }

  /*
   * TODO: IMPROVE!
   */
  private boolean checkIsHostName(String mightBeHostName) {
    if (mightBeHostName.length() > NAME_REASONABLE_MAX_LENGTH || mightBeHostName.equals("|") || mightBeHostName.equals("\\r")) {
      return false; // Since Gibson rarely starts a new sentence if he'll just say something short.
    }
    return true;
  }

  /**
   * Parses the header of the transcript. This method requires the `rawBody` to start with a header that is formatted as following:
   * ```
   * GIBSON RESEARCH CORPORATION <text>
   * <p>
   * SERIES: <text>
   * EPISODE: <text>
   * DATE: <text>
   * TITLE:	<text>
   * HOSTS:	<text> // SPEAKERS:	<text>
   * SOURCE: <text> // SOURCE FILE: <text>
   * ARCHIVE: <text>
   * ```
   *
   * @param lines An array holding lines of a transcript.
   * @return A populated {@link TranscriptHeaderEntity}
   */
  private TranscriptHeaderEntity parseHeader(String[] lines) {
    TranscriptHeaderEntity header = new TranscriptHeaderEntity();
    for (String line : lines) {
      if (line.startsWith(EPISODE)) {
        String episode = getLineValue(EPISODE, line).replace("#", "");
        header.setSourceText(String.format("https://www.grc.com/sn/sn-%03d.txt", Integer.parseInt(episode)));
        header.setEpisode(Integer.valueOf(getLineValue(EPISODE, episode)));
      } else if (line.startsWith(DATE)) {
        header.setDate(getLineValue(DATE, line));
      } else if (line.startsWith(TITLE)) {
        header.setTitle(getLineValue(TITLE, line));
      } else if (line.startsWith(SPEAKERS)) {
        header.setHosts(parseHosts(getLineValue(SPEAKERS, line)));
      } else if (line.startsWith(HOSTS)) {
        header.setHosts(parseHosts(getLineValue(HOSTS, line)));
      } else if (line.startsWith(SOURCE_FILE)) {
        header.setSourceAudio(getLineValue(SOURCE_FILE, line));
        return header; // Last interesting property.
      } else if (line.startsWith(SOURCE)) {
        header.setSourceAudio(getLineValue(SOURCE, line));
        return header; // Last interesting property.
      }
    }

    return header;
  }


  private int getFirstHostIndex(TranscriptHeaderEntity transcriptHeader, String[] rawBodyLines) {
    for (int i = 0; i < rawBodyLines.length; i++) {
      for (String host : transcriptHeader.getHosts()) {
        if (rawBodyLines[i].toLowerCase().startsWith(host.toLowerCase())) return i;
      }
    }

    return -1;
  }

  private Set<String> parseHosts(String line) {
    String[] hosts = line.split("&");
    for (int i = 0; i < hosts.length; i++) {
      hosts[i] = hosts[i].trim();
    }
    return new HashSet<String>(Arrays.asList(hosts));
  }

  private String getLineValue(String prefix, String line) {
    return line.replace(prefix + ":", "").trim();
  }
}
