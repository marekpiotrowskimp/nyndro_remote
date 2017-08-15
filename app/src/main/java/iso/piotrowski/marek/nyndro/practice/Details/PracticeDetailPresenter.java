package iso.piotrowski.marek.nyndro.practice.Details;

import iso.piotrowski.marek.nyndro.DataSource.DBQuery;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;

/**
 * Created by marek.piotrowski on 15/08/2017.
 */

public class PracticeDetailPresenter implements PracticeDetailContract.IPresenter {

    private DataSource dataSource;
    private PracticeDetailContract.IViewer viewer;

    PracticeDetailPresenter (PracticeDetailContract.IViewer viewer, DataSource dataSource){
        this.dataSource = dataSource;
        this.viewer = viewer;
        this.viewer.setPresenter(this);
    }

    @Override
    public void loadPracticeData(long practiceId) {
        viewer.setPractice(dataSource.fetchPractice(practiceId));
    }

    @Override
    public long getLastHistoryOfPractice(long practiceId) {
        HistoryModel history = dataSource.fetchLastHistoryOfPractice(practiceId);
        return history != null ? history.getPracticeData() : -1;
    }

    @Override
    public long getNextPlanedOfPractice(long practiceId) {
        ReminderModel reminder = dataSource.fetchNextPlanedOfPractice(practiceId);
        return reminder !=null ? reminder.getPracticeDate() : -1;
    }

    @Override
    public void addHistoryForPractice(PracticeModel practice, int addProgress) {
        dataSource.addHistoryForPractice(practice, addProgress);
    }

    @Override
    public void addProgressToPractice(PracticeModel practice, int addProgress) {
        dataSource.addProgressToPractice(practice, addProgress);
    }

    @Override
    public void updatePractice(PracticeModel practice) {
        dataSource.updatePractice(practice);
    }
}
