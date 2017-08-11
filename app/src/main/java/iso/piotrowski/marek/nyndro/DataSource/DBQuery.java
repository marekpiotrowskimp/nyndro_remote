package iso.piotrowski.marek.nyndro.DataSource;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import java.util.List;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;

/**
 * Created by marek.piotrowski on 06/08/2017.
 */

public class DBQuery {

    public static List<PracticeModel> getPractices() {
        return new Select().from(PracticeModel.class).execute();
    }

    public static PracticeModel getPractice(long id) {
        return new Select().from(PracticeModel.class).where("_id = ?", id).executeSingle();
    }

    public static List<HistoryModel> getHistory() {
        return new Select().from(HistoryModel.class).execute();
    }

    public static List<ReminderModel> getReminders() {
        return new Select().from(ReminderModel.class).execute();
    }

    public static boolean adjustDatabase() {
        if (ActiveAndroid.getDatabase().getVersion() == 2) {
            List<PracticeModel> practices = getPractices();
            List<HistoryModel> histories = getHistory();
            List<ReminderModel> remainders = getReminders();

            if (isNotEmpty(practices, histories, remainders)) {

                for (ReminderModel remainder : remainders) {
                    if (remainder.getPractice() == null) {
                        remainder.setPractice(getPractice(remainder.getID()));
                        remainder.save();
                    }
                }

                for (HistoryModel history : histories) {
                    if (history.getPractice() == null) {
                        history.setPractice(getPractice(history.getID()));
                        history.save();
                    }
                }
            }

        }
        return true;
    }

    private static boolean isNotEmpty(List<PracticeModel> practices, List<HistoryModel> histories, List<ReminderModel> remainders) {
        return practices.size() + histories.size() + remainders.size() > 0;
    }

}
