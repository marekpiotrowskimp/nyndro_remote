package iso.piotrowski.marek.nyndro.practice;

import android.app.backup.BackupManager;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.DataSource.IDataSource;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;

/**
 * Created by marek.piotrowski on 11/08/2017.
 */

public class PracticePresenter implements PracticeContract.IPresenter {
    private PracticeContract.IViewer viewer;
    private IDataSource dataSource;

    public PracticePresenter (PracticeContract.IViewer viewer, IDataSource dataSource){
        this.viewer = viewer;
        this.dataSource = dataSource;
        this.viewer.setPresenter(this);
    }

    @Override
    public void dataWereChanged() {
        viewer.refreshPracticeRecyclerView(dataSource.fetchPractices());
    }

    @Override
    public void loadPracticeData() {
        viewer.setUpRecyclerView(dataSource.fetchPractices());
    }

    @Override
    public HistoryModel getLastHistoryOfPractice(long practiceId) {
        return dataSource.fetchLastHistoryOfPractice(practiceId);
    }

    @Override
    public ReminderModel getNextPlanedOfPractice(long practiceId) {
        return dataSource.fetchNextPlanedOfPractice(practiceId);
    }

    @Override
    public void requestBackup() {
        if (SQLHelper.isUpdateDatabase) {
            try {
                SQLHelper.isUpdateDatabase = false;
                BackupManager backupManager = new BackupManager(NyndroApp.getContect());
                backupManager.dataChanged();
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void addHistoryForPractice(PracticeModel practice, int multiple) {
        dataSource.addHistoryForPractice(practice, multiple);
    }

    @Override
    public void addProgressToPractice(PracticeModel practice, int multiple) {
        dataSource.addProgressToPractice(practice, multiple);
    }
}
