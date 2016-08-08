package japps.trendymovies.db;


import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static japps.trendymovies.db.MovieDbContract.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Julio on 5/8/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TestMovieContract {
    private static final long TEST_MOVIE_ID = 15217;

    @Test
    public void testBuildMovieUri() {
        Uri movieUri = MovieEntry.buildMovieUri(TEST_MOVIE_ID);
        assertNotNull("Error: Null Uri returned.",movieUri);
        assertEquals("Error: Movie ID not properly appended to the end of the Uri",
                Long.toString(TEST_MOVIE_ID), movieUri.getLastPathSegment());
        assertEquals("Error: Movie Uri doesn't match the expected result",
                movieUri.toString(),
                "content://japps.trendymovies/movie/15217");
    }
}
