package japps.trendymovies.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import japps.trendymovies.R;
import japps.trendymovies.activities.MovieDetailActivity;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.utilities.Utils;

/**
 * Created by Julio on 28/1/2016.
 */
public class MovieDetailFragment extends Fragment {

    private Context mContext;
    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {return null;}
        int fragmentMovieDetail = R.layout.fragment_movie_detail;
        View rootView = inflater.inflate(fragmentMovieDetail,container,false);
        mContext = getActivity();
        ActionBar actionBar = ((MovieDetailActivity)mContext).getSupportActionBar();
        if (actionBar != null) actionBar.setElevation(0);
        setSynopsisViewScrolling(rootView);
        setMovieDataToViews(rootView);
        //addTrailerButtons(rootView, inflater, container);
        return rootView;
    }

    private void setMovieDataToViews(View rootView){
        TextView titleView = (TextView) rootView.findViewById(R.id.movie_detail_title);
        TextView statisticsView = (TextView) rootView.findViewById(R.id.movie_detail_year_length_rate);
        TextView synopsisView = (TextView) rootView.findViewById(R.id.movie_detail_synopsis);
        ImageView posterImg = (ImageView) rootView.findViewById(R.id.movie_detail_poster_image);
        Intent intent = ((MovieDetailActivity)mContext).getIntent();
        Bundle extras = intent.getExtras();
        String a = extras.getString(MovieData.TITLE_PARAM);
        String b = extras.getString(MovieData.SYNOPSIS_PARAM);
        String c = extras.getString(MovieData.POSTER_PATH_PARAM);
        String d = extras.getString(MovieData.RELEASE_DATE_PARAM);
        int e = extras.getInt(MovieData.RUNTIME_PARAM);
        double f = extras.getDouble(MovieData.RATE_PARAM);
        titleView.setText(a);
        synopsisView.setText(b);
        Picasso.with(mContext).load(c).into(posterImg);
        statisticsView.setText(Utils.createStatisticsRow(
                d,
                e,
                f
        ));
    }

    private void addTrailerButtons(View rootView, LayoutInflater inflater, ViewGroup container) {

    }

    public void setSynopsisViewScrolling(View rootView) {
        TextView movie_desc_textView = (TextView) rootView.findViewById(R.id.movie_detail_synopsis);
        movie_desc_textView.setMovementMethod(new ScrollingMovementMethod());
    }
    
}
