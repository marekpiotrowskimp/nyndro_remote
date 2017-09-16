package iso.piotrowski.marek.nyndro.plans.PlansList;

import java.util.Date;
import java.util.List;

import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.plans.RepeaterDialog.RepeaterDialogFragment;
import iso.piotrowski.marek.nyndro.FragmentsFactory.IBasePresenter;
import iso.piotrowski.marek.nyndro.FragmentsFactory.IBaseViewer;

/**
 * Created by marek.piotrowski on 04/09/2017.
 */

public class PlansListContract {

    interface IViewer extends IBaseViewer{
        void setPresenter (IPresenter presenter);
        void showPlansList (List<ReminderModel> reminderList);
        void refreshPlansList (List<ReminderModel> reminderList);
        void getRepeaterToggle(RepeaterDialogFragment.OnRemainderDetailsListener toggleSwitchListener);
        Date getSelectedDate();
    }

    interface IPresenter extends IBasePresenter {
        void removeRemainder(long remainderId);
        void loadPlansForDate (Date date);
        void refreshPlansForDate (Date date);
    }
}
