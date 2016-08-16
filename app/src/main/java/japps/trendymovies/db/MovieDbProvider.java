package japps.trendymovies.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import japps.trendymovies.db.MovieDbContract.*;

/**
 * Created by Julio on 31/7/2016.
 */
public class MovieDbProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mHelper;

    static final int MOVIE = 100;
    static final int MOVIE_FULL_DATA = 101; // Discarded: Query not atomic
    static final int MOVIE_WITH_CAST_CREW = 102; // Discarded: Query not atomic
    static final int TRAILER = 200;
    static final int REVIEW = 300;
    static final int CAST = 400;
    static final int CREW = 500;

    private static final SQLiteQueryBuilder sMovieFullDataQueryBuilder;
    private static final SQLiteQueryBuilder sMovieWithCastAndCrewQueryBuilder;
    private static final SQLiteQueryBuilder sMovieQueryBuilder;
    private static final SQLiteQueryBuilder sTrailerQueryBuilder;
    private static final SQLiteQueryBuilder sReviewQueryBuilder;
    private static final SQLiteQueryBuilder sCastQueryBuilder;
    private static final SQLiteQueryBuilder sCrewQueryBuilder;

    static {
        sMovieFullDataQueryBuilder = new SQLiteQueryBuilder();
        sMovieWithCastAndCrewQueryBuilder = new SQLiteQueryBuilder();
        sMovieQueryBuilder = new SQLiteQueryBuilder();
        sTrailerQueryBuilder = new SQLiteQueryBuilder();
        sReviewQueryBuilder = new SQLiteQueryBuilder();
        sCastQueryBuilder = new SQLiteQueryBuilder();
        sCrewQueryBuilder = new SQLiteQueryBuilder();

        sMovieFullDataQueryBuilder.setTables(
                MovieEntry.TABLE_NAME + " INNER JOIN " + TrailerEntry.TABLE_NAME + " ON " +
                        MovieEntry.TABLE_NAME + "." + MovieEntry.COLUMN_MOVIE_ID + " = " +
                        TrailerEntry.TABLE_NAME + "." + TrailerEntry.COLUMN_MOVIE_ID +
                        " INNER JOIN " + ReviewEntry.TABLE_NAME + " ON " +
                        TrailerEntry.TABLE_NAME + "." + TrailerEntry.COLUMN_MOVIE_ID + " = "+
                        ReviewEntry.TABLE_NAME + "." + ReviewEntry.COLUMN_MOVIE_ID +
                        " INNER JOIN " + CastEntry.TABLE_NAME + " as c ON " +
                        ReviewEntry.TABLE_NAME + "." + ReviewEntry.COLUMN_MOVIE_ID + " = "+
                        "c." + CastEntry.COLUMN_MOVIE_ID +
                        " INNER JOIN " + CrewEntry.TABLE_NAME + " ON " +
                        "c." + CastEntry.COLUMN_MOVIE_ID + " = "+
                        CrewEntry.TABLE_NAME + "." + CrewEntry.COLUMN_MOVIE_ID
        );
        sMovieWithCastAndCrewQueryBuilder.setTables(
                MovieEntry.TABLE_NAME + " INNER JOIN " + CastEntry.TABLE_NAME + " as c ON " +
                        MovieEntry.TABLE_NAME + "." + MovieEntry.COLUMN_MOVIE_ID + " = " +
                        "c." + CastEntry.COLUMN_MOVIE_ID +
                        " INNER JOIN " + CrewEntry.TABLE_NAME + " ON " +
                        "c." + CastEntry.COLUMN_MOVIE_ID + " = "+
                        CrewEntry.TABLE_NAME + "." + CrewEntry.COLUMN_MOVIE_ID
        );
        sMovieQueryBuilder.setTables(MovieEntry.TABLE_NAME);
        sTrailerQueryBuilder.setTables(TrailerEntry.TABLE_NAME);
        sReviewQueryBuilder.setTables(ReviewEntry.TABLE_NAME);
        sCastQueryBuilder.setTables(CastEntry.TABLE_NAME);
        sCrewQueryBuilder.setTables(CrewEntry.TABLE_NAME);
    }

    static UriMatcher buildUriMatcher() {
        // 1) The code passed into the constructor represents the code to return for the root
        // URI.  It's common to use NO_MATCH as the code for this case. Add the constructor below.
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        String authority = MovieDbContract.CONTENT_AUTHORITY;
        String pathMovie = MovieDbContract.PATH_MOVIE;
        String pathFullMovie = MovieDbContract.PATH_FULL_MOVIE;
        String pathMovieCastCrew = MovieDbContract.PATH_MOVIE_CAST_CREW;
        String pathTrailer = MovieDbContract.PATH_TRAILER;
        String pathReview = MovieDbContract.PATH_REVIEW;
        String pathCast = MovieDbContract.PATH_CAST;
        String pathCrew = MovieDbContract.PATH_CREW;

        uriMatcher.addURI(authority, pathMovie, MOVIE);
        uriMatcher.addURI(authority, pathFullMovie + "/#", MOVIE_FULL_DATA);
        uriMatcher.addURI(authority, pathMovieCastCrew + "/#", MOVIE_WITH_CAST_CREW);
        uriMatcher.addURI(authority, pathTrailer, TRAILER);
        uriMatcher.addURI(authority, pathReview, REVIEW);
        uriMatcher.addURI(authority, pathCast, CAST);
        uriMatcher.addURI(authority, pathCrew, CREW);

        // 3) Return the new matcher!
        return uriMatcher;
    }

    public static final String sTableMovieIdSelection = MovieEntry.COLUMN_MOVIE_ID + " = ?" ;
    public static final String sTableTrailerIdSelection = TrailerEntry.COLUMN_MOVIE_ID + " = ?" ;
    public static final String sTableReviewIdSelection = ReviewEntry.COLUMN_MOVIE_ID + " = ?" ;
    public static final String sTableCastIdSelection = CastEntry.COLUMN_MOVIE_ID + " = ?" ;
    public static final String sTableCrewIdSelection = CrewEntry.COLUMN_MOVIE_ID + " = ?" ;

    private Cursor getMovie (String[] projection, String selection, String[] selectionArgs) {
        return sMovieQueryBuilder.query(mHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }
    private Cursor getMovieFullData (Uri uri, String[] projection) {
        return sMovieFullDataQueryBuilder.query(mHelper.getReadableDatabase(),
                projection,
                sTableMovieIdSelection,
                new String[]{MovieEntry.getMovieIdFromUri(uri)},
                null,
                null,
                null
        );
    }
    private Cursor getMovieWithCastAndCrew (Uri uri, String[] projection) {
        return sMovieWithCastAndCrewQueryBuilder.query(mHelper.getReadableDatabase(),
                projection,
                sTableMovieIdSelection,
                new String[]{MovieEntry.getMovieIdFromUri(uri)},
                null,
                null,
                null
        );
    }
    private Cursor getTrailers (String[] projection, String[] args) {
        return sTrailerQueryBuilder.query(mHelper.getReadableDatabase(),
                projection,
                sTableTrailerIdSelection,
                args,
                null,
                null,
                null
        );
    }
    private Cursor getReviews (String[] projection, String[] args) {
        return sReviewQueryBuilder.query(mHelper.getReadableDatabase(),
                projection,
                sTableReviewIdSelection,
                args,
                null,
                null,
                null
        );
    }

    private Cursor getCast (String[] projection, String[] args) {
        return sCastQueryBuilder.query(mHelper.getReadableDatabase(),
                projection,
                sTableCastIdSelection,
                args,
                null,
                null,
                null
        );
    }

    private Cursor getCrew (String[] projection, String[] args) {
        return sCrewQueryBuilder.query(mHelper.getReadableDatabase(),
                projection,
                sTableCrewIdSelection,
                args,
                null,
                null,
                null
        );
    }

    @Override
    public boolean onCreate() {
        mHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                retCursor = getMovie(projection, selection, selectionArgs);
                break;
            }

            case MOVIE_FULL_DATA: {
                retCursor = getMovieFullData(uri, projection);
                break;
            }
            case MOVIE_WITH_CAST_CREW: {
                retCursor = getMovieWithCastAndCrew(uri, projection);
                break;
            }
            case TRAILER: {
                retCursor = getTrailers(projection, selectionArgs);
                break;
            }
            case REVIEW: {
                retCursor = getReviews(projection, selectionArgs);
                break;
            }
            case CAST: {
                retCursor = getCast(projection, selectionArgs);
                break;
            }
            case CREW: {
                retCursor = getCrew(projection, selectionArgs);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            // Student: Uncomment and fill out these two cases
            case MOVIE:
                return MovieEntry.CONTENT_TYPE;
            case MOVIE_FULL_DATA:
                return MovieEntry.FULL_MOVIE_CONTENT_TYPE;
            case MOVIE_WITH_CAST_CREW:
                return MovieEntry.CAST_CREW_CONTENT_TYPE;
            case TRAILER:
                return TrailerEntry.CONTENT_TYPE;
            case REVIEW:
                return ReviewEntry.CONTENT_TYPE;
            case CAST:
                return CastEntry.CONTENT_TYPE;
            case CREW:
                return CrewEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MovieEntry.buildMovieUri(values.getAsLong(MovieEntry.COLUMN_MOVIE_ID));
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case TRAILER: {
                long _id = db.insert(TrailerEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = TrailerEntry.buildTrailerUri(values.getAsLong(TrailerEntry.COLUMN_MOVIE_ID));
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case REVIEW: {
                long _id = db.insert(ReviewEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ReviewEntry.buildReviewUri(values.getAsLong(ReviewEntry.COLUMN_MOVIE_ID));
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CAST: {
                long _id = db.insert(CastEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = CastEntry.buildCastUri(values.getAsLong(CastEntry.COLUMN_MOVIE_ID));
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case CREW: {
                long _id = db.insert(CrewEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = CrewEntry.buildCrewUri(values.getAsLong(CrewEntry.COLUMN_MOVIE_ID));
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case TRAILER: {
                return performInsertTransaction(uri, TrailerEntry.TABLE_NAME, values);
            }
            case REVIEW: {
                return performInsertTransaction(uri, ReviewEntry.TABLE_NAME, values);
            }
            case CAST: {
                return performInsertTransaction(uri, CastEntry.TABLE_NAME, values);
            }
            case CREW: {
                return performInsertTransaction(uri, CrewEntry.TABLE_NAME, values);
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private int performInsertTransaction (Uri uri, String table, ContentValues[] values){
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(table, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        selection = selection == null ? "1" : selection;
        switch (match) {
            case MOVIE: {
                rowsDeleted = db.delete(MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case TRAILER: {
                rowsDeleted = db.delete(TrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case REVIEW: {
                rowsDeleted = db.delete(ReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case CAST: {
                rowsDeleted = db.delete(CastEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case CREW: {
                rowsDeleted = db.delete(CrewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        // Student: This is a lot like the delete function.  We return the number of rows impacted
        // by the update.
        final SQLiteDatabase db = mHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsAffected;
        switch (match) {
            case MOVIE: {
                rowsAffected = db.update(MovieEntry.TABLE_NAME, contentValues, sTableMovieIdSelection,
                        selectionArgs);
                break;
            }
            case TRAILER: {
                rowsAffected = db.update(TrailerEntry.TABLE_NAME, contentValues, sTableMovieIdSelection,
                        selectionArgs);
                break;
            }
            case REVIEW: {
                rowsAffected = db.update(ReviewEntry.TABLE_NAME, contentValues, sTableMovieIdSelection,
                        selectionArgs);
                break;
            }
            case CAST: {
                rowsAffected = db.update(CastEntry.TABLE_NAME, contentValues, sTableMovieIdSelection,
                        selectionArgs);
                break;
            }
            case CREW: {
                rowsAffected = db.update(CrewEntry.TABLE_NAME, contentValues, sTableMovieIdSelection,
                        selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsAffected != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsAffected;
    }
}
