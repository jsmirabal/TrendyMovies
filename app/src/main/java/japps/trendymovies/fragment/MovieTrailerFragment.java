package japps.trendymovies.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import japps.trendymovies.R;
import japps.trendymovies.activity.MainActivity;
import japps.trendymovies.activity.MovieDetailActivity;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.utility.Utilities;

/**
 * Created by Julio on 5/7/2016.
 */
public class MovieTrailerFragment extends Fragment {
    private Context mContext;
    private ListView mListView;
    private TrailerListAdapter mAdapter;
    private final String LOG_TAG = MovieTrailerFragment.class.getSimpleName();
    private Bundle mExtras;
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
        if (mContext instanceof MainActivity) {
            mExtras = ((MainActivity) mContext).getIntent().getExtras();
        } else {
            mExtras = ((MovieDetailActivity) mContext).getIntent().getExtras();
        }
        Bundle trailerBundle = mExtras.getBundle(MovieData.TRAILERS_PARAM);

        int trailersCount = trailerBundle != null ? trailerBundle.getInt(MovieData.TRAILERS_COUNT) : 0;
        if (trailersCount == 0) {
            return inflater.inflate(R.layout.no_trailer, container, false);
        }
        View rootView = inflater.inflate(R.layout.fragment_movie_trailer, container, false);
        View listItemView = inflater.inflate(R.layout.list_item_trailer, container, false);
        mAdapter = new TrailerListAdapter(listItemView, mContext, trailerBundle);
        mListView = (ListView) rootView.findViewById(R.id.trailer_list_view);
        mListView.setAdapter(mAdapter);

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

    public static class TrailerListAdapter extends BaseAdapter {
        private int mTrailerCount;
        private Context mContext;
        private View mListItemView;
        private Bundle mTrailerBundle;

        public TrailerListAdapter(View itemView, Context context, Bundle trailerBundle) {
            mListItemView = itemView;
            mContext = context;
            mTrailerBundle = trailerBundle;
            mTrailerCount = trailerBundle != null ? trailerBundle.getInt(MovieData.TRAILERS_COUNT) : 0;
        }

        @Override
        public int getCount() {
            return mTrailerCount;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ArrayList<String> listTitle = mTrailerBundle.getStringArrayList(MovieData.TRAILER_TITLE_PARAM);
            ArrayList<String> listPath = mTrailerBundle.getStringArrayList(MovieData.TRAILER_SOURCE_PARAM);
            ArrayList<String> listThumbnailPath = mTrailerBundle.getStringArrayList(MovieData.TRAILER_THUMBNAIL_PARAM);
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_trailer, viewGroup, false);
            if ((listTitle != null && !listTitle.isEmpty()) &&
                    (listPath != null && !listPath.isEmpty()) &&
                    (listThumbnailPath != null && !listThumbnailPath.isEmpty())) {
                String trailerTitle = listTitle.get(position);
               final String trailerPath = listPath.get(position);
                String trailerThumbnailPath = listThumbnailPath.get(position);

                ImageView thumbnailView = (ImageView) view.findViewById(R.id.thumbnail_view);
                TextView trailerTitleView = (TextView) view.findViewById(R.id.trailer_title_view);

                final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
                final Button button = (Button) view.findViewById(R.id.trailer_button);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!startTrailerVideo(trailerPath)){
                            Snackbar.make(view, "No app to handle", Snackbar.LENGTH_LONG).show();
                        }
                    }
                });

                Picasso.with(mContext).load(trailerThumbnailPath).into(thumbnailView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(ProgressBar.GONE);
                        button.setVisibility(Button.VISIBLE);
                    }

                    @Override
                    public void onError() {

                    }
                });
                trailerTitleView.setText(trailerTitle);
            }
            return view;
        }

        private boolean startTrailerVideo(String path){
            Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse(path));
            PackageManager manager = mContext.getPackageManager();
            List<ResolveInfo> infos = manager.queryIntentActivities(intent, 0);
            if (infos.size() > 0) {
                mContext.startActivity(intent);
                return true;
            }else{
                return false;
            }
        }
    }
}
