package iso.piotrowski.marek.nyndro.PracticeMain.BoomButton;

import java.util.List;

import iso.piotrowski.marek.nyndro.DataSource.IDataSource;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;

/**
 * Created by marek.piotrowski on 05/09/2017.
 */

public class AddedPracticesBoomButtonAdapter implements IBoomButtonAdapter {

    List<PracticeModel> practiceList;

    public AddedPracticesBoomButtonAdapter (IDataSource dataSource) {
        practiceList = dataSource.fetchUnfinishedPractices();
    }

    @Override
    public int getSize() {
        return practiceList.size() > 9 ? 9 : practiceList.size();
    }

    @Override
    public int getIconResourcesId(int position) {
        return practiceList.get(position).getPracticeImageId();
    }

    @Override
    public String getName(int position) {
        return practiceList.get(position).getName();
    }
}
