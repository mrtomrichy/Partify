package com.apadmi.partify.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.apadmi.partify.R;
import com.apadmi.partify.spotify.SpotifyManager;
import com.apadmi.partify.ui.fragments.SearchFragment;

/**
 * Created by tom on 26/10/14.
 */
public class AttendeeActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_attendee);

    ActionBar ab = getActionBar();
    ab.setSubtitle("Party Mode");

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
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.player_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.attendee_menu_exit:
        exitToStartScreen();
        break;
      case R.id.attendee_menu_add:
        break;
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
