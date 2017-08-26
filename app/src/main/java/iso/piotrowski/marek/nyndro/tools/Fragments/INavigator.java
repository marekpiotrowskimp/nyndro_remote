package iso.piotrowski.marek.nyndro.tools.Fragments;

import android.support.v4.app.Fragment;

/**
 * Created by marek.piotrowski on 24/08/2017.
 */

public interface INavigator {
    void changeFragmentInContainer(NyndroFragment newFragment);
    void changeFragmentInContainer(NyndroFragment newFragment, boolean addToBackStack);
    boolean goBack();
    NyndroFragment getCurrentFragment();
    int countOfFragments();
}
