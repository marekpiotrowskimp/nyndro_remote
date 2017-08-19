package iso.piotrowski.marek.nyndro.plans.AddNewPlans;

import java.util.List;

import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.plans.PlansContract;

/**
 * Created by marek.piotrowski on 19/08/2017.
 */

public class AddRemainderContract {
    interface IViewer {
        void setPresenter(IPresenter presenter);
        void setUpPracticeRecyclerView(List<PracticeModel> practiceList);
    }

    interface IPresenter {
        void loadPracticeData();
        void addReminder(long date, int repeater, PracticeModel practice);
        void updateReminder(ReminderModel reminder, long date, int repeater, PracticeModel practice);
    }

}
