package com.serggggg.movieguide.app.data.remote;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.serggggg.movieguide.app.utils.WebUtils;
import com.serggggg.movieguide.app.data.model.MovieInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class FetchMovieTask extends AsyncTask<String, Void, MovieInfo> {

    private final static String LOG_TAG = "FetchMovieTask";

    private final static String CORE_URL = "https://api.themoviedb.org/3/movie/";

    private final static String PARAM_API_KEY = "api_key";

    private IDataLoadListener mDataLoadListener = null;

    public FetchMovieTask(IDataLoadListener dataLoadListener) {
        mDataLoadListener = dataLoadListener;
    }

    private MovieInfo parseFromJSON(String jsonStr) throws JSONException {

        String posterUrlPrefix = "http://image.tmdb.org/t/p/" + WebUtils.IMAGE_SIZE_PREFIX;

        JSONObject root = new JSONObject(jsonStr);

        int id = root.getInt("id");
        String title = root.getString("title");
        String posterUrl = posterUrlPrefix + root.getString("poster_path");

        MovieInfo movieInfo = new MovieInfo(id, title, posterUrl);
        movieInfo.setDescription(root.getString("overview"));
        movieInfo.setDuration(root.getInt("runtime"));
        String releaseYear = root.getString("release_date").substring(0, 4);
        movieInfo.setYear(Integer.parseInt(releaseYear));
        movieInfo.setRating(root.getString("vote_average") + " / 10");

        return movieInfo;
    }

    @Override
    protected MovieInfo doInBackground(String... params) {

        if (params.length == 0)
            return null;

        String apiKey = params[0];
        String id = params[1];

        Uri uri = Uri.parse(CORE_URL + id).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, apiKey).build();

        String jsonResponse = WebUtils.getJsonFromUrl(uri.toString());
        if (jsonResponse == null)
            return null;

        MovieInfo result = null;
        try {
            result = parseFromJSON(jsonResponse);
        }
        catch (JSONException e) {
            Log.e(LOG_TAG, "JSON parsing error: " + e.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(MovieInfo movieData) {

        if (mDataLoadListener != null)
            mDataLoadListener.onDataSingleLoaded(movieData);
    }
}
