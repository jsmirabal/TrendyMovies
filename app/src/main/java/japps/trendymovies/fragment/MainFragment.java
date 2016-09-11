package japps.trendymovies.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Locale;

import japps.trendymovies.R;
import japps.trendymovies.activity.MainActivity;
import japps.trendymovies.activity.MovieDetailActivity;
import japps.trendymovies.adapter.ImageAdapter;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.data.MovieHandler;
import japps.trendymovies.data.MovieListData;
import japps.trendymovies.task.FetchMovieTask;
import japps.trendymovies.utility.Utilities;

/**
 * Created by Julio on 21/1/2016.
 */
public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {
    public static final String LOAD_MOVIE_LIST_FINISHED = "LMLF";
    private ImageAdapter mAdapter;
    private GridView mGrid;
    private Context mContext;
    private MovieHandler mMovieHandler;
    private ArrayList<String> mMoviePosterList;
    private ArrayList<String> mMovieIdList;
    private Bundle mMovieListBundle;
    private FetchMovieTask task;
    private MainActivity mActivity;
    private boolean mIsFavouriteViewActive;
    private final String LOG_TAG = MainFragment.class.getSimpleName();
    private final String MOVIE_LIST = "movie_list";
    public static final String MOVIE_DATA = "movie_data";

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int fragmentMainRes = R.layout.fragment_main;

        View root = inflater.inflate(fragmentMainRes, container, false);

        mGrid = (GridView) root.findViewById(R.id.gridView);
        mGrid.setOnItemClickListener(this);

        Log.d(LOG_TAG, "LOCALE: " + Locale.getDefault());

        return root;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAdapter = new ImageAdapter(mContext, R.layout.list_item_poster, R.id.poster_view);
        if (savedInstanceState != null) {
            mMovieListBundle = savedInstanceState.getParcelable(MOVIE_LIST);
            mAdapter.setItems(mMovieListBundle);
            mGrid.setAdapter(mAdapter);
        } else {
            fetchMovieList(FetchMovieTask.MOST_POPULAR);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String movieId = view.getTag().toString();
        if (!mIsFavouriteViewActive) {
            setIntentDataFromTask(movieId);
        } else {
            setIntentDataFromDb(movieId);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mActivity = (MainActivity) mContext;
        ActionBar actionBar = ((MainActivity) mContext).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setElevation(0);
        }
    }

    public void fetchMovieList(int sortBy) {
        Bundle params = new Bundle();
        params.putInt(FetchMovieTask.SORT_KEY, sortBy);
        mIsFavouriteViewActive = false;
        task = new FetchMovieTask() {
            @Override
            protected void onPostExecute(MovieHandler data) {
                super.onPostExecute(data);
                if (data == null) {
//                    Toast.makeText(mContext, "Unable to fetch movie data. Try again later.",
//                            Toast.LENGTH_LONG).show();
                    Snackbar.make(getView(), "Unable to fetch movie data. Try again later.",
                            Snackbar.LENGTH_LONG).show();
                    return;
                }
                mMovieHandler = data;
                mMovieListBundle = ((MovieListData) mMovieHandler).getMovieDataBundle();
                mAdapter.setItems(mMovieListBundle);
                mGrid.setAdapter(mAdapter);
                if (mActivity.isTableMode()){
                    mGrid.performItemClick(mAdapter.getView(0,null,null),0,mAdapter.getItemId(0));
                }
            }
        };
        task.execute(FetchMovieTask.FETCH_MOVIE_LIST, params, mContext);
    }

    public void fetchFavourites() {
        mMovieListBundle = Utilities.getFavouritesDataList(mContext);
        if (!mMovieListBundle.isEmpty()) {
            mAdapter.setItems(mMovieListBundle);
            mGrid.setAdapter(mAdapter);
            if (mActivity.isTableMode()){
                mGrid.performItemClick(mAdapter.getView(0,null,null),0,mAdapter.getItemId(0));
            }
            mIsFavouriteViewActive = true;
            return;
        }
        mGrid.setAdapter(new ImageAdapter(mContext, R.layout.list_item_poster, R.id.poster_view));
        Log.d(LOG_TAG, "No favourites rows were found.");
    }

    private void setIntentDataFromDb(String movieId) {
        Bundle data = Utilities.getFavouriteMovie(mContext, movieId);
        Intent intent;
        if (data.isEmpty()) {
            return;
        }
        if (mActivity.isTableMode()) {
            intent = mActivity.getIntent();
        } else {
            intent = new Intent(mContext, MovieDetailActivity.class);
        }

        intent.putExtra(MovieData.DETAILS_PARAM, data.getBundle(MovieData.DETAILS_PARAM));
        intent.putExtra(MovieData.TRAILERS_PARAM, data.getBundle(MovieData.TRAILERS_PARAM));
        intent.putExtra(MovieData.REVIEWS_PARAM, data.getBundle(MovieData.REVIEWS_PARAM));
        intent.putExtra(MovieData.CAST_PARAM, data.getBundle(MovieData.CAST_PARAM));
        intent.putExtra(MovieData.CREW_PARAM, data.getBundle(MovieData.CREW_PARAM));

        if (!mActivity.isTableMode()){
            mContext.startActivity(intent);
        } else {
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(LOAD_MOVIE_LIST_FINISHED));
        }
    }

    private void setIntentDataFromTask(String movieId) {
        Bundle params = new Bundle();
        params.putString(FetchMovieTask.MOVIE_ID_KEY, movieId);
        task = new FetchMovieTask() {
            @Override
            protected void onPostExecute(MovieHandler data) {
                super.onPostExecute(data);
                if (data == null) {
//                    Toast.makeText(mContext, "Unable to fetch movie data. Try again later.",
//                            Toast.LENGTH_LONG).show();
                    Snackbar.make(getView(), "Unable to fetch movie data. Try again later.",
                            Snackbar.LENGTH_LONG).show();
                    return;
                }
                MovieData movieData = (MovieData) data;
                Intent intent;
                if (mActivity.isTableMode()) {
                    intent = mActivity.getIntent();
                } else {
                    intent = new Intent(mContext, MovieDetailActivity.class);
                }

                intent.putExtra(MovieData.DETAILS_PARAM, movieData.getDetailBundle());
                intent.putExtra(MovieData.TRAILERS_PARAM, movieData.getTrailerBundle());
                intent.putExtra(MovieData.REVIEWS_PARAM, movieData.getReviewBundle());
                intent.putExtra(MovieData.CAST_PARAM, movieData.getCastBundle());
                intent.putExtra(MovieData.CREW_PARAM, movieData.getCrewBundle());

                if (!mActivity.isTableMode()){
                    mContext.startActivity(intent);
                } else {
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(LOAD_MOVIE_LIST_FINISHED));
                }
            }

        };
        task.execute(FetchMovieTask.FETCH_MOVIE, params, mContext);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(MOVIE_LIST, mMovieListBundle);
    }

    public boolean isFavouriteViewActive() {
        return mIsFavouriteViewActive;
    }
}
