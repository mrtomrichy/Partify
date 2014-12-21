package com.apadmi.partify.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by tom on 26/10/14.
 */
public class PlaylistFragment extends Fragment {

  private ListView playlistList;
  private NowPlayingAdapter mAdapter = null;

  private ArrayList<String> tracks;

  public PlaylistFragment() {
    this.setHasOptionsMenu(true);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    activity.getActionBar().setSubtitle("Party playlist");
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_playlist, null);

    tracks = new ArrayList<String>();

    playlistList = (ListView) root.findViewById(R.id.list_playlist);
    if (mAdapter == null)
      mAdapter = new NowPlayingAdapter(getActivity(), 0, tracks);
    playlistList.setAdapter(mAdapter);

    RequestQueue queue = Volley.newRequestQueue(getActivity());

    String url = "";
    try {
      url = String.format("http://partify.apphb.com/api/party/GetPlaylist?partyCodePlaylist=%s",
          URLEncoder.encode(SpotifyManager.getSpotifyManager().getSession().getPartyCode(), "UTF-8"));
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
                tracks.add("spotify:track:" + ids.getString(i));
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

    return root;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    menu.clear();
    inflater.inflate(R.menu.playlist_menu, menu);
  }
}
