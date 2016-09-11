package japps.trendymovies.utility;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import japps.trendymovies.R;
import japps.trendymovies.activity.MainActivity;
import japps.trendymovies.activity.MovieDetailActivity;
import japps.trendymovies.adapter.MovieDetailPagerAdapter;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.db.MovieDbProvider;
import japps.trendymovies.fragment.MovieDetailsFragment;
import japps.trendymovies.fragment.MovieOverviewFragment;
import japps.trendymovies.fragment.MovieReviewFragment;
import japps.trendymovies.fragment.MovieTrailerFragment;

import static japps.trendymovies.db.MovieDbContract.CastEntry;
import static japps.trendymovies.db.MovieDbContract.CrewEntry;
import static japps.trendymovies.db.MovieDbContract.MovieEntry;
import static japps.trendymovies.db.MovieDbContract.ReviewEntry;
import static japps.trendymovies.db.MovieDbContract.TrailerEntry;

/**
 * Created by Julio on 10/2/2016.
 */
public class Utilities {

    public static String getYear(String dateStr) {
        try {
            Date date = new SimpleDateFormat("yyyy-mm-dd", Locale.ENGLISH).parse(dateStr);
            return new SimpleDateFormat("yyyy", Locale.getDefault()).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateStr;
    }

    public static String createStatisticsRow(String date, int duration, double rate) {
        return getYear(date) + " | " + duration + " min | " + rate + "/10";
    }

    public static String getLocale() {
        return Resources.getSystem().getConfiguration().locale.toString().substring(0, 2);
    }

    public static String formatGenres(ArrayList<String> genresList) {
        String stringGenres = "-";
        if (genresList != null && !genresList.isEmpty()) {
            stringGenres = genresList.toString().replaceAll("[\\[&\\]]", "");
        }
        return stringGenres;
    }

    public static String formatDate(String strDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(strDate);
            return capitalize(DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public static String formatPopularity(double popularity) {
        return String.format(Locale.getDefault(), "%.1f", popularity);
    }

    public static String formatRuntime(int runtime) {
        if (runtime == 0) {
            return "-";
        }
        if (runtime >= 60 & runtime <= 119) {
            if (runtime == 60) {
                return "1h";
            }
            return "1h " + (runtime - 60) + "m";
        }

        if (runtime >= 120 & runtime <= 179) {
            if (runtime == 120) {
                return "2h";
            }
            return "2h " + (runtime - 120) + "m";
        }

        if (runtime >= 180 & runtime <= 239) {
            if (runtime == 180) {
                return "3h";
            }
            return "3h " + (runtime - 180) + "m";
        }

        return Integer.toString(runtime);
    }

    public static String formatLanguage(String langCode) {
        return capitalize(new Locale(langCode).getDisplayLanguage());
    }

    public static String capitalize(String text) {
        if (!text.isEmpty()) {
            return text.substring(0, 1).toUpperCase() + text.substring(1);
        }
        return text;
    }

    public static String formatCurrency(long value) {
        if (value == 0) {
            return "-";
        }
        return NumberFormat.getCurrencyInstance().format(value);
    }

    public static String getToday(String pattern) {
        return new SimpleDateFormat(pattern).format(new Date());
    }

    public static String getDateFrom60days(String pattern) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -60);
        Date date = calendar.getTime();
        return new SimpleDateFormat(pattern).format(date);
    }

    public static boolean isFavourite(Context context, String movieId) {
        boolean isFav = false;
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(MovieEntry.CONTENT_URI,
                new String[]{MovieEntry.COLUMN_MOVIE_ID},
                MovieDbProvider.sTableMovieIdSelection,
                new String[]{movieId},
                null);
        if (cursor != null) {
            isFav = cursor.moveToFirst();
            cursor.close();
        }
        return isFav;
    }

    public static void addFavourite(Context context, Bundle extras) throws SQLException {
        ContentResolver cr = context.getContentResolver();
        ContentValues movieValues = buildDetailContentValues(context,
                extras.getBundle(MovieData.DETAILS_PARAM));
        Uri returnedUri = cr.insert(MovieEntry.CONTENT_URI, movieValues);
        long movieId = Long.parseLong(MovieEntry.getMovieIdFromUri(returnedUri));
        int rowsInserted = 0;

        // Trailer ContentValues[]
        Bundle trailerBundle = extras.getBundle(MovieData.TRAILERS_PARAM);
        int trailerCount = trailerBundle.getInt(MovieData.TRAILERS_COUNT);
        ContentValues[] trailerValues = new ContentValues[trailerCount];
        ArrayList<String> listTitle = trailerBundle.getStringArrayList(MovieData.TRAILER_TITLE_PARAM);
        ArrayList<String> listSource = trailerBundle.getStringArrayList(MovieData.TRAILER_SOURCE_PARAM);
        ArrayList<String> listThumbnail = trailerBundle.getStringArrayList(MovieData.TRAILER_THUMBNAIL_PARAM);
        if (listTitle != null && listSource != null && listThumbnail != null) {
            for (int j = 0; j < trailerCount; j++) {
                ContentValues values = new ContentValues();
                values.put(TrailerEntry.COLUMN_MOVIE_ID, movieId);
                values.put(TrailerEntry.COLUMN_TITLE, listTitle.get(j));
                values.put(TrailerEntry.COLUMN_SOURCE, listSource.get(j));
                values.put(TrailerEntry.COLUMN_THUMBNAIL_PATH, listThumbnail.get(j));
                trailerValues[j] = values;
            }
            rowsInserted = cr.bulkInsert(TrailerEntry.CONTENT_URI, trailerValues);
            if (rowsInserted == 0) {
                throw new SQLException("No trailer rows were inserted.");
            }
        }

        // Review ContentValues[]
        Bundle reviewBundle = extras.getBundle(MovieData.REVIEWS_PARAM);
        int reviewCount = reviewBundle.getInt(MovieData.REVIEWS_COUNT);
        ContentValues[] reviewValues = new ContentValues[reviewCount];
        ArrayList<String> authorList = reviewBundle.getStringArrayList(MovieData.REVIEWS_AUTHOR_PARAM);
        ArrayList<String> contentList = reviewBundle.getStringArrayList(MovieData.REVIEWS_CONTENT_PARAM);
        if (authorList != null && contentList != null) {
            for (int j = 0; j < reviewCount; j++) {
                ContentValues values = new ContentValues();
                values.put(ReviewEntry.COLUMN_MOVIE_ID, movieId);
                values.put(ReviewEntry.COLUMN_AUTHOR, authorList.get(j));
                values.put(ReviewEntry.COLUMN_CONTENT, contentList.get(j));
                reviewValues[j] = values;
            }
            rowsInserted = cr.bulkInsert(ReviewEntry.CONTENT_URI, reviewValues);
            if (rowsInserted == 0) {
                throw new SQLException("No review rows were inserted.");
            }
        }

        // Cast ContentValues[]
        Bundle castBundle = extras.getBundle(MovieData.CAST_PARAM);
        int castCount = castBundle.getInt(MovieData.PEOPLE_COUNT);
        ContentValues[] castValues = new ContentValues[castCount];
        ArrayList<String> nameList = castBundle.getStringArrayList(MovieData.CAST_NAME_PARAM);
        ;
        ArrayList<String> characterList = castBundle.getStringArrayList(MovieData.CAST_CHARACTER_PARAM);
        ArrayList<String> profileList = castBundle.getStringArrayList(MovieData.PROFILE_PATH_PARAM);
        if (nameList != null && characterList != null && profileList != null) {
            for (int j = 0; j < castCount; j++) {
                ContentValues values = new ContentValues();
                values.put(CastEntry.COLUMN_MOVIE_ID, movieId);
                values.put(CastEntry.COLUMN_NAME, nameList.get(j));
                values.put(CastEntry.COLUMN_CHARACTER, characterList.get(j));
                values.put(CastEntry.COLUMN_PROFILE_PATH, profileList.get(j));
                castValues[j] = values;
            }
            rowsInserted = cr.bulkInsert(CastEntry.CONTENT_URI, castValues);
            if (rowsInserted == 0) {
                throw new SQLException("No cast rows were inserted.");
            }
        }

        // Crew ContentValues[]
        Bundle crewBundle = extras.getBundle(MovieData.CREW_PARAM);
        int crewCount = crewBundle.getInt(MovieData.REVIEWS_COUNT);
        ContentValues[] crewValues = new ContentValues[crewCount];
        nameList = crewBundle.getStringArrayList(MovieData.CREW_NAME_PARAM);
        ArrayList<String> jobList = crewBundle.getStringArrayList(MovieData.CREW_JOB_PARAM);
        profileList = crewBundle.getStringArrayList(MovieData.PROFILE_PATH_PARAM);
        if (nameList != null && jobList != null && profileList != null) {
            for (int j = 0; j < crewCount; j++) {
                ContentValues values = new ContentValues();
                values.put(CrewEntry.COLUMN_MOVIE_ID, movieId);
                values.put(CrewEntry.COLUMN_NAME, nameList.get(j));
                values.put(CrewEntry.COLUMN_JOB, jobList.get(j));
                values.put(CrewEntry.COLUMN_PROFILE_PATH, profileList.get(j));
                crewValues[j] = values;
            }
            rowsInserted = cr.bulkInsert(CrewEntry.CONTENT_URI, crewValues);
            if (rowsInserted == 0) {
                throw new SQLException("No crew rows were inserted.");
            }
        }
        Log.d("Favourite", "Added!");

    }

    public static void removeFavourite(Context context, Bundle extras) throws SQLException {
        ContentResolver cr = context.getContentResolver();
        String movieId = Integer.toString(extras.getBundle(MovieData.DETAILS_PARAM)
                .getInt(MovieData.ID_PARAM));

        // Delete movie
        int rowsDeleted = cr.delete(MovieEntry.CONTENT_URI, MovieDbProvider.sTableMovieIdSelection,
                new String[]{movieId});
        if (rowsDeleted == 0) {
            throw new SQLException("No movie row was deleted.");
        }

        // Delete trailers
        cr.delete(TrailerEntry.CONTENT_URI, MovieDbProvider.sTableTrailerIdSelection,
                new String[]{movieId});

        // Delete reviews
        cr.delete(ReviewEntry.CONTENT_URI, MovieDbProvider.sTableReviewIdSelection,
                new String[]{movieId});

        // Delete cast
        rowsDeleted = cr.delete(CastEntry.CONTENT_URI, MovieDbProvider.sTableCastIdSelection,
                new String[]{movieId});
        if (rowsDeleted == 0) {
            throw new SQLException("No cast rows were deleted.");
        }

        // Delete crew
        rowsDeleted = cr.delete(CrewEntry.CONTENT_URI, MovieDbProvider.sTableCrewIdSelection,
                new String[]{movieId});
        if (rowsDeleted == 0) {
            throw new SQLException("No crew rows were deleted.");
        }
        Log.d("Favourite", "Removed!");
    }

    public static Bundle getFavouriteMovie(Context context, String movieId) throws SQLException {
        Bundle returnData = new Bundle();
        Bundle detailBundle = new Bundle();
        Bundle trailerBundle = new Bundle();
        Bundle reviewBundle = new Bundle();
        Bundle castBundle = new Bundle();
        Bundle crewBundle = new Bundle();
        ContentResolver cr = context.getContentResolver();

        // Fetch Detail Data
        Cursor cursor = cr.query(
                MovieEntry.CONTENT_URI,
                null,
                MovieDbProvider.sTableMovieIdSelection,
                new String[]{movieId},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            for (int j = 0; j < cursor.getCount(); j++) {
                detailBundle.putInt(MovieData.ID_PARAM, cursor.getInt(
                        cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID)));
                detailBundle.putString(MovieData.TITLE_PARAM, cursor.getString(
                        cursor.getColumnIndex(MovieEntry.COLUMN_TITLE)));
                detailBundle.putString(MovieData.GENRES_PARAM, cursor.getString(
                        cursor.getColumnIndex(MovieEntry.COLUMN_GENRES)));
                detailBundle.putString(MovieData.OVERVIEW_PARAM, cursor.getString(
                        cursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW)));
                detailBundle.putString(MovieData.POSTER_PATH_PARAM, cursor.getString(
                        cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH)));
                detailBundle.putByteArray(MovieData.POSTER_BLOB_PARAM, cursor.getBlob(
                        cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_BLOB)));
                detailBundle.putString(MovieData.BACKDROP_PATH_PARAM, cursor.getString(
                        cursor.getColumnIndex(MovieEntry.COLUMN_BACKDROP_PATH)));
                detailBundle.putString(MovieData.RELEASE_DATE_PARAM, cursor.getString(
                        cursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE)));
                detailBundle.putInt(MovieData.RUNTIME_PARAM, cursor.getInt(
                        cursor.getColumnIndex(MovieEntry.COLUMN_RUNTIME)));
                detailBundle.putDouble(MovieData.RATE_PARAM, cursor.getDouble(
                        cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_AVERAGE)));
                detailBundle.putString(MovieData.VOTES_PARAM, Integer.toString(
                        cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_VOTE_COUNT))
                ));
                detailBundle.putDouble(MovieData.POPULARITY_PARAM, cursor.getDouble(
                        cursor.getColumnIndex(MovieEntry.COLUMN_POPULARITY)));
                detailBundle.putString(MovieData.ORIGINAL_LANG_PARAM, cursor.getString(
                        cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_LANG)));
                detailBundle.putString(MovieData.ORIGINAL_TITLE_PARAM, cursor.getString(
                        cursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TITLE)));
                detailBundle.putLong(MovieData.REVENUE_PARAM, cursor.getLong(
                        cursor.getColumnIndex(MovieEntry.COLUMN_REVENUE)));
                detailBundle.putLong(MovieData.BUDGET_PARAM, cursor.getLong(
                        cursor.getColumnIndex(MovieEntry.COLUMN_BUDGET)));
            }
            cursor.close();
        } else {
            throw new SQLException("No movie [" + movieId + "] row was found.");
        }

        // Fetch Trailer Data
        ArrayList<String> trailerTitleList = new ArrayList<>();
        ArrayList<String> trailerPathList = new ArrayList<>();
        ArrayList<String> trailerThumbnailPathList = new ArrayList<>();
        cursor = cr.query(
                TrailerEntry.CONTENT_URI,
                null,
                MovieDbProvider.sTableTrailerIdSelection,
                new String[]{movieId},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            for (int j = 0; j < cursor.getCount(); j++, cursor.moveToNext()) {
                trailerTitleList.add(cursor.getString(
                        cursor.getColumnIndex(TrailerEntry.COLUMN_TITLE)));
                trailerPathList.add(cursor.getString(
                        cursor.getColumnIndex(TrailerEntry.COLUMN_SOURCE)));
                trailerThumbnailPathList.add(cursor.getString(
                        cursor.getColumnIndex(TrailerEntry.COLUMN_THUMBNAIL_PATH)));
            }
            trailerBundle.putStringArrayList(MovieData.TRAILER_TITLE_PARAM, trailerTitleList);
            trailerBundle.putStringArrayList(MovieData.TRAILER_SOURCE_PARAM, trailerPathList);
            trailerBundle.putStringArrayList(MovieData.TRAILER_THUMBNAIL_PARAM, trailerThumbnailPathList);
            trailerBundle.putInt(MovieData.TRAILERS_COUNT, cursor.getCount());
            cursor.close();
        }

        // Fetch Review Data
        ArrayList<String> reviewAuthorList = new ArrayList<>();
        ArrayList<String> reviewContentList = new ArrayList<>();
        cursor = cr.query(
                ReviewEntry.CONTENT_URI,
                null,
                MovieDbProvider.sTableReviewIdSelection,
                new String[]{movieId},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            for (int j = 0; j < cursor.getCount(); j++, cursor.moveToNext()) {
                reviewAuthorList.add(cursor.getString(
                        cursor.getColumnIndex(ReviewEntry.COLUMN_AUTHOR)));
                reviewContentList.add(cursor.getString(
                        cursor.getColumnIndex(ReviewEntry.COLUMN_CONTENT)));
            }
            reviewBundle.putStringArrayList(MovieData.REVIEWS_AUTHOR_PARAM, reviewAuthorList);
            reviewBundle.putStringArrayList(MovieData.REVIEWS_CONTENT_PARAM, reviewContentList);
            reviewBundle.putInt(MovieData.REVIEWS_COUNT, cursor.getCount());
            cursor.close();
        }

        // Fetch Cast Data
        ArrayList<String> castNameList = new ArrayList<>();
        ArrayList<String> castCharacterList = new ArrayList<>();
        ArrayList<String> castProfileList = new ArrayList<>();
        cursor = cr.query(
                CastEntry.CONTENT_URI,
                null,
                MovieDbProvider.sTableCastIdSelection,
                new String[]{movieId},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            for (int j = 0; j < cursor.getCount(); j++, cursor.moveToNext()) {
                castNameList.add(cursor.getString(
                        cursor.getColumnIndex(CastEntry.COLUMN_NAME)));
                castCharacterList.add(cursor.getString(
                        cursor.getColumnIndex(CastEntry.COLUMN_CHARACTER)));
                castProfileList.add(cursor.getString(
                        cursor.getColumnIndex(CastEntry.COLUMN_PROFILE_PATH)));
            }
            castBundle.putStringArrayList(MovieData.CAST_NAME_PARAM, castNameList);
            castBundle.putStringArrayList(MovieData.CAST_CHARACTER_PARAM, castCharacterList);
            castBundle.putStringArrayList(MovieData.PROFILE_PATH_PARAM, castProfileList);
            castBundle.putString(MovieData.PEOPLE_TYPE, MovieData.CAST_PARAM);
            castBundle.putInt(MovieData.PEOPLE_COUNT, cursor.getCount());
            cursor.close();
        }

        // Fetch Crew Data
        ArrayList<String> crewNameList = new ArrayList<>();
        ArrayList<String> crewJobList = new ArrayList<>();
        ArrayList<String> crewProfileList = new ArrayList<>();
        cursor = cr.query(
                CrewEntry.CONTENT_URI,
                null,
                MovieDbProvider.sTableCrewIdSelection,
                new String[]{movieId},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            for (int j = 0; j < cursor.getCount(); j++, cursor.moveToNext()) {
                crewNameList.add(cursor.getString(
                        cursor.getColumnIndex(CrewEntry.COLUMN_NAME)));
                crewJobList.add(cursor.getString(
                        cursor.getColumnIndex(CrewEntry.COLUMN_JOB)));
                crewProfileList.add(cursor.getString(
                        cursor.getColumnIndex(CrewEntry.COLUMN_PROFILE_PATH)));
            }
            crewBundle.putStringArrayList(MovieData.CREW_NAME_PARAM, crewNameList);
            crewBundle.putStringArrayList(MovieData.CREW_JOB_PARAM, crewJobList);
            crewBundle.putStringArrayList(MovieData.PROFILE_PATH_PARAM, crewProfileList);
            crewBundle.putString(MovieData.PEOPLE_TYPE, MovieData.CREW_PARAM);
            crewBundle.putInt(MovieData.PEOPLE_COUNT, cursor.getCount());
            cursor.close();
        }
        returnData.putBundle(MovieData.DETAILS_PARAM, detailBundle);
        returnData.putBundle(MovieData.TRAILERS_PARAM, trailerBundle);
        returnData.putBundle(MovieData.REVIEWS_PARAM, reviewBundle);
        returnData.putBundle(MovieData.CAST_PARAM, castBundle);
        returnData.putBundle(MovieData.CREW_PARAM, crewBundle);
        return returnData;
    }

    public static Bundle getFavouritesDataList(Context context) {
        Bundle values = new Bundle();
        ArrayList<String> movieIdList = new ArrayList<>();
        ArrayList<String> posterList = new ArrayList<>();
        ArrayList<byte[]> posterBlobList = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(
                MovieEntry.CONTENT_URI,
                new String[]{MovieEntry.COLUMN_MOVIE_ID, MovieEntry.COLUMN_POSTER_PATH,
                        MovieEntry.COLUMN_POSTER_BLOB}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            for (int j = 0; j < cursor.getCount(); j++, cursor.moveToNext()) {
                movieIdList.add(cursor.getString(
                        cursor.getColumnIndex(MovieEntry.COLUMN_MOVIE_ID)));
                posterList.add(MovieData.BASEPATH_W342 + cursor.getString(
                        cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH)));
                posterBlobList.add(cursor.getBlob(
                        cursor.getColumnIndex(MovieEntry.COLUMN_POSTER_BLOB)));
            }
            values.putStringArrayList(MovieData.ID_PARAM, movieIdList);
            values.putStringArrayList(MovieData.POSTER_PATH_PARAM, posterList);
            values.putSerializable(MovieData.POSTER_BLOB_PARAM, posterBlobList);
            values.putInt(MovieData.MOVIE_LIST_COUNT, cursor.getCount());
            cursor.close();
        }
        return values;
    }

    private static ContentValues buildDetailContentValues(Context context, Bundle detailBundle) {
        ContentValues cv = new ContentValues();
        cv.put(MovieEntry.COLUMN_TITLE,
                detailBundle.getString(MovieData.TITLE_PARAM));
        cv.put(MovieEntry.COLUMN_ORIGINAL_TITLE,
                detailBundle.getString(MovieData.ORIGINAL_TITLE_PARAM));
        cv.put(MovieEntry.COLUMN_OVERVIEW,
                detailBundle.getString(MovieData.OVERVIEW_PARAM));
        cv.put(MovieEntry.COLUMN_GENRES,
                detailBundle.getString(MovieData.GENRES_PARAM));
        cv.put(MovieEntry.COLUMN_RELEASE_DATE,
                detailBundle.getString(MovieData.RELEASE_DATE_PARAM));
        cv.put(MovieEntry.COLUMN_MOVIE_ID,
                detailBundle.getInt(MovieData.ID_PARAM));
        cv.put(MovieEntry.COLUMN_VOTE_AVERAGE,
                detailBundle.getDouble(MovieData.RATE_PARAM));
        cv.put(MovieEntry.COLUMN_RUNTIME,
                detailBundle.getInt(MovieData.RUNTIME_PARAM));
        cv.put(MovieEntry.COLUMN_POSTER_PATH,
                detailBundle.getString(MovieData.POSTER_PATH_PARAM));
        cv.put(MovieEntry.COLUMN_POSTER_BLOB,
                detailBundle.getByteArray(MovieData.POSTER_BLOB_PARAM));
        cv.put(MovieEntry.COLUMN_BACKDROP_PATH,
                detailBundle.getString(MovieData.BACKDROP_PATH_PARAM));
        cv.put(MovieEntry.COLUMN_VOTE_COUNT,
                detailBundle.getString(MovieData.VOTES_PARAM));
        cv.put(MovieEntry.COLUMN_POPULARITY,
                detailBundle.getDouble(MovieData.POPULARITY_PARAM));
        cv.put(MovieEntry.COLUMN_ORIGINAL_LANG,
                detailBundle.getString(MovieData.ORIGINAL_LANG_PARAM));
        cv.put(MovieEntry.COLUMN_BUDGET,
                detailBundle.getLong(MovieData.BUDGET_PARAM));
        cv.put(MovieEntry.COLUMN_REVENUE,
                detailBundle.getLong(MovieData.REVENUE_PARAM));
        return cv;
    }

    public static byte[] getImageBytesArrayFromPath(Context context, String posterPath) {
        if (!posterPath.equals("")) {
            try {
                Bitmap bitmap = Picasso.with(context).load(posterPath).get();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                return stream.toByteArray();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
        return null;
    }

    public static void setupPager(ViewPager viewPager, TabLayout tabLayout, Context context) {
        if (viewPager.getChildCount() > 0){
            viewPager.getAdapter().notifyDataSetChanged();
            return;
        }

        MovieDetailPagerAdapter pagerAdapter;
        if (context instanceof MainActivity) {
            pagerAdapter = new MovieDetailPagerAdapter(((MainActivity) context).getSupportFragmentManager());
        } else if (context instanceof MovieDetailActivity) {
            pagerAdapter = new MovieDetailPagerAdapter(((MovieDetailActivity) context).getSupportFragmentManager());
        } else {
            return;
        }

        pagerAdapter.addPage(new MovieOverviewFragment());
        pagerAdapter.addPage(new MovieDetailsFragment());
        pagerAdapter.addPage(new MovieTrailerFragment());
        pagerAdapter.addPage(new MovieReviewFragment());

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    public static void setupTabs(TabLayout tabLayout, ViewPager viewPager, final Context context) {
        if (tabLayout.getTabCount() > 0) {
            return;
        }
        Drawable iconOverview = context.getResources().getDrawable(R.drawable.overview);

        iconOverview.setColorFilter(context.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        Drawable iconDetail = context.getResources().getDrawable(R.drawable.detail);
        iconDetail.setColorFilter(context.getResources().getColor(R.color.grey700), PorterDuff.Mode.SRC_IN);
        Drawable iconTrailer = context.getResources().getDrawable(R.drawable.trailer);
        iconTrailer.setColorFilter(context.getResources().getColor(R.color.grey700), PorterDuff.Mode.SRC_IN);
        Drawable iconReview = context.getResources().getDrawable(R.drawable.review);
        iconReview.setColorFilter(context.getResources().getColor(R.color.grey700), PorterDuff.Mode.SRC_IN);

        String label1 = context.getString(R.string.tab_overview);
        String label2 = context.getString(R.string.tab_details);
        String label3 = context.getString(R.string.tab_trailers);
        String label4 = context.getString(R.string.tab_reviews);

        tabLayout.addTab(tabLayout.newTab().setIcon(iconOverview).setTag(label1));
        tabLayout.addTab(tabLayout.newTab().setIcon(iconDetail).setTag(label2));
        tabLayout.addTab(tabLayout.newTab().setIcon(iconTrailer).setTag(label3));
        tabLayout.addTab(tabLayout.newTab().setIcon(iconReview).setTag(label4));

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                int tabIconColor = ContextCompat.getColor(context, R.color.colorAccent);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                if (context instanceof MainActivity) {
                    ((MainActivity) context).setTitle(tab.getTag().toString());
                } else if (context instanceof MovieDetailActivity) {
                    ((MovieDetailActivity) context).setTitle(tab.getTag().toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                int tabIconColor = ContextCompat.getColor(context, R.color.grey700);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }
        });
    }
}
