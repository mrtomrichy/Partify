package com.apadmi.partify.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.apadmi.partify.R;

/**
 * Created by tom on 26/10/14.
 */
public class PlaylistFragment extends Fragment {

  ListView playlistList;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    activity.getActionBar().setSubtitle("Party playlist");
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_playlist, null);

    playlistList = (ListView) root.findViewById(R.id.list_playlist);

    return root;
  }
}