package japps.trendymovies.db;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static japps.trendymovies.db.MovieDbContract.CastEntry;
import static japps.trendymovies.db.MovieDbContract.CrewEntry;
import static japps.trendymovies.db.MovieDbContract.MovieEntry;
import static japps.trendymovies.db.MovieDbContract.ReviewEntry;
import static japps.trendymovies.db.MovieDbContract.TrailerEntry;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Julio on 5/8/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TestProvider {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();
    private Context mContext;
    private MovieDbHelper mHelper;

    @Before
    public void setUp() {
        mContext = InstrumentationRegistry.getTargetContext();
        deleteAllRecordsFromProvider();
    }

    @Test
    public void deleteAllRecordsFromProvider() {
        mContext.getContentResolver().delete(MovieEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(TrailerEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(ReviewEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(CastEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().delete(CrewEntry.CONTENT_URI, null, null);

        // Check Movie table records 
        Cursor cursor = mContext.getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null,
                null);
        assertEquals("Error: Records not deleted from Movie table during delete", 0, cursor.getCount());
        cursor.close();

        // Check Trailer table records 
        cursor = mContext.getContentResolver().query(TrailerEntry.CONTENT_URI, null, null, null,
                null);
        assertEquals("Error: Records not deleted from Trailer table during delete", 0, cursor.getCount());
        cursor.close();

        // Check Review table records 
        cursor = mContext.getContentResolver().query(ReviewEntry.CONTENT_URI, null, null, null,
                null);
        assertEquals("Error: Records not deleted from Review table during delete", 0, cursor.getCount());
        cursor.close();

        // Check Cast table records
        cursor = mContext.getContentResolver().query(CastEntry.CONTENT_URI, null, null, null,
                null);
        assertEquals("Error: Records not deleted from Cast table during delete", 0, cursor.getCount());
        cursor.close();

        // Check Crew table records 
        cursor = mContext.getContentResolver().query(CrewEntry.CONTENT_URI, null, null, null,
                null);
        assertEquals("Error: Records not deleted from Crew table during delete", 0, cursor.getCount());
        cursor.close();
    }

    @Test
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();

        // We define the component name based on the package name from the context and the
        // WeatherProvider class.
        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                MovieDbProvider.class.getName());
        try {
            // Fetch the provider info using the component name from the PackageManager
            // This throws an exception if the provider isn't registered.
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);

            // Make sure that the registered authority matches the authority from the Contract.
            assertEquals("Error: MovieDbProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + MovieDbContract.CONTENT_AUTHORITY,
                    providerInfo.authority, MovieDbContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            // I guess the provider isn't registered correctly.
            assertTrue("Error: MovieDbProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }

    @Test
    public void testGetType() {
        // content://japps.trendymovies/movie/
        String type = mContext.getContentResolver().getType(MovieEntry.CONTENT_URI);
        assertEquals("Error: the MovieEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                MovieEntry.CONTENT_TYPE, type);

        long movieId = 209112;

        // content://japps.trendymovies/full_movie/209112
        type = mContext.getContentResolver().getType(MovieEntry.buildFullMovieUri(movieId));
        assertEquals("Error: the MovieEntry CONTENT_URI should return .../full_movie/209112",
                MovieEntry.FULL_MOVIE_CONTENT_TYPE, type);

        // content://japps.trendymovies/movie_cast_crew/209112
        type = mContext.getContentResolver().getType(MovieEntry.buildMovieCastCrewUri(movieId));
        assertEquals("Error: the MovieEntry CONTENT_URI should return .../movie_cast_crew/209112",
                MovieEntry.CAST_CREW_CONTENT_TYPE, type);

        // content://japps.trendymovies/review/
        type = mContext.getContentResolver().getType(TrailerEntry.CONTENT_URI);
        assertEquals("Error: the TrailerEntry CONTENT_URI should return TrailerEntry.CONTENT_TYPE",
                TrailerEntry.CONTENT_TYPE, type);

        // content://japps.trendymovies/cast/
        type = mContext.getContentResolver().getType(ReviewEntry.CONTENT_URI);
        assertEquals("Error: the ReviewEntry CONTENT_URI should return ReviewEntry.CONTENT_TYPE",
                ReviewEntry.CONTENT_TYPE, type);

        // content://japps.trendymovies/crew/
        type = mContext.getContentResolver().getType(CastEntry.CONTENT_URI);
        assertEquals("Error: the CastEntry CONTENT_URI should return CastEntry.CONTENT_TYPE",
                CastEntry.CONTENT_TYPE, type);

        // content://japps.trendymovies/trailer/
        type = mContext.getContentResolver().getType(CrewEntry.CONTENT_URI);
        assertEquals("Error: the CrewEntry CONTENT_URI should return CrewEntry.CONTENT_TYPE",
                CrewEntry.CONTENT_TYPE, type);
    }

    @Test
    public void testMovieInsert() {
        mHelper = new MovieDbHelper(mContext);
        ContentValues cv = TestUtilities.createMovieData();
        ContentResolver cr = mContext.getContentResolver();
        String movieId;
        Uri movieUri;
        Cursor cursor;
        String[] movieIdArg = new String[1];

        // Testing Movie insert
        movieUri = cr.insert(MovieEntry.CONTENT_URI, cv);
        movieId = MovieEntry.getMovieIdFromUri(movieUri);
        movieIdArg[0] = movieId;

        cursor = cr.query(MovieEntry.CONTENT_URI, null, MovieDbProvider.sTableMovieIdSelection,
                movieIdArg, null);
        assertNotNull("Empty cursor was returned at Movie insert test.", cursor);
        assertTrue("No Movie data was found at Movie table with ID: " + movieId,
                cursor.moveToFirst());
        TestUtilities.validateCurrentRecord("Data mismatch at Movie insert test", cursor, cv);
        cursor.close();
    }

    @Test
    public void testBulkInserts() {
        mHelper = new MovieDbHelper(mContext);
        ContentValues cv = TestUtilities.createMovieData();
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor;
        Uri movieUri = cr.insert(MovieEntry.CONTENT_URI, cv);
        String movieId = MovieEntry.getMovieIdFromUri(movieUri);
        String[] movieIdArg = {movieId};
        int rowsInserted;

        // Test Trailer insert
        ContentValues[] trailerList = TestUtilities.createTrailerData(
                Long.parseLong(movieId)).toArray(new ContentValues[2]);
        rowsInserted = cr.bulkInsert(TrailerEntry.CONTENT_URI, trailerList);
        assertTrue("Error at Trailer: Num rows inserted: " + rowsInserted + " Num rows expected: " + trailerList.length,
                rowsInserted == trailerList.length);
        cursor = cr.query(TrailerEntry.CONTENT_URI, null, null, movieIdArg, null);
        assertNotNull("Empty cursor was returned at Trailer insert test.", cursor);
        assertTrue("No Trailer data was found at Trailer table with ID: " + movieId,
                cursor.moveToFirst());
        for (int j = 0; j < trailerList.length; j++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("Data mismatch at Trailer insert test",
                    cursor, trailerList[j]);
        }
        cursor.close();

        // Test Review insert
        ContentValues[] reviewList = TestUtilities.createReviewData(
                Long.parseLong(movieId)).toArray(new ContentValues[2]);
        rowsInserted = cr.bulkInsert(ReviewEntry.CONTENT_URI, reviewList);
        assertTrue("Error at Review: Num rows inserted: " + rowsInserted + " Num rows expected: " + reviewList.length,
                rowsInserted == reviewList.length);
        cursor = cr.query(ReviewEntry.CONTENT_URI, null, null, movieIdArg, null);
        assertNotNull("Empty cursor was returned at Review insert test.", cursor);
        assertTrue("No Review data was found at Review table with ID: " + movieId,
                cursor.moveToFirst());
        for (int j = 0; j < reviewList.length; j++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("Data mismatch at Review insert test",
                    cursor, reviewList[j]);
        }
        cursor.close();

        // Test Cast insert
        ContentValues[] castList = TestUtilities.createCastData(
                Long.parseLong(movieId)).toArray(new ContentValues[3]);
        rowsInserted = cr.bulkInsert(CastEntry.CONTENT_URI, castList);
        assertTrue("Error at Cast: Num rows inserted: " + rowsInserted + " Num rows expected: " + castList.length,
                rowsInserted == castList.length);
        cursor = cr.query(CastEntry.CONTENT_URI, null, null, movieIdArg, null);
        assertNotNull("Empty cursor was returned at Cast insert test.", cursor);
        assertTrue("No Cast data was found at Cast table with ID: " + movieId,
                cursor.moveToFirst());
        for (int j = 0; j < castList.length; j++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("Data mismatch at Cast insert test",
                    cursor, castList[j]);
        }
        cursor.close();

        // Test Crew insert
        ContentValues[] crewList = TestUtilities.createCrewData(
                Long.parseLong(movieId)).toArray(new ContentValues[2]);
        rowsInserted = cr.bulkInsert(CrewEntry.CONTENT_URI, crewList);
        assertTrue("Error at Crew: Num rows inserted: " + rowsInserted + " Num rows expected: " + crewList.length,
                rowsInserted == crewList.length);
        cursor = cr.query(CrewEntry.CONTENT_URI, null, null, movieIdArg, null);
        assertNotNull("Empty cursor was returned at Crew insert test.", cursor);
        assertTrue("No Crew data was found at Crew table with ID: " + movieId,
                cursor.moveToFirst());
        for (int j = 0; j < crewList.length; j++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("Data mismatch at Crew insert test",
                    cursor, crewList[j]);
        }
        cursor.close();
    }

    @Test
    public void testMovieListQuery() {
        mHelper = new MovieDbHelper(mContext);
        ContentResolver cr = mContext.getContentResolver();
        String[] columns = {MovieEntry.COLUMN_POSTER_PATH, MovieEntry.COLUMN_RELEASE_DATE, 
                MovieEntry.COLUMN_POPULARITY, MovieEntry.COLUMN_VOTE_AVERAGE};
        ContentValues movieData1 = TestUtilities.createMovieData();        
        ContentValues movieData2 = TestUtilities.createMovieData2();
        ContentValues fetch1 = new ContentValues();
        fetch1.put(MovieEntry.COLUMN_POSTER_PATH, movieData1.getAsString(MovieEntry.COLUMN_POSTER_PATH));
        fetch1.put(MovieEntry.COLUMN_RELEASE_DATE, movieData1.getAsString(MovieEntry.COLUMN_RELEASE_DATE));
        fetch1.put(MovieEntry.COLUMN_POPULARITY, movieData1.getAsString(MovieEntry.COLUMN_POPULARITY));
        fetch1.put(MovieEntry.COLUMN_VOTE_AVERAGE, movieData1.getAsString(MovieEntry.COLUMN_VOTE_AVERAGE));
        ContentValues fetch2 = new ContentValues();
        fetch2.put(MovieEntry.COLUMN_POSTER_PATH, movieData2.getAsString(MovieEntry.COLUMN_POSTER_PATH));
        fetch2.put(MovieEntry.COLUMN_RELEASE_DATE, movieData2.getAsString(MovieEntry.COLUMN_RELEASE_DATE));
        fetch2.put(MovieEntry.COLUMN_POPULARITY, movieData2.getAsString(MovieEntry.COLUMN_POPULARITY));
        fetch2.put(MovieEntry.COLUMN_VOTE_AVERAGE, movieData2.getAsString(MovieEntry.COLUMN_VOTE_AVERAGE));

        TestUtilities.insertMovieData(mContext);
        TestUtilities.insertMovieData2(mContext);

        // Fetch list of Movies
        Cursor cursor = cr.query(MovieEntry.CONTENT_URI, columns, null, null, null);
        assertNotNull("Empty cursor was returned at Movie List Query.", cursor);
        assertTrue("No Movie data was found at Movie List Query",
                cursor.moveToFirst());
        TestUtilities.validateCurrentRecord("Mismatch at Movie List Query [1]",
                cursor, fetch1);
        cursor.moveToNext();
        TestUtilities.validateCurrentRecord("Mismatch at Movie List Query [2]",
                cursor, fetch2);
    }

    @Test
    public void testUpdate(){
        mHelper = new MovieDbHelper(mContext);
        ContentValues movieData1 = TestUtilities.createMovieData();
        ContentValues movieData2 = TestUtilities.createMovieData2();
        ContentResolver cr = mContext.getContentResolver();
        String movieId = Long.toString(TestUtilities.TEST_MOVIE_1_ID);
        String movieId2 = Long.toString(TestUtilities.TEST_MOVIE_2_ID);
        Uri movieUri;
        Cursor cursor;
        String[] movieIdArg = {movieId};
        String[] movieIdArg2 = {movieId2};

        // Inserting Movie # 1
        cr.insert(MovieEntry.CONTENT_URI, movieData1);
        cursor = cr.query(MovieEntry.CONTENT_URI, null, MovieDbProvider.sTableMovieIdSelection,
                movieIdArg, null);
        assertNotNull("Empty cursor was returned at Update test. Phase Inserting Movie # 1",
                cursor);
        assertTrue("No Movie data was found at Movie table with ID: " + movieId+
                " Phase Inserting Movie # 1", cursor.moveToFirst());
        TestUtilities.validateCurrentRecord("Data mismatch at Update test. Phase Inserting Movie # 1",
                cursor, movieData1);
        cursor.close();

        // Updating Movie # 1 with data from Movie # 2
        int rowsAffected = cr.update(MovieEntry.CONTENT_URI, movieData2, null,movieIdArg);
        assertTrue("No rows were updated. Phase Updating Movie # 1", rowsAffected > 0);

        // Checking updated data to match with Movie # 2
        cursor = cr.query(MovieEntry.CONTENT_URI, null, MovieDbProvider.sTableMovieIdSelection,
                movieIdArg2, null);
        assertNotNull("Empty cursor was returned at Update test. Phase Checking Movie # 2",
                cursor);
        assertTrue("No Movie data was found at Movie table with ID: " + movieId2+
                " Phase Inserting Movie # 2", cursor.moveToFirst());
        TestUtilities.validateCurrentRecord("Data mismatch at Update test. Phase Checking Movie # 2",
                cursor, movieData2);
        cursor.close();
    }

    @Test
    public void testDelete(){
        mHelper = new MovieDbHelper(mContext);
        ContentValues cv = TestUtilities.createMovieData();
        ContentValues cv2 = TestUtilities.createMovieData2();
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor;
        Uri movieUri = cr.insert(MovieEntry.CONTENT_URI, cv);
        Uri movieUri2 = cr.insert(MovieEntry.CONTENT_URI, cv2);
        String movieId = MovieEntry.getMovieIdFromUri(movieUri);
        String movieId2 = MovieEntry.getMovieIdFromUri(movieUri2);
        String[] movieIdArg = {movieId};
        String[] movieIdArg2 = {movieId2};
        int rowsInserted;
        ContentValues[] trailerList1 = TestUtilities.createTrailerData(
                Long.parseLong(movieId)).toArray(new ContentValues[2]);
        ContentValues[] trailerList2 = TestUtilities.createTrailerData2(
                Long.parseLong(movieId2)).toArray(new ContentValues[2]);

        // Inserting Trailer Movie # 1. Phase # 1
        rowsInserted = cr.bulkInsert(TrailerEntry.CONTENT_URI, trailerList1);
        assertTrue("Error at Delete test. Phase # 1: Num rows inserted: " + rowsInserted + " Num rows expected: " + trailerList1.length,
                rowsInserted == trailerList1.length);

        // Checking Trailer Movie # 1 data. Phase # 2
        cursor = cr.query(TrailerEntry.CONTENT_URI, null, null, movieIdArg, null);
        assertNotNull("Empty cursor was returned at Delete test. Phase # 2", cursor);
        assertTrue("No Trailer data was found at Delete test with ID: " + movieId+
                " Phase # 2",
                cursor.moveToFirst());
        for (int j = 0; j < trailerList1.length; j++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("Data mismatch at Delete test. Phase # 2",
                    cursor, trailerList1[j]);
        }
        cursor.close();

        // Inserting Trailer Movie # 2. Phase # 3
        rowsInserted = cr.bulkInsert(TrailerEntry.CONTENT_URI, trailerList2);
        assertTrue("Error at Delete test. Phase # 3: Num rows inserted: " + rowsInserted + " Num rows expected: " + trailerList1.length,
                rowsInserted == trailerList2.length);
        // Checking Trailer Movie # 2 data. Phase # 4
        cursor = cr.query(TrailerEntry.CONTENT_URI, null, null, movieIdArg2, null);
        assertNotNull("Empty cursor was returned at Delete test. Phase # 4", cursor);
        assertTrue("No Trailer data was found at Delete test with ID: " + movieId2+
                        " Phase # 4",
                cursor.moveToFirst());
        for (int j = 0; j < trailerList2.length; j++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("Data mismatch at Delete test. Phase # 4",
                    cursor, trailerList2[j]);
        }
        cursor.close();

        // Deleting Trailer Movie # 1. Phase # 5
        int rowsDeleted = cr.delete(TrailerEntry.CONTENT_URI, MovieDbProvider.sTableTrailerIdSelection,
                movieIdArg);
        assertTrue("No row was deleted at Delete test. Phase # 5", rowsDeleted > 0);

        // Checking Trailer Movie # 1 again. Phase # 6
        cursor = cr.query(TrailerEntry.CONTENT_URI, null, null, movieIdArg, null);
        assertNotNull("Empty cursor was returned at Delete test. Phase # 6", cursor);
        assertFalse("Cursor should not have data at Delete test. Phase # 6",
                cursor.moveToFirst());
        cursor.close();

        // Checking Trailer Movie # 2 data again. Phase # 7
        cursor = cr.query(TrailerEntry.CONTENT_URI, null, null, movieIdArg2, null);
        assertNotNull("Empty cursor was returned at Delete test. Phase # 7", cursor);
        assertTrue("No Trailer data was found at Delete test with ID: " + movieId2+
                        " Phase # 7",
                cursor.moveToFirst());
        for (int j = 0; j < trailerList2.length; j++, cursor.moveToNext()) {
            TestUtilities.validateCurrentRecord("Data mismatch at Delete test. Phase # 7",
                    cursor, trailerList2[j]);
        }
        cursor.close();
    }
}
