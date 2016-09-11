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
import android.widget.TextView;

import japps.trendymovies.R;
import japps.trendymovies.activity.MainActivity;
import japps.trendymovies.activity.MovieDetailActivity;
import japps.trendymovies.adapter.PersonRecyclerAdapter;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.utility.Utilities;

/**
 * Created by Julio on 9/7/2016.
 */
public class MovieDetailsFragment extends Fragment {

    private Context mContext;
    private Bundle mExtras;
    private final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        if (mContext instanceof MainActivity) {
            mExtras = ((MainActivity) mContext).getIntent().getExtras();
        } else {
            mExtras = ((MovieDetailActivity) mContext).getIntent().getExtras();
        }
        Bundle detailBundle = mExtras.getBundle(MovieData.DETAILS_PARAM);
        ViewHolder holder = new ViewHolder(rootView);
        holder.genreValueView.setText(detailBundle.getString(MovieData.GENRES_PARAM));
        holder.releaseDateValueView.setText(Utilities.formatDate(detailBundle.getString(MovieData.RELEASE_DATE_PARAM)));
        holder.popularityValueView.setText(Utilities.formatPopularity(detailBundle.getDouble(MovieData.POPULARITY_PARAM)));
        holder.runtimeValueView.setText(Utilities.formatRuntime(detailBundle.getInt(MovieData.RUNTIME_PARAM)));
        holder.originalLangValueView.setText(Utilities.formatLanguage(detailBundle.getString(MovieData.ORIGINAL_LANG_PARAM)));
        holder.originalTitleValueView.setText(detailBundle.getString(MovieData.ORIGINAL_TITLE_PARAM));
        holder.budgetValueView.setText(Utilities.formatCurrency(detailBundle.getLong(MovieData.BUDGET_PARAM)));
        holder.revenueValueView.setText(Utilities.formatCurrency(detailBundle.getLong(MovieData.REVENUE_PARAM)));
        holder.rankingValueView.setText(Double.toString(detailBundle.getDouble(MovieData.RATE_PARAM)));
        holder.votesValueView.setText(detailBundle.getString(MovieData.VOTES_PARAM));

        RecyclerView castRecyclerView = (RecyclerView) rootView.findViewById(R.id.cast_recycler_view);
        RecyclerView crewRecyclerView = (RecyclerView) rootView.findViewById(R.id.crew_recycler_view);
        RecyclerView.LayoutManager castManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        RecyclerView.LayoutManager crewManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
        Bundle castBundle = mExtras.getBundle(MovieData.CAST_PARAM);
        Bundle crewBundle = mExtras.getBundle(MovieData.CREW_PARAM);
        PersonRecyclerAdapter castAdapter = new PersonRecyclerAdapter(castBundle);
        PersonRecyclerAdapter crewAdapter = new PersonRecyclerAdapter(crewBundle);
        castRecyclerView.setLayoutManager(castManager);
        castRecyclerView.setAdapter(castAdapter);
        crewRecyclerView.setLayoutManager(crewManager);
        crewRecyclerView.setAdapter(crewAdapter);
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

    public static class ViewHolder{
        public TextView genreValueView;
        public TextView releaseDateValueView;
        public TextView popularityValueView;
        public TextView runtimeValueView;
        public TextView originalLangValueView;
        public TextView originalTitleValueView;
        public TextView budgetValueView;
        public TextView revenueValueView;
        public TextView rankingValueView;
        public TextView votesValueView;

        public ViewHolder(View rootView){
            genreValueView = (TextView) rootView.findViewById(R.id.genres_value);
            releaseDateValueView = (TextView) rootView.findViewById(R.id.release_date_value);
            popularityValueView = (TextView) rootView.findViewById(R.id.popularity_value);
            runtimeValueView = (TextView) rootView.findViewById(R.id.runtime_value);
            originalLangValueView = (TextView) rootView.findViewById(R.id.original_lang_value);
            originalTitleValueView = (TextView) rootView.findViewById(R.id.original_title_value);
            budgetValueView = (TextView) rootView.findViewById(R.id.budget_value);
            revenueValueView = (TextView) rootView.findViewById(R.id.revenue_value);
            rankingValueView = (TextView) rootView.findViewById(R.id.ranking_value);
            votesValueView = (TextView) rootView.findViewById(R.id.votes_value);
        }
    }


}
