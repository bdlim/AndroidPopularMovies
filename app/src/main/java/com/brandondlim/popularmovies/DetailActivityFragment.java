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

    private final String TITLE_INTENT_KEY = "title";
    private final String THUMBNAIL_INTENT_KEY = "thumbnail";
    private final String OVERVIEW_INTENT_KEY = "overview";
    private final String USER_RATING_INTENT_KEY = "user_rating";
    private final String RELEASE_DATE_INTENT_KEY = "release_date";

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

            TextView title = (TextView) rootView.findViewById(R.id.title);
            title.setText(intent.getStringExtra(TITLE_INTENT_KEY));

            final String BASE_URL = "http://image.tmdb.org/t/p/w185";
            Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                    .appendEncodedPath(intent.getStringExtra(THUMBNAIL_INTENT_KEY))
                    .build();
            String movieUrl = buildUri.toString();

            ImageView imageView = (ImageView) rootView.findViewById(R.id.thumbnail);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setAdjustViewBounds(true);

            Picasso.with(getActivity()).load(movieUrl).into(imageView);

            TextView overview = (TextView) rootView.findViewById(R.id.overview);
            overview.setText(intent.getStringExtra(OVERVIEW_INTENT_KEY));

            TextView userRating = (TextView) rootView.findViewById(R.id.user_rating);
            userRating.setText("User Rating: " + String.valueOf(intent.getDoubleExtra(USER_RATING_INTENT_KEY, 0)) + " / 10.0");

            TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date);
            releaseDate.setText("Release Date: " + intent.getStringExtra(RELEASE_DATE_INTENT_KEY));
        }


        return rootView;
    }
}
