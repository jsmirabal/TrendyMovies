package japps.trendymovies.data;

import android.content.Context;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Julio on 9/2/2016.
 */
public class MovieListData implements MovieHandler{
    private JSONObject mJsonData;

    private ArrayList<String> movieIdList;
    private ArrayList<String> moviePosterPathList;
    private Bundle movieDataBundle;
    private Context mContext;

    public MovieListData(Context context, JSONObject jsonData) throws JSONException {
        if (context != null && jsonData != null) {
            mContext = context;
            mJsonData = jsonData;
            setData();
        }
    }

    public ArrayList<String> getMovieIdList() {
        return movieIdList;
    }

    public ArrayList<String> getMoviePosterPathList() {
        return moviePosterPathList;
    }

    public Bundle getMovieDataBundle(){return movieDataBundle;}

    private void setData() throws JSONException {
        JSONArray resultsArray = mJsonData.getJSONArray("results");
        movieDataBundle = new Bundle();
        movieIdList = new ArrayList<>();
        moviePosterPathList = new ArrayList<>();
        for (int j = 0; j < resultsArray.length(); j++){
            String posterPath = MovieData.BASEPATH_W342+resultsArray.getJSONObject(j)
                    .getString(MovieData.POSTER_PATH_PARAM);
            movieIdList.add(resultsArray.getJSONObject(j).getString(MovieData.ID_PARAM));
            moviePosterPathList.add(posterPath);
        }
        movieDataBundle.putStringArrayList(MovieData.ID_PARAM, movieIdList);
        movieDataBundle.putStringArrayList(MovieData.POSTER_PATH_PARAM, moviePosterPathList);
        movieDataBundle.putSerializable(MovieData.POSTER_BLOB_PARAM, null);
        movieDataBundle.putInt(MovieData.MOVIE_LIST_COUNT, resultsArray.length());
    }
}
