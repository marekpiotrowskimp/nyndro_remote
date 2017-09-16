package iso.piotrowski.marek.nyndro.plans;

import java.util.List;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.tools.Fragments.IBasePresenter;
import iso.piotrowski.marek.nyndro.tools.Fragments.IBaseViewer;

/**
 * Created by marek.piotrowski on 19/08/2017.
 */

public class PlansContract {
    interface IViewer extends IBaseViewer{
        void setPresenter(IPresenter presenter);
        void showPlans (List<ReminderModel> reminderList);
        void refreshPlans (List<ReminderModel> reminderList);
    }

    interface IPresenter extends IBasePresenter{
        void loadPlansData();
        void refreshPlansData();
        void removeRemainder(long remainderId);
    }
}
