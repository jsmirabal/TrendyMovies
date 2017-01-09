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
    private ArrayList<String> movieBackdropPathList;
    private ArrayList<String> movieReleaseDateList;
    private ArrayList<String> movieTitleList;
    private ArrayList<Double> moviePopularityList;
    private ArrayList<Double> movieRateList;
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
        int currentPage = mJsonData.getInt("page");
        int totalPages = mJsonData.getInt("total_pages");
        movieDataBundle = new Bundle();
        movieIdList = new ArrayList<>();
        moviePosterPathList = new ArrayList<>();
        movieBackdropPathList = new ArrayList<>();
        moviePopularityList = new ArrayList<>();
        movieTitleList = new ArrayList<>();
        movieRateList = new ArrayList<>();
        movieReleaseDateList = new ArrayList<>();
        for (int j = 0; j < resultsArray.length(); j++){
            String posterPath = MovieData.BASEPATH_W342+resultsArray.getJSONObject(j)
                    .getString(MovieData.POSTER_PATH_PARAM);
            String backdropPath = MovieData.BASEPATH_W500+resultsArray.getJSONObject(j)
                    .getString(MovieData.BACKDROP_PATH_PARAM);
            movieIdList.add(resultsArray.getJSONObject(j).getString(MovieData.ID_PARAM));
            moviePosterPathList.add(posterPath);
            movieBackdropPathList.add(backdropPath);
            moviePopularityList.add(resultsArray.getJSONObject(j).getDouble(MovieData.POPULARITY_PARAM));
            movieRateList.add(resultsArray.getJSONObject(j).getDouble(MovieData.RATE_PARAM));
            movieReleaseDateList.add(resultsArray.getJSONObject(j).getString(MovieData.RELEASE_DATE_PARAM));
            movieTitleList.add(resultsArray.getJSONObject(j).getString(MovieData.TITLE_PARAM));

        }
        movieDataBundle.putStringArrayList(MovieData.ID_PARAM, movieIdList);
        movieDataBundle.putStringArrayList(MovieData.POSTER_PATH_PARAM, moviePosterPathList);
        movieDataBundle.putStringArrayList(MovieData.BACKDROP_PATH_PARAM, movieBackdropPathList);
        movieDataBundle.putStringArrayList(MovieData.RELEASE_DATE_PARAM, movieReleaseDateList);
        movieDataBundle.putStringArrayList(MovieData.TITLE_PARAM, movieTitleList);
        movieDataBundle.putSerializable(MovieData.POPULARITY_PARAM, moviePopularityList);
        movieDataBundle.putSerializable(MovieData.RATE_PARAM, movieRateList);
        movieDataBundle.putSerializable(MovieData.POSTER_BLOB_PARAM, null);
        movieDataBundle.putInt(MovieData.MOVIE_LIST_COUNT, resultsArray.length());
        movieDataBundle.putInt(MovieData.CURRENT_PAGE, currentPage);
        movieDataBundle.putInt(MovieData.TOTAL_PAGES, totalPages);
    }
}
