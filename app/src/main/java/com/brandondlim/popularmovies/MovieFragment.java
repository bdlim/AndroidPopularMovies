package com.brandondlim.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

public class MovieFragment extends Fragment {

    private MovieAdapter mMovieAdapter;
    private Movie[] mMovies;

    public final String POPULAR = "popular";
    public final String TOP_RATED = "top_rated";

    private final String MOVIE_INTENT_KEY = "movie";

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
                        .putExtra(MOVIE_INTENT_KEY, movie);
                startActivity(intent);
            }
        });

        updateMovies(POPULAR);

        return rootView;
    }

    public void updateMovies(String filter) {
        FetchMovieVolley movieVolley = new FetchMovieVolley(this.getActivity());
        movieVolley.sendRequest(filter, mMovieAdapter);
    }
}
