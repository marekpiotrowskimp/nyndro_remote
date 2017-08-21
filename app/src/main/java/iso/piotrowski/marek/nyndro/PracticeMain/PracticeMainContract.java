package iso.piotrowski.marek.nyndro.PracticeMain;

import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;

/**
 * Created by marek.piotrowski on 21/08/2017.
 */

public class PracticeMainContract {

    interface IViewer {
        void setPresenter(IPresenter presenter);
    }

    interface IPresenter {
        void insertPractice (Practice practice);
    }

}
