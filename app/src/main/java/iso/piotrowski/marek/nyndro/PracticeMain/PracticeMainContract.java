package iso.piotrowski.marek.nyndro.PracticeMain;

import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.tools.Fragments.IBasePresenter;

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
