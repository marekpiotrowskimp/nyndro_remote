package iso.piotrowski.marek.nyndro.PracticeMain;

import iso.piotrowski.marek.nyndro.PracticeMain.BoomButton.IBoomButtonAdapter;

/**
 * Created by marek.piotrowski on 21/08/2017.
 */

public class PracticeMainContract {

    public enum TypeOfBoomButton {
        BasicPractice, AddedPractice
    }

    interface IViewer {
        void setPresenter(IPresenter presenter);
        void setUpBoomButton(IBoomButtonAdapter buttonAdapter);
        void setUpBoomButtonToolBar(IBoomButtonAdapter buttonAdapter);
    }

    interface IPresenter {
        void adjustBoomButton(TypeOfBoomButton typeOfBoomButton);
        void adjustBoomButtonToolBar(TypeOfBoomButton typeOfBoomButton);
    }

}
