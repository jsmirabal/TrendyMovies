package japps.trendymovies.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import japps.trendymovies.R;
import japps.trendymovies.activity.MainActivity;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.fragment.MainFragment;
import japps.trendymovies.utility.Utilities;

/**
 * Created by Julio on 11/9/2016.
 */
public class PosterListRecyclerAdapter extends RecyclerView.Adapter<PosterListRecyclerAdapter.ViewHolder> {
    private Bundle mData;
    private MainFragment mFragment;
    public static final int TYPE_POSTER_NORMAL = 100;
    public static final int TYPE_POSTER_WIDE = 101;

    public PosterListRecyclerAdapter(Bundle data, MainFragment mainFragment) {
        mData = data;
        mFragment = mainFragment;
    }

    @Override
    public PosterListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_POSTER_WIDE){
         return new ViewHolder(LayoutInflater.from(parent.getContext())
                 .inflate(R.layout.list_item_poster_wide, parent, false));
        } else {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_poster, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        MainActivity activity = (MainActivity) mFragment.getActivity();
        if (activity.getOrientation() == Configuration.ORIENTATION_PORTRAIT){
            return position % 3 == 0 || position == 19 ? TYPE_POSTER_WIDE : TYPE_POSTER_NORMAL;
        } else {
            return position % 5 == 0 ? TYPE_POSTER_WIDE : TYPE_POSTER_NORMAL;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(final PosterListRecyclerAdapter.ViewHolder holder, int position) {
        ArrayList<String> movieIdList = mData.getStringArrayList(MovieData.ID_PARAM);
        ArrayList<String> posterPathList = mData.getStringArrayList(MovieData.POSTER_PATH_PARAM);
        ArrayList<String> backdropPathList = mData.getStringArrayList(MovieData.BACKDROP_PATH_PARAM);
        ArrayList<String> releaseDateList = mData.getStringArrayList(MovieData.RELEASE_DATE_PARAM);
        ArrayList<String> titleList = mData.getStringArrayList(MovieData.TITLE_PARAM);
        ArrayList<Double> popularityList = (ArrayList<Double>) mData.getSerializable(MovieData.POPULARITY_PARAM);
        ArrayList<Double> rateList = (ArrayList<Double>) mData.getSerializable(MovieData.RATE_PARAM);
        ArrayList<byte[]> posterBlobList = (ArrayList<byte[]>) mData.getSerializable(MovieData.POSTER_BLOB_PARAM);
        if (movieIdList != null && !movieIdList.isEmpty()) {
            String movieId = movieIdList.get(position);
            holder.mPosterView.setTag(movieId);
            String posterPath = posterPathList != null ? posterPathList.get(position) : "";
            String backdropPath = backdropPathList != null ? backdropPathList.get(position) : "";
            String date = releaseDateList != null ? releaseDateList.get(position) : "";
            String title = titleList != null ? titleList.get(position) : "";
            Double popularity = popularityList != null ? popularityList.get(position) : 0.0;
            Double rating = rateList != null ? rateList.get(position) : 0.0;
            byte[] imgBlob = posterBlobList != null ? posterBlobList.get(position) : null;

            holder.mPopularity.setText(Utilities.formatPopularity(popularity));
            if (getItemViewType(position) == TYPE_POSTER_WIDE){
                holder.mDate.setText(title+" ("+Utilities.getYear(date)+")");
            } else {
                holder.mDate.setText(Utilities.getYear(date));
            }
            holder.mRating.setText(Utilities.formatRating(rating));
            if (imgBlob == null) {
                holder.mProgressBar.setVisibility(ProgressBar.VISIBLE);

                MainActivity activity = (MainActivity) mFragment.getActivity();
                if (activity.getOrientation() == Configuration.ORIENTATION_PORTRAIT){
                    posterPath = position % 3 == 0 || position == 19 ? backdropPath : posterPath;
                } else {
                    posterPath = position % 5 == 0 ? backdropPath : posterPath;
                }
                Picasso.with(holder.mContext).load(posterPath).into(holder.mPosterView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.mProgressBar.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onError() {
                    }
                });
            } else {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imgBlob);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                holder.mPosterView.setImageBitmap(bitmap);
            }
        } else {
            throw new IllegalArgumentException("There are not items to process");
        }

        holder.mPosterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String movieId = view.getTag().toString();
                if(!mFragment.isFavouriteViewActive()){
                    mFragment.setIntentDataFromTask(movieId);
                } else {
                    mFragment.setIntentDataFromDb(movieId);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.getInt(MovieData.MOVIE_LIST_COUNT);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ImageButton.OnClickListener, PopupMenu.OnMenuItemClickListener {
        public TextView mPopularity, mRating, mDate;
        public ImageButton mMenuButton, mFavourite, mShare;
        public ImageView mPosterView;
        public ProgressBar mProgressBar;
        private Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);
            mContext = itemView.getContext();
            mPosterView = (ImageView) itemView.findViewById(R.id.poster_view);
            mPopularity = (TextView) itemView.findViewById(R.id.popularity_poster_item);
            mRating = (TextView) itemView.findViewById(R.id.rating_poster_item);
            mDate = (TextView) itemView.findViewById(R.id.date_poster_item);
            mMenuButton = (ImageButton) itemView.findViewById(R.id.menu_button_poster_item);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
            mFavourite = (ImageButton) itemView.findViewById(R.id.wide_fav_button);
            mShare = (ImageButton) itemView.findViewById(R.id.wide_share_button);

            if (mMenuButton != null){
                mMenuButton.setOnClickListener(this);
            }

            if (mFavourite != null){
                mFavourite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "FAV", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if (mShare != null){
                mShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mContext, "Share", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public void onClick(View view) {
            PopupMenu popup = new PopupMenu(mContext, view);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.poster_item_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(this);
            popup.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.poster_item_action_fav:
                    Toast.makeText(mContext, "FAV", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.poster_item_action_share:
                    Toast.makeText(mContext, "Share", Toast.LENGTH_SHORT).show();
                    return true;
                default: return false;
            }
        }
    }
}
