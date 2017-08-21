package iso.piotrowski.marek.nyndro.PracticeMain;

import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.DataSource.IDataSource;

/**
 * Created by marek.piotrowski on 21/08/2017.
 */

public class PracticeMainPresenter implements PracticeMainContract.IPresenter {

    private PracticeMainContract.IViewer viewer;
    private IDataSource dataSource;

    PracticeMainPresenter (PracticeMainContract.IViewer viewer, IDataSource dataSource){
        this.viewer = viewer;
        this.dataSource = dataSource;
        this.viewer.setPresenter(this);
    }

    @Override
    public void insertPractice(Practice practice) {
        dataSource.insertPractice(practice);
    }
}
