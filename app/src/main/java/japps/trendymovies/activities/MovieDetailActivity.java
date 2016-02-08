package japps.trendymovies.activities;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import japps.trendymovies.R;
import japps.trendymovies.fragments.MovieDetailFragment;


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
        getSupportActionBar().setElevation(0);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_movie_detail, new MovieDetailFragment())
                .commit();
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
