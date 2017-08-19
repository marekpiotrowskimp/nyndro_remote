package iso.piotrowski.marek.nyndro.plans;

import java.util.List;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;

/**
 * Created by marek.piotrowski on 19/08/2017.
 */

public class PlansContract {
    interface IViewer {
        void setPresenter(IPresenter presenter);
        void showPlans (List<ReminderModel> reminderList);
        void refreshPlans (List<ReminderModel> reminderList);
    }

    interface IPresenter {
        void loadPlansData();
        void refreshPlansData();
        void removeRemainder(long remainderId);
    }
}
