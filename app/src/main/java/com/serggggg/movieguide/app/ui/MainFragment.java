package com.serggggg.movieguide.app.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.serggggg.movieguide.app.data.remote.FetchMoviesListTask;
import com.serggggg.movieguide.app.data.remote.IDataLoadListener;
import com.serggggg.movieguide.app.data.adapters.MovieInfoAdapter;
import com.serggggg.movieguide.app.R;
import com.serggggg.movieguide.app.utils.WebUtils;
import com.serggggg.movieguide.app.data.model.MovieInfo;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements IDataLoadListener {

    public static final String FRAGMENT_TAG = "MainFragment";

    private String mSearchType = null;
    private int mPageNum = 1;

    private MovieInfoAdapter mMovieInfoAdapter = null;

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        GridView gridView = (GridView) inflater.inflate(R.layout.fragment_main, container, false);
        if (mMovieInfoAdapter == null)
            mMovieInfoAdapter = new MovieInfoAdapter(getActivity(),
                    R.layout.main_grid_item, new ArrayList<MovieInfo>());

        gridView.setAdapter(mMovieInfoAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                passToDetails(Long.toString(adapter.getItemIdAtPosition(position)));
            }
        });
        setupScrollListener(gridView);

        return gridView;
    }

    private void setupScrollListener(GridView gridView) {

        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            private int lastVisiblePos = 0;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int currentFirstVisPos = view.getFirstVisiblePosition();
                if (currentFirstVisPos > lastVisiblePos) {
                    if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                        updateMovieData();
                    }

                }
                lastVisiblePos = currentFirstVisPos;
            }
        });
    }

    private String getSearchTypeFromPrefs() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String searchType = preferences.getString(getString(R.string.prefs_search_type_key),
                getString(R.string.prefs_search_type_default));
        return searchType;
    }

    private void resetData() {
        mPageNum = 1;
        mMovieInfoAdapter.clear();
    }

    @Override
    public void onStart() {
        super.onStart();

        String searchType = getSearchTypeFromPrefs();
        if (!searchType.equals(mSearchType)) {
            mSearchType = searchType;
            resetData();
            updateMovieData();
        }
    }

    private void updateMovieData() {

        if (!WebUtils.isConnectedToInternet(getActivity())) {
            Toast.makeText(getActivity(),
                    "No internet connection.\r\nData load operation canceled",
                    Toast.LENGTH_LONG).show();
            return;
        }
        FetchMoviesListTask task = new FetchMoviesListTask(this);
        task.execute(getString(R.string.api_key), mSearchType, Integer.toString(mPageNum++));
    }

    private void passToDetails(String movieID) {

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.KEY_MOVIE_ID, movieID);
        startActivity(intent);
    }

    @Override
    public void onDataListLoaded(List<MovieInfo> data) {
        if (data != null && data.size() > 0) {
            mMovieInfoAdapter.addAll(data);
        }
    }

    @Override
    public void onDataSingleLoaded(MovieInfo data) {

    }

}
