package com.serggggg.movieguide.app.data.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.serggggg.movieguide.app.R;
import com.serggggg.movieguide.app.data.model.MovieInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieInfoAdapter extends ArrayAdapter<MovieInfo> {

    private final String LOG_TAG = "MovieInfoAdapter";

    private int mResource;

    public MovieInfoAdapter(Context context, int resource, List<MovieInfo> objects) {
        super(context, resource, objects);
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MovieInfo item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(mResource, parent, false);
        }

        ImageView image = (ImageView) convertView.findViewById(R.id.movie_img);
        Picasso.with(getContext()).load(item.getPosterUrl()).into(image);

        return convertView;
    }

    @Override
    public long getItemId(int position) {

        return getItem(position).getId();
    }
}
