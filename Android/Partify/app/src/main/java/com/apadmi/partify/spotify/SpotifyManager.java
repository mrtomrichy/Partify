package com.apadmi.partify.spotify;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.playback.ConnectionStateCallback;
import com.spotify.sdk.android.playback.Connectivity;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;

/**
 * Created by tom on 25/10/14.
 */
public class SpotifyManager implements PlayerNotificationCallback, ConnectionStateCallback {

  public class SessionDetails {
    String partyCode;
    String partyName;
    String userId;

    public SessionDetails(String partyCode, String partyName, String userId) {
      this.partyCode = partyCode;
      this.partyName = partyName;
      this.userId = userId;
    }

    public String getPartyCode() {
      return this.partyCode;
    }

    public String getPartyName() {
      return this.partyName;
    }

    public String getUserId() {
      return this.userId;
    }
  }

  private static SpotifyManager sSharedInstance = null;

  public static final String CLIENT_ID = "ae0aa20d4b694ca38dacb56c2c2298c4";
  public static final String REDIRECT_URI = "partify://";

  private Player mPlayer;

  private PlayerNotificationCallback playBackEventListener = null;

  private SessionDetails currentSession;

  private String currentlyPlaying;

  private SpotifyManager() {

  }

  public static SpotifyManager getSpotifyManager() {
    if(sSharedInstance == null)
      sSharedInstance = new SpotifyManager();

    return sSharedInstance;
  }

  public void newSession(SessionDetails session) {
    this.currentSession = session;
  }

  public SessionDetails getSession() {
    return this.currentSession;
  }

  public void endSession() {
    this.currentSession = null;
  }


  public static Connectivity getNetworkConnectivity(Context context) {
    ConnectivityManager connectivityManager;
    connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
    if (activeNetwork != null) {
      return Connectivity.fromNetworkType(activeNetwork.getType());
    } else {
      return Connectivity.OFFLINE;
    }
  }

  public void setCurrentlyPlaying(String playing) {
    this.currentlyPlaying = playing;
  }

  public String getCurrentlyPlaying() {
    return this.currentlyPlaying;
  }

  public void setSpotify(Spotify spotify, final Context appContext) {
    this.mPlayer = spotify.getPlayer(appContext, "Partify",
        sSharedInstance, new Player.InitializationObserver() {
          @Override
          public void onInitialized() {
            Log.d("Spotify Manager", "-- Player initialized --");
            mPlayer.setConnectivityStatus(SpotifyManager.getNetworkConnectivity(appContext));
            mPlayer.addPlayerNotificationCallback(SpotifyManager.getSpotifyManager());
            mPlayer.addConnectionStateCallback(SpotifyManager.getSpotifyManager());
          }

          @Override
          public void onError(Throwable error) {
            Log.d("Spotify Manager", "Error in initialization: " + error.getMessage());
          }
        });
  }

  public void setListener(PlayerNotificationCallback listener)
  {
    playBackEventListener = listener;
  }

  public void removeListener()
  {
    playBackEventListener = null;
  }

  public Player getPlayer() {
    return this.mPlayer;
  }

  public void destroyPlayer() {
    Spotify.destroyPlayer(sSharedInstance);
  }


  @Override
  public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
    if(playBackEventListener != null) {
      playBackEventListener.onPlaybackEvent(eventType, playerState);
    }
  }

  @Override
  public void onNewCredentials(String s) {
    Log.d("SpotifyManager", s);
  }

  @Override
  public void onLoginFailed(Throwable throwable) {
    Log.d("SpotifyManager", "Login fail: " + throwable.getMessage());
  }

  @Override
  public void onLoggedOut() {
    Log.d("SpotifyManager", "Logged out");
  }

  @Override
  public void onLoggedIn() {
    Log.d("SpotifyManager", "Logged in");
  }

  @Override
  public void onConnectionMessage(String s) {
    Log.d("SpotifyManager", s);
  }

  @Override
  public void onTemporaryError() {
    Log.d("SpotifyManager", "Some shitty error");
  }
}
