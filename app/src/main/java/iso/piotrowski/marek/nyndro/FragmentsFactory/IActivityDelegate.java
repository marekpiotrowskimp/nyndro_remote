package iso.piotrowski.marek.nyndro.FragmentsFactory;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by marek.piotrowski on 24/08/2017.
 */

public interface IActivityDelegate {
    AppCompatActivity getMainActivity ();
    void onFragmentChange(Fragment currentFragment);
}
