package iso.piotrowski.marek.nyndro.plans;

import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.DataSource.IDataSource;
import iso.piotrowski.marek.nyndro.plans.AddNewPlans.AddRemainderFragment;
import iso.piotrowski.marek.nyndro.tools.Fragments.Navigator;
import iso.piotrowski.marek.nyndro.tools.Fragments.NyndroPresenter;

/**
 * Created by marek.piotrowski on 19/08/2017.
 */

public class PlanPresenter extends NyndroPresenter implements PlansContract.IPresenter {

    private PlansContract.IViewer viewer;
    private IDataSource dataSource;

    PlanPresenter (PlansContract.IViewer viewer, IDataSource dataSource){
        this.viewer = viewer;
        this.dataSource = dataSource;
        this.viewer.setPresenter(this);
    }

    @Override
    public void loadPlansData() {
        viewer.showPlans(dataSource.fetchRemainders());
    }

    @Override
    public void refreshPlansData() {
        viewer.refreshPlans(dataSource.fetchRemainders());
    }

    @Override
    public void removeRemainder(long remainderId) {
        dataSource.deleteRemainder(remainderId);
    }

}
