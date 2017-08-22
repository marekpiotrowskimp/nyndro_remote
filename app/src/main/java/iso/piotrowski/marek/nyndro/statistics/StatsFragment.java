package iso.piotrowski.marek.nyndro.statistics;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.Fragments.FragmentsFactory;
import iso.piotrowski.marek.nyndro.tools.Fragments.IFragmentParams;

public class StatsFragment extends Fragment implements StatsContract.IViewer, IFragmentParams {
    private RecyclerView statsRecyclerView;
    private StatsContract.IPresenter presenter;

    public StatsFragment() {
    }

    public static StatsFragment getInstance() {
        return new StatsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new StatsPresenter(this, DataSource.getInstance());
        statsRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_stats, container, false);
        calculateStatsInBackground(false);
        return statsRecyclerView;
    }

    private void calculateStatsInBackground(boolean refresh) {
        new Thread() {
            @Override
            public void run() {
                HistoryAnalysis[] historyAnalysises = presenter.doHistoryAnalysis(refresh);
                new Handler(NyndroApp.getContect().getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        presenter.propagateAnalysis(historyAnalysises);
                    }
                });
            }
        }.start();
    }

    @Override
    public void setPresenter(StatsContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showAnalysisResult(HistoryAnalysis[] historyAnalysises) {
        showDataOfAnalysis(historyAnalysises);
    }

    private void showDataOfAnalysis(HistoryAnalysis[] historyAnalysises) {
        StatsRecyclerViewAdapter statsRecyclerViewAdapter = new StatsRecyclerViewAdapter();
        statsRecyclerViewAdapter.setHistoryAnalysis(historyAnalysises);
        statsRecyclerViewAdapter.setDays(getActivity().getResources().getStringArray(R.array.day_of_week));
        statsRecyclerViewAdapter.setMonths(getActivity().getResources().getStringArray(R.array.months));
        statsRecyclerView.setAdapter(statsRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        statsRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public String getFragmentName() {
        return getResources().getString(R.string.app_label_stats);
    }

    @Override
    public FragmentsFactory.TypeOfFragment getTypeOf() {
        return FragmentsFactory.TypeOfFragment.Stats;
    }

    @Override
    public boolean isButtonVisible() {
        return false;
    }
}
