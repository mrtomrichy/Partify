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
import com.apadmi.partify.ui.AttendeeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by tom on 26/10/14.
 */
public class EventJoinFragment extends Fragment {

  private ProgressDialog progressDialog;
  private String partyCode;

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    activity.getActionBar().setDisplayHomeAsUpEnabled(true);
    getActivity().getActionBar().setSubtitle("Join a party");
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_join, container, false);

    final EditText partyCodeText = (EditText) root.findViewById(R.id.editText_event_join_name);

    Button joinButton = (Button) root.findViewById(R.id.button_join_event);
    joinButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        partyCode = partyCodeText.getText().toString();
        if(partyCode != null && partyCode.length() > 0)
          joinParty(partyCode);
      }
    });

    return root;
  }

  private void joinParty(String partyCode) {
    // Instantiate the RequestQueue.
    RequestQueue queue = Volley.newRequestQueue(getActivity());
    String url = "http://partify.apphb.com/api/Party/Join?partyCode=";
    try {
      url += URLEncoder.encode(partyCode, "UTF-8");
    }catch(UnsupportedEncodingException e){}

    JsonRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, url, null,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            hideProgress();
            try {
              JSONObject j = new JSONObject(response.toString());

              Log.e("USER CODE", j.getString("AttendeeId"));
              Log.e("PARTY NAME", j.getString("PartyName"));
              goToPartyScreen(j.getString("AttendeeId"), j.getString("PartyName"));
            }catch(JSONException e){
              e.printStackTrace();
            }
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            Toast.makeText(getActivity(), "Party not found", Toast.LENGTH_SHORT).show();
            hideProgress();
          }
        });

    showProgress("Joining party");

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

  private void goToPartyScreen(String userID, String partyName) {
    Intent intent = new Intent(getActivity(), AttendeeActivity.class);
    intent.putExtra("partyName", partyName);
    intent.putExtra("attendeeId", userID);
    intent.putExtra("partyCode", partyCode);
    getActivity().startActivity(intent);
    getActivity().finish();
  }
}
