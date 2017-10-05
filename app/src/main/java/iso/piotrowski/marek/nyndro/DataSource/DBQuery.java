package iso.piotrowski.marek.nyndro.DataSource;

import android.content.Context;
import android.content.SharedPreferences;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;

import java.util.Date;
import java.util.List;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.tools.Utility;

/**
 * Created by marek.piotrowski on 06/08/2017.
 */

public class DBQuery {
    private static String NYNDRO_PREFERENCES = "NyndroPreferences";
    private static String DB_VERSION_2 = "NyndroPreferences";


    public static List<PracticeModel> getPractices() {
        return new Select().from(PracticeModel.class).orderBy("PROGRESS DESC").where("ACTIVE = 1").execute();
    }

    public static List<PracticeModel> getUnfinishedPractices() {
        return new Select().from(PracticeModel.class).orderBy("PROGRESS DESC").where("ACTIVE = 1 AND PROGRESS < MAX_REPETITION").execute();
    }

    public static PracticeModel getPractice(long id) {
        return new Select().from(PracticeModel.class).where("_id = ?", id).executeSingle();
    }

    public static List<HistoryModel> getHistory() {
        return new Select().from(HistoryModel.class).orderBy("PRACTICE_ID, PRACTICE_DATE DESC").where("ACTIVE = 1").execute();
    }

    public static HistoryModel getHistoryForPractice(long practiceId) {
        return new Select().from(HistoryModel.class).where("PRACTICE_ID = ?", practiceId).orderBy("PRACTICE_DATE DESC").executeSingle();
    }

    public static ReminderModel getNearestRemainderForPractice(long practiceId) {
        return new Select().from(ReminderModel.class).where("PRACTICE_ID = ?", practiceId).orderBy("PRACTICE_DATE DESC").executeSingle();
    }

    public static List<HistoryModel> getAllHistoryForPractice(long practiceId) {
        return new Select().from(HistoryModel.class).where("PRACTICE_ID = ?", practiceId).orderBy("PRACTICE_DATE DESC").execute();
    }

    public static HistoryModel getHistoryForDate(long dateInSecondsFrom1970, long practiceId) {
        return new Select().from(HistoryModel.class).where("PRACTICE_DATE = ? AND PRACTICE_ID = ?", dateInSecondsFrom1970, practiceId).executeSingle();
    }

    public static void addHistoryForPractice(PracticeModel practice, int addProgress){
        long secondsFrom1970 = Utility.removeTimeFromDate(new Date()).getTime();
        HistoryModel history = getHistoryForDate(secondsFrom1970, practice.getID());
        int addRepetition = addProgress;
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

    public static void markActive (PracticeModel practice, boolean active){
        updateActive(practice, active, PracticeModel.class, "_id = ?");
        updateActive(practice, active, HistoryModel.class, "PRACTICE_ID = ?");
        updateActive(practice, active, ReminderModel.class, "PRACTICE_ID = ?");
    }

    private static void updateActive(PracticeModel practice, boolean active, Class<? extends Model> tableClass, String whereColumn) {
        new Update(tableClass).set("ACTIVE = ?", String.valueOf(active ? 1 : 0))
                .where(whereColumn, practice.getID())
                .execute();
    }

    public static void deleteNoActive(){
        deleteNoActiveEntry(HistoryModel.class);
        deleteNoActiveEntry(ReminderModel.class);
        deleteNoActiveEntry(PracticeModel.class);
    }

    private static void deleteNoActiveEntry(Class<? extends Model> tableClass) {
        new Delete().from(tableClass)
                .where("ACTIVE = 0")
                .execute();
    }

    public static void addProgressToPractice (PracticeModel practice, int addProgress) {
        practice.setProgress(practice.getProgress() + addProgress)
                .save();
    }

    public static List<ReminderModel> getReminders() {
        return new Select().from(ReminderModel.class).where("ACTIVE = 1").execute();
    }

    public static List<ReminderModel> getRemindersForDate(Date date) {
        Date dateWithoutHours = Utility.removeTimeFromDate(date);
        Date nextDay = Utility.addDayToDate(dateWithoutHours);
        return new Select().from(ReminderModel.class)
                .where("ACTIVE = 1 AND PRACTICE_DATE > ? AND PRACTICE_DATE < ?", dateWithoutHours.getTime(), nextDay.getTime())
                .execute();
    }

    public static void addReminders(long date, int repeater, PracticeModel practice) {
        new ReminderModel().setRepetition(practice.getRepetition())
                .setPracticeId(practice.getID())
                .setPractice(practice)
                .setRepeater(repeater)
                .setPracticeDate(date)
                .setActive(true)
                .save();
    }

    public static void updateReminders(ReminderModel reminder, long date, int repeater, PracticeModel practice) {
        reminder.setRepetition(practice.getRepetition())
                .setPracticeId(practice.getID())
                .setPractice(practice)
                .setRepeater(repeater)
                .setPracticeDate(date)
                .setActive(true)
                .save();
    }

    public static void deleteReminder(long remainderId) {
        new Delete().from(ReminderModel.class)
                .where("_id = ?", remainderId)
                .execute();
    }

    public static void updatePractice (PracticeModel practice){
        practice.save();
    }

    public static void insertPractice (Practice practice) {
        new PracticeModel().setName(practice.getName())
                .setProgress(practice.getProgress())
                .setRepetition(practice.getRepetition())
                .setActive(true)
                .setDescription(practice.getDescription())
                .setMaxRepetition(practice.getMaxRepetition())
                .setPracticeImageId(practice.getImageResourcesId())
                .save();
    }

    public static boolean adjustDatabase() {
        SharedPreferences preferences = NyndroApp.getContext().getSharedPreferences(NYNDRO_PREFERENCES, Context.MODE_PRIVATE);
        if (!preferences.getBoolean(DB_VERSION_2, false)) {
            if (ActiveAndroid.getDatabase().getVersion() == 2) {
                List<PracticeModel> practices = getPractices();
                List<HistoryModel> histories = getHistory();
                List<ReminderModel> remainders = getReminders();
                if (isNotEmpty(practices, histories, remainders)) {
                    for (ReminderModel remainder : remainders) {
                        if (remainder.getPractice() == null) {
                            remainder.setPractice(getPractice(remainder.getPracticeId()));
                            remainder.save();
                        }
                    }
                    for (HistoryModel history : histories) {
                        if (history.getPractice() == null) {
                            history.setPractice(getPractice(history.getPracticeId()));
                            history.save();
                        }
                    }
                }
                for (PracticeModel practice : practices) {
                    practice.setPracticeImageId(checkIsOldVersionId(practice.getRawPracticeImageId()));
                    practice.save();
                }

            }
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(DB_VERSION_2, true);
            editor.apply();
            editor.commit();
        }
        return true;
    }

    private static int checkIsOldVersionId(int value) {
        switch (value) {
            case 2130837588:
                return 0;
            case 2130837587:
                return 1;
            case 2130837590:
                return 2;
            case 2130837580:
                return 3;
            case 2130837586:
                return 4;
            case 2130837584:
                return 5;
            case 2130837591:
                return 6;
            case 2130837585:
                return 7;
        }
        return value;
    }


    private static boolean isNotEmpty(List<PracticeModel> practices, List<HistoryModel> histories, List<ReminderModel> remainders) {
        return practices.size() + histories.size() + remainders.size() > 0;
    }
}
