package com.brandondlim.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
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

        final String BASE_URL = "http://image.tmdb.org/t/p/w185";

        Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                .appendEncodedPath(movie.getThumbnail())
                .build();

        String movieUrl = buildUri.toString();

        Log.d(LOG_TAG, "Movie URL: " + movieUrl);

        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setAdjustViewBounds(true);

        Picasso.with(mContext).load(movieUrl).into(imageView);

        return imageView;
    }

}
