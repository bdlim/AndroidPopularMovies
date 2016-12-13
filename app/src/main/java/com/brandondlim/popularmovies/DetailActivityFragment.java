package com.brandondlim.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String LOG_TAG = DetailActivityFragment.class.getSimpleName();

    private final String MOVIE_INTENT_KEY = "movie";

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();
        if (intent != null) {

            Movie movie = (Movie) intent.getParcelableExtra(MOVIE_INTENT_KEY);

            if (movie != null) {

                TextView title = (TextView) rootView.findViewById(R.id.title);
                title.setText(movie.getTitle());

                final String BASE_URL = "http://image.tmdb.org/t/p/w185";
                Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                        .appendEncodedPath(movie.getThumbnail())
                        .build();
                String movieUrl = buildUri.toString();

                ImageView imageView = (ImageView) rootView.findViewById(R.id.thumbnail);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setAdjustViewBounds(true);

                Picasso.with(getActivity()).load(movieUrl).into(imageView);

                TextView overview = (TextView) rootView.findViewById(R.id.overview);
                overview.setText(movie.getSynopsis());

                TextView userRating = (TextView) rootView.findViewById(R.id.user_rating);
                userRating.setText("User Rating: " + String.valueOf(movie.getUserRating()) + " / 10.0");

                TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date);
                releaseDate.setText("Release Date: " + movie.getReleaseDate());
            }
        }


        return rootView;
    }
}
