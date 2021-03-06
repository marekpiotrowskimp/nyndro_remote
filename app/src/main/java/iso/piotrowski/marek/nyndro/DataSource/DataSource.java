package iso.piotrowski.marek.nyndro.DataSource;

import java.util.Date;
import java.util.List;

import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.GoogleAnalytics.Analytics;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.tools.Utility;

/**
 * Created by marek.piotrowski on 11/08/2017.
 */

public class DataSource implements IDataSource {

    private static DataSource instance;

    private DataSource(){
    }

    public static DataSource getInstance(){
        DBQuery.adjustDatabase();
        return instance == null ? instance = new DataSource() : instance;
    }

    @Override
    public List<PracticeModel> fetchPractices() {
        return DBQuery.getPractices();
    }

    @Override
    public List<PracticeModel> fetchUnfinishedPractices() {
        return DBQuery.getUnfinishedPractices();
    }

    @Override
    public void insertPractice(Practice practice) {
        Analytics.logPracticeEvent(Analytics.TypeOfEvent.AddPractice.toString(), practice.getName(), "");
        DBQuery.insertPractice(practice);
    }

    @Override
    public List<HistoryModel> fetchHistory() {
        return DBQuery.getHistory();
    }

    @Override
    public List<HistoryModel> fetchHistoryForPractice(long practiceId) {
        return DBQuery.getAllHistoryForPractice(practiceId);
    }

    @Override
    public PracticeModel fetchPractice(long practiceId) {
        return DBQuery.getPractice(practiceId);
    }

    @Override
    public HistoryModel fetchLastHistoryOfPractice(long practiceId) {
        return DBQuery.getHistoryForPractice(practiceId);
    }

    @Override
    public ReminderModel fetchNextPlanedOfPractice(long practiceId) {
        return DBQuery.getNearestRemainderForPractice(practiceId);
    }

    @Override
    public List<ReminderModel> fetchRemainders() {
        return DBQuery.getReminders();
    }

    @Override
    public List<ReminderModel> fetchRemaindersForDate(Date date) {
        return DBQuery.getRemindersForDate(date);
    }

    @Override
    public void addHistoryForPractice(PracticeModel practice, int addProgress) {
        Analytics.logPracticeEvent(Analytics.TypeOfEvent.AddHistory.toString(), practice.getName(),
                String.valueOf(addProgress) + ", " + practice.getStringProgress());
        DBQuery.addHistoryForPractice(practice, addProgress);
    }

    @Override
    public void addProgressToPractice(PracticeModel practice, int addProgress) {
        DBQuery.addProgressToPractice(practice, addProgress);
    }

    @Override
    public void markActive(PracticeModel practice, boolean active) {
        DBQuery.markActive(practice, active);
    }

    @Override
    public void deleteNoActive() {
        DBQuery.deleteNoActive();
    }

    @Override
    public void updatePractice(PracticeModel practice) {
        DBQuery.updatePractice(practice);
    }

    @Override
    public void deleteRemainder(long remainderId) {
        DBQuery.deleteReminder(remainderId);
    }

    @Override
    public void addRemainder(long date, int repeater, PracticeModel practice) {
        Analytics.logPracticeEvent(Analytics.TypeOfEvent.AddRemainder.toString(), practice.getName(),
                Utility.getStringDate(date));
        DBQuery.addReminders(date, repeater, practice);
    }

    @Override
    public void updateRemainder(ReminderModel reminder, long date, int repeater, PracticeModel practice) {
        DBQuery.updateReminders(reminder, date, repeater, practice);
    }


}
