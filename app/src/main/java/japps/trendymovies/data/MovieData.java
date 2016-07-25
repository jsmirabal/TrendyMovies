package japps.trendymovies.data;


import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
    public static final String GENRES_PARAM = "genres";
    public static final String POPULARITY_PARAM = "popularity";
    public static final String ORIGINAL_LANG_PARAM = "original_language";
    public static final String BUDGET_PARAM = "budget";
    public static final String REVENUE_PARAM = "revenue";
    public static final String VOTES_PARAM = "vote_count";

    public static final String BASEPATH_W154 = "http://image.tmdb.org/t/p/w154";
    public static final String BASEPATH_W185 = "http://image.tmdb.org/t/p/w185";
    public static final String BASEPATH_W342 = "http://image.tmdb.org/t/p/w342";
    public static final String BASEPATH_W500 = "http://image.tmdb.org/t/p/w500";
    private static final String TRAILER_THUMBNAIL_BASEPATH = "http://img.youtube.com/vi/";
    private static final String TRAILER_BASEPATH = "https://www.youtube.com/watch?v=";

    public static final String TRAILERS_PARAM = "trailers";
    public static final String TRAILER_THUMBNAIL_PARAM = "trailer_thumbnail";
    public static final String REVIEWS_PARAM = "reviews";
    private static final String GENRE_NAME_PARAM = "name";
    public static final String TRAILER_SOURCE_PARAM = "source";
    public static final String TRAILER_NAME_PARAM = "name";
    public static final String TRAILERS_COUNT = "has_trailer";

    public static final String CREDITS_PARAM = "credits";
    public static final String CAST_PARAM = "cast";
    public static final String CAST_CHARACTER_PARAM = "character";
    public static final String CAST_NAME_PARAM = "name";
    public static final String CREW_PARAM = "crew";
    public static final String CREW_JOB_PARAM = "job";
    public static final String CREW_NAME_PARAM = "name";
    public static final String PEOPLE_COUNT = "count";
    public static final String PEOPLE_TYPE = "type";
    public static final String PROFILE_PATH_PARAM = "profile_path";

    private final String RESULTS_PARAM = "results";
    private final String YOUTUBE_PARAM = "youtube";

    private JSONObject mJsonData;
    private String movieTitle, movieOriginalTitle, movieSynopsis, movieReleaseDate, moviePosterPath,
            movieVotes, movieOriginalLang , movieBackdropPath, imdbId;
    private int movieId, movieRuntime;
    private double movieRate, moviePopularity;
    private Bundle trailerBundle, reviewBundle, genresBundle, castBundle, crewBundle;
    private long movieBudget, movieRevenue;

    public MovieData(JSONObject jsonData) throws JSONException {
        if (jsonData == null) {
            return;
        }
        mJsonData = jsonData;
        setData();
    }

    private void setData() throws JSONException {
        // Facts data
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
        movieVotes = mJsonData.getString(VOTES_PARAM);
        moviePopularity = mJsonData.getDouble(POPULARITY_PARAM);
        movieOriginalLang = mJsonData.getString(ORIGINAL_LANG_PARAM);
        movieBudget = mJsonData.getLong(BUDGET_PARAM);
        movieRevenue = mJsonData.getLong(REVENUE_PARAM);

        JSONArray trailers = mJsonData.getJSONObject(TRAILERS_PARAM).getJSONArray(YOUTUBE_PARAM);
        JSONArray reviews = mJsonData.getJSONObject(REVIEWS_PARAM).getJSONArray(RESULTS_PARAM);
        JSONArray genres = mJsonData.getJSONArray(GENRES_PARAM);
        JSONArray cast = mJsonData.getJSONObject(CREDITS_PARAM).getJSONArray(CAST_PARAM);
        JSONArray crew = mJsonData.getJSONObject(CREDITS_PARAM).getJSONArray(CREW_PARAM);

        // Genres data
        genresBundle = new Bundle();
        if (genres.length() > 0){
            ArrayList<String> genreList = new ArrayList<>();
            for (int j = 0; j < genres.length() ;j++){
                genreList.add(genres.getJSONObject(j).getString(GENRE_NAME_PARAM));
            }
            genresBundle.putStringArrayList(GENRES_PARAM,genreList);
        }
        
        // Trailer data
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

        // Cast data
        castBundle = new Bundle();
        castBundle.putString(PEOPLE_TYPE, CAST_PARAM);
        if (cast.length() > 0) {
            int length = cast.length() > 6 ? 6 : cast.length();
            castBundle.putInt(PEOPLE_COUNT, length);
            ArrayList<String> nameList = new ArrayList<>();
            ArrayList<String> characterList = new ArrayList<>();
            ArrayList<String> profileList = new ArrayList<>();
            for (int j = 0; j < length; j++) {
                String name = cast.getJSONObject(j).getString(CAST_NAME_PARAM);
                String character = cast.getJSONObject(j).getString(CAST_CHARACTER_PARAM);
                String profile = cast.getJSONObject(j).getString(PROFILE_PATH_PARAM);
                profile = profile.equals("null") ? "" : BASEPATH_W185 + profile;
                nameList.add(name);
                characterList.add(character);
                profileList.add(profile);
            }
            castBundle.putStringArrayList(CAST_NAME_PARAM, nameList);
            castBundle.putStringArrayList(CAST_CHARACTER_PARAM, characterList);
            castBundle.putStringArrayList(PROFILE_PATH_PARAM, profileList);
        } else {
            castBundle.putInt(PEOPLE_COUNT, 0);
        }

        // Crew data
        crewBundle = new Bundle();
        crewBundle.putString(PEOPLE_TYPE, CREW_PARAM);
        if (crew.length() > 0) {
            int length = crew.length() > 6 ? 6 : crew.length();
            crewBundle.putInt(PEOPLE_COUNT, length);
            ArrayList<String> nameList = new ArrayList<>();
            ArrayList<String> characterList = new ArrayList<>();
            ArrayList<String> profileList = new ArrayList<>();
            for (int j = 0; j < length; j++) {
                String name = crew.getJSONObject(j).getString(CREW_NAME_PARAM);
                String job = crew.getJSONObject(j).getString(CREW_JOB_PARAM);
                String profile = crew.getJSONObject(j).getString(PROFILE_PATH_PARAM);
                profile = profile.equals("null") ? "" : BASEPATH_W185 + profile;

                nameList.add(name);
                characterList.add(job);
                profileList.add(profile);
            }
            crewBundle.putStringArrayList(CREW_NAME_PARAM, nameList);
            crewBundle.putStringArrayList(CREW_JOB_PARAM, characterList);
            crewBundle.putStringArrayList(PROFILE_PATH_PARAM, profileList);
        } else {
            crewBundle.putInt(PEOPLE_COUNT, 0);
        }

        // Reviews data
        reviewBundle = new Bundle();
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

    public Bundle getReviewBundle() {
        return reviewBundle;
    }

    public Bundle getGenresBundle() {
        return genresBundle;
    }

    public String getMovieVotes() {
        return movieVotes;
    }

    public double getMoviePopularity() {
        return moviePopularity;
    }

    public String getMovieOriginalLang() {
        return movieOriginalLang;
    }

    public long getMovieBudget() {
        return movieBudget;
    }

    public long getMovieRevenue() {
        return movieRevenue;
    }

    public Bundle getCastBundle() {
        return castBundle;
    }

    public Bundle getCrewBundle() {
        return crewBundle;
    }
}
