package com.serggggg.movieguide.app.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtils {

    private final static String LOG_TAG = "WebUtils";

    public static String IMAGE_SIZE_PREFIX = null;

    public static String getJsonFromUrl(String urlStr) {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;

        String result = null;

        try {
            URL url = new URL(urlStr);

            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            if (inputStream == null)
                return null;

            StringBuffer stringBuffer = new StringBuffer();

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line + "\n");
            }

            if (stringBuffer.length() == 0)
                return null;

            result = stringBuffer.toString();

        }
        catch (IOException e) {
            Log.e(LOG_TAG, "Data fetching error", e);
            return null;
        }
        finally {
            if (httpURLConnection != null)
                httpURLConnection.disconnect();
            if (bufferedReader != null)
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
        }

        return result;
    }

    public static NetworkInfo getNetworkInfo(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo();
    }

    public static boolean isConnectedToInternet(Context context)
    {
        NetworkInfo info = getNetworkInfo(context);
        return (info != null && info.isConnected());
    }


}
