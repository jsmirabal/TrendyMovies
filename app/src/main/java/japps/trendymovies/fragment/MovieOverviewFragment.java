package japps.trendymovies.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import japps.trendymovies.R;
import japps.trendymovies.activity.MovieDetailActivity;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.utility.Utilities;

/**
 * Created by Julio on 28/1/2016.
 */
public class MovieOverviewFragment extends Fragment {

    private Context mContext;
    private final String LOG_TAG = MovieOverviewFragment.class.getSimpleName();

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {return null;}
        int fragmentMovieOverview = R.layout.fragment_movie_overview;
        View rootView = inflater.inflate(fragmentMovieOverview,container,false);
        mContext = getActivity();
        setSynopsisViewScrolling(rootView);
        setMovieDataToViews(rootView);
        return rootView;
    }

    private void setMovieDataToViews(View rootView){
        TextView titleView = (TextView) rootView.findViewById(R.id.movie_detail_title);
        TextView statisticsView = (TextView) rootView.findViewById(R.id.movie_detail_year_length_rate);
        TextView synopsisView = (TextView) rootView.findViewById(R.id.movie_detail_synopsis);
        ImageView posterImg = (ImageView) rootView.findViewById(R.id.movie_detail_poster_image);
        final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        Intent intent = ((MovieDetailActivity)mContext).getIntent();
        Bundle detailBundle = intent.getExtras().getBundle(MovieData.DETAILS_PARAM);
        String a = detailBundle.getString(MovieData.TITLE_PARAM);
        String b = detailBundle.getString(MovieData.OVERVIEW_PARAM);
        String c = detailBundle.getString(MovieData.POSTER_PATH_PARAM);
        String d = detailBundle.getString(MovieData.RELEASE_DATE_PARAM);
        int e = detailBundle.getInt(MovieData.RUNTIME_PARAM);
        double f = detailBundle.getDouble(MovieData.RATE_PARAM);
        titleView.setText(a);
        synopsisView.setText(b);
        byte[] imgByte = detailBundle.getByteArray(MovieData.POSTER_BLOB_PARAM);
        if (imgByte == null){
            progressBar.setVisibility(ProgressBar.VISIBLE);
            Picasso.with(mContext).load(c).into(posterImg, new Callback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(ProgressBar.GONE);
                }
                @Override
                public void onError() {}
            });
        } else {
            posterImg.setImageBitmap(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));
        }
        statisticsView.setText(Utilities.createStatisticsRow(
                d,
                e,
                f
        ));
    }

    public void setSynopsisViewScrolling(View rootView) {
        TextView movie_desc_textView = (TextView) rootView.findViewById(R.id.movie_detail_synopsis);
        movie_desc_textView.setMovementMethod(new ScrollingMovementMethod());
    }
    
}
