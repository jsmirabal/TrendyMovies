package japps.trendymovies.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import japps.trendymovies.R;
import japps.trendymovies.activity.MainActivity;
import japps.trendymovies.activity.MovieDetailActivity;
import japps.trendymovies.adapter.ReviewRecyclerAdapter;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.utility.Utilities;

/**
 * Created by Julio on 9/7/2016.
 */
public class MovieReviewFragment extends Fragment {

    private Context mContext;
    private Bundle mExtras;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
    }

    private final String LOG_TAG = MovieReviewFragment.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        if (mContext instanceof MainActivity) {
            mExtras = ((MainActivity) mContext).getIntent().getExtras();
        } else {
            mExtras = ((MovieDetailActivity) mContext).getIntent().getExtras();
        }
        Bundle reviewBundle = mExtras.getBundle(MovieData.REVIEWS_PARAM);
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.movie_detail_action_fav);
        Bundle detailBundle = mExtras.getBundle(MovieData.DETAILS_PARAM);
        String movieId = Integer.toString(detailBundle.getInt(MovieData.ID_PARAM));
        if (Utilities.isFavourite(mContext, movieId)) {
            menuItem.setIcon(R.drawable.favourite_on);
        }
        super.onPrepareOptionsMenu(menu);
    }
}
