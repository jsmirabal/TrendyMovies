package japps.trendymovies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import japps.trendymovies.db.MovieDbContract.*;

/**
 * Created by Julio on 31/7/2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper{    
    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                MovieEntry._ID + " INTEGER AUTO_INCREMENT," +

                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL," +
                MovieEntry.COLUMN_ORIGINAL_LANG + " TEXT NOT NULL," +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL," +
                MovieEntry.COLUMN_GENRES + " TEXT NOT NULL," +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL," +
                MovieEntry.COLUMN_RUNTIME + " INTEGER NOT NULL," +
                MovieEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL," +
                MovieEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL," +

                MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieEntry.COLUMN_BUDGET + " INTEGER NULL, " +

                MovieEntry.COLUMN_REVENUE + " INTEGER NULL, " +
                MovieEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                
                " PRIMARY KEY (" + MovieEntry._ID + "," + MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
                TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                TrailerEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_SOURCE + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + TrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + ")); ";
        
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                ReviewEntry.COLUMN_AUTHOR + " TEXT  NOT NULL, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + ")); ";
        
        final String SQL_CREATE_CAST_TABLE = "CREATE TABLE " + CastEntry.TABLE_NAME + " (" +
                CastEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CastEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                CastEntry.COLUMN_CHARACTER + " TEXT NOT NULL, " +
                CastEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CastEntry.COLUMN_PROFILE_PATH + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + CastEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + ")); ";
        
        final String SQL_CREATE_CREW_TABLE = "CREATE TABLE " + CrewEntry.TABLE_NAME + " (" +
                CrewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CrewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                CrewEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                CrewEntry.COLUMN_JOB + " TEXT NOT NULL, " +
                CrewEntry.COLUMN_PROFILE_PATH + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + CrewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME + " (" + MovieEntry.COLUMN_MOVIE_ID + ")); ";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CAST_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_CREW_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CastEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CrewEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
