package iso.piotrowski.marek.nyndro.history;


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
import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.parsers.Parser;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.FragmentsFactory.FragmentsFactory;
import iso.piotrowski.marek.nyndro.FragmentsFactory.NyndroFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends NyndroFragment implements HistoryContract.IViewer {

    @BindView(R.id.history_recyclerview) RecyclerView statsRecyclerView;

    public HistoryFragment() {
    }

    public static HistoryFragment getInstance(){
        return new HistoryFragment();
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
        getPresenter().loadHistoryData();
    }

    private HistoryContract.IPresenter getPresenter(){
        return (HistoryContract.IPresenter) presenter;
    }

    @Override
    public void setPresenter(HistoryContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showHistory(List<HistoryModel> historyList) {
        HistoryRecyclerViewAdapter historyRecyclerViewAdapter =
                new HistoryRecyclerViewAdapter(Parser.convertHistoryModelToSectioned(historyList));
        statsRecyclerView.setAdapter(historyRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        statsRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public String getFragmentName() {
        return NyndroApp.getContext().getResources().getString(R.string.app_label_history);
    }

    @Override
    public FragmentsFactory.TypeOfFragment getTypeOf() {
        return FragmentsFactory.TypeOfFragment.History;
    }

    @Override
    public boolean isButtonVisible() {
        return false;
    }
}
