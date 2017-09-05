package iso.piotrowski.marek.nyndro.plans.PlansList;

import java.util.Date;

import iso.piotrowski.marek.nyndro.DataSource.IDataSource;
import iso.piotrowski.marek.nyndro.plans.AddNewPlans.AddRemainderFragment;
import iso.piotrowski.marek.nyndro.tools.Fragments.Navigator;
import iso.piotrowski.marek.nyndro.tools.Fragments.NyndroPresenter;

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
//        dataSource.fetchUnfinishedPractices().get(position);
        Navigator.getInstance().changeFragmentInContainer(AddRemainderFragment.getInstance(null), true);
    }
}
