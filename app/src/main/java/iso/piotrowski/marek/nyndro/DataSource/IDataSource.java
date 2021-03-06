package iso.piotrowski.marek.nyndro.DataSource;

import java.util.Date;
import java.util.List;

import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;

/**
 * Created by marek.piotrowski on 11/08/2017.
 */

public interface IDataSource {
    List<PracticeModel> fetchPractices();
    List<PracticeModel> fetchUnfinishedPractices();
    void insertPractice (Practice practice);
    List<HistoryModel> fetchHistory();
    List<HistoryModel> fetchHistoryForPractice(long practiceId);
    PracticeModel fetchPractice(long practiceId);
    HistoryModel fetchLastHistoryOfPractice(long practiceId);
    ReminderModel fetchNextPlanedOfPractice(long practiceId);
    List<ReminderModel> fetchRemainders();
    List<ReminderModel> fetchRemaindersForDate(Date date);

    void addHistoryForPractice(PracticeModel practice, int multiple);
    void addProgressToPractice (PracticeModel practice, int multiple);
    void markActive (PracticeModel practice, boolean active);
    void deleteNoActive();
    void updatePractice(PracticeModel practice);
    void deleteRemainder (long remainderId);
    void addRemainder (long date, int repeater, PracticeModel practice);
    void updateRemainder (ReminderModel reminder, long date, int repeater, PracticeModel practice);
}
