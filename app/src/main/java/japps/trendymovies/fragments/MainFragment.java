package japps.trendymovies.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.Map;

import japps.trendymovies.R;
import japps.trendymovies.activities.MovieDetailActivity;
import japps.trendymovies.data.ImageAdapter;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.tasks.FetchMovieTask;

/**
 * Created by Julio on 21/1/2016.
 */
public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ImageAdapter mAdapter;
    private GridView mGrid;
    private Context mContext;
    private MovieData mMovies;
    private final String LOG_TAG = MainFragment.class.getSimpleName();
    private Map<Integer,String> mapMovieTitles;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int fragmentMainRes = R.layout.fragment_main;
        final int imageViewId = R.id.poster_view;
        final int gridItemRes = R.layout.list_item_poster;

        View root = inflater.inflate(fragmentMainRes, container, false);

        mAdapter = new ImageAdapter(mContext,gridItemRes,imageViewId);

        mGrid = (GridView) root.findViewById(R.id.gridView);
        mGrid.setOnItemClickListener(this);

        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(mContext,MovieDetailActivity.class);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        FetchMovieTask task = new FetchMovieTask(){
            @Override
            protected void onPostExecute(MovieData data) {
                super.onPostExecute(data);
                mMovies = data;
                mAdapter.setItems(data.getMoviePosterPathList());
                mGrid.setAdapter(mAdapter);
            }
        };
        task.execute();
    }
}
