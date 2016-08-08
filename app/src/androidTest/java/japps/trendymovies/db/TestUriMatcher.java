package japps.trendymovies.db;

import android.content.UriMatcher;
import android.net.Uri;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static japps.trendymovies.db.MovieDbContract.*;
import static junit.framework.Assert.assertEquals;

/**
 * Created by Julio on 5/8/2016.
 */
@RunWith(AndroidJUnit4.class)
public class TestUriMatcher {
    private static final long TEST_MOVIE_ID = 15217;
    private static final Uri TEST_MOVIE_DIR = MovieEntry.CONTENT_URI;
    private static final Uri TEST_FULL_MOVIE_DIR = MovieEntry.buildFullMovieUri(TEST_MOVIE_ID);
    private static final Uri TEST_MOVIE_CAST_CREW_DIR = MovieEntry.buildMovieCastCrewUri(TEST_MOVIE_ID);
    private static final Uri TEST_TRAILER_DIR = TrailerEntry.CONTENT_URI;
    private static final Uri TEST_REVIEW_DIR = ReviewEntry.CONTENT_URI;
    private static final Uri TEST_CAST_DIR = CastEntry.CONTENT_URI;
    private static final Uri TEST_CREW_DIR = CrewEntry.CONTENT_URI;

    @Test
    public void testUriMatcher() {
        UriMatcher testMatcher = MovieDbProvider.buildUriMatcher();
        assertEquals("Error: The MOVIE URI was matched incorrectly.",
                MovieDbProvider.MOVIE, testMatcher.match(TEST_MOVIE_DIR));
        assertEquals("Error: The FULL_MOVIE URI was matched incorrectly.",
                MovieDbProvider.MOVIE_FULL_DATA, testMatcher.match(TEST_FULL_MOVIE_DIR));
        assertEquals("Error: The MOVIE_CAST_CREW URI was matched incorrectly.",
                MovieDbProvider.MOVIE_WITH_CAST_CREW, testMatcher.match(TEST_MOVIE_CAST_CREW_DIR));
        assertEquals("Error: The TRAILER URI was matched incorrectly.",
                MovieDbProvider.TRAILER, testMatcher.match(TEST_TRAILER_DIR));
        assertEquals("Error: The REVIEW URI was matched incorrectly.",
                MovieDbProvider.REVIEW, testMatcher.match(TEST_REVIEW_DIR));
        assertEquals("Error: The CAST URI was matched incorrectly.",
                MovieDbProvider.CAST, testMatcher.match(TEST_CAST_DIR));
        assertEquals("Error: The CREW URI was matched incorrectly.",
                MovieDbProvider.CREW, testMatcher.match(TEST_CREW_DIR));
    }
}
