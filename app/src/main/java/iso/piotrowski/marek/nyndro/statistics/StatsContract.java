package iso.piotrowski.marek.nyndro.statistics;

import java.util.List;

import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;

/**
 * Created by marek.piotrowski on 18/08/2017.
 */

public class StatsContract {
    interface IViewer {
        void setPresenter(IPresenter presenter);
        void showAnalysisResult(HistoryAnalysis[] historyAnalysises);
    }

    interface IPresenter {
        HistoryAnalysis[] doHistoryAnalysis(boolean refresh);
        void propagateAnalysis(HistoryAnalysis[] historyAnalysises);
    }
}
