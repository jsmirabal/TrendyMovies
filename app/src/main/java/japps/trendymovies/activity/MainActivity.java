package japps.trendymovies.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import japps.trendymovies.R;
import japps.trendymovies.fragment.MainFragment;
import japps.trendymovies.task.FetchMovieTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    private MainFragment mMainFragment;
    private int mItemSelected;
    private static final String MAIN_FRAGMENT_TAG = "main_fragment";
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_container);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.action_navigation_drawer_open, R.string.action_navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null){
            mMainFragment = new MainFragment();
            mItemSelected = R.id.nav_trending;
            getFragmentManager().beginTransaction()
                    .add(R.id.main_coordinator_layout, mMainFragment, MAIN_FRAGMENT_TAG).commit();
        } else {
            mMainFragment = (MainFragment) getFragmentManager().findFragmentByTag(MAIN_FRAGMENT_TAG);
        }
        setReceivers();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (mMainFragment != null && mItemSelected != id) {
            mItemSelected = id;
            switch (id){
                case R.id.nav_trending:{
                    mMainFragment.fetchMovieList(FetchMovieTask.MOST_POPULAR);
                    break;
                }
                case R.id.nav_top_rated:{
                    mMainFragment.fetchMovieList(FetchMovieTask.TOP_RATED);
                    break;
                }
                case R.id.nav_upcoming:{
                    mMainFragment.fetchMovieList(FetchMovieTask.UPCOMING);
                    break;
                }
                case R.id.nav_now_playing:{
                    mMainFragment.fetchMovieList(FetchMovieTask.NOW_PLAYING);
                    break;
                }
                case R.id.nav_revenue:{
                    mMainFragment.fetchMovieList(FetchMovieTask.REVENUE);
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

    private void setReceivers () {
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()){
                    case MovieDetailActivity.FAVOURITE_REMOVED:{
                        if (mMainFragment.isFavouriteViewActive()) {
                            mMainFragment.fetchFavourites();
                        }
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mBroadcastReceiver, new IntentFilter(MovieDetailActivity.FAVOURITE_REMOVED));
    }
}
