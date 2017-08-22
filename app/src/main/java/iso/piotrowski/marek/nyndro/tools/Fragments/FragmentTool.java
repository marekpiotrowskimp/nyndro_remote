package iso.piotrowski.marek.nyndro.tools.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import iso.piotrowski.marek.nyndro.R;

/**
 * Created by marek.piotrowski on 22/08/2017.
 */

public class FragmentTool {

    public static void changeFragmentInContainer(AppCompatActivity activity, Fragment newFragment) {
        changeFragmentInContainer(activity, newFragment, true);
    }

    public static void changeFragmentInContainer(AppCompatActivity activity, Fragment newFragment, boolean addToBackStack) {
        FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit);
        ft.replace(R.id.main_fragment_container, newFragment, "visible_tag");
        if (addToBackStack) ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    public static void goToMainFragment(AppCompatActivity activity) {
        activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static void goToPreviousFragment(AppCompatActivity activity) {
        activity.getSupportFragmentManager().popBackStack();
    }
}
