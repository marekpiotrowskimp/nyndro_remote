package iso.piotrowski.marek.nyndro.DataSource;

import java.util.List;

import iso.piotrowski.marek.nyndro.Model.PracticeModel;

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
}
