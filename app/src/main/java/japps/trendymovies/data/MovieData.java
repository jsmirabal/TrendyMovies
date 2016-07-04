package japps.trendymovies.data;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieData implements MovieHandler{
    public static final String TITLE_PARAM = "title";
    public static final String ORIGINAL_TITLE_PARAM = "original_title";
    public static final String SYNOPSIS_PARAM = "overview";
    public static final String RELEASE_DATE_PARAM = "release_date";
    public static final String ID_PARAM = "id";
    public static final String IMDB_ID_PARAM = "imdb_id";
    public static final String RATE_PARAM = "vote_average";
    public static final String RUNTIME_PARAM = "runtime";
    public static final String POSTER_PATH_PARAM = "poster_path";
    public static final String BASEPATH_W154 = "http://image.tmdb.org/t/p/w154";
    public static final String BASEPATH_W185 = "http://image.tmdb.org/t/p/w185";
    public static final String BASEPATH_W342 = "http://image.tmdb.org/t/p/w342";
    public static final String BASEPATH_W500 = "http://image.tmdb.org/t/p/w500";

    private final String TRAILERS_PARAM = "trailers";
    private final String YOUTUBE_PARAM = "youtube";
    private final String SOURCE_PARAM = "source";
    private final String REVIEWS_PARAM = "reviews";
    private final String RESULTS_PARAM = "results";

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
    private List<String> trailersList;
    private List<String> reviewsList;

    public MovieData(JSONObject jsonData) throws JSONException {
        if (jsonData == null) {return;}
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

        JSONArray trailers = mJsonData.getJSONObject(TRAILERS_PARAM).getJSONArray(YOUTUBE_PARAM);
        JSONArray reviews = mJsonData.getJSONObject(REVIEWS_PARAM).getJSONArray(RESULTS_PARAM);

        if (trailers.length() > 0){
            trailersList = new ArrayList<>();
            for (int j = 0; j < trailers.length(); j++){
                trailersList.add(trailers.getJSONObject(j).getString(SOURCE_PARAM));
            }
        }

        if (reviews.length() > 0){
            reviewsList = new ArrayList<>();
            for (int j = 0; j < reviews.length(); j++){
                //reviewsList.add(reviews.getJSONObject(j).getString(SOURCE_PARAM));
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
        return BASEPATH_W342+moviePosterPath;
    }

    public String getImdbId() {
        return imdbId;
    }

    public int getMovieRuntime() {
        return movieRuntime;
    }

    public List<String> getTrailersList() {
        return trailersList;
    }

    public List<String> getReviewsList() {
        return reviewsList;
    }
}
