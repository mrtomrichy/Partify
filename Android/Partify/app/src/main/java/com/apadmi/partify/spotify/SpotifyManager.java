package com.apadmi.partify.spotify;

import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tom on 25/10/14.
 */
public class SpotifyManager implements PlayerNotificationCallback {
  //   ____                _              _
  //  / ___|___  _ __  ___| |_ __ _ _ __ | |_ ___
  // | |   / _ \| '_ \/ __| __/ _` | '_ \| __/ __|
  // | |__| (_) | | | \__ \ || (_| | | | | |_\__ \
  //  \____\___/|_| |_|___/\__\__,_|_| |_|\__|___/
  //

  private static final String CLIENT_ID = "cedaa376d2684e77bbeff705278613f2";
  private static final String REDIRECT_URI = "testschema://callback";

  private static final String TEST_SONG_URI = "spotify:track:6KywfgRqvgvfJc3JRwaZdZ";
  private static final String TEST_SONG_MONO_URI = "spotify:track:1FqY3uJypma5wkYw66QOUi";
  private static final String TEST_SONG_48kHz_URI = "spotify:track:3wxTNS3aqb9RbBLZgJdZgH";
  private static final String TEST_PLAYLIST_URI = "spotify:user:sqook:playlist:0BZvnsfuqmnLyj6WVRuSte";
  private static final String TEST_QUEUE_SONG_URI = "spotify:track:5EEOjaJyWvfMglmEwf9bG3";

  // Currently the way to play the album is to resolve the list of tracks using
  // WebAPI (as shown here https://developer.spotify.com/web-api/get-albums-tracks/)
  // and pass it to Player#playTrackList(java.util.List)
  // The list of tracks below is from the album: spotify:album:4JWoGR0Kwa0DlqbikKNqOc
  private static final List<String> TEST_ALBUM_TRACKS = Arrays.asList(
      "spotify:track:2To3PTOTGJUtRsK3nQemP4",
      "spotify:track:0tDoBMgyAzGgLhs73KPrJL",
      "spotify:track:5YkSQuB8i7J4TTyj0xw6ol",
      "spotify:track:3WpLfCkrlQxj8SISLzhs06",
      "spotify:track:2lGNTC3NKCG1d4lR8x3611",
      "spotify:track:0kdSj5REwpHjTBaBsm1wv8",
      "spotify:track:3BgnZiGnnRlXfeGR8ryKzT",
      "spotify:track:00cVWQIFyQnIdsgoVy7qAG",
      "spotify:track:6eEEoowHpnaD3q83ZhYmhZ",
      "spotify:track:1HFBn8S30ndZ7lLb9HbENU",
      "spotify:track:1I9VibKgJTqGfrh8fEK3sL",
      "spotify:track:6rXSPMgGIyOYiMhsj3eSAi",
      "spotify:track:2xwuXthwdNGbPyEqifPQNW",
      "spotify:track:5vRuWI48vKn4TV7efrYtJL",
      "spotify:track:4SEDYSBDd4Ota125LjHa2w",
      "spotify:track:2bVTnSTjLWAizyj4XcU5bf",
      "spotify:track:4gQzqlFuqv6l4Ka633Ue7T",
      "spotify:track:0SLVmM7IrrtkPNa1Fi3IKT"
  );

  private static SpotifyManager sSharedInstance;

  public static SpotifyManager getSpotifyManager() {
    if(sSharedInstance == null)
      sSharedInstance = new SpotifyManager();

    return sSharedInstance;
  }

  @Override
  public void onPlaybackEvent(PlayerNotificationCallback.EventType eventType, PlayerState playerState) {

  }

}
