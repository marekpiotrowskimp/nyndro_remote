package iso.piotrowski.marek.nyndro.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iso.piotrowski.marek.nyndro.DataSource.DBQuery;
import iso.piotrowski.marek.nyndro.DataSource.IDataSource;
import iso.piotrowski.marek.nyndro.Model.AnalysisInfo;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.history.HistoryRecyclerViewAdapter;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;

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
    public void doHistoryAnalysis() {
        List<PracticeModel> practices = dataSource.fetchPractices();
        int count = practices.size();
        HistoryAnalysis[] historyAnalysises = new HistoryAnalysis[count];
        for (int ind = 0; ind < count; ind++) {
            PracticeModel practice = practices.get(ind);
            List<HistoryModel> historyList = dataSource.fetchHistoryForPractice(practice.getID());
            historyAnalysises[ind] = new HistoryAnalysis(historyList);
            if (historyAnalysises[ind].getAnalysisResult().isEmpty()) {
                Map<String, AnalysisInfo> analysisEmpty = new HashMap<String, AnalysisInfo>();
                            analysisEmpty.put("practice_name", new AnalysisInfo(practice.getName()));
                            analysisEmpty.put("practice_image_id", new AnalysisInfo(practice.getPracticeImageId()));
                historyAnalysises[ind].setAnalysisResult(analysisEmpty);
            }
        }
        viewer.showAnalysisResult(historyAnalysises);
    }
}
