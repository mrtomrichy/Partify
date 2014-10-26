package com.apadmi.partify.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.apadmi.partify.R;
import com.apadmi.partify.ui.fragments.SearchFragment;

import java.util.ArrayList;

/**
 * Created by tom on 26/10/14.
 */
public class SearchResultsAdapter extends ArrayAdapter<SearchFragment.Track> {

  private Context mContext;

  public SearchResultsAdapter(Context context, int resource, ArrayList<SearchFragment.Track> objects) {
    super(context, resource, objects);
    mContext = context;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    TextView nameText;
    TextView artistText;
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

    nameText.setText(getItem(position).getName());
    artistText.setText(getItem(position).getArtist());
    albumArt.setImageDrawable(null);

    if(albumArt.getTag() != null && albumArt.getTag() instanceof ImageRequest)
      ((ImageRequest)albumArt.getTag()).cancel();

    RequestQueue queue = Volley.newRequestQueue(mContext);

    String url = getItem(position).getImageURL();

    ImageRequest ir = new ImageRequest(url, new Response.Listener<Bitmap>() {

      @Override
      public void onResponse(Bitmap response) {
        albumArt.setImageBitmap(response);

      }
    }, 0, 0, null, null);

    queue.add(ir);
    albumArt.setTag(ir);

    return convertView;
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
