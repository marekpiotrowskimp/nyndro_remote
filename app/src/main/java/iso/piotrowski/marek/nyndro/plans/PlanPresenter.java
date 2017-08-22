package iso.piotrowski.marek.nyndro.plans;

import iso.piotrowski.marek.nyndro.DataSource.DBQuery;
import iso.piotrowski.marek.nyndro.DataSource.IDataSource;

/**
 * Created by marek.piotrowski on 19/08/2017.
 */

public class PlanPresenter implements PlansContract.IPresenter {

    private PlansContract.IViewer viewer;
    private IDataSource dataSource;

    PlanPresenter (PlansContract.IViewer viewer, IDataSource dataSource){
        this.viewer = viewer;
        this.dataSource = dataSource;
        this.viewer.setPresenter(this);
    }

    @Override
    public void loadPlansData() {
        viewer.showPlans(DBQuery.getReminders());
    }

    @Override
    public void refreshPlansData() {
        viewer.refreshPlans(DBQuery.getReminders());
    }

    @Override
    public void removeRemainder(long remainderId) {
        dataSource.deleteRemainder(remainderId);
    }
}