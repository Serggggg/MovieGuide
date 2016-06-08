package com.serggggg.movieguide.app.data.remote;

import com.serggggg.movieguide.app.data.model.MovieInfo;

import java.util.List;

public interface IDataLoadListener {

    void onDataListLoaded(List<MovieInfo> data);
    void onDataSingleLoaded(MovieInfo data);
}
