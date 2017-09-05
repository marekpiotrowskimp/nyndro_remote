package iso.piotrowski.marek.nyndro.tools.Fragments;

import iso.piotrowski.marek.nyndro.PracticeMain.PracticeMainContract;

/**
 * Created by marek.piotrowski on 22/08/2017.
 */

public interface IFragmentParams {
    String getFragmentName();
    FragmentsFactory.TypeOfFragment getTypeOf();
    boolean isButtonVisible();
    PracticeMainContract.TypeOfBoomButton getTypeOfBoomButton();
    void refreshData();
}
