package iso.piotrowski.marek.nyndro.DataSource;

import java.util.List;

import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;

/**
 * Created by marek.piotrowski on 11/08/2017.
 */

public class DataSource implements IDataSource {

    private static DataSource instance;

    private DataSource(){
    }

    public static DataSource getInstance(){
        return instance == null ? instance = new DataSource() : instance;
    }

    @Override
    public List<PracticeModel> fetchPractices() {
        return DBQuery.getPractices();
    }

    @Override
    public HistoryModel fetchLastHistoryOfPractice(long practiceId) {
        return DBQuery.getHistoryForPractice(practiceId);
    }

    @Override
    public ReminderModel fetchNextPlanedOfPractice(long practiceId) {
        return null;
    }

    @Override
    public void addHistoryForPractice(PracticeModel practice, int multiple) {
        DBQuery.addHistoryForPractice(practice, multiple);
    }

    @Override
    public void addProgressToPractice(PracticeModel practice, int multiple) {
        DBQuery.addProgressToPractice(practice, multiple);
    }

    @Override
    public void markActive(PracticeModel practice, boolean active) {
        DBQuery.markActive(practice, active);
    }

    @Override
    public void deleteNoActive() {
        DBQuery.deleteNoActive();
    }


}
