package japps.trendymovies.tasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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

/**
 * Created by Julio on 9/2/2016.
 */
public class FetchMovieTask extends AsyncTask<Void, Void, MovieData> {

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();
    private Context mContext;

    protected MovieData doInBackground(Void... params ) {
        String sortBy = "popularity.desc";
        JSONObject jsonData = getMoviesFromApi(sortBy);
        MovieData movieData;
        if (jsonData != null){
            try {
                //Log.d(LOG_TAG,jsonData.toString(4));
                movieData = new MovieData(jsonData);
                //Log.d(LOG_TAG, movieData.getMoviePosterPathList().toString());
                return  movieData;
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(LOG_TAG, "Error printing json data.");
            }
        }
        return null;
    }

    private JSONObject getMoviesFromApi(String sortBy) {
        final String BASE_URI = "http://api.themoviedb.org/3/discover/movie";
        final String SORT_PARAM = "sort_by";
        final String API_KEY_PARAM = "api_key";
        final String API_KEY = BuildConfig.MOVIE_DB_API_KEY;
        HttpURLConnection urlConnection = null;

        String requestUri = Uri.parse(BASE_URI).buildUpon().appendQueryParameter(SORT_PARAM,sortBy)
                .appendQueryParameter(API_KEY_PARAM,API_KEY).build().toString();

        try {
            URL url = new URL(requestUri);
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
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {
                Log.e(LOG_TAG, "Error closing stream", e);
            }
        }
        return buffer.toString();
    }
}
