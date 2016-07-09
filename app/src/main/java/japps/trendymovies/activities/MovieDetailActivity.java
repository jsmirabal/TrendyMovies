package japps.trendymovies.activities;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import japps.trendymovies.R;
import japps.trendymovies.adapters.MovieDetailPagerAdapter;
import japps.trendymovies.fragments.MovieOverviewFragment;
import japps.trendymovies.fragments.MovieTrailerFragment;


/**
 * Created by Julio on 28/1/2016.
 */
public class MovieDetailActivity extends AppCompatActivity {

    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if (savedInstanceState != null) {
            return;
        }
        ViewPager viewPager = (ViewPager) findViewById(R.id.pagerView);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        MovieDetailPagerAdapter pagerAdapter = new MovieDetailPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addPage(new MovieOverviewFragment(), getString(R.string.tab_overview));
        pagerAdapter.addPage(new MovieTrailerFragment(), getString(R.string.tab_trailers));
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.activity_movie_detail, new MovieOverviewFragment())
//                .commit();
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setElevation(0);
        count = 0;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.movie_detail_action_fav){
            count++;
            if (count == 1){item.setIcon(android.R.drawable.star_on);}
            if (count == 2){item.setIcon(android.R.drawable.star_off); count = 0;}
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
