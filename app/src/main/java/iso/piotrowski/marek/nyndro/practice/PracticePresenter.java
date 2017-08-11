package iso.piotrowski.marek.nyndro.practice;

import iso.piotrowski.marek.nyndro.DataSource.IDataSource;

/**
 * Created by marek.piotrowski on 11/08/2017.
 */

public class PracticePresenter implements PracticeContract.IPresenter {
    private PracticeContract.IViewer viewer;
    private IDataSource dataSource;

    public PracticePresenter (PracticeContract.IViewer viewer, IDataSource dataSource){
        this.viewer = viewer;
        this.dataSource = dataSource;
        this.viewer.setPresenter(this);
    }

    @Override
    public void dataWereChanged() {
        viewer.refreshPracticeRecyclerView(dataSource.fetchPractices());

    }

    @Override
    public void loadPracticeData() {
        viewer.setUpRecyclerView(dataSource.fetchPractices());
    }
}
