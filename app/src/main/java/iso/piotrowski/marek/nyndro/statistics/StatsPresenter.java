package iso.piotrowski.marek.nyndro.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iso.piotrowski.marek.nyndro.DataSource.IDataSource;
import iso.piotrowski.marek.nyndro.Model.AnalysisInfo;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;

/**
 * Created by marek.piotrowski on 18/08/2017.
 */

public class StatsPresenter implements StatsContract.IPresenter {

    private StatsContract.IViewer viewer;
    private IDataSource dataSource;

    StatsPresenter (StatsContract.IViewer viewer, IDataSource dataSource) {
        this.viewer = viewer;
        this.dataSource = dataSource;
        this.viewer.setPresenter(this);
    }

    @Override
    public HistoryAnalysis[] doHistoryAnalysis(boolean refresh) {
        List<PracticeModel> practices = dataSource.fetchPractices();
        int count = practices.size();
        HistoryAnalysis[] historyAnalysis = new HistoryAnalysis[count];
        for (int ind = 0; ind < count; ind++) {
            PracticeModel practice = practices.get(ind);
            List<HistoryModel> historyList = dataSource.fetchHistoryForPractice(practice.getID());
            historyAnalysis[ind] = new HistoryAnalysis(historyList);
            if (historyAnalysis[ind].getAnalysisResult().isEmpty()) {
                Map<String, AnalysisInfo> analysisEmpty = new HashMap<String, AnalysisInfo>();
                            analysisEmpty.put("practice_name", new AnalysisInfo(practice.getName()));
                            analysisEmpty.put("practice_image_id", new AnalysisInfo(practice.getPracticeImageId()));
                historyAnalysis[ind].setAnalysisResult(analysisEmpty);
            }
        }
        return historyAnalysis;
    }

    @Override
    public void propagateAnalysis(HistoryAnalysis[] historyAnalysises){
        viewer.showAnalysisResult(historyAnalysises);
    }

    @Override
    public void loadData() {

    }

    @Override
    public void refreshData() {

    }
}
