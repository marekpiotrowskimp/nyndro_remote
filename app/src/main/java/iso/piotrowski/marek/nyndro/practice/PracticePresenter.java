package iso.piotrowski.marek.nyndro.practice;

import android.app.backup.BackupManager;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.DataSource.IDataSource;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;

/**
 * Created by marek.piotrowski on 11/08/2017.
 */

public class PracticePresenter implements PracticeContract.IPresenter {
    private PracticeContract.IViewer viewer;
    private IDataSource dataSource;
    private boolean canceledDelete;

    public PracticePresenter(PracticeContract.IViewer viewer, IDataSource dataSource) {
        this.viewer = viewer;
        this.dataSource = dataSource;
        this.viewer.setPresenter(this);
    }

    @Override
    public void refreshData() {
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
        dataSource.addHistoryForPractice(practice, practice.getRepetition() * multiple);
    }

    @Override
    public void addProgressToPractice(PracticeModel practice, int multiple) {
        dataSource.addProgressToPractice(practice, practice.getRepetition() * multiple);
    }

    @Override
    public void deletePractice(PracticeModel practice, View view) {
        dataSource.markActive(practice, false);
        canceledDelete = false;
        refreshData();
        Snackbar.make(view, NyndroApp.getContect().getResources().getString(R.string.deleted_practice_info)
                + " " + practice.getName(), 10000)
                .setAction(R.string.cancel_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        canceledDelete = true;
                        dataSource.markActive(practice, true);
                        refreshData();
                    }
                }).show();

        Handler deleteHandler = new Handler();
        deleteHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!canceledDelete) {
                    dataSource.deleteNoActive();
                    requestBackup();
                }
            }
        }, 11000);
    }

}
