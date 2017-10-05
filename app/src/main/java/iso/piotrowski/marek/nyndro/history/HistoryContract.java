package iso.piotrowski.marek.nyndro.history;

import java.util.List;

import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.FragmentsFactory.IBasePresenter;
import iso.piotrowski.marek.nyndro.FragmentsFactory.IBaseViewer;

/**
 * Created by marek.piotrowski on 18/08/2017.
 */

public class HistoryContract {

    interface IViewer extends IBaseViewer {
        void setPresenter(IPresenter presenter);
        void showHistory(List<HistoryModel> historyList);
    }

    interface IPresenter extends IBasePresenter {
        void loadHistoryData();
    }
}
