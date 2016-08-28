package japps.trendymovies.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julio on 5/7/2016.
 */
public class MovieDetailPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> listFragment;

    public MovieDetailPagerAdapter(FragmentManager fm) {
        super(fm);
        listFragment = new ArrayList<>();

    }

    public void addPage(Fragment fragment){
        listFragment.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

}
