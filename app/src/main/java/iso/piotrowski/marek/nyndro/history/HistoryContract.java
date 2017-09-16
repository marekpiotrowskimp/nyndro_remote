package iso.piotrowski.marek.nyndro.history;

import android.view.View;

import java.util.List;

import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.practice.PracticeContract;
import iso.piotrowski.marek.nyndro.tools.Fragments.IBasePresenter;
import iso.piotrowski.marek.nyndro.tools.Fragments.IBaseViewer;

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
