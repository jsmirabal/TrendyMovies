package japps.trendymovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.List;

import japps.trendymovies.db.MovieDbContract.CastEntry;
import japps.trendymovies.db.MovieDbContract.CrewEntry;
import japps.trendymovies.db.MovieDbContract.MovieEntry;
import japps.trendymovies.db.MovieDbContract.ReviewEntry;
import japps.trendymovies.db.MovieDbContract.TrailerEntry;

import static org.junit.Assert.assertTrue;


/**
 * Created by Julio on 5/8/2016.
 */

@RunWith(AndroidJUnit4.class)
public class TestDb {
    public static final String LOG_TAG = TestDb.class.getSimpleName();
    private Context mContext;

    @Before
    public void setUp(){
        mContext = InstrumentationRegistry.getTargetContext();
        deleteTheDatabase();
    }

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    /*
        Test tables creation
    */
    @Test
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieEntry.TABLE_NAME);
        tableNameHashSet.add(TrailerEntry.TABLE_NAME);
        tableNameHashSet.add(ReviewEntry.TABLE_NAME);
        tableNameHashSet.add(CastEntry.TABLE_NAME);
        tableNameHashSet.add(CrewEntry.TABLE_NAME);        

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(this.mContext).getWritableDatabase();
        assertTrue(db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        assertTrue("Error: Your database was created without its tables",
                tableNameHashSet.isEmpty());
                
        // MovieEntry ------------------------------------------------------------------------------
        c = db.rawQuery("PRAGMA table_info(" + MovieEntry.TABLE_NAME + ")",
                null);
        assertTrue("Error: It wasn't possible to query the database for table MovieEntry.",
                c.moveToFirst());
        // Hash set of MovieEntry columns that will be verified 
        final HashSet<String> movieColumnHashSet = new HashSet<>();
        movieColumnHashSet.add(MovieEntry._ID);
        movieColumnHashSet.add(MovieEntry.COLUMN_BACKDROP_PATH);
        movieColumnHashSet.add(MovieEntry.COLUMN_BUDGET);
        movieColumnHashSet.add(MovieEntry.COLUMN_GENRES);
        movieColumnHashSet.add(MovieEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(MovieEntry.COLUMN_ORIGINAL_LANG);
        movieColumnHashSet.add(MovieEntry.COLUMN_ORIGINAL_TITLE);
        movieColumnHashSet.add(MovieEntry.COLUMN_OVERVIEW);
        movieColumnHashSet.add(MovieEntry.COLUMN_POPULARITY);
        movieColumnHashSet.add(MovieEntry.COLUMN_POSTER_PATH);
        movieColumnHashSet.add(MovieEntry.COLUMN_RELEASE_DATE);
        movieColumnHashSet.add(MovieEntry.COLUMN_REVENUE);
        movieColumnHashSet.add(MovieEntry.COLUMN_RUNTIME);
        movieColumnHashSet.add(MovieEntry.COLUMN_TITLE);
        movieColumnHashSet.add(MovieEntry.COLUMN_VOTE_AVERAGE);
        movieColumnHashSet.add(MovieEntry.COLUMN_VOTE_COUNT);        
        
        // If this fail means that the database doesn't have all the columns
        assertTrue("Error: The database doesn't contain all of the required MovieEntry columns",
                TestUtilities.verifyTableColumns(c, movieColumnHashSet));

        // TrailerEntry ----------------------------------------------------------------------------
        c = db.rawQuery("PRAGMA table_info(" + TrailerEntry.TABLE_NAME + ")",null);
        assertTrue("Error: It wasn't possible to query the database for table TrailerEntry.",
                c.moveToFirst());
        // Hash set of TrailerEntry columns that will be verified 
        final HashSet<String> trailerColumnHashSet = new HashSet<>();
        movieColumnHashSet.add(TrailerEntry._ID);
        movieColumnHashSet.add(TrailerEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(TrailerEntry.COLUMN_SOURCE);
        movieColumnHashSet.add(TrailerEntry.COLUMN_TITLE);       
        
        assertTrue("Error: The database doesn't contain all of the required TrailerEntry columns",
                TestUtilities.verifyTableColumns(c, trailerColumnHashSet));

        // ReviewEntry ----------------------------------------------------------------------------
        c = db.rawQuery("PRAGMA table_info(" + ReviewEntry.TABLE_NAME + ")",null);
        assertTrue("Error: It wasn't possible to query the database for table ReviewEntry.",
                c.moveToFirst());
        // Hash set of ReviewEntry columns that will be verified 
        final HashSet<String> reviewColumnHashSet = new HashSet<>();
        movieColumnHashSet.add(ReviewEntry._ID);
        movieColumnHashSet.add(ReviewEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(ReviewEntry.COLUMN_AUTHOR);
        movieColumnHashSet.add(ReviewEntry.COLUMN_CONTENT);

        assertTrue("Error: The database doesn't contain all of the required ReviewEntry columns",
                TestUtilities.verifyTableColumns(c, reviewColumnHashSet));

        // CastEntry ----------------------------------------------------------------------------
        c = db.rawQuery("PRAGMA table_info(" + CastEntry.TABLE_NAME + ")",null);
        assertTrue("Error: It wasn't possible to query the database for table CastEntry.",
                c.moveToFirst());
        // Hash set of CastEntry columns that will be verified 
        final HashSet<String> castColumnHashSet = new HashSet<>();
        movieColumnHashSet.add(CastEntry._ID);
        movieColumnHashSet.add(CastEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(CastEntry.COLUMN_CHARACTER);
        movieColumnHashSet.add(CastEntry.COLUMN_NAME);
        movieColumnHashSet.add(CastEntry.COLUMN_PROFILE_PATH);

        assertTrue("Error: The database doesn't contain all of the required CastEntry columns",
                TestUtilities.verifyTableColumns(c, castColumnHashSet));

        // CrewEntry ----------------------------------------------------------------------------
        c = db.rawQuery("PRAGMA table_info(" + CrewEntry.TABLE_NAME + ")",null);
        assertTrue("Error: It wasn't possible to query the database for table CrewEntry.",
                c.moveToFirst());
        // Hash set of CrewEntry columns that will be verified 
        final HashSet<String> crewColumnHashSet = new HashSet<>();
        movieColumnHashSet.add(CrewEntry._ID);
        movieColumnHashSet.add(CrewEntry.COLUMN_MOVIE_ID);
        movieColumnHashSet.add(CrewEntry.COLUMN_JOB);
        movieColumnHashSet.add(CrewEntry.COLUMN_NAME);
        movieColumnHashSet.add(CrewEntry.COLUMN_PROFILE_PATH);

        assertTrue("Error: The database doesn't contain all of the required CrewEntry columns",
                TestUtilities.verifyTableColumns(c, crewColumnHashSet));
        db.close();
    }

    @Test
    public void testMovieTable(){
        ContentValues values = TestUtilities.createMovieData();
        TestUtilities.insertMovieData(mContext);
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(MovieEntry.TABLE_NAME,null,null,null,null,null,null);

        c.moveToFirst();

        TestUtilities.validateCurrentRecord("Invalid data at MovieEntry",c,values);

        c.close();
        dbHelper.close();
    }

    @Test
    public void testTrailerTable(){
        long movieId = TestUtilities.insertMovieData(mContext);
        List<ContentValues> listCv = TestUtilities.createTrailerData(movieId);
        TestUtilities.insertTrailerData(mContext, movieId);
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(TrailerEntry.TABLE_NAME,null,null,null,null,null,null);

        assertTrue("Empty cursor returned at TrailerEntry", c.moveToFirst());

        for (ContentValues values : listCv) {
            TestUtilities.validateCurrentRecord("Invalid data at TrailerEntry", c, values);
            c.moveToNext();
        }
        c.close();
        dbHelper.close();
    }

    @Test
    public void testReviewTable(){
        long movieId = TestUtilities.insertMovieData(mContext);
        List<ContentValues> listCv = TestUtilities.createReviewData(movieId);
        TestUtilities.insertReviewData(mContext, movieId);
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(ReviewEntry.TABLE_NAME,null,null,null,null,null,null);

        assertTrue("Empty cursor returned at ReviewEntry", c.moveToFirst());

        for (ContentValues values : listCv) {
            TestUtilities.validateCurrentRecord("Invalid data at ReviewEntry", c, values);
            c.moveToNext();
        }
        c.close();
        dbHelper.close();
    }

    @Test
    public void testCastTable(){
        long movieId = TestUtilities.insertMovieData(mContext);
        List<ContentValues> listCv = TestUtilities.createCastData(movieId);
        TestUtilities.insertCastData(mContext, movieId);
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(CastEntry.TABLE_NAME,null,null,null,null,null,null);

        assertTrue("Empty cursor returned at CastEntry", c.moveToFirst());

        for (ContentValues values : listCv) {
            TestUtilities.validateCurrentRecord("Invalid data at CastEntry", c, values);
            c.moveToNext();
        }
        c.close();
        dbHelper.close();
    }

    @Test
    public void testCrewTable(){
        long movieId = TestUtilities.insertMovieData(mContext);
        List<ContentValues> listCv = TestUtilities.createCrewData(movieId);
        TestUtilities.insertCrewData(mContext, movieId);
        MovieDbHelper dbHelper = new MovieDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor c = db.query(CrewEntry.TABLE_NAME,null,null,null,null,null,null);

        assertTrue("Empty cursor returned at CrewEntry", c.moveToFirst());

        for (ContentValues values : listCv) {
            TestUtilities.validateCurrentRecord("Invalid data at CrewEntry", c, values);
            c.moveToNext();
        }
        c.close();
        dbHelper.close();
    }
}
