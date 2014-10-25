package com.apadmi.partify.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.apadmi.partify.R;
import com.apadmi.partify.spotify.SpotifyManager;
import com.apadmi.partify.ui.fragments.EventTypePickerFragment;
import com.spotify.sdk.android.Spotify;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.authentication.SpotifyAuthentication;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class StartActivity extends Activity {

  private ProgressDialog progressDialog;

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
      onAuthenticationComplete(SpotifyAuthentication.parseOauthResponse(uri));
    }
  }

  private void onAuthenticationComplete(AuthenticationResponse authResponse) {
    Log.d("Spotify Manager", "Got authentication token");

    SpotifyManager.getSpotifyManager().setSpotify(new Spotify(authResponse.getAccessToken()), getApplicationContext());

    requestID();
  }

  private void requestID() {
    // Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(this);
    final String partyName = "Big dong phil";
    String url = "http://partify.apphb.com/api/Party/Create?partyName=";
    try {
      url += URLEncoder.encode(partyName, "UTF-8");
    }catch(UnsupportedEncodingException e){}

    JsonRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, null,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            Log.e("THIS", response.toString());
            hideProgress();
            try {
              JSONObject j = new JSONObject(response.toString());

              goToPlayScreen(j.getString("PartyCode"));
            }catch(JSONException e){

            }
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            hideProgress();
          }
        });

    showProgress();

    queue.add(jsObjRequest);
  }

  private void goToPlayScreen(String partyCode) {
    Log.e("PartyCode", partyCode);
    startActivity(new Intent(this, PlayerActivity.class));
    finish();
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    Log.e("HH", "CALLED");
    if (item.getItemId() == android.R.id.home) {
      getFragmentManager().popBackStackImmediate();
      getActionBar().setDisplayHomeAsUpEnabled(false);
    }
    return true;
  }

  private void showProgress() {
    if (progressDialog == null)
      progressDialog = new ProgressDialog(this);

    progressDialog.setMessage("Requesting code");
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        progressDialog.show();
      }
    });
  }

  private void hideProgress() {
    progressDialog.hide();
    progressDialog = null;
  }


}