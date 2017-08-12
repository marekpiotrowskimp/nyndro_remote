package iso.piotrowski.marek.nyndro.practice;

import java.util.List;

import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;

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
        HistoryModel getLastHistoryOfPractice(long practiceId);
        ReminderModel getNextPlanedOfPractice(long practiceId);
        void requestBackup();
        void addHistoryForPractice(PracticeModel practice, int multiple);
        void addProgressToPractice (PracticeModel practice, int multiple);
    }
}
