package japps.trendymovies.data;


import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieData implements MovieHandler {
    public static final String TITLE_PARAM = "title";
    public static final String ORIGINAL_TITLE_PARAM = "original_title";
    public static final String SYNOPSIS_PARAM = "overview";
    public static final String RELEASE_DATE_PARAM = "release_date";
    public static final String ID_PARAM = "id";
    public static final String IMDB_ID_PARAM = "imdb_id";
    public static final String RATE_PARAM = "vote_average";
    public static final String RUNTIME_PARAM = "runtime";
    public static final String POSTER_PATH_PARAM = "poster_path";
    public static final String BACKDROP_PATH_PARAM = "backdrop_path";
    public static final String BASEPATH_W154 = "http://image.tmdb.org/t/p/w154";
    public static final String BASEPATH_W185 = "http://image.tmdb.org/t/p/w185";
    public static final String BASEPATH_W342 = "http://image.tmdb.org/t/p/w342";
    public static final String BASEPATH_W500 = "http://image.tmdb.org/t/p/w500";
    private static final String TRAILER_THUMBNAIL_BASEPATH = "http://img.youtube.com/vi/";
    private static final String TRAILER_BASEPATH = "https://www.youtube.com/watch?v=";

    public static final String TRAILERS_PARAM = "trailers";
    public static final String TRAILER_THUMBNAIL_PARAM = "trailer_thumbnail";
    public static final String REVIEWS_PARAM = "reviews";
    public static final String TRAILER_SOURCE_PARAM = "source";
    public static final String TRAILER_NAME_PARAM = "name";
    public static final String TRAILERS_COUNT = "has_trailer";

    private final String RESULTS_PARAM = "results";
    private final String YOUTUBE_PARAM = "youtube";

    private JSONObject mJsonData;
    private String movieTitle;
    private String movieOriginalTitle;
    private String movieSynopsis;
    private String movieReleaseDate;
    private int movieId;
    private String imdbId;
    private double movieRate;
    private int movieRuntime;
    private String moviePosterPath;
    private String movieBackdropPath;
    private Bundle trailerBundle;
    private List<String> reviewBundle;

    public MovieData(JSONObject jsonData) throws JSONException {
        if (jsonData == null) {
            return;
        }
        mJsonData = jsonData;
        setData();
    }

    private void setData() throws JSONException {
        movieTitle = mJsonData.getString(TITLE_PARAM);
        movieOriginalTitle = mJsonData.getString(ORIGINAL_TITLE_PARAM);
        movieSynopsis = mJsonData.getString(SYNOPSIS_PARAM);
        movieReleaseDate = mJsonData.getString(RELEASE_DATE_PARAM);
        movieId = mJsonData.getInt(ID_PARAM);
        imdbId = mJsonData.getString(IMDB_ID_PARAM);
        movieRate = mJsonData.getDouble(RATE_PARAM);
        movieRuntime = mJsonData.getInt(RUNTIME_PARAM);
        moviePosterPath = mJsonData.getString(POSTER_PATH_PARAM);
        movieBackdropPath = mJsonData.getString(BACKDROP_PATH_PARAM);

        JSONArray trailers = mJsonData.getJSONObject(TRAILERS_PARAM).getJSONArray(YOUTUBE_PARAM);
        JSONArray reviews = mJsonData.getJSONObject(REVIEWS_PARAM).getJSONArray(RESULTS_PARAM);

        trailerBundle = new Bundle();
        trailerBundle.putInt(TRAILERS_COUNT, trailers.length());
        if (trailers.length() > 0) {
            ArrayList<String> nameList = new ArrayList<>();
            ArrayList<String> trailerPathList = new ArrayList<>();
            ArrayList<String> thumbnailPathList = new ArrayList<>();
            for (int j = 0; j < trailers.length(); j++) {
                String name = trailers.getJSONObject(j).getString(TRAILER_NAME_PARAM);
                String videoId = trailers.getJSONObject(j).getString(TRAILER_SOURCE_PARAM);
                String trailerPath = TRAILER_BASEPATH + videoId;
                String thumbnailPath = TRAILER_THUMBNAIL_BASEPATH + videoId + "/0.jpg";

                nameList.add(name);
                trailerPathList.add(trailerPath);
                thumbnailPathList.add(thumbnailPath);
            }
            trailerBundle.putStringArrayList(TRAILER_SOURCE_PARAM, trailerPathList);
            trailerBundle.putStringArrayList(TRAILER_NAME_PARAM, nameList);
            trailerBundle.putStringArrayList(TRAILER_THUMBNAIL_PARAM, thumbnailPathList);
        }

        reviewBundle = new ArrayList<>();
        if (reviews.length() > 0) {
            for (int j = 0; j < reviews.length(); j++) {
                //reviewBundle.add(reviews.getJSONObject(j).getString(TRAILER_SOURCE_PARAM));
            }
        }
    }


    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieOriginalTitle() {
        return movieOriginalTitle;
    }

    public String getMovieSynopsis() {
        return movieSynopsis;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public int getMovieId() {
        return movieId;
    }

    public double getMovieRate() {
        return movieRate;
    }

    public String getMoviePosterPath() {
        return BASEPATH_W342 + moviePosterPath;
    }

    public String getMovieBackdropPath() {
        return BASEPATH_W342 + movieBackdropPath;
    }

    public String getImdbId() {
        return imdbId;
    }

    public int getMovieRuntime() {
        return movieRuntime;
    }

    public Bundle getTrailerBundle() {
        return trailerBundle;
    }

    public List<String> getReviewBundle() {
        return reviewBundle;
    }
}
