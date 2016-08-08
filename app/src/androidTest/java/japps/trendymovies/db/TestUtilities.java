package japps.trendymovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.runner.AndroidJUnit4;

import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static japps.trendymovies.db.MovieDbContract.CastEntry;
import static japps.trendymovies.db.MovieDbContract.CrewEntry;
import static japps.trendymovies.db.MovieDbContract.MovieEntry;
import static japps.trendymovies.db.MovieDbContract.ReviewEntry;
import static japps.trendymovies.db.MovieDbContract.TrailerEntry;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Julio on 6/8/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TestUtilities {

    public static final long TEST_MOVIE_1_ID = 209112;
    public static final long TEST_MOVIE_2_ID = 254234;
    static boolean verifyTableColumns (Cursor cursor, HashSet columnHashSet){
        int columnNameIndex = cursor.getColumnIndex("name");
        do {
            String columnName = cursor.getString(columnNameIndex);
            columnHashSet.remove(columnName);
        } while(cursor.moveToNext());
        return columnHashSet.isEmpty();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        //assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int cursorIdx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, cursorIdx == -1);
            String expectedValue = entry.getValue().toString();
            String obtainedValue = valueCursor.getString(cursorIdx);
            assertEquals("Value '" + obtainedValue +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, obtainedValue);
        }
    }

    static ContentValues createMovieData2 () {
        ContentValues cv = new ContentValues();
        cv.put(MovieEntry.COLUMN_BACKDROP_PATH, "/vsjBeMPZtyB7yNsYY56XYxifaQZ.jpg");
        cv.put(MovieEntry.COLUMN_BUDGET, 300000000);
        cv.put(MovieEntry.COLUMN_GENRES, "Drama, Comedy");
        cv.put(MovieEntry.COLUMN_MOVIE_ID, TEST_MOVIE_2_ID);
        cv.put(MovieEntry.COLUMN_ORIGINAL_LANG, "en");
        cv.put(MovieEntry.COLUMN_ORIGINAL_TITLE, "Me before you");
        cv.put(MovieEntry.COLUMN_OVERVIEW, "Some overview");
        cv.put(MovieEntry.COLUMN_POPULARITY, 35.568);
        cv.put(MovieEntry.COLUMN_POSTER_PATH, "/cGOPbv9wA5gEejkUN892JrveARt.jpg");
        cv.put(MovieEntry.COLUMN_RELEASE_DATE, "2016-06-12");
        cv.put(MovieEntry.COLUMN_REVENUE, 602000000);
        cv.put(MovieEntry.COLUMN_RUNTIME, 130);
        cv.put(MovieEntry.COLUMN_TITLE, "Me before you");
        cv.put(MovieEntry.COLUMN_VOTE_AVERAGE, 6.3);
        cv.put(MovieEntry.COLUMN_VOTE_COUNT, 5032);
        return cv;
    }

    static long insertMovieData2 (Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieData2();

        long movieRowId = db.insert(MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Movie Values", movieRowId != -1);
        db.close();
        return testValues.getAsLong(MovieEntry.COLUMN_MOVIE_ID);
    }

    static ContentValues createMovieData () {
        ContentValues cv = new ContentValues();
        cv.put(MovieEntry.COLUMN_BACKDROP_PATH, "/vsjBeMPZtyB7yNsYY56XYxifaQZ.jpg");
        cv.put(MovieEntry.COLUMN_BUDGET, 250000000);
        cv.put(MovieEntry.COLUMN_GENRES, "Action, Adventure, Fantasy");
        cv.put(MovieEntry.COLUMN_MOVIE_ID, TEST_MOVIE_1_ID);
        cv.put(MovieEntry.COLUMN_ORIGINAL_LANG, "en");
        cv.put(MovieEntry.COLUMN_ORIGINAL_TITLE, "Batman v Superman: Dawn of Justice");
        cv.put(MovieEntry.COLUMN_OVERVIEW, "Fearing the actions of a god-like Super Hero left unchecked, Gotham City’s own formidable, forceful vigilante takes on Metropolis’s most revered, modern-day savior, while the world wrestles with what sort of hero it really needs. And with Batman and Superman at war with one another, a new threat quickly arises, putting mankind in greater danger than it’s ever known before.");
        cv.put(MovieEntry.COLUMN_POPULARITY, 46.702);
        cv.put(MovieEntry.COLUMN_POSTER_PATH, "/cGOPbv9wA5gEejkUN892JrveARt.jpg");
        cv.put(MovieEntry.COLUMN_RELEASE_DATE, "2016-03-23");
        cv.put(MovieEntry.COLUMN_REVENUE, 872000000);
        cv.put(MovieEntry.COLUMN_RUNTIME, 151);
        cv.put(MovieEntry.COLUMN_TITLE, "Batman v Superman: Dawn of Justice");
        cv.put(MovieEntry.COLUMN_VOTE_AVERAGE, 5.5);
        cv.put(MovieEntry.COLUMN_VOTE_COUNT, 3051);
        return cv;
    }

    static long insertMovieData (Context context) {
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieData();

        long movieRowId = db.insert(MovieEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert Movie Values", movieRowId != -1);
        db.close();
        return testValues.getAsLong(MovieEntry.COLUMN_MOVIE_ID);
    }

    static List<ContentValues> createTrailerData (long movieId){
        ContentValues cv = new ContentValues();
        List<ContentValues> listCv = new ArrayList<>();
        cv.put(TrailerEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(TrailerEntry.COLUMN_TITLE, "Exclusive Sneak");
        cv.put(TrailerEntry.COLUMN_SOURCE, "6as8ahAr1Uc");
        listCv.add(cv);
        cv = new ContentValues();
        cv.put(TrailerEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(TrailerEntry.COLUMN_TITLE, "Official Comic-Con Trailer");
        cv.put(TrailerEntry.COLUMN_SOURCE, "0WWzgGyAH6Y");
        listCv.add(cv);
        return listCv;
    }

    static void insertTrailerData(Context context, long movieId){
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<ContentValues> listCv = TestUtilities.createTrailerData(movieId);
        for (ContentValues cv : listCv) {
            long trailerRowId = db.insert(TrailerEntry.TABLE_NAME, null, cv);
            // Verify we got a row back.
            assertTrue("Error: Failure to insert Trailer Values.", trailerRowId != -1);
        }
        db.close();
    }

    static List<ContentValues> createTrailerData2 (long movieId){
        ContentValues cv = new ContentValues();
        List<ContentValues> listCv = new ArrayList<>();
        cv.put(TrailerEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(TrailerEntry.COLUMN_TITLE, "Official Trailer 1");
        cv.put(TrailerEntry.COLUMN_SOURCE, "6as8ahAr1Uc");
        listCv.add(cv);
        cv = new ContentValues();
        cv.put(TrailerEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(TrailerEntry.COLUMN_TITLE, "Official Trailer 2");
        cv.put(TrailerEntry.COLUMN_SOURCE, "0WWzgGyAH6Y");
        listCv.add(cv);
        return listCv;
    }

    static void insertTrailerData2 (Context context, long movieId){
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<ContentValues> listCv = TestUtilities.createTrailerData2(movieId);
        for (ContentValues cv : listCv) {
            long trailerRowId = db.insert(TrailerEntry.TABLE_NAME, null, cv);
            // Verify we got a row back.
            assertTrue("Error: Failure to insert Trailer Values.", trailerRowId != -1);
        }
        db.close();
    }

    static List<ContentValues> createReviewData (long movieId){
        List<ContentValues> listCv = new ArrayList<>();
        ContentValues cv = new ContentValues();
        cv.put(ReviewEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(ReviewEntry.COLUMN_AUTHOR, "Rahul Gupta");
        cv.put(ReviewEntry.COLUMN_CONTENT, "Awesome moview. Best Action sequence.\\r\\n\\r\\n**Slow in the first half**");
        listCv.add(cv);
        cv = new ContentValues();
        cv.put(ReviewEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(ReviewEntry.COLUMN_AUTHOR, "shekzilla");
        cv.put(ReviewEntry.COLUMN_CONTENT, "A bit slow but overall good. There are many sub plots in the movie that could've been made into independent movies of their own.");
        listCv.add(cv);
        return listCv;
    }

    static void insertReviewData(Context context, long movieId){
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<ContentValues> listCv = TestUtilities.createReviewData(movieId);
        for (ContentValues cv : listCv) {
            long reviewRowId = db.insert(ReviewEntry.TABLE_NAME, null, cv);
            // Verify we got a row back.
            assertTrue("Error: Failure to insert Review Values.", reviewRowId != -1);
        }
        db.close();
    }

    static List<ContentValues> createCastData (long movieId){
        List<ContentValues> listCv = new ArrayList<>();
        ContentValues cv = new ContentValues();
        cv.put(CastEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(CastEntry.COLUMN_NAME, "Ben Affleck");
        cv.put(CastEntry.COLUMN_CHARACTER, "Bruce Wayne / Batman");
        cv.put(CastEntry.COLUMN_PROFILE_PATH, "/yXtyygmSGtrwTfEmr6g2WgHFJIZ.jpg");
        listCv.add(cv);
        cv = new ContentValues();
        cv.put(CastEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(CastEntry.COLUMN_NAME, "Henry Cavill");
        cv.put(CastEntry.COLUMN_CHARACTER, "Clark Kent / Superman");
        cv.put(CastEntry.COLUMN_PROFILE_PATH, "/qDJ3TIIHnaE9x6GUt9QlDXi3KRZ.jpg");
        listCv.add(cv);
        cv = new ContentValues();
        cv.put(CastEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(CastEntry.COLUMN_NAME, "Amy Adams");
        cv.put(CastEntry.COLUMN_CHARACTER, "Lois Lane");
        cv.put(CastEntry.COLUMN_PROFILE_PATH, "/tk5eWJcOBr9uRefeUm9ntvehbLA.jpg");
        listCv.add(cv);
        return listCv;
    }

    static void insertCastData(Context context, long movieId){
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<ContentValues> listCv = TestUtilities.createCastData(movieId);
        for (ContentValues cv : listCv) {
            long castRowId = db.insert(CastEntry.TABLE_NAME, null, cv);
            // Verify we got a row back.
            assertTrue("Error: Failure to insert Cast Values.", castRowId != -1);
        }
        db.close();
    }

    static List<ContentValues> createCrewData (long movieId){
        List<ContentValues> listCv = new ArrayList<>();
        ContentValues cv = new ContentValues();
        cv.put(CrewEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(CrewEntry.COLUMN_NAME, "Jay Oliva");
        cv.put(CrewEntry.COLUMN_JOB, "Storyboard");
        cv.put(CrewEntry.COLUMN_PROFILE_PATH, "/az4rTiBwQlsUUK8WGNZpOI69QLo.jpg");
        listCv.add(cv);
        cv = new ContentValues();
        cv.put(CrewEntry.COLUMN_MOVIE_ID, movieId);
        cv.put(CrewEntry.COLUMN_NAME, "Zack Snyder");
        cv.put(CrewEntry.COLUMN_JOB, "Director");
        cv.put(CrewEntry.COLUMN_PROFILE_PATH, "/tlFsY1Ddv6d8JyftIKE3L71PD7g.jpg");
        listCv.add(cv);
        return listCv;
    }

    static void insertCrewData(Context context, long movieId){
        // insert our test records into the database
        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<ContentValues> listCv = TestUtilities.createCrewData(movieId);
        for (ContentValues cv : listCv) {
            long crewRowId = db.insert(CrewEntry.TABLE_NAME, null, cv);
            // Verify we got a row back.
            assertTrue("Error: Failure to insert Crew Values.", crewRowId != -1);
        }
        db.close();
    }
}
