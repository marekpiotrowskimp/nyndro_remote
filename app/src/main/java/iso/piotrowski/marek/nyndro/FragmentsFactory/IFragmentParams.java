package iso.piotrowski.marek.nyndro.FragmentsFactory;

import iso.piotrowski.marek.nyndro.PracticeMain.PracticeMainContract;

/**
 * Created by marek.piotrowski on 22/08/2017.
 */

public interface IFragmentParams {
    String getFragmentName();
    FragmentsFactory.TypeOfFragment getTypeOf();
    boolean isButtonVisible();
    boolean isButtonToolBarVisible();
    PracticeMainContract.TypeOfBoomButton getTypeOfBoomButton();
    void refreshData();
}
