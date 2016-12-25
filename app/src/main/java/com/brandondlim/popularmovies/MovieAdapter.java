package com.brandondlim.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends BaseAdapter {

    private final String LOG_TAG = MovieAdapter.class.getSimpleName();

    private Context mContext;
    private ArrayList<Movie> mMovies;

    public MovieAdapter(Context c, Movie[] movies) {
        mContext = c;
        mMovies = new ArrayList<>();

        for (int i = 0; i < movies.length; i++) {
            mMovies.add(movies[i]);
        }
    }

    public void clearMovies() {
        Log.d(LOG_TAG, "clearMovies");
        mMovies.clear();
    }

    public void addMovies(Movie movie) {
        Log.d(LOG_TAG, "addMovies");
        mMovies.add(movie);
    }

    @Override
    public int getCount() {
        Log.d(LOG_TAG, "Count: " + mMovies.size());
        return mMovies.size();
    }

    @Override
    public long getItemId(int position) {
        return mMovies.get(position).getId();
    }

    @Override
    public Object getItem(int position) {
        return mMovies.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Movie movie = mMovies.get(position);
        ViewHolder viewHolder;

        final String BASE_URL = "http://image.tmdb.org/t/p/w185";

        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(movie.getThumbnail())
                .build();

        String movieUrl = buildUri.toString();

        Log.d(LOG_TAG, "Movie URL: " + movieUrl);

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.movie_grid, null);

            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.movie_grid_image);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(mContext).load(movieUrl).into(viewHolder.imageView);

        return convertView;
    }

    static class ViewHolder {
        ImageView imageView;
    }

}
