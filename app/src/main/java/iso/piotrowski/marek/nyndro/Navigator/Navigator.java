package iso.piotrowski.marek.nyndro.Navigator;

import java.lang.ref.WeakReference;
import java.util.Stack;

import iso.piotrowski.marek.nyndro.FragmentsFactory.FragmentTool;
import iso.piotrowski.marek.nyndro.FragmentsFactory.IActivityDelegate;
import iso.piotrowski.marek.nyndro.FragmentsFactory.NyndroFragment;

/**
 * Created by marek.piotrowski on 24/08/2017.
 */

public class Navigator implements INavigator {
    private NyndroFragment currentFragment;
    private Stack<NyndroFragment> fragmentStack;
    private WeakReference<IActivityDelegate> activityDelegate;
    private static Navigator instance;

    private Navigator (WeakReference<IActivityDelegate> activityDelegate) {
        this.activityDelegate = activityDelegate;
        fragmentStack = new Stack<>();
    }

    public static void initialization (WeakReference<IActivityDelegate> mainActivity){
        instance = new Navigator(mainActivity);
    }

    public static Navigator getInstance(){
        return instance;
    }

    @Override
    public void changeFragmentInContainer(NyndroFragment newFragment) {
        changeFragmentInContainer(newFragment, false);
    }

    @Override
    public void changeFragmentInContainer(NyndroFragment newFragment, boolean addToBackStack){
        changeFragmentInContainer(newFragment, addToBackStack, false);
    }

    public void changeFragmentInContainer(NyndroFragment newFragment, boolean addToBackStack, boolean goBack) {
        if ((activityDelegate != null)&&(activityDelegate.get()!=null)) {
            if (addToBackStack) {
                fragmentStack.push(currentFragment);
            } else {
                if (!goBack) fragmentStack.clear();
            }
            currentFragment = newFragment;
            activityDelegate.get().onFragmentChange(currentFragment);
            FragmentTool.changeFragmentInContainer(activityDelegate.get().getMainActivity(), newFragment);
        }
    }

    @Override
    public boolean goBack() {
        if (fragmentStack.size()>0) {
            changeFragmentInContainer(fragmentStack.pop(), false, true);
            return true;
        }
        return false;
    }

    @Override
    public NyndroFragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public int countOfFragments() {
        return fragmentStack.size();
    }

}
