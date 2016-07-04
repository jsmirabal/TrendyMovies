package japps.trendymovies.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julio on 9/2/2016.
 */
public class MovieListData implements MovieHandler{
    private JSONObject mJsonData;

    private List<String> movieIdList;
    private List<String> moviePosterPathList;

    public MovieListData(JSONObject jsonData) throws JSONException {
        mJsonData = jsonData;
        setData();
    }

    public List<String> getMovieIdList() {
        return movieIdList;
    }

    public List<String> getMoviePosterPathList() {
        return moviePosterPathList;
    }

    private void setData() throws JSONException {
        JSONArray resultsArray = mJsonData.getJSONArray("results");
        movieIdList = new ArrayList<>();
        moviePosterPathList = new ArrayList<>();
        for (int j = 0; j < resultsArray.length(); j++){
            movieIdList.add(resultsArray.getJSONObject(j).getString(MovieData.ID_PARAM));
            moviePosterPathList.add(MovieData.BASEPATH_W342+resultsArray.getJSONObject(j)
                    .getString(MovieData.POSTER_PATH_PARAM));
        }
    }
}
