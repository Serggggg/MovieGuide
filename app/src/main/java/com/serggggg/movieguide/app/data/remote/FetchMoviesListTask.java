package com.serggggg.movieguide.app.data.remote;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.serggggg.movieguide.app.utils.WebUtils;
import com.serggggg.movieguide.app.data.model.MovieInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FetchMoviesListTask extends AsyncTask<String, Void, List<MovieInfo>> {

    private final static String LOG_TAG = "FetchMoviesListTask";

    private final static String CORE_URL = "https://api.themoviedb.org/3/movie/";

    private final static String PARAM_API_KEY = "api_key";
    private final static String PARAM_PAGE = "page";

    private IDataLoadListener mDataLoadListener = null;

    public FetchMoviesListTask(IDataLoadListener dataLoadListener) {
        mDataLoadListener = dataLoadListener;
    }

    private List<MovieInfo> parseFromJSON(String jsonStr) throws JSONException {

        String posterUrlPrefix = "http://image.tmdb.org/t/p/" + WebUtils.IMAGE_SIZE_PREFIX;

        JSONObject root = new JSONObject(jsonStr);
        JSONArray movies = root.getJSONArray("results");

        ArrayList<MovieInfo> movieInfoArrayList = new ArrayList<>();

        for (int i = 0; i < movies.length(); i++) {

            JSONObject movie = movies.getJSONObject(i);
            int id = movie.getInt("id");
            String title = movie.getString("title");
            String posterUrl = posterUrlPrefix + movie.getString("poster_path");

            MovieInfo movieInfo = new MovieInfo(id, title, posterUrl);
            movieInfoArrayList.add(movieInfo);
        }

        return movieInfoArrayList;
    }

    @Override
    protected List<MovieInfo> doInBackground(String... params) {

        if (params.length == 0)
            return null;

        String apiKey = params[0];
        String searchType = params[1];
        String pageNum = params[2];

        Uri uri = Uri.parse(CORE_URL + searchType).buildUpon()
                .appendQueryParameter(PARAM_PAGE, pageNum)
                .appendQueryParameter(PARAM_API_KEY, apiKey).build();


        String jsonResponse = WebUtils.getJsonFromUrl(uri.toString());
        if (jsonResponse == null)
            return null;

        List<MovieInfo> result = null;
        try {
            result = parseFromJSON(jsonResponse);
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, "JSON parsing error: " + e.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(List<MovieInfo> movieData) {

        if (mDataLoadListener != null)
            mDataLoadListener.onDataListLoaded(movieData);
    }
}
