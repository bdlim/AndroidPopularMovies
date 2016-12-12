package com.brandondlim.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MovieFragment extends Fragment {

    private MovieAdapter mMovieAdapter;
    private Movie[] mMovies;

    public final String POPULAR = "popular";
    public final String TOP_RATED = "top_rated";

    private final String TITLE_INTENT_KEY = "title";
    private final String THUMBNAIL_INTENT_KEY = "thumbnail";
    private final String OVERVIEW_INTENT_KEY = "overview";
    private final String USER_RATING_INTENT_KEY = "user_rating";
    private final String RELEASE_DATE_INTENT_KEY = "release_date";

    public MovieFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.moviefragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_popular) {
            updateMovies(POPULAR);
            return true;
        } else if (id == R.id.action_sort_top_rated) {
            updateMovies(TOP_RATED);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMovies = new Movie[0];
        mMovieAdapter = new MovieAdapter(getActivity(), mMovies);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(mMovieAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = (Movie) mMovieAdapter.getItem(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(TITLE_INTENT_KEY, movie.getTitle())
                        .putExtra(THUMBNAIL_INTENT_KEY, movie.getThumbnail())
                        .putExtra(OVERVIEW_INTENT_KEY, movie.getSynopsis())
                        .putExtra(USER_RATING_INTENT_KEY, movie.getUserRating())
                        .putExtra(RELEASE_DATE_INTENT_KEY, movie.getReleaseDate());
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        updateMovies(POPULAR);
    }

    public void updateMovies(String filter) {

        FetchMovieTask movieTask = new FetchMovieTask();
        movieTask.execute(filter);
    }

    public class FetchMovieTask extends AsyncTask<String, Void, Movie[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();


        private Movie[] getMovieDataFromJson(String movieJsonStr) throws JSONException {

            final String RESULTS = "results";
            final String ORIGINAL_TITLE = "original_title";
            final String POSTER_PATH = "poster_path";
            final String OVERVIEW = "overview";
            final String VOTE_AVERAGE = "vote_average";
            final String RELEASE_DATE = "release_date";

            JSONObject movieJson = new JSONObject(movieJsonStr);
            JSONArray movieArray = movieJson.getJSONArray(RESULTS);

            Movie[] movies = new Movie[movieArray.length()];

            for (int i = 0; i < movieArray.length(); i++) {

                JSONObject movieObject = movieArray.getJSONObject(i);

                String originalTitle = movieObject.getString(ORIGINAL_TITLE);
                String posterPath = movieObject.getString(POSTER_PATH);
                String overview = movieObject.getString(OVERVIEW);
                Double voteAverage = movieObject.getDouble(VOTE_AVERAGE);
                String releaseDate = movieObject.getString(RELEASE_DATE);

                Movie movie = new Movie(originalTitle, posterPath, overview,
                        voteAverage, releaseDate, i);

                movies[i] = movie;
            }

            for (int i = 0; i < movies.length; i++) {
                Log.d(LOG_TAG, "#" + i + " " + movies[i].toString());
            }

            return movies;
        }

        @Override
        protected Movie[] doInBackground(String... params) {
            Log.d(LOG_TAG, "doInBackground");

            if (params.length == 0) {
                return null;
            }

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String movieJsonStr = null;

            try {
                final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
                final String API_KEY_PARAM = "api_key";

                Uri buildUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendEncodedPath(params[0])
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                        .build();

                URL url = new URL(buildUri.toString());

                Log.d(LOG_TAG, "URL String: " + url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream in = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (in == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                movieJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            Log.d(LOG_TAG, "Movie JSON: " + movieJsonStr);

            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            Log.d(LOG_TAG, "onPostExecute");

            mMovies = movies;

            mMovieAdapter.clearMovies();

            for (int i = 0; i < movies.length; i++) {
                mMovieAdapter.addMovies(movies[i]);
            }

            mMovieAdapter.notifyDataSetChanged();
        }
    }
}
