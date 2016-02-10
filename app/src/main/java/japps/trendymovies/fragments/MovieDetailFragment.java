package japps.trendymovies.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apmem.tools.layouts.FlowLayout;

import java.util.HashMap;
import java.util.Map;

import japps.trendymovies.R;

/**
 * Created by Julio on 28/1/2016.
 */
public class MovieDetailFragment extends Fragment {

    private Context mContext;
    private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    private final int FRAGMENT_MOVIE_DETAIL_RES = R.layout.fragment_movie_detail;
    private final String[] TRAILER_LABELS_LIST = {"Trailer 1", "Trailer 2", "Trailer 3"};

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {return null;}
        View rootView = inflater.inflate(FRAGMENT_MOVIE_DETAIL_RES,container,false);
        mContext = getActivity();
        setTextViewScrolling(rootView);
        //setMovieData(rootView);
        addTrailerButtons(rootView, inflater, container);
        return rootView;
    }

    private void setMovieData (View rootView){
        TextView titleView = (TextView) rootView.findViewById(R.id.movie_detail_title);
        TextView statisticsView = (TextView) rootView.findViewById(R.id.movie_detail_year_length_rate);
        TextView synopsisView = (TextView) rootView.findViewById(R.id.movie_detail_synopsis);
        ImageView posterImg = (ImageView) rootView.findViewById(R.id.movie_detail_poster_image);
        String movieName = getActivity().getIntent().getExtras().getString("movie_name");
        switch (movieName){
            case "sherlock_holmes":{
                titleView.setText(getString(R.string.example_movie_title_1));
                statisticsView.setText(getString(R.string.example_movie_details_1));
                synopsisView.setText(getString(R.string.example_movie_synopsis_1));
                //posterImg.setImageResource(R.drawable.sherlock);
                Picasso.with(mContext).load(R.drawable.sherlock).into(posterImg);
            }
            break;
            case "interstellar":{
                titleView.setText(getString(R.string.example_movie_title_2));
                statisticsView.setText(getString(R.string.example_movie_details_2));
                synopsisView.setText(getString(R.string.example_movie_synopsis_2));
                //posterImg.setImageResource(R.drawable.interstellar);
                Picasso.with(mContext).load(R.drawable.interstellar).into(posterImg);
            }
            break;
            case "batman":{
                titleView.setText(getString(R.string.example_movie_title_3));
                statisticsView.setText(getString(R.string.example_movie_details_3));
                synopsisView.setText(getString(R.string.example_movie_synopsis_3));
//                posterImg.setImageResource(R.drawable.the_dark_knight);
                Picasso.with(mContext).load(R.drawable.the_dark_knight).into(posterImg);
            }
            break;
            default:{
                titleView.setText("Not matched movie");
                statisticsView.setText("Not matched movie");
                synopsisView.setText("Not matched movie");
            }
        }
    }

    private void addTrailerButtons(View rootView, LayoutInflater inflater, ViewGroup container) {
        TextView label;
        ImageButton button;
        ViewGroup trailerButtonsLayout;
        FlowLayout movieDetailTrailersLayout = (FlowLayout) rootView.findViewById(R.id.movie_detail_trailer_layout);
        final Map<Integer,String> link = new HashMap<>();
        int count = 1;

        if (movieDetailTrailersLayout == null){
            Log.d(LOG_TAG,"Error: MovieDetailTrailersLayout is null");
            return;
        }

        for (String labelName : TRAILER_LABELS_LIST){
            trailerButtonsLayout = (RelativeLayout)inflater.inflate(R.layout.trailer_button_movie_detail, container, false);

            label = (TextView) trailerButtonsLayout.findViewWithTag(getString(R.string.trailer_label_tag));
            label.setText(labelName);

            button = (ImageButton) trailerButtonsLayout.findViewWithTag(getString(R.string.trailer_button_tag));
            button.setId(R.id.trailer_button + count);
            link.put(R.id.trailer_button + count,labelName);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String value = link.get(v.getId());
                    Toast.makeText(mContext,"TAG: "+value, Toast.LENGTH_LONG).show();
                }
            });

            movieDetailTrailersLayout.addView(trailerButtonsLayout);
            count++;

        }

    }

    public void setTextViewScrolling(View rootView) {
        TextView movie_desc_textView = (TextView) rootView.findViewById(R.id.movie_detail_synopsis);
        movie_desc_textView.setMovementMethod(new ScrollingMovementMethod());
    }
    
}
