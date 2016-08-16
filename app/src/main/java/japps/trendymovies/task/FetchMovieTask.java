package japps.trendymovies.task;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import japps.trendymovies.BuildConfig;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.data.MovieHandler;
import japps.trendymovies.data.MovieListData;
import japps.trendymovies.utility.Utilities;

/**
 * Created by Julio on 9/2/2016.
 */
public class FetchMovieTask extends AsyncTask<Object, Void, MovieHandler> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private final String BASE_URI_MOVIE = "http://api.themoviedb.org/3/movie/";
    private final String BASE_URI_MOVIE_LIST = "http://api.themoviedb.org/3/discover/movie";
    private final String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
    public static final int FETCH_MOVIE = 1;
    public static final int FETCH_MOVIE_LIST = 2;
    public static final String MOVIE_ID_KEY = "movie_id";
    public static final String SORT_KEY = "sort";
    public static final int MOST_POPULAR = 10;
    public static final int TOP_RATED = 11;
    public static final int UPCOMING = 12;
    public static final int NOW_PLAYING = 13;
    public static final int REVENUE = 14;

    private Context mContext;

    protected MovieHandler doInBackground(Object... params ) {

        if (!(params[0] instanceof Integer)){return null;}
        if (!(params[1] instanceof Bundle)){return null;}
        if (!(params[2] instanceof Context)){return null;}

        Integer fetchType = (Integer) params[0];
        mContext = (Context) params[2];
        final String SORT_PARAM = "sort_by";
        final String LANG_PARAM = "language";
        final String ATR_PARAM = "append_to_response";
        final String API_KEY_PARAM = "api_key";
        String requestPath;
        JSONObject jsonData;
        switch(fetchType){
            case FETCH_MOVIE:{
                String movieId = ((Bundle)params[1]).getString(MOVIE_ID_KEY);
                requestPath = Uri.parse(BASE_URI_MOVIE+movieId).buildUpon()
//                        .appendQueryParameter(LANG_PARAM, Utilities.getLocale())
                        .appendQueryParameter(LANG_PARAM, "en")
                        .appendQueryParameter(ATR_PARAM, "trailers,reviews,credits")
                        .appendQueryParameter(API_KEY_PARAM,API_KEY)
                        .build().toString();
                Log.d("Single Movie", requestPath);
                jsonData = fetchDataFromTMDB(requestPath);
                if (jsonData == null){
                    return null;
                }
                try {
                    return new MovieData(mContext, jsonData);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "JSON ERROR: "+ e.getMessage());
                }
                break;
            }
            case FETCH_MOVIE_LIST:{
                int sortBy = ((Bundle)params[1]).getInt(SORT_KEY);
                Uri.Builder requestUri = Uri.parse(BASE_URI_MOVIE_LIST).buildUpon()
                        .appendQueryParameter(API_KEY_PARAM,API_KEY);
                switch (sortBy){
                    case MOST_POPULAR:{
                        requestPath = requestUri.appendQueryParameter(SORT_PARAM, "popularity.desc")
                                .build().toString();
                        break;
                    }
                    case TOP_RATED:{
                        requestPath = requestUri
                                .appendQueryParameter(SORT_PARAM, "vote_average.desc")
                                .appendQueryParameter("vote_count.gte", "150")
                                .build().toString();
                        break;
                    }
                    case UPCOMING:{
                        requestPath = requestUri
                                .appendQueryParameter(SORT_PARAM, "primary_release_date.asc")
                                .appendQueryParameter("vote_count.gte", "10")
                                .appendQueryParameter("primary_release_date.gte", Utilities.getToday("yyyy-MM-dd"))
                                .build().toString();
                        break;
                    }
                    case NOW_PLAYING:{
                        requestPath = requestUri
                                .appendQueryParameter(SORT_PARAM, "popularity.desc")
                                .appendQueryParameter("primary_release_date.gte", Utilities.getDateFrom60days("yyyy-MM-dd"))
                                .appendQueryParameter("primary_release_date.lte", Utilities.getToday("yyyy-MM-dd"))
                                .appendQueryParameter("vote_count.gte", "10")
                                .build().toString();
                        break;
                    }
                    case REVENUE:{
                        requestPath = requestUri.appendQueryParameter(SORT_PARAM, "revenue.desc")
                                .build().toString();
                        break;
                    }
                    default:{
                        requestPath = requestUri.build().toString();
                    }
                }
                Log.d("Movie List", requestPath);
                jsonData = fetchDataFromTMDB(requestPath);
                if (jsonData == null){
                    return null;
                }
                try {
                    MovieHandler movieList = new MovieListData(mContext, jsonData);
                    Log.d(LOG_TAG, ((MovieListData) movieList).getMovieIdList().toString());
                    return movieList;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, "JSON ERROR: "+ e.getMessage());
                }
                break;
            }

        }
        return null;
    }

    @Nullable
    private JSONObject fetchDataFromTMDB(String requestPath) {

        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(requestPath);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            return new JSONObject(jsonStrFromFile(urlConnection.getInputStream()));
        }catch (IOException | JSONException ex){
            ex.printStackTrace();
            Log.e(LOG_TAG, "Error fetching JSON through API: "+ex.getMessage());
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    private String jsonStrFromFile(InputStream inputStream) throws IOException {
        StringBuffer buffer = new StringBuffer();

        if (inputStream == null) {
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line + "\n");
        }

        if (buffer.length() == 0) {
            return null;
        }
        try {
            reader.close();
        } catch (final IOException e) {
            Log.e(LOG_TAG, "Error closing stream", e);
        }
        return buffer.toString();
    }
}
