package japps.trendymovies.activity;


import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import japps.trendymovies.R;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.utility.Utilities;


/**
 * Created by Julio on 28/1/2016.
 */
public class MovieDetailActivity extends AppCompatActivity {

    public static final String FAVOURITE_REMOVED = "favourite_removed";
    private Context mContext;
    private final String LOG_TAG = MovieDetailActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState != null) {
            return;
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.pagerView);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        Utilities.setupTabs(tabLayout, viewPager, this);
        Utilities.setupPager(viewPager, tabLayout, this);

        getSupportActionBar().setElevation(0f);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.movie_detail_action_fav);
        Bundle detailBundle = getIntent().getExtras().getBundle(MovieData.DETAILS_PARAM);
        String movieId = Integer.toString(detailBundle.getInt(MovieData.ID_PARAM));
        if (Utilities.isFavourite(this, movieId)){
            menuItem.setIcon(R.drawable.favourite_on);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.movie_detail_action_fav){
            Bundle extras = getIntent().getExtras();
            String movieId = Integer.toString(extras.getBundle(MovieData.DETAILS_PARAM)
                    .getInt(MovieData.ID_PARAM));
            try {
                if (Utilities.isFavourite(this, movieId)) {
                    Utilities.removeFavourite(this, extras);
                    item.setIcon(R.drawable.favourite_off);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(FAVOURITE_REMOVED));
                } else {
                    Utilities.addFavourite(this, extras);
                    item.setIcon(R.drawable.favourite_on);
                }
            } catch (SQLException ex){
                Log.d(LOG_TAG, ex.getMessage());
                ex.printStackTrace();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
