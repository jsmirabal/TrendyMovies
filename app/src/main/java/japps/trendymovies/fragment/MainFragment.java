package japps.trendymovies.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import japps.trendymovies.R;
import japps.trendymovies.activity.MainActivity;
import japps.trendymovies.activity.MovieDetailActivity;
import japps.trendymovies.adapter.PosterListRecyclerAdapter;
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
    //    private ImageAdapter mAdapter;
    private PosterListRecyclerAdapter mAdapter;
    private GridView mGrid;
    private RecyclerView mRecyclerGrid;
    private Context mContext;
    private MovieHandler mMovieHandler;
    private ArrayList<String> mMoviePosterList;
    private ArrayList<String> mMovieIdList;
    private Bundle mMovieListBundle;
    private FetchMovieTask task;
    private MainActivity mActivity;
    private boolean mIsFavouriteViewActive;
    private int mCurrentSort;
    private final String LOG_TAG = MainFragment.class.getSimpleName();
    private final String MOVIE_LIST = "movie_list";
    public static final String MOVIE_DATA = "movie_data";

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int fragmentMainRes = R.layout.fragment_main;

        View root = inflater.inflate(fragmentMainRes, container, false);

        mRecyclerGrid = (RecyclerView) root.findViewById(R.id.poster_list_recycler_view);
        GridLayoutManager gridLayoutManager;
        if (mActivity.getOrientation() == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(mContext, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return mAdapter.getItemViewType(position) ==
                            PosterListRecyclerAdapter.TYPE_POSTER_WIDE ? 2 : 1;
                }
            });
        } else {
            gridLayoutManager = new GridLayoutManager(mContext, 4);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return mAdapter.getItemViewType(position) ==
                            PosterListRecyclerAdapter.TYPE_POSTER_WIDE ? 2 : 1;
                }
            });
        }
        mRecyclerGrid.setLayoutManager(gridLayoutManager);
        Log.d(LOG_TAG, "LOCALE: " + Locale.getDefault());

        return root;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mAdapter = new ImageAdapter(mContext, R.layout.list_item_poster, R.id.poster_view);

        if (savedInstanceState != null) {
            mMovieListBundle = savedInstanceState.getParcelable(MOVIE_LIST);
//            mAdapter.setItems(mMovieListBundle);
            mAdapter = new PosterListRecyclerAdapter(mMovieListBundle, this);
//            mGrid.setAdapter(mAdapter);
            mRecyclerGrid.setAdapter(mAdapter);
        } else {
            fetchMovieList(FetchMovieTask.MOST_POPULAR, 1);
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

    public void fetchMovieList(int sortBy, int pageNum) {
        Bundle params = new Bundle();
        params.putInt(FetchMovieTask.SORT_KEY, sortBy);
        params.putInt(FetchMovieTask.PAGE_KEY, pageNum);
        mIsFavouriteViewActive = false;
        mCurrentSort = sortBy;
        final MainFragment mainFragment = this;
        task = new FetchMovieTask();
        task.getMovieListObservable(FetchMovieTask.FETCH_MOVIE_LIST, params, mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieHandler>() {
                    @Override
                    public void accept(@NonNull MovieHandler movieHandler) throws Exception {
                        if (movieHandler == null) {
                            Snackbar.make(getView(), "Unable to fetch movie data. Try again later.",
                                    Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        mMovieHandler = movieHandler;
                        mMovieListBundle = ((MovieListData) mMovieHandler).getMovieDataBundle();
//                mAdapter.setItems(mMovieListBundle);
                        mAdapter = new PosterListRecyclerAdapter(mMovieListBundle, mainFragment);
                        mRecyclerGrid.setAdapter(mAdapter);
//                mGrid.setAdapter(mAdapter);
                        if (mActivity.isTabletMode()) {
//                    mGrid.performItemClick(mAdapter.getView(0,null,null),0,mAdapter.getItemId(0));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(LOG_TAG, "onError RX: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(LOG_TAG, "onComplete RX");
                    }
                });
    }

    public void fetchFavourites() {
        mMovieListBundle = Utilities.getFavouritesDataList(mContext);
        if (!mMovieListBundle.isEmpty()) {
            mAdapter = new PosterListRecyclerAdapter(mMovieListBundle, this);
            mRecyclerGrid.setAdapter(mAdapter);
            if (mActivity.isTabletMode()) {
//                mGrid.performItemClick(mAdapter.getView(0,null,null),0,mAdapter.getItemId(0));
            }
            mIsFavouriteViewActive = true;
            return;
        }
//        mGrid.setAdapter(new ImageAdapter(mContext, R.layout.list_item_poster, R.id.poster_view));
        Log.d(LOG_TAG, "No favourites rows were found.");
    }

    public void showPageSwitcher(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.page_switcher_popup);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        final int currentPage = mMovieListBundle.getInt(MovieData.CURRENT_PAGE);
        final int totalPages = mMovieListBundle.getInt(MovieData.TOTAL_PAGES);
        final TextView pageCountView = (TextView) dialog.findViewById(R.id.page_count);
        pageCountView.setText(currentPage + " of " + totalPages);
        ImageButton previous = (ImageButton) dialog.findViewById(R.id.previous_page_button);
        ImageButton next = (ImageButton) dialog.findViewById(R.id.next_page_button);
        final ImageButton clear = (ImageButton) dialog.findViewById(R.id.clear_button);
        final EditText editText = (EditText) dialog.findViewById(R.id.page_editText);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "Previous Page", Toast.LENGTH_SHORT).show();
                int currentPage = mMovieListBundle.getInt(MovieData.CURRENT_PAGE);
                if (currentPage > 1) {
                    fetchMovieList(mCurrentSort, currentPage - 1);
                    pageCountView.setText(currentPage - 1 + " of " + totalPages);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "Next Page", Toast.LENGTH_SHORT).show();
                int currentPage = mMovieListBundle.getInt(MovieData.CURRENT_PAGE);
                if (currentPage < totalPages) {
                    fetchMovieList(mCurrentSort, currentPage + 1);
                    pageCountView.setText(currentPage + 1 + " of " + totalPages);
                }
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    clear.setVisibility(View.VISIBLE);
                } else {
                    clear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                String pageStr = textView.getText().toString();
                int pageNum = !pageStr.isEmpty() ? Integer.parseInt(pageStr) : 0;
                if (actionId == EditorInfo.IME_ACTION_GO && pageNum > 0 && pageNum != currentPage
                        && pageNum <= totalPages) {
                    fetchMovieList(mCurrentSort, pageNum);
                    pageCountView.setText(pageNum + " of " + totalPages);
                    return true;
                }
                return false;
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.getText().clear();
            }
        });
        dialog.show();
    }

    public void setIntentDataFromDb(String movieId) {
        Bundle data = Utilities.getFavouriteMovie(mContext, movieId);
        Intent intent;
        if (data.isEmpty()) {
            return;
        }
        if (mActivity.isTabletMode()) {
            intent = mActivity.getIntent();
        } else {
            intent = new Intent(mContext, MovieDetailActivity.class);
        }

        intent.putExtra(MovieData.DETAILS_PARAM, data.getBundle(MovieData.DETAILS_PARAM));
        intent.putExtra(MovieData.TRAILERS_PARAM, data.getBundle(MovieData.TRAILERS_PARAM));
        intent.putExtra(MovieData.REVIEWS_PARAM, data.getBundle(MovieData.REVIEWS_PARAM));
        intent.putExtra(MovieData.CAST_PARAM, data.getBundle(MovieData.CAST_PARAM));
        intent.putExtra(MovieData.CREW_PARAM, data.getBundle(MovieData.CREW_PARAM));

        if (!mActivity.isTabletMode()) {
            mContext.startActivity(intent);
        } else {
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(LOAD_MOVIE_LIST_FINISHED));
        }
    }

    public void setIntentDataFromTask(String movieId) {
        Bundle params = new Bundle();
        params.putString(FetchMovieTask.MOVIE_ID_KEY, movieId);
        task = new FetchMovieTask();
        task.getMovieListObservable(FetchMovieTask.FETCH_MOVIE, params, mContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<MovieHandler>() {
                    @Override
                    public void accept(@NonNull MovieHandler movieHandler) throws Exception {
                        if (movieHandler == null) {
//                    Toast.makeText(mContext, "Unable to fetch movie data. Try again later.",
//                            Toast.LENGTH_LONG).show();
                            Snackbar.make(getView(), "Unable to fetch movie data. Try again later.",
                                    Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        MovieData movieData = (MovieData) movieHandler;
                        Intent intent;
                        if (mActivity.isTabletMode()) {
                            intent = mActivity.getIntent();
                        } else {
                            intent = new Intent(mContext, MovieDetailActivity.class);
                        }

                        intent.putExtra(MovieData.DETAILS_PARAM, movieData.getDetailBundle());
                        intent.putExtra(MovieData.TRAILERS_PARAM, movieData.getTrailerBundle());
                        intent.putExtra(MovieData.REVIEWS_PARAM, movieData.getReviewBundle());
                        intent.putExtra(MovieData.CAST_PARAM, movieData.getCastBundle());
                        intent.putExtra(MovieData.CREW_PARAM, movieData.getCrewBundle());

                        if (!mActivity.isTabletMode()) {
                            mContext.startActivity(intent);
                        } else {
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(LOAD_MOVIE_LIST_FINISHED));
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.e(LOG_TAG, "onError RX: " + throwable.getMessage());
                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(LOG_TAG, "onCompleteDetail RX");
                    }
                });
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
