package com.apadmi.partify.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.apadmi.partify.R;
import com.apadmi.partify.spotify.SpotifyManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by tom on 26/10/14.
 */
public class NowPlayingAdapter extends ArrayAdapter<String> {

  private Context mContext;
  private ImageLoader im;

  public NowPlayingAdapter(Context context, int resource, ArrayList<String> objects) {
    super(context, resource, objects);
    mContext = context;

    RequestQueue queue = Volley.newRequestQueue(mContext);

    im = new ImageLoader(queue, new ImageLoader.ImageCache() {
      private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(30);

      @Override
      public Bitmap getBitmap(String url) {
        return cache.get(url);
      }

      @Override
      public void putBitmap(String url, Bitmap bitmap) {
        cache.put(url, bitmap);
      }
    });
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    TextView nameText;
    final TextView artistText;
    final ImageView albumArt;

    if(convertView == null){
      convertView = LayoutInflater.from(mContext).inflate(R.layout.result_list_item, null);
      nameText = (TextView) convertView.findViewById(R.id.list_item_name);
      artistText = (TextView) convertView.findViewById(R.id.list_item_artist);
      albumArt = (ImageView) convertView.findViewById(R.id.list_item_album_art);
      convertView.setTag(new ViewHolder(nameText, artistText, albumArt));
    }else{
      ViewHolder holder = (ViewHolder) convertView.getTag();
      nameText = holder.nameText;
      artistText = holder.artistText;
      albumArt = holder.albumArt;
    }

    getTrackDetails(getItem(position), nameText, artistText);

    albumArt.setBackground(null);

    if(getItem(position).equals(SpotifyManager.getSpotifyManager().getCurrentlyPlaying()))
      albumArt.setBackgroundResource(android.R.drawable.presence_online);

    return convertView;
  }

  private void getTrackDetails(String id, final TextView nameText, final TextView artistText) {
    RequestQueue queue = Volley.newRequestQueue(mContext);

    String url = "https://api.spotify.com/v1/tracks/";
    try {
      url += URLEncoder.encode(id.substring(14), "UTF-8");
    } catch (UnsupportedEncodingException e) {
    }

    JsonRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null,
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            try {

              JSONObject j = new JSONObject(response.toString());

              Log.e("RESPONSE", response.toString());

              String name = j.getString("name");

              String artist = j.getJSONArray("artists").getJSONObject(0).getString("name");

              nameText.setText(name);
              artistText.setText(artist);

            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
          }
        });

    queue.add(jsObjRequest);
  }

  private class ViewHolder {
    public TextView nameText;
    public TextView artistText;
    public ImageView albumArt;

    public ViewHolder(TextView nameText, TextView artistText, ImageView albumArt) {
      this.nameText = nameText;
      this.artistText = artistText;
      this.albumArt = albumArt;
    }
  }
}
