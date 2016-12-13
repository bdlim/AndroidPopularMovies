package com.brandondlim.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FetchMovieVolley {

    private final String LOG_TAG = FetchMovieVolley.class.getSimpleName();

    private Context mContext;

    public FetchMovieVolley(Context context) {
        mContext = context;
    }

    public void sendRequest(String sortBy, final MovieAdapter movieAdapter) {
        RequestQueue queue = Volley.newRequestQueue(mContext);

        if (sortBy == null) {
            return;
        }

        final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
        final String API_KEY_PARAM = "api_key";

        Uri buildUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendEncodedPath(sortBy)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
                .build();

        String url = buildUri.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Movie[] movies = getMovieDataFromJson(response);

                            if (movies != null) {

                                movieAdapter.clearMovies();

                                for (int i = 0; i < movies.length; i++) {
                                    movieAdapter.addMovies(movies[i]);
                                }

                                movieAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            Log.e(LOG_TAG, e.getMessage(), e);
                        }
                    }

                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(LOG_TAG, error.getMessage());
                }
        });

        queue.add(stringRequest);
    }

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

}
