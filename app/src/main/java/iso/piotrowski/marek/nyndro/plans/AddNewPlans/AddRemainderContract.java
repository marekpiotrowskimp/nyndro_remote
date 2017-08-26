package iso.piotrowski.marek.nyndro.plans.AddNewPlans;

import java.util.List;

import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.plans.PlansContract;
import iso.piotrowski.marek.nyndro.tools.Fragments.IBasePresenter;
import iso.piotrowski.marek.nyndro.tools.Fragments.IBaseViewer;

/**
 * Created by marek.piotrowski on 19/08/2017.
 */

public class AddRemainderContract {
    interface IViewer extends IBaseViewer{
        void setPresenter(IPresenter presenter);
        void setUpPracticeRecyclerView(List<PracticeModel> practiceList);
    }

    interface IPresenter extends IBasePresenter{
        void loadPracticeData();
        void addReminder(long date, int repeater, PracticeModel practice);
        void updateReminder(ReminderModel reminder, long date, int repeater, PracticeModel practice);
    }

}
