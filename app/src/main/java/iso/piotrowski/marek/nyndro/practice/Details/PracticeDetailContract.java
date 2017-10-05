package iso.piotrowski.marek.nyndro.practice.Details;

import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.FragmentsFactory.IBasePresenter;
import iso.piotrowski.marek.nyndro.FragmentsFactory.IBaseViewer;

/**
 * Created by marek.piotrowski on 15/08/2017.
 */

public class PracticeDetailContract {
    interface IViewer extends IBaseViewer {
        void setPresenter (IPresenter presenter);
        void setPractice (PracticeModel practice);
        void showPractice();
    }

    interface IPresenter extends IBasePresenter {
        void loadPracticeData(long practiceId);
        long getLastHistoryOfPractice(long practiceId);
        long getNextPlanedOfPractice(long practiceId);
        void addHistoryForPractice(PracticeModel practice, int addProgress);
        void addProgressToPractice (PracticeModel practice, int addProgress);
        void updatePractice(PracticeModel practice);
    }
}
