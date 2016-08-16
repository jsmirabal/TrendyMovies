package japps.trendymovies.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Julio on 31/7/2016.
 */
public class MovieDbContract {
    public static final String CONTENT_AUTHORITY = "japps.trendymovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "movie";
    public static final String PATH_FULL_MOVIE = "full_movie";
    public static final String PATH_MOVIE_CAST_CREW = "movie_cast_crew";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_CAST = "cast";
    public static final String PATH_CREW = "crew";

    /* Table contents of the movie table */
    public static final class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_ORIGINAL_LANG = "original_language";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_GENRES = "genres";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_BUDGET = "budget";
        public static final String COLUMN_REVENUE = "revenue";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_POSTER_BLOB = "poster_blob";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();
        public static final Uri FULL_MOVIE_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FULL_MOVIE).build();
        public static final Uri CAST_CREW_CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_CAST_CREW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String FULL_MOVIE_CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FULL_MOVIE;
        public static final String CAST_CREW_CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_CAST_CREW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static Uri buildFullMovieUri(long id) {
            return ContentUris.withAppendedId(FULL_MOVIE_CONTENT_URI, id);
        }
        public static Uri buildMovieCastCrewUri(long id) {
            return ContentUris.withAppendedId(CAST_CREW_CONTENT_URI, id);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }
    }

    /* Table contents of the trailer table */
    public static final class TrailerEntry implements BaseColumns{
        public static final String TABLE_NAME = "trailer";
        public static final String COLUMN_MOVIE_ID = "trailer_movie_id";
        public static final String COLUMN_TITLE = "trailer_title";
        public static final String COLUMN_SOURCE = "trailer_source";
        public static final String COLUMN_THUMBNAIL_PATH = "trailer_thumbnail_path";
        public static final String COLUMN_THUMBNAIL_BLOB = "trailer_thumbnail_blob";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }
    }

    /* Table contents of the review table */
    public static final class ReviewEntry implements BaseColumns{
        public static final String TABLE_NAME = "review";
        public static final String COLUMN_MOVIE_ID = "review_movie_id";
        public static final String COLUMN_AUTHOR = "review_author";
        public static final String COLUMN_CONTENT = "review_content";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }
    }

    /* Table contents of the cast table */
    public static final class CastEntry implements BaseColumns{
        public static final String TABLE_NAME = "cast";
        public static final String COLUMN_MOVIE_ID = "cast_movie_id";
        public static final String COLUMN_NAME = "cast_name";
        public static final String COLUMN_CHARACTER = "cast_character";
        public static final String COLUMN_PROFILE_PATH = "cast_profile_path";
        public static final String COLUMN_PROFILE_BLOB = "cast_profile_blob";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CAST).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CAST;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CAST;

        public static Uri buildCastUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }
    }

    /* Table contents of the crew table */
    public static final class CrewEntry implements BaseColumns{
        public static final String TABLE_NAME = "crew";
        public static final String COLUMN_MOVIE_ID = "crew_movie_id";
        public static final String COLUMN_NAME = "crew_name";
        public static final String COLUMN_JOB = "crew_job";
        public static final String COLUMN_PROFILE_PATH = "crew_profile_path";
        public static final String COLUMN_PROFILE_BLOB = "crew_profile_blob";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CREW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CAST;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CAST;

        public static Uri buildCrewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        public static String getMovieIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }
    }
}
