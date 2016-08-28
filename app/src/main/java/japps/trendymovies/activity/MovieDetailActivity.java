package japps.trendymovies.activity;


import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import japps.trendymovies.R;
import japps.trendymovies.adapter.MovieDetailPagerAdapter;
import japps.trendymovies.data.MovieData;
import japps.trendymovies.fragment.MovieDetailsFragment;
import japps.trendymovies.fragment.MovieOverviewFragment;
import japps.trendymovies.fragment.MovieReviewFragment;
import japps.trendymovies.fragment.MovieTrailerFragment;
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
        mContext = this;

        ViewPager viewPager = (ViewPager) findViewById(R.id.pagerView);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        setupTabs(tabLayout);
        MovieDetailPagerAdapter pagerAdapter = new MovieDetailPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addPage(new MovieOverviewFragment());
        pagerAdapter.addPage(new MovieDetailsFragment());
        pagerAdapter.addPage(new MovieTrailerFragment());
        pagerAdapter.addPage(new MovieReviewFragment());

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                int tabIconColor = ContextCompat.getColor(mContext, R.color.colorAccent);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
                ((MovieDetailActivity)mContext).setTitle(tab.getTag().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                super.onTabUnselected(tab);
                int tabIconColor = ContextCompat.getColor(mContext, R.color.grey700);
                tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
            }
        });
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
    }

    private void setupTabs(TabLayout tabLayout){
        Drawable iconOverview = getResources().getDrawable(R.drawable.overview);
        iconOverview.setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_IN);
        Drawable iconDetail = getResources().getDrawable(R.drawable.detail);
        iconDetail.setColorFilter(getResources().getColor(R.color.grey700), PorterDuff.Mode.SRC_IN);
        Drawable iconTrailer = getResources().getDrawable(R.drawable.trailer);
        iconTrailer.setColorFilter(getResources().getColor(R.color.grey700), PorterDuff.Mode.SRC_IN);
        Drawable iconReview = getResources().getDrawable(R.drawable.review);
        iconReview.setColorFilter(getResources().getColor(R.color.grey700), PorterDuff.Mode.SRC_IN);

        String label1 = getString(R.string.tab_overview);
        String label2 = getString(R.string.tab_details);
        String label3 = getString(R.string.tab_trailers);
        String label4 = getString(R.string.tab_reviews);

        tabLayout.addTab(tabLayout.newTab().setIcon(iconOverview).setTag(label1));
        tabLayout.addTab(tabLayout.newTab().setIcon(iconDetail).setTag(label2));
        tabLayout.addTab(tabLayout.newTab().setIcon(iconTrailer).setTag(label3));
        tabLayout.addTab(tabLayout.newTab().setIcon(iconReview).setTag(label4));
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
        if (Utilities.isFavourite(mContext, movieId)){
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
                if (Utilities.isFavourite(mContext, movieId)) {
                    Utilities.removeFavourite(mContext, extras);
                    item.setIcon(R.drawable.favourite_off);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(FAVOURITE_REMOVED));
                } else {
                    Utilities.addFavourite(mContext, extras);
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
