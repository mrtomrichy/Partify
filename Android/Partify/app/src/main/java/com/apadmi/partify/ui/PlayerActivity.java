package com.apadmi.partify.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.apadmi.partify.R;
import com.apadmi.partify.spotify.SpotifyManager;
import com.spotify.sdk.android.playback.Player;
import com.spotify.sdk.android.playback.PlayerNotificationCallback;
import com.spotify.sdk.android.playback.PlayerState;
import com.spotify.sdk.android.playback.PlayerStateCallback;

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
  private Player mPlayer;
  private SeekBar mSeekBar;

  private ScheduledExecutorService scheduler;

  private Runnable seekUpdater = new Runnable() {
    public void run() {
      mPlayer.getPlayerState(PlayerActivity.this);
    }
  };

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_player);

    ActionBar ab = getActionBar();
    ab.setSubtitle("Party host");

    Bundle b = getIntent().getExtras();

    if(b != null) {
      partyCode = b.containsKey("partyCode") ? b.getString("partyCode") : "";
    }

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
          mPlayer.play("spotify:track:2ahnofp2LbBWDXcJbMaSTu");
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

    SpotifyManager.getSpotifyManager().setListener(this);
  }

  private void play(PlayerState playerState) {
    state = PlayState.PLAYING;
    mSeekBar.setMax(playerState.durationInMs);
    mSeekBar.setProgress(playerState.positionInMs);
    startUpdatingSeekBar();
  }

  private void pause() {
    state = PlayState.PAUSED;
    stopUpdatingSeekBar();
  }

  private void startUpdatingSeekBar() {
    if (scheduler != null)
      scheduler.shutdown();
    scheduler = Executors.newSingleThreadScheduledExecutor();
    scheduler.scheduleAtFixedRate(seekUpdater, 0, 1, TimeUnit.SECONDS);
  }

  private void stopUpdatingSeekBar() {
    if(scheduler != null)
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
    }

    setPlayImage();
  }

  @Override
  public void onPlayerState(PlayerState state) {
    updateSeekBar(state.positionInMs, state.durationInMs);
  }

  public void onDestroy() {
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
