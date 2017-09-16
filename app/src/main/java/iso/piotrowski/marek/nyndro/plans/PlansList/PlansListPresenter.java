package iso.piotrowski.marek.nyndro.plans.PlansList;

import android.os.Handler;

import java.util.Calendar;
import java.util.Date;

import iso.piotrowski.marek.nyndro.DataSource.IDataSource;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.plans.RepeaterDialog.RepeaterDialogFragment;
import iso.piotrowski.marek.nyndro.FragmentsFactory.NyndroPresenter;

/**
 * Created by marek.piotrowski on 04/09/2017.
 */

public class PlansListPresenter extends NyndroPresenter implements PlansListContract.IPresenter {

    private PlansListContract.IViewer viewer;
    private IDataSource dataSource;

    public PlansListPresenter(PlansListContract.IViewer viewer, IDataSource dataSource) {
        this.viewer = viewer;
        this.dataSource = dataSource;
        this.viewer.setPresenter(this);
    }

    @Override
    public void loadData() {
        viewer.showPlansList(dataSource.fetchRemainders());
    }

    @Override
    public void refreshData() {
        viewer.refreshPlansList(dataSource.fetchRemainders());
    }

    @Override
    public void removeRemainder(long remainderId) {
        dataSource.deleteRemainder(remainderId);
    }

    @Override
    public void loadPlansForDate(Date date) {
        viewer.showPlansList(dataSource.fetchRemaindersForDate(date));
    }

    @Override
    public void refreshPlansForDate(Date date) {
        viewer.refreshPlansList(dataSource.fetchRemaindersForDate(date));
    }

    @Override
    public void selectedPractice(int position) {
        super.selectedPractice(position);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addNewRemainder(position);
            }
        }, 750);
    }

    private void addNewRemainder(final int position) {
        viewer.getRepeaterToggle(new RepeaterDialogFragment.OnRemainderDetailsListener() {
            @Override
            public void onRemainderDetailsDone(int hourOfDay, int minute, int togglePosition) {
                saveRemainder(hourOfDay, minute, position, togglePosition);
            }
        });
    }

    private void saveRemainder(int hourOfDay, int minute, int position, int togglePosition) {
        Date date = viewer.getSelectedDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        PracticeModel practice = dataSource.fetchUnfinishedPractices().get(position);
        dataSource.addRemainder(calendar.getTimeInMillis(), togglePosition, practice);
        refreshPlansForDate(date);
    }
}
