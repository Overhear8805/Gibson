package se.tuxflux.gibson.common.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import se.tuxflux.gibson.index.model.TranscriptHeaderEntity;

/**
 * Entity describing a paragraph
 */
@Document(indexName = "paragraph", type = "gibson", shards = 1, replicas = 0, refreshInterval = "-1")
public class ParagraphEntity {

  @Id
  private String id;
  private String paragraph;
  private Integer episode;
  private String speaker;
  private String airingDate;
  private String sourceAudioUrl;
  private String sourceTranscriptUrl;
  private String aproxPlaytime;
  private Integer sequence;

  public ParagraphEntity(){}

  public ParagraphEntity(String id, String paragraph, String speaker, String aproxPlaytime, Integer sequence, TranscriptHeaderEntity transcriptHeaderEntity) {
    this.id = id;
    this.paragraph = paragraph;
    this.episode = transcriptHeaderEntity.getEpisode();
    this.speaker = speaker;
    this.airingDate = transcriptHeaderEntity.getDate();
    this.sourceAudioUrl = transcriptHeaderEntity.getSourceAudio();
    this.sourceTranscriptUrl = transcriptHeaderEntity.getSourceText();
    this.aproxPlaytime = aproxPlaytime;
    this.sequence = sequence;
  }

  public ParagraphEntity(String id, String paragraph, Integer episode, String speaker, String airingDate, String sourceAudioUrl, String sourceTranscriptUrl, String aproxPlaytime, Integer sequence) {
    this.id = id;
    this.paragraph = paragraph;
    this.episode = episode;
    this.speaker = speaker;
    this.airingDate = airingDate;
    this.sourceAudioUrl = sourceAudioUrl;
    this.sourceTranscriptUrl = sourceTranscriptUrl;
    this.aproxPlaytime = aproxPlaytime;
    this.sequence = sequence;
  }

  public Integer getSequence() {
    return sequence;
  }

  public void setSequence(Integer sequence) {
    this.sequence = sequence;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getParagraph() {
    return paragraph;
  }

  public void setParagraph(String paragraph) {
    this.paragraph = paragraph;
  }

  public Integer getEpisode() {
    return episode;
  }

  public void setEpisode(Integer episode) {
    this.episode = episode;
  }

  public String getSpeaker() {
    return speaker;
  }

  public void setSpeaker(String speaker) {
    this.speaker = speaker;
  }

  public String getAiringDate() {
    return airingDate;
  }

  public void setAiringDate(String airingDate) {
    this.airingDate = airingDate;
  }

  public String getSourceAudioUrl() {
    return sourceAudioUrl;
  }

  public void setSourceAudioUrl(String sourceAudioUrl) {
    this.sourceAudioUrl = sourceAudioUrl;
  }

  public String getSourceTranscriptUrl() {
    return sourceTranscriptUrl;
  }

  public void setSourceTranscriptUrl(String sourceTranscriptUrl) {
    this.sourceTranscriptUrl = sourceTranscriptUrl;
  }

  public String getAproxPlaytime() {
    return aproxPlaytime;
  }

  public void setAproxPlaytime(String aproxPlaytime) {
    this.aproxPlaytime = aproxPlaytime;
  }

  @Override
  public String toString() {
    return "ParagraphEntity{" +
        "id='" + id + '\'' +
        ", paragraph='" + paragraph + '\'' +
        ", episode='" + episode + '\'' +
        ", speaker='" + speaker + '\'' +
        ", airingDate=" + airingDate +
        ", sourceAudioUrl='" + sourceAudioUrl + '\'' +
        ", sourceTranscriptUrl='" + sourceTranscriptUrl + '\'' +
        ", aproxPlaytime='" + aproxPlaytime + '\'' +
        '}';
  }
}
