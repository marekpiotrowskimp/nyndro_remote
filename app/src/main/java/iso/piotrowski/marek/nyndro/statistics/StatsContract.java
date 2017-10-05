package iso.piotrowski.marek.nyndro.statistics;

import iso.piotrowski.marek.nyndro.FragmentsFactory.IBasePresenter;
import iso.piotrowski.marek.nyndro.FragmentsFactory.IBaseViewer;

/**
 * Created by marek.piotrowski on 18/08/2017.
 */

public class StatsContract {
    interface IViewer extends IBaseViewer {
        void setPresenter(IPresenter presenter);
        void showAnalysisResult(HistoryAnalysis[] historyAnalysises);
    }

    interface IPresenter extends IBasePresenter {
        HistoryAnalysis[] doHistoryAnalysis();
        void propagateAnalysis(HistoryAnalysis[] historyAnalysises);
    }
}
