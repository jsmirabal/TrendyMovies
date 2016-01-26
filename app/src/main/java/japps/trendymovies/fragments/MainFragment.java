package japps.trendymovies.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import japps.trendymovies.R;

/**
 * Created by Julio on 21/1/2016.
 */
public class MainFragment extends Fragment implements OnNavigationItemSelectedListener {
    private BaseAdapter mAdapter;
    private final String LOG_TAG = MainFragment.class.getSimpleName();

    public MainFragment() {
        Log.d(LOG_TAG, "IN!");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int fragmentMainRes = R.layout.fragment_main;
        final int imageRes = R.drawable.sherlock;
        final int imageViewId = R.id.poster_view;
        final int listItemViewRes = R.layout.list_item_poster;
        final Object[] posterList = {imageRes, imageRes, imageRes, imageRes, imageRes, imageRes, imageRes, imageRes};
        View root = inflater.inflate(fragmentMainRes, container, false);
        mAdapter = new ArrayAdapter(getActivity(), fragmentMainRes, imageViewId, posterList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return createViewFromResource(inflater, posterList, position, parent,
                        listItemViewRes, imageViewId);
            }
        };
        GridView gridView = (GridView) root.findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);
        return root;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        return false;
    }

    private View createViewFromResource(LayoutInflater inflater, Object[] items, int position,
                                        ViewGroup parent, int layout, int viewId) {

        ImageView imageView;
        View view;
        view = inflater.inflate(layout, parent, false);
        imageView = (ImageView) view.findViewById(viewId);

        if (items[position] instanceof Integer) {
            int imgRes = (int) items[position];
            imageView.setImageResource(imgRes);
        } else {
            throw new IllegalArgumentException("(Object[] items) must be instance of Integer");
        }

        return imageView;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int[] imageIDs;

        public ImageAdapter(Context c, int[] ids) {
            context = c;
            imageIDs = ids;
        }

        //---returns the number of images---
        public int getCount() {
            return imageIDs.length;
        }

        //---returns the ID of an item---
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        //---returns an ImageView view---
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(5, 5, 5, 5);
            } else {
                imageView = (ImageView) convertView;
            }
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(imageIDs[position]);
                }
            });
            t.start();
            return imageView;
        }
    }
}
