package japps.trendymovies.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.SQLException;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import japps.trendymovies.R;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.fragment.MainFragment;
import japps.trendymovies.task.FetchMovieTask;
import japps.trendymovies.utility.Utilities;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private static final String OVERVIEW_FRAGMENT_TAG = "OFT";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private MainFragment mMainFragment;
    private int mItemSelected;
    private boolean mTabletMode;
    private static final String MAIN_FRAGMENT_TAG = "main_fragment";
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.main_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.action_navigation_drawer_open, R.string.action_navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mItemSelected = R.id.nav_trending;
        setupReceivers();
        if (findViewById(R.id.movie_detail_container) != null) {
            mTabletMode = true;
            if (savedInstanceState == null) {

            }
        } else {
            mTabletMode = false;
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_container);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        if (mTabletMode) {
            getMenuInflater().inflate(R.menu.movie_detail_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            // Only on tablet mode
            case R.id.movie_detail_action_fav:{
                Bundle extras = getIntent().getExtras();
                String movieId = Integer.toString(extras.getBundle(MovieData.DETAILS_PARAM)
                        .getInt(MovieData.ID_PARAM));
                try {
                    if (Utilities.isFavourite(this, movieId)) {
                        Utilities.removeFavourite(this, extras);
                        item.setIcon(R.drawable.favourite_off);
                        mMainFragment.fetchFavourites();
                    } else {
                        Utilities.addFavourite(this, extras);
                        item.setIcon(R.drawable.favourite_on);
                    }
                } catch (SQLException ex){
                    Log.d(LOG_TAG, ex.getMessage());
                    ex.printStackTrace();
                }
                return true;
            }// Tablet Case 1

            case R.id.action_search:{
                return true;
            }// Case 1
            case R.id.action_pages:{
                mMainFragment.showPageSwitcher(this);
                return true;
            }// Case 2
            case R.id.action_settings:{
                return true;
            }// Case 3
            default: {
                return super.onOptionsItemSelected(item);
            }// Default
        }// Switch
    }// Method

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (mMainFragment != null && mItemSelected != id) {
            mItemSelected = id;
            switch (id){
                case R.id.nav_trending:{
                    mMainFragment.fetchMovieList(FetchMovieTask.MOST_POPULAR, 1);
                    break;
                }
                case R.id.nav_top_rated:{
                    mMainFragment.fetchMovieList(FetchMovieTask.TOP_RATED, 1);
                    break;
                }
                case R.id.nav_upcoming:{
                    mMainFragment.fetchMovieList(FetchMovieTask.UPCOMING, 1);
                    break;
                }
                case R.id.nav_now_playing:{
                    mMainFragment.fetchMovieList(FetchMovieTask.NOW_PLAYING, 1);
                    break;
                }
                case R.id.nav_revenue:{
                    mMainFragment.fetchMovieList(FetchMovieTask.REVENUE, 1);
                    break;
                }
                case R.id.nav_favourites:{
                    mMainFragment.fetchFavourites();
                }
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_container);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupReceivers() {
        final Context contx = this;
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case MovieDetailActivity.FAVOURITE_REMOVED:{
                        if (mMainFragment.isFavouriteViewActive()) {
                            mMainFragment.fetchFavourites();
                        }
                    }
                    case MainFragment.LOAD_MOVIE_LIST_FINISHED:{
                        ViewPager viewPager = (ViewPager) findViewById(R.id.pagerView);
                        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
                        Utilities.setupTabs(tabLayout, viewPager, contx);
                        Utilities.setupPager(viewPager, tabLayout, contx);
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mBroadcastReceiver, new IntentFilter(MovieDetailActivity.FAVOURITE_REMOVED));
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mBroadcastReceiver, new IntentFilter(MainFragment.LOAD_MOVIE_LIST_FINISHED));
    }

    public boolean isTabletMode(){
        return mTabletMode;
    }

    public int getOrientation(){return this.getResources().getConfiguration().orientation;}
}
