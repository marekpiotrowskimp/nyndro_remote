package iso.piotrowski.marek.nyndro.history;

import iso.piotrowski.marek.nyndro.DataSource.IDataSource;

/**
 * Created by marek.piotrowski on 18/08/2017.
 */

public class HistoryPresenter implements HistoryContract.IPresenter {

    private HistoryContract.IViewer viewer;
    private IDataSource dataSource;

    HistoryPresenter (HistoryContract.IViewer viewer, IDataSource dataSource){
        this.viewer = viewer;
        this.dataSource = dataSource;
        this.viewer.setPresenter(this);
    }

    @Override
    public void loadHistoryData() {
        viewer.showHistory(dataSource.fetchHistory());
    }
}
