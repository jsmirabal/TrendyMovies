package japps.trendymovies.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import japps.trendymovies.R;

/**
 * Created by Julio on 9/7/2016.
 */
public class MovieReviewFragment extends Fragment {

    private Context mContext;
    private final String LOG_TAG = MovieReviewFragment.class.getSimpleName();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        mContext = getActivity();
        View rootView = inflater.inflate(R.layout.fragment_movie_review, container, false);

        return rootView;
    }
}
