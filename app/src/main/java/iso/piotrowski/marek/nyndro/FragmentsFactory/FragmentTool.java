package iso.piotrowski.marek.nyndro.FragmentsFactory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.GoogleAnalytics.Analytics;
import iso.piotrowski.marek.nyndro.R;

/**
 * Created by marek.piotrowski on 22/08/2017.
 */

public class FragmentTool {

    public static void changeFragmentInContainer(AppCompatActivity activity, Fragment newFragment) {
        try {
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
            ft.replace(R.id.main_fragment_container, newFragment, "visible_tag");
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        } catch (Exception e) {
            Analytics.logErrorEvent("Error", "changeFragmentInContainer", newFragment != null ? newFragment.toString() : "New Fragment is NULL");
            Toast.makeText(NyndroApp.getContext(), "Error during changing of view", Toast.LENGTH_LONG);
        }
    }
}
