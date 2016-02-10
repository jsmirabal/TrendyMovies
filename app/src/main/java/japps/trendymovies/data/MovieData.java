package japps.trendymovies.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Julio on 9/2/2016.
 */
public class MovieData {
    private JSONObject mJsonData;
    private List<String> movieTitleList;
    private List<String> movieOriginalTitleList;
    private List<String> movieSynopsisList;
    private List<String> movieReleaseDateList;
    private List<String> movieIdList;
    private List<String> movieRateList;
    private List<String> moviePosterPathList;

    public static final String TITLE_PARAM = "title";
    public static final String ORIGINAL_TITLE_PARAM = "original_title";
    public static final String SYNOPSIS_PARAM = "overview";
    public static final String RELEASE_DATE_PARAM = "release_date";
    public static final String ID_PARAM = "id";
    public static final String RATE_PARAM = "vote_average";
    public static final String POSTER_PATH_PARAM = "poster_path";
    private final String BASEPATH_W154 = "http://image.tmdb.org/t/p/w154";
    private final String BASEPATH_W185 = "http://image.tmdb.org/t/p/w185";
    private final String BASEPATH_W342 = "http://image.tmdb.org/t/p/w342";
    private final String BASEPATH_W500 = "http://image.tmdb.org/t/p/w500";

    public MovieData(JSONObject jsonData) throws JSONException {
        mJsonData = jsonData;
        setData();
    }

    public List<String> getMovieTitleList() {
        return movieTitleList;
    }

    public List<String> getMovieOriginalTitleList() {
        return movieOriginalTitleList;
    }

    public List<String> getMovieSynopsisList() {
        return movieSynopsisList;
    }

    public List<String> getMovieReleaseDateList() {
        return movieReleaseDateList;
    }

    public List<String> getMovieIdList() {
        return movieIdList;
    }

    public List<String> getMovieRateList() {
        return movieRateList;
    }

    public List<String> getMoviePosterPathList() {
        return moviePosterPathList;
    }

    private void setData() throws JSONException {
        JSONArray resultsArray = mJsonData.getJSONArray("results");
        movieTitleList = new ArrayList<>();
        movieOriginalTitleList = new ArrayList<>();
        movieSynopsisList = new ArrayList<>();
        movieReleaseDateList = new ArrayList<>();
        movieIdList = new ArrayList<>();
        movieRateList = new ArrayList<>();
        moviePosterPathList = new ArrayList<String>(){
            @Override
            public String toString() {
                return overwrittenToString(this);
            }
        };
        for (int j = 0; j < resultsArray.length(); j++){
            movieTitleList.add(resultsArray.getJSONObject(j).getString(TITLE_PARAM));
            movieOriginalTitleList.add(resultsArray.getJSONObject(j).getString(ORIGINAL_TITLE_PARAM));
            movieSynopsisList.add(resultsArray.getJSONObject(j).getString(SYNOPSIS_PARAM));
            movieReleaseDateList.add(resultsArray.getJSONObject(j).getString(RELEASE_DATE_PARAM));
            movieIdList.add(resultsArray.getJSONObject(j).getString(ID_PARAM));
            movieRateList.add(resultsArray.getJSONObject(j).getString(RATE_PARAM));
            moviePosterPathList.add(BASEPATH_W342+resultsArray.getJSONObject(j).getString(POSTER_PATH_PARAM));
        }
    }

    private String overwrittenToString(ArrayList<String> list) {
        if (list.isEmpty()) {
            return "[]";
        }

        StringBuilder buffer = new StringBuilder(list.size() * 16);
        buffer.append('[');
        Iterator<?> it = list.iterator();
        while (it.hasNext()) {
            Object next = it.next();
            if (next != this) {
                buffer.append(next);
            } else {
                buffer.append("(this Collection)");
            }
            if (it.hasNext()) {
                buffer.append(", \n");
            }
        }
        buffer.append(']');
        return buffer.toString();
    }
}
