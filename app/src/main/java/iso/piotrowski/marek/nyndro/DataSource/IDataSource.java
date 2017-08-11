package iso.piotrowski.marek.nyndro.DataSource;

import java.util.List;

import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;

/**
 * Created by marek.piotrowski on 11/08/2017.
 */

public interface IDataSource {
    List<PracticeModel> fetchPractices();

}
