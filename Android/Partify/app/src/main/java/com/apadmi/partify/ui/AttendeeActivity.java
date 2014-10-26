package com.apadmi.partify.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.apadmi.partify.R;
import com.apadmi.partify.spotify.SpotifyManager;
import com.apadmi.partify.ui.fragments.PlaylistFragment;
import com.apadmi.partify.ui.fragments.SearchFragment;

/**
 * Created by tom on 26/10/14.
 */
public class AttendeeActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_attendee);

    Bundle b = getIntent().getExtras();

    SpotifyManager.SessionDetails currentSession = null;

    if(b != null && b.containsKey("partyName") && b.containsKey("attendeeId") && b.containsKey("partyCode")) {
      currentSession = SpotifyManager.getSpotifyManager().new SessionDetails(b.getString("partyCode"), b.getString("partyName"), b.getString("userId"));
      SpotifyManager.getSpotifyManager().newSession(currentSession);
    } else {
      exitToStartScreen();
    }

    TextView partyNameText = (TextView) findViewById(R.id.text_show_party_name);
    TextView partyCodeText = (TextView) findViewById(R.id.text_show_party_code);
    partyNameText.setText(currentSession.getPartyName());
    partyCodeText.setText(currentSession.getPartyCode());

    if(savedInstanceState == null)
      getFragmentManager().beginTransaction().add(R.id.attendee_content, new SearchFragment()).commit();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.search_menu_exit:
        exitToStartScreen();
        break;
      case R.id.playlist_menu_exit:
        exitToStartScreen();
        break;
      case R.id.search_menu_playlist:
        getFragmentManager().beginTransaction().replace(R.id.attendee_content, new PlaylistFragment()).commit();
        break;
      case R.id.playlist_menu_search:
        getFragmentManager().beginTransaction().replace(R.id.attendee_content, new SearchFragment()).commit();
        break;
    }
    return true;
  }

  private void exitToStartScreen() {
    startActivity(new Intent(this, StartActivity.class));
    SpotifyManager.getSpotifyManager().endSession();
    finish();
  }

  @Override
  public void onBackPressed() {
    exitToStartScreen();
  }
}
