package com.apadmi.partify.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import com.apadmi.partify.R;
import com.apadmi.partify.ui.fragments.PlaceholderFragment;

public class StartActivity extends Activity {

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);

    if(savedInstanceState == null) {
      getFragmentManager().beginTransaction()
          .add(R.id.container, new PlaceholderFragment())
          .commit();
    }

    ActionBar ab = getActionBar();
    ab.setTitle("Partify");
    ab.setSubtitle("Spotify for parties");
  }
}