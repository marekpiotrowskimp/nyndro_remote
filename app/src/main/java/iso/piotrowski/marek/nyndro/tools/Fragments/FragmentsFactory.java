package iso.piotrowski.marek.nyndro.tools.Fragments;

import android.support.v4.app.Fragment;

import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.TabCounter.TabCounterFragment;
import iso.piotrowski.marek.nyndro.history.HistoryFragment;
import iso.piotrowski.marek.nyndro.plans.PlansFragment;
import iso.piotrowski.marek.nyndro.practice.PracticeMainFragment;
import iso.piotrowski.marek.nyndro.statistics.StatsFragment;

/**
 * Created by marek.piotrowski on 22/08/2017.
 */

public class FragmentsFactory {

    public enum TypeOfFragment {
        Main, Stats, History, Plans, TapCounter, PracticeList, PracticeDetail, AddRemainder, None
    }

    public static TypeOfFragment getFragmentForTap(int tapId){
        switch (tapId){
            case R.id.tab_practice:
                return TypeOfFragment.Main;
            case R.id.tab_statistic:
                return TypeOfFragment.Stats;
            case R.id.tab_history:
                return TypeOfFragment.History;
            case R.id.tab_plans:
                return TypeOfFragment.Plans;
            case R.id.tab_counter:
                return TypeOfFragment.TapCounter;
        }
        return TypeOfFragment.None;
    }

    public static int getTapIdForFragment(TypeOfFragment typeOfFragment){
        switch (typeOfFragment){
            case Main:
                return R.id.tab_practice;
            case Stats:
                return R.id.tab_statistic;
            case History:
                return R.id.tab_history;
            case Plans:
                return R.id.tab_plans;
            case TapCounter:
                return R.id.tab_counter;
        }
        return 0;
    }

    public static NyndroFragment getFragment(TypeOfFragment typeOfFragment){
        switch (typeOfFragment){
            case Main:
                return PracticeMainFragment.getInstance();
            case Stats:
                return StatsFragment.getInstance();
            case History:
                return HistoryFragment.getInstance();
            case Plans:
                return PlansFragment.getInstance();
            case TapCounter:
                return TabCounterFragment.getInstance();
        }
        return null;
    }
}
