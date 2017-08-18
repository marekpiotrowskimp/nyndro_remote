package iso.piotrowski.marek.nyndro.statistics;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.DataSource.PracticeDatabaseHelper;
import iso.piotrowski.marek.nyndro.Model.AnalysisInfo;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;
import iso.piotrowski.marek.nyndro.history.HistoryRecyclerViewAdapter;

import java.util.HashMap;
import java.util.Map;

public class StatsFragment extends Fragment implements StatsContract.IViewer {
    private RecyclerView statsRecyclerView;
    private StatsContract.IPresenter presenter;

    public StatsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new StatsPresenter(this, DataSource.getInstance());
        statsRecyclerView= (RecyclerView) inflater.inflate(R.layout.fragment_stats, container, false);
        calculateStatsInBackround();
        return statsRecyclerView;
    }

    private void calculateStatsInBackround ()
    {
        new Thread(){
            @Override
            public void run() {
                presenter.doHistoryAnalysis();
            }
        }.start();
    }

    @Override
    public void setPresenter(StatsContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showAnalysisResult(HistoryAnalysis[] historyAnalyses) {
        StatsRecyclerViewAdaprer statsRecyclerViewAdaprer = new StatsRecyclerViewAdaprer();
        statsRecyclerViewAdaprer.setHistoryAnalysis(historyAnalyses);
        statsRecyclerViewAdaprer.setDays(getActivity().getResources().getStringArray(R.array.day_of_week));
        statsRecyclerViewAdaprer.setMonths(getActivity().getResources().getStringArray(R.array.months));

        statsRecyclerView.setAdapter(statsRecyclerViewAdaprer);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        statsRecyclerView.setLayoutManager(linearLayoutManager);

    }
}
