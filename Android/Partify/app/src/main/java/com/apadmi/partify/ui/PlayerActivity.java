package com.apadmi.partify.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.apadmi.partify.R;
import com.apadmi.partify.adapters.NowPlayingAdapter;
import com.apadmi.partify.spotify.SpotifyManager;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;
import com.spotify.sdk.android.playback.PlayerStateCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by tom on 25/10/14.
 */
public class PlayerActivity extends Activity implements PlayerNotificationCallback, PlayerStateCallback {

  private enum PlayState {
    STOPPED, PLAYING, PAUSED
  }

  private PlayState state;

  private String partyCode;

  private ImageView playButton;
  private ImageView nextButton;
  private Player mPlayer;
  private SeekBar mSeekBar;

  private ScheduledExecutorService scheduler;
  private ScheduledExecutorService playlistScheduler;

  private Runnable seekUpdater = new Runnable() {
    public void run() {
      mPlayer.getPlayerState(PlayerActivity.this);
    }
  };

  private Runnable playlistUpdater = new Runnable() {
    public void run() {
      getTracks();
    }
  };

  private ArrayList<String> tracks;

  private String currentTrack = "";

  private ListView playlistList;
  private NowPlayingAdapter mAdapter;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);

    ActionBar ab = getActionBar();
    ab.setSubtitle("Party host");

    Bundle b = getIntent().getExtras();

    if (b != null) {
      partyCode = b.containsKey("partyCode") ? b.getString("partyCode") : "";
    }

    tracks = new ArrayList<String>();

    startUpdatingPlaylist();

    TextView partyCodeText = (TextView) findViewById(R.id.text_party_code);
    partyCodeText.setText("Party Code: " + partyCode);

    mPlayer = SpotifyManager.getSpotifyManager().getPlayer();

    state = PlayState.STOPPED;

    playButton = (ImageView) findViewById(R.id.button_play);
    setPlayImage();
    playButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (state == PlayState.STOPPED) {
          mPlayer.play(tracks);
        } else if (state == PlayState.PLAYING) {
          mPlayer.pause();
          state = PlayState.PAUSED;
        } else if (state == PlayState.PAUSED) {
          mPlayer.resume();
          state = PlayState.PLAYING;
        }

        setPlayImage();
      }
    });

    nextButton = (ImageView) findViewById(R.id.button_next);
    nextButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        mPlayer.skipToNext();
      }
    });

    mSeekBar = (SeekBar) findViewById(R.id.seek_song);
    mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
        stopUpdatingSeekBar();
      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
        mPlayer.seekToPosition(seekBar.getProgress());
        startUpdatingSeekBar();
      }
    });
    mSeekBar.setMax(100);
    mSeekBar.setProgress(0);

    playlistList = (ListView) findViewById(R.id.list_currently_playing);
    if(mAdapter == null)
      mAdapter = new NowPlayingAdapter(this, 0, tracks);

    playlistList.setAdapter(mAdapter);

    SpotifyManager.getSpotifyManager().setListener(this);
  }

  private void getTracks() {
    RequestQueue queue = Volley.newRequestQueue(this);

    String url = "";
    try {
      url = String.format("http://partify.apphb.com/api/party/GetPlaylist?partyCodePlaylist=%s",
          URLEncoder.encode(partyCode, "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    JsonRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            try {
              tracks.clear();
              JSONArray ids = response.getJSONArray("SpotifyIds");
              for (int i = 0; i < ids.length(); i++) {
                Log.e("TRACKS", ids.getString(i));
                tracks.add("spotify:track:"+ids.getString(i));
                mAdapter.notifyDataSetChanged();
              }
            } catch (JSONException ex) {
            }
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {

          }
        });

    queue.add(jsObjRequest);
  }

  private void play(PlayerState playerState) {
    state = PlayState.PLAYING;
    currentTrack = playerState.trackUri;
    SpotifyManager.getSpotifyManager().setCurrentlyPlaying(currentTrack);
    mSeekBar.setMax(playerState.durationInMs);
    mSeekBar.setProgress(playerState.positionInMs);
    mAdapter.notifyDataSetChanged();
    startUpdatingSeekBar();
  }

  private void pause() {
    state = PlayState.PAUSED;
    stopUpdatingSeekBar();
  }

  private void startUpdatingPlaylist() {
    if (playlistScheduler != null)
      playlistScheduler.shutdown();
    playlistScheduler = Executors.newSingleThreadScheduledExecutor();
    playlistScheduler.scheduleAtFixedRate(playlistUpdater, 0, 5, TimeUnit.SECONDS);
  }

  private void stopUpdatingPlaylist() {
    if (playlistScheduler != null)
      playlistScheduler.shutdown();
    playlistScheduler = null;
  }

  private void startUpdatingSeekBar() {
    if (scheduler != null)
      scheduler.shutdown();
    scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(seekUpdater, 0, 1, TimeUnit.SECONDS);
  }

  private void stopUpdatingSeekBar() {
    if (scheduler != null)
      scheduler.shutdown();
    scheduler = null;
  }

  private void updateSeekBar(long progress, long maxTime) {
    mSeekBar.setMax((int) maxTime);
    mSeekBar.setProgress((int) progress);
  }

  private void setPlayImage() {
    int resource = 0;
    switch (state) {
      case PLAYING:
        resource = android.R.drawable.ic_media_pause;
        break;
      case PAUSED:
        resource = android.R.drawable.ic_media_play;
        break;
      case STOPPED:
        resource = android.R.drawable.ic_media_play;
        break;
    }
    playButton.setImageResource(resource);
  }

  @Override
  public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
    switch (eventType) {
      case PLAY:
        play(playerState);
        break;
      case PAUSE:
        pause();
        break;
      case SKIP_NEXT:
        if(state == PlayState.PLAYING)
          mPlayer.play(tracks, tracks.indexOf(currentTrack)+1);
        play(playerState);
        break;
      case TRACK_CHANGED:
        play(playerState);
        break;
    }

    setPlayImage();
  }

  @Override
  public void onPlayerState(PlayerState state) {
    updateSeekBar(state.positionInMs, state.durationInMs);
  }

  public void onDestroy() {
    stopUpdatingPlaylist();
    SpotifyManager.getSpotifyManager().destroyPlayer();
    super.onDestroy();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.player_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.player_menu_exit:
        exitToStartScreen();
    }
    return true;
  }

  private void exitToStartScreen() {
    startActivity(new Intent(this, StartActivity.class));
    finish();
  }

  @Override
  public void onBackPressed() {
    exitToStartScreen();
  }
}
