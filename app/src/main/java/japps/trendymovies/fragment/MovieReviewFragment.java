package japps.trendymovies.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import japps.trendymovies.R;
import japps.trendymovies.activity.MovieDetailActivity;
import japps.trendymovies.adapter.ReviewRecyclerAdapter;
import japps.trendymovies.data.MovieData;

/**
 * Created by Julio on 9/7/2016.
 */
public class MovieReviewFragment extends Fragment {

    private Context mContext;
    private final String LOG_TAG = MovieReviewFragment.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        mContext = getActivity();
        Intent intent = ((MovieDetailActivity) mContext).getIntent();
        Bundle extras = intent.getExtras();
        Bundle reviewBundle = extras.getBundle(MovieData.REVIEWS_PARAM);
        View rootView;
        if (reviewBundle != null && reviewBundle.getInt(MovieData.REVIEWS_COUNT) > 0) {
            rootView = inflater.inflate(R.layout.fragment_movie_review, container, false);
            RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.reviews_recycler_view);
            RecyclerView.LayoutManager manager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
            ReviewRecyclerAdapter adapter = new ReviewRecyclerAdapter(reviewBundle);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(adapter);
        } else {
            rootView = inflater.inflate(R.layout.no_review, container, false);
        }
        return rootView;
    }
}
