package com.apadmi.partify.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apadmi.partify.R;

/**
 * Created by tom on 25/10/14.
 */
public class EventTypePickerFragment extends Fragment {
  private Button startPartyButton;
  private Button joinPartyButton;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_start, container, false);

    startPartyButton = (Button) root.findViewById(R.id.button_start_party);
    joinPartyButton = (Button) root.findViewById(R.id.button_join_party);

    startPartyButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        startParty();
      }
    });

    joinPartyButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        joinParty();
      }
    });

    getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
    getActivity().getActionBar().setSubtitle("Spotify for parties");

    return root;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    activity.getActionBar().setDisplayHomeAsUpEnabled(false);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
  {
    super.onCreateOptionsMenu(menu, inflater);
    menu.clear();


    //fragment specific menu creation
  }

  private void startParty() {
    getFragmentManager().beginTransaction().replace(R.id.main_content, new EventDetailsFragment()).addToBackStack(null).commit();
  }

  private void joinParty() {
    getFragmentManager().beginTransaction().replace(R.id.main_content, new EventJoinFragment()).addToBackStack(null).commit();
  }
}
