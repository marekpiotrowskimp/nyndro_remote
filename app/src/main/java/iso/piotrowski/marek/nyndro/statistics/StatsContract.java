package iso.piotrowski.marek.nyndro.statistics;

import java.util.List;

import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.tools.Fragments.IBasePresenter;
import iso.piotrowski.marek.nyndro.tools.Fragments.IBaseViewer;

/**
 * Created by marek.piotrowski on 18/08/2017.
 */

public class StatsContract {
    interface IViewer extends IBaseViewer {
        void setPresenter(IPresenter presenter);
        void showAnalysisResult(HistoryAnalysis[] historyAnalysises);
    }

    interface IPresenter extends IBasePresenter {
        HistoryAnalysis[] doHistoryAnalysis(boolean refresh);
        void propagateAnalysis(HistoryAnalysis[] historyAnalysises);
    }
}
