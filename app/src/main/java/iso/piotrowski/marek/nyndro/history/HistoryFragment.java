package iso.piotrowski.marek.nyndro.history;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.DataSource.PracticeDatabaseHelper;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements HistoryContract.IViewer {

    @BindView(R.id.history_recyclerview) RecyclerView statsRecyclerView;
    private HistoryContract.IPresenter presenter;

    public HistoryFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new HistoryPresenter(this, DataSource.getInstance());
        LinearLayout historicalLayout = (LinearLayout) inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, historicalLayout);
        return historicalLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadHistoryData();
    }

    @Override
    public void setPresenter(HistoryContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showHistory(List<HistoryModel> historyList) {
        HistoryRecyclerViewAdapter historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(historyList);
        statsRecyclerView.setAdapter(historyRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        statsRecyclerView.setLayoutManager(linearLayoutManager);
    }
}
