package iso.piotrowski.marek.nyndro.PracticeMain;

import iso.piotrowski.marek.nyndro.DataSource.IDataSource;
import iso.piotrowski.marek.nyndro.PracticeMain.BoomButton.BoomButtonFactory;
import iso.piotrowski.marek.nyndro.tools.Fragments.NyndroPresenter;

/**
 * Created by marek.piotrowski on 21/08/2017.
 */

public class PracticeMainPresenter extends NyndroPresenter implements PracticeMainContract.IPresenter {

    private PracticeMainContract.IViewer viewer;
    private IDataSource dataSource;

    PracticeMainPresenter (PracticeMainContract.IViewer viewer, IDataSource dataSource){
        this.viewer = viewer;
        this.dataSource = dataSource;
        this.viewer.setPresenter(this);
    }

    @Override
    public void adjustBoomButton(PracticeMainContract.TypeOfBoomButton typeOfBoomButton) {
        viewer.setUpBoomButton(BoomButtonFactory.getBoomButtonAdapter(typeOfBoomButton, dataSource));
    }

    @Override
    public void adjustBoomButtonToolBar(PracticeMainContract.TypeOfBoomButton typeOfBoomButton) {
        viewer.setUpBoomButtonToolBar(BoomButtonFactory.getBoomButtonAdapter(typeOfBoomButton, dataSource));
    }
}
