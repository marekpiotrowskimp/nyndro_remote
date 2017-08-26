package iso.piotrowski.marek.nyndro.tools.Fragments;

import android.support.v4.app.Fragment;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.R;

/**
 * Created by marek.piotrowski on 26/08/2017.
 */

public class NyndroFragment extends Fragment implements IFragmentParams {


    @Override
    public String getFragmentName() {
        return NyndroApp.getContect().getResources().getString(R.string.default_fragment_name);
    }

    @Override
    public FragmentsFactory.TypeOfFragment getTypeOf() {
        return FragmentsFactory.TypeOfFragment.None;
    }

    @Override
    public boolean isButtonVisible() {
        return false;
    }
}
