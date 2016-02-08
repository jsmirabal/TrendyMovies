package japps.trendymovies.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import japps.trendymovies.R;
import japps.trendymovies.activities.MovieDetailActivity;

/**
 * Created by Julio on 21/1/2016.
 */
public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {
    private BaseAdapter mAdapter;
    private final String LOG_TAG = MainFragment.class.getSimpleName();
    private Map<Integer,String> mapMovieTitles;
    public MainFragment() {
        Log.d(LOG_TAG, "IN!");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int fragmentMainRes = R.layout.fragment_main;
        final int sherlockImg = R.drawable.sherlock;
        final int interstellarImg = R.drawable.interstellar;
        final int batmanImg = R.drawable.the_dark_knight;
        final int imageViewId = R.id.poster_view;
        final int gridItemRes = R.layout.list_item_poster;
        final Object[] posterList = {sherlockImg, interstellarImg, batmanImg, interstellarImg,
                sherlockImg, batmanImg, sherlockImg, interstellarImg};
        View root = inflater.inflate(fragmentMainRes, container, false);
        mAdapter = new ArrayAdapter(getActivity(), fragmentMainRes, imageViewId, posterList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return createViewFromResource(inflater, posterList, position, parent,
                        gridItemRes, imageViewId);
            }
        };
        GridView gridView = (GridView) root.findViewById(R.id.gridView);
        gridView.setAdapter(mAdapter);
        gridView.setOnItemClickListener(this);
        mapMovieTitles = new HashMap<>();
        return root;
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
            setMapData(items,position);
        } else {
            throw new IllegalArgumentException("(Object[] items) must be instance of Integer");
        }

        return imageView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(),MovieDetailActivity.class);
        intent.putExtra("movie_name", mapMovieTitles.get(position));
        startActivity(intent);
    }

    private void setMapData(Object[] items, int position){
        final int sherlockImg = R.drawable.sherlock;
        final int interstellarImg = R.drawable.interstellar;
        final int batmanImg = R.drawable.the_dark_knight;
        int objectValue = (Integer)items[position];
        switch (objectValue){
            case sherlockImg:{
                mapMovieTitles.put(position,"sherlock_holmes");
            }
            break;
            case interstellarImg:{
                mapMovieTitles.put(position,"interstellar");
            }
            break;
            case batmanImg:{
                mapMovieTitles.put(position,"batman");
            }

        }
    }
}
