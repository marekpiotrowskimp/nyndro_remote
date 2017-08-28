package iso.piotrowski.marek.nyndro.plans.AddNewPlans;

import iso.piotrowski.marek.nyndro.DataSource.IDataSource;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.tools.Fragments.NyndroPresenter;

/**
 * Created by marek.piotrowski on 19/08/2017.
 */

public class AddRemainderPresenter extends NyndroPresenter implements AddRemainderContract.IPresenter {

    private AddRemainderContract.IViewer viewer;
    private IDataSource dataSource;

    AddRemainderPresenter (AddRemainderContract.IViewer viewer, IDataSource dataSource){
        this.viewer = viewer;
        this.dataSource = dataSource;
        this.viewer.setPresenter(this);
    }

    @Override
    public void loadPracticeData() {
        viewer.setUpPracticeRecyclerView(dataSource.fetchPractices());
    }

    @Override
    public void addReminder(long date, int repeater, PracticeModel practice) {
        dataSource.addRemainder(date, repeater, practice);
    }

    @Override
    public void updateReminder(ReminderModel reminder, long date, int repeater, PracticeModel practice) {
        dataSource.updateRemainder(reminder, date, repeater, practice);;
    }
}
