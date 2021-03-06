package iso.piotrowski.marek.nyndro.FragmentsFactory;

import android.support.v4.app.Fragment;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.PracticeMain.PracticeMainContract;
import iso.piotrowski.marek.nyndro.R;

/**
 * Created by marek.piotrowski on 26/08/2017.
 */

public class NyndroFragment extends Fragment implements IFragmentParams, IBaseViewer {

    protected IBasePresenter presenter;

    @Override
    public String getFragmentName() {
        return NyndroApp.getContext().getResources().getString(R.string.default_fragment_name);
    }

    @Override
    public FragmentsFactory.TypeOfFragment getTypeOf() {
        return FragmentsFactory.TypeOfFragment.None;
    }

    @Override
    public boolean isButtonVisible() {
        return false;
    }

    @Override
    public boolean isButtonToolBarVisible() {
        return false;
    }

    @Override
    public PracticeMainContract.TypeOfBoomButton getTypeOfBoomButton() {
        return PracticeMainContract.TypeOfBoomButton.BasicPractice;
    }

    @Override
    public void refreshData() {
        presenter.refreshData();
    }

    @Override
    public IBasePresenter getBasePresenter() {
        return presenter;
    }

    @Override
    public void showMessage(String message) {
    }
}
