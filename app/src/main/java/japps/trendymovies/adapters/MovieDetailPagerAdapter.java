package japps.trendymovies.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julio on 5/7/2016.
 */
public class MovieDetailPagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> listFragment;
    private List<String> listTitle;

    public MovieDetailPagerAdapter(FragmentManager fm) {
        super(fm);
        listFragment = new ArrayList<>();
        listTitle = new ArrayList<>();
    }

    public void addPage(Fragment fragment, String title){
        listFragment.add(fragment);
        listTitle.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTitle.get(position);
    }
}
