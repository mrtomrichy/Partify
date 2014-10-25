package com.apadmi.partify.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.apadmi.partify.R;
import com.apadmi.partify.spotify.SpotifyManager;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;

/**
 * Created by tom on 25/10/14.
 */
public class EventDetailsFragment extends Fragment {

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    activity.getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_event_details, container, false);

    final EditText nameInput = (EditText) root.findViewById(R.id.editText_event_name);
    Button submitEventDetails = (Button) root.findViewById(R.id.button_start_event);

    submitEventDetails.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startParty();
      }
    });

    return root;
  }

  public void startParty() {
    SpotifyAuthentication.openAuthWindow(SpotifyManager.CLIENT_ID, "token", SpotifyManager.REDIRECT_URI,
        new String[]{"user-read-private", "streaming"},
        null, getActivity());
  }


}
