package com.apadmi.partify.spotify;

/**
 * Created by tom on 26/10/14.
 */
public class Track {
  private String name;
  private String artist;
  private String uri;
  private String imageURL;

  public Track(String name, String artist, String uri, String imageURL) {
    this.name = name;
    this.artist = artist;
    this.uri = uri;
    this.imageURL = imageURL;
  }

  public String getName() {
    return this.name;
  }

  public String getArtist() {
    return this.artist;
  }

  public String getUri() {
    return this.uri;
  }

  public String getImageURL() {
    return this.imageURL;
  }
}
