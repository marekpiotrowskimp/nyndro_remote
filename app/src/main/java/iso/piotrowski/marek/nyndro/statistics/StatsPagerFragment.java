package iso.piotrowski.marek.nyndro.statistics;


import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.history.HistoryFragment;


public class StatsPagerFragment extends Fragment {

    private ViewPager viewPagerLayout= null;
    private StatsFragmentPagerAdapter pagerAdapter =null;

    public StatsPagerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewPagerLayout = (ViewPager)inflater.inflate(R.layout.fragment_stats_pager, container, false);
        return viewPagerLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pagerAdapter = new StatsFragmentPagerAdapter(getChildFragmentManager());
        viewPagerLayout.setAdapter(pagerAdapter);
    }

    private class StatsFragmentPagerAdapter extends FragmentPagerAdapter{

        public StatsFragmentPagerAdapter (FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = "";
            switch (position)
            {
                case 0:
                    title = getString(R.string.StatisticsPage);
                    break;
                case 1:
                    title = getString(R.string.HistoryPage);
                    break;
            }
            return title;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment=null;
            switch (position)
            {
                case 0:
                    fragment = new StatsFragment();
                    break;
                case 1:
                    fragment = new HistoryFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
