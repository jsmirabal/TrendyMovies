package japps.trendymovies.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import japps.trendymovies.R;
import japps.trendymovies.activity.MovieDetailActivity;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.utility.Utils;

/**
 * Created by Julio on 9/7/2016.
 */
public class MovieDetailsFragment extends Fragment {

    private Context mContext;
    private final String LOG_TAG = MovieDetailsFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        mContext = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        Intent intent = ((MovieDetailActivity) mContext).getIntent();
        Bundle extras = intent.getExtras();

        ViewHolder holder = new ViewHolder(rootView);
        holder.genreValueView.setText(Utils.formatGenres(
                extras.getBundle(MovieData.GENRES_PARAM).getStringArrayList(MovieData.GENRES_PARAM)
        ));
        holder.releaseDateValueView.setText(Utils.formatDate(extras.getString(MovieData.RELEASE_DATE_PARAM)));
        holder.popularityValueView.setText(Utils.formatPopularity(extras.getDouble(MovieData.POPULARITY_PARAM)));
        holder.runtimeValueView.setText(Utils.formatRuntime(extras.getInt(MovieData.RUNTIME_PARAM)));
        holder.originalLangValueView.setText(Utils.formatLanguage(extras.getString(MovieData.ORIGINAL_LANG_PARAM)));
        holder.originalTitleValueView.setText(extras.getString(MovieData.ORIGINAL_TITLE_PARAM));
        holder.budgetValueView.setText(Utils.formatCurrency(extras.getLong(MovieData.BUDGET_PARAM)));
        holder.revenueValueView.setText(Utils.formatCurrency(extras.getLong(MovieData.REVENUE_PARAM)));
        holder.rankingValueView.setText(Double.toString(extras.getDouble(MovieData.RATE_PARAM)));
        holder.votesValueView.setText(extras.getString(MovieData.VOTES_PARAM));
        return rootView;
    }

    public static class ViewHolder {
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
