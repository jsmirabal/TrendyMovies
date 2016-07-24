package japps.trendymovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import japps.trendymovies.R;

/**
 * Created by Julio on 10/2/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private int mImageViewId;
    private List<String> mItems;

    public ImageAdapter(Context context, int layout, int imageViewId) {
        mContext = context;
        mImageViewId = imageViewId;
        mLayout = layout;
    }

    public void setItems(List<String> items) {
        mItems = items;
    }

    @Override
    public int getCount() {
        if (mItems == null){
            return 0;
        }
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        View view;
        view = LayoutInflater.from(mContext).inflate(mLayout, parent, false);
        imageView = (ImageView) view.findViewById(mImageViewId);
        if (mItems.get(position) != null) {
            String imgPath = mItems.get(position);
            final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
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
            throw new IllegalArgumentException("There are not items to process");
        }

        return view;
    }
}
