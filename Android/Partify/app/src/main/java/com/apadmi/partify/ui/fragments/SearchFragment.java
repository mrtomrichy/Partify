package com.apadmi.partify.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.apadmi.partify.R;
import com.apadmi.partify.adapters.SearchResultsAdapter;
import com.apadmi.partify.spotify.SpotifyManager;
import com.apadmi.partify.spotify.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by tom on 26/10/14.
 */
public class SearchFragment extends Fragment {

  private JsonRequest jsObjRequest;

  private ArrayList<Track> trackList;

  private ListView resultsList;
  private SearchResultsAdapter mAdapter;

  public SearchFragment() {
    if (trackList == null)
      trackList = new ArrayList<Track>();
    this.setHasOptionsMenu(true);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    activity.getActionBar().setSubtitle("Search for a song");
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_attendee_search, container, false);

    EditText searchBox = (EditText) root.findViewById(R.id.text_search_track);
    searchBox.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

      }

      @Override
      public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        if (charSequence.length() > 0) {
          searchForTrack(charSequence.toString());
        } else {
          trackList.clear();
          mAdapter.notifyDataSetChanged();
        }

      }

      @Override
      public void afterTextChanged(Editable editable) {

      }
    });

    resultsList = (ListView) root.findViewById(R.id.list_search_results);

    if (mAdapter == null)
      mAdapter = new SearchResultsAdapter(getActivity(), 0, trackList);

    resultsList.setAdapter(mAdapter);

    resultsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Track selected = trackList.get(i);
        request(selected);
      }
    });

    return root;
  }

  private void searchForTrack(String track) {

    RequestQueue queue = Volley.newRequestQueue(getActivity());

    if (jsObjRequest != null) {
      jsObjRequest.cancel();
    }

    String url = "https://api.spotify.com/v1/search?q=";
    try {
      url += URLEncoder.encode(track, "UTF-8");
    } catch (UnsupportedEncodingException e) {
    }
    url += "&type=track&limit=20";

    jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            jsObjRequest = null;
            //hideProgress();
            try {

              trackList.clear();

              JSONObject j = new JSONObject(response.toString());

              Log.e("RETURNED", j.toString());
              JSONObject tracks = j.getJSONObject("tracks");
              JSONArray items = tracks.getJSONArray("items");
              for (int i = 0; i < items.length(); i++) {
                String name = items.getJSONObject(i).getString("name");
                String artists = "";
                JSONArray artistList = items.getJSONObject(i).getJSONArray("artists");
                for (int a = 0; a < artistList.length(); a++)
                  artists += artistList.getJSONObject(a).getString("name");
                String uri = items.getJSONObject(i).getString("id");

                JSONArray images = items.getJSONObject(i).getJSONObject("album").getJSONArray("images");
                String imageURL = images.getJSONObject(images.length() - 1).getString("url");
                trackList.add(new Track(name, artists, uri, imageURL));
              }


            } catch (JSONException e) {
              e.printStackTrace();
            }

            mAdapter.notifyDataSetChanged();
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            jsObjRequest = null;
            //Toast.makeText(getActivity(), "Party not found", Toast.LENGTH_SHORT).show();
            //hideProgress();
          }
        });

    //showProgress("Joining party");

    queue.add(jsObjRequest);
  }

  public void request(Track selected) {
    RequestQueue queue = Volley.newRequestQueue(getActivity());

    String url = "";
    try {
      url = String.format("http://partify.apphb.com/api/party/Add?songId=%s&partyId=%s",
          URLEncoder.encode(selected.getUri(), "UTF-8"),
          URLEncoder.encode(SpotifyManager.getSpotifyManager().getSession().getPartyCode(), "UTF-8"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, null,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            Log.e("GUNE", response.toString());
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {

          }
        });

    queue.add(jsObjRequest);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    menu.clear();
    inflater.inflate(R.menu.search_menu, menu);
  }
}
