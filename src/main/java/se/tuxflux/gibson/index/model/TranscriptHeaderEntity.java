package se.tuxflux.gibson.index.model;

import java.util.Set;

public class TranscriptHeaderEntity {
  private Integer episode;
  private String date;
  private String title;
  private Set<String> hosts;
  private String sourceAudio;
  private String sourceText;

  public TranscriptHeaderEntity() {
  }

  public TranscriptHeaderEntity(Integer episode, String date, String title, Set<String> hosts, String sourceAudio, String sourceText) {
    this.episode = episode;
    this.date = date;
    this.title = title;
    this.hosts = hosts;
    this.sourceAudio = sourceAudio;
    this.sourceText = sourceText;
  }

  public Integer getEpisode() {
    return episode;
  }

  public void setEpisode(Integer episode) {
    this.episode = episode;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Set<String> getHosts() {
    return hosts;
  }

  public void setHosts(Set<String> hosts) {
    this.hosts = hosts;
  }

  public String getSourceAudio() {
    return sourceAudio;
  }

  public void setSourceAudio(String sourceAudio) {
    this.sourceAudio = sourceAudio;
  }

  public String getSourceText() {
    return sourceText;
  }

  public void setSourceText(String sourceText) {
    this.sourceText = sourceText;
  }

  @Override
  public String toString() {
    return "TranscriptHeaderEntity{" +
        "episode=" + episode +
        ", date='" + date + '\'' +
        ", title='" + title + '\'' +
        ", hosts=" + hosts +
        ", sourceAudio='" + sourceAudio + '\'' +
        ", sourceText='" + sourceText + '\'' +
        '}';
  }
}
