package com.serggggg.movieguide.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.serggggg.movieguide.app.R;
import com.serggggg.movieguide.app.data.model.MovieInfo;
import com.serggggg.movieguide.app.data.remote.FetchMovieTask;
import com.serggggg.movieguide.app.data.remote.IDataLoadListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailFragment extends Fragment implements IDataLoadListener {

    private String movieID = null;

    private TextView mTitleText;
    private ImageView mPosterImg;
    private TextView mDescText;
    private TextView mYearText;
    private TextView mDurationText;
    private TextView mRatingText;
    private Button mFavButton;

    private MovieInfo mMovieInfo = null;

    public DetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null && args.containsKey(DetailActivity.KEY_MOVIE_ID)) {
            movieID = args.getString(DetailActivity.KEY_MOVIE_ID);
        }
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        initControls(rootView);

        return rootView;
    }

    private void initControls(View root) {

        if (root == null)
            return;

        mTitleText = (TextView) root.findViewById(R.id.title_text);
        mPosterImg = (ImageView) root.findViewById(R.id.poster_image);
        mDescText = (TextView) root.findViewById(R.id.desc_text);
        mYearText = (TextView) root.findViewById(R.id.year_text);
        mDurationText = (TextView) root.findViewById(R.id.duration_text);
        mRatingText = (TextView) root.findViewById(R.id.rating_text);
        mFavButton = (Button) root.findViewById(R.id.fav_button);
    }

    private void updateInfo() {

        if (mMovieInfo == null)
            return;

        mTitleText.setText(mMovieInfo.getTitle());
        mDescText.setText(mMovieInfo.getDescription());
        mYearText.setText(Integer.toString(mMovieInfo.getYear()));
        mDurationText.setText(mMovieInfo.getDurationStr());
        mRatingText.setText(mMovieInfo.getRating());
        Picasso.with(getContext()).load(mMovieInfo.getPosterUrl()).into(mPosterImg);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMovieInfo == null)
            requestMovieInfo();
        else
            updateInfo();
    }

    private void requestMovieInfo() {

        FetchMovieTask movieInfoTask = new FetchMovieTask(this);
        movieInfoTask.execute(getString(R.string.api_key), movieID);
    }

    @Override
    public void onDataListLoaded(List<MovieInfo> data) {

    }

    @Override
    public void onDataSingleLoaded(MovieInfo data) {

        mMovieInfo = data;
        updateInfo();
    }
}
