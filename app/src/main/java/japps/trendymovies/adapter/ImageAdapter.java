package japps.trendymovies.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import japps.trendymovies.R;
import japps.trendymovies.data.MovieData;

/**
 * Created by Julio on 10/2/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private int mImageViewId;
    private Bundle mItems;

    public ImageAdapter(Context context, int layout, int imageViewId) {
        mContext = context;
        mImageViewId = imageViewId;
        mLayout = layout;
    }

    public void setItems(Bundle items) {
        mItems = items;
    }

    @Override
    public int getCount() {
        if (mItems == null){
            return 0;
        }
        return mItems.getInt(MovieData.MOVIE_LIST_COUNT);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        View view;
        view = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
        imageView = (ImageView) view.findViewById(mImageViewId);
        ArrayList<String> movieIdList = mItems.getStringArrayList(MovieData.ID_PARAM);
        ArrayList<String> posterPathList = mItems.getStringArrayList(MovieData.POSTER_PATH_PARAM);
        ArrayList<byte[]> posterBlobList = (ArrayList<byte[]>) mItems.getSerializable(MovieData.POSTER_BLOB_PARAM);
        if (movieIdList != null && !movieIdList.isEmpty()) {
            String movieId = movieIdList.get(position);
            view.setTag(movieId);
            String imgPath = posterPathList != null ? posterPathList.get(position) : "";
            byte[] imgBlob = posterBlobList != null ? posterBlobList.get(position) : null;
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            if (imgBlob == null) {
                progressBar.setVisibility(ProgressBar.VISIBLE);
                Picasso.with(mContext).load(imgPath).into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(ProgressBar.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });
            } else {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(imgBlob);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            }
        } else {
            throw new IllegalArgumentException("There are not items to process");
        }

        return view;
    }
}
