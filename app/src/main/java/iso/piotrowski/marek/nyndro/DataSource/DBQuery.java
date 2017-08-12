package iso.piotrowski.marek.nyndro.DataSource;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.tools.Utility;

/**
 * Created by marek.piotrowski on 06/08/2017.
 */

public class DBQuery {

    public static List<PracticeModel> getPractices() {
        return new Select().from(PracticeModel.class).orderBy("PROGRESS").execute();
    }

    public static PracticeModel getPractice(long id) {
        return new Select().from(PracticeModel.class).where("_id = ?", id).executeSingle();
    }

    public static List<HistoryModel> getHistory() {
        return new Select().from(HistoryModel.class).orderBy("PRACTICE_DATE").execute();
    }

    public static HistoryModel getHistoryForPractice(long practiceId) {
        return new Select().from(HistoryModel.class).where("PRACTICE_ID = ?", practiceId).orderBy("PRACTICE_DATE DESC").executeSingle();
    }

    public static HistoryModel getHistoryForDate(long dateInSecondsFrom1970, long practiceId) {
        return new Select().from(HistoryModel.class).where("PRACTICE_DATE = ? AND PRACTICE_ID = ?", dateInSecondsFrom1970, practiceId).executeSingle();
    }

    public static void addHistoryForPractice(PracticeModel practice, int multiple){
        long secondsFrom1970 = Utility.removeTimeFromDate(new Date()).getTime();
        HistoryModel history = getHistoryForDate(secondsFrom1970, practice.getID());
        int addRepetition = practice.getRepetition() * multiple;
        if (history == null) {
            new HistoryModel().setActive(true)
                    .setPractice(practice)
                    .setPracticeData(secondsFrom1970)
                    .setPracticeId(practice.getID())
                    .setProgress(practice.getProgress() + addRepetition)
                    .setRepetition(addRepetition)
                    .save();
        } else {
            history.setRepetition(history.getRepetition() + addRepetition)
                    .setProgress(history.getProgress() + addRepetition)
                    .save();
        }
    }

    public static void addProgressToPractice (PracticeModel practice, int multiple) {
        practice.setProgress(practice.getProgress() + practice.getRepetition() * multiple)
                .save();
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
