package com.apadmi.partify.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.apadmi.partify.R;
import com.apadmi.partify.spotify.SpotifyManager;
import com.apadmi.partify.ui.PlayerActivity;
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by tom on 25/10/14.
 */
public class EventDetailsFragment extends Fragment {

  private ProgressDialog progressDialog;
  private String name;

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
        name = nameInput.getText().toString();
        if(name != null && name.length() > 0)
          startParty();
        else
          Toast.makeText(getActivity(), "Please input a party name", Toast.LENGTH_SHORT).show();
      }
    });

    return root;
  }

  public void onAuthenticationComplete(AuthenticationResponse authResponse) {
    Log.d("Spotify Manager", "Got authentication token");

    SpotifyManager.getSpotifyManager().setSpotify(new Spotify(authResponse.getAccessToken()), getActivity().getApplicationContext());

    requestID();
  }

  private void requestID() {
    // Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(getActivity());
    String url = "http://partify.apphb.com/api/Party/Create?partyName=";
    try {
      url += URLEncoder.encode(name, "UTF-8");
    }catch(UnsupportedEncodingException e){}

    JsonRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, null,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            hideProgress();
            try {
              JSONObject j = new JSONObject(response.toString());

              goToPlayScreen(j.getString("PartyCode"));
            }catch(JSONException e){
              e.printStackTrace();
            }
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            hideProgress();
          }
        });

    showProgress("Requesting party code");

    queue.add(jsObjRequest);
  }

  private void showProgress(String message) {
    if (progressDialog == null)
      progressDialog = new ProgressDialog(getActivity());

    progressDialog.setMessage(message);
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        progressDialog.show();
      }
    });
  }

  private void hideProgress() {
    progressDialog.dismiss();
    progressDialog = null;
  }

  private void startParty() {
    showProgress("Logging the fuck in");
    SpotifyAuthentication.openAuthWindow(SpotifyManager.CLIENT_ID, "token", SpotifyManager.REDIRECT_URI,
        new String[]{"user-read-private", "streaming"},
        null, getActivity());
  }

  private void goToPlayScreen(String partyCode) {
    Log.e("PartyCode", partyCode);
    Intent intent = new Intent(getActivity(), PlayerActivity.class);
    intent.putExtra("partyCode", partyCode);
    startActivity(intent);
    getActivity().finish();
  }


}
