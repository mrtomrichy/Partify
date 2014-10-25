package com.apadmi.partify.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import com.apadmi.partify.R;
import com.apadmi.partify.ui.fragments.EventDetailsFragment;
import com.apadmi.partify.ui.fragments.EventTypePickerFragment;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;

public class StartActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);

    if (savedInstanceState == null)
      getFragmentManager().beginTransaction().add(R.id.main_content, new EventTypePickerFragment()).commit();

    ActionBar ab = getActionBar();
    ab.setTitle("Partify");
    ab.setSubtitle("Spotify for parties");
  }

  @Override
  public void onNewIntent(Intent intent) {
    Uri uri = intent.getData();
    if (uri != null) {
      Fragment currentFragment = getFragmentManager().findFragmentById(R.id.main_content);
      if(currentFragment instanceof EventDetailsFragment){
        EventDetailsFragment frag = (EventDetailsFragment) currentFragment;
        frag.onAuthenticationComplete(SpotifyAuthentication.parseOauthResponse(uri));
      }
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      getFragmentManager().popBackStackImmediate();
      getActionBar().setDisplayHomeAsUpEnabled(false);
    }
    return true;
  }

}