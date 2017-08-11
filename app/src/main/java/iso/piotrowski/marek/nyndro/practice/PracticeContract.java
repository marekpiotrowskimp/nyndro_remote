package iso.piotrowski.marek.nyndro.practice;

import java.util.List;

import iso.piotrowski.marek.nyndro.Model.PracticeModel;

/**
 * Created by marek.piotrowski on 11/08/2017.
 */

public class PracticeContract {

    interface IViewer {
        void refreshPracticeRecyclerView(List<PracticeModel> practices);
        void setPresenter(IPresenter presenter);
        void setUpRecyclerView(List<PracticeModel> practices);
    }

    interface IPresenter {
        void dataWereChanged();
        void loadPracticeData();
    }
}
