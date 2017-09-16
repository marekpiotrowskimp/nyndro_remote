package iso.piotrowski.marek.nyndro.plans.PlansList;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.PracticeMain.PracticeMainContract;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.plans.RepeaterDialog.RepeaterDialogFragment;
import iso.piotrowski.marek.nyndro.practice.SimpleCallbackForTouches;
import iso.piotrowski.marek.nyndro.FragmentsFactory.NyndroFragment;

/**
 * Created by marek.piotrowski on 04/09/2017.
 */

public class PlansListFragment extends NyndroFragment implements PlansListContract.IViewer, PlansAdapter.OnRemainderItemClickListener{

    @BindView(R.id.plans_recyclerview) RecyclerView plansRecyclerView;
    private Date selectedDate;

    public PlansListFragment () {
    }

    public static PlansListFragment getInstance(Date selectedDate){
        PlansListFragment plansListFragment = new PlansListFragment();
        plansListFragment.selectedDate = selectedDate;
        return plansListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_plans_list,container,false);
        ButterKnife.bind(this, linearLayout);
        return linearLayout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new PlansListPresenter(this, DataSource.getInstance());
        setItemTouchHelper();
        getPresenter().loadPlansForDate(selectedDate);
    }

    private PlansListContract.IPresenter getPresenter(){
        return (PlansListContract.IPresenter) presenter;
    }

    @Override
    public void setPresenter(PlansListContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public String getFragmentName() {
        return NyndroApp.getContext().getResources().getString(R.string.plans_list_name) + String.format(" %tD", selectedDate);
    }

    @Override
    public boolean isButtonVisible() {
        return true;
    }

    public void showList(List<ReminderModel> reminderList, boolean refresh) {
        PlansAdapter plansAdapter = new PlansAdapter(reminderList);
        plansAdapter.setOnRemainderItemClickListener(this);
        if (refresh)
            plansRecyclerView.swapAdapter(plansAdapter, false);
        else
            plansRecyclerView.setAdapter(plansAdapter);
        plansRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void showPlansList(List<ReminderModel> reminderList) {
        showList(reminderList, false);
    }

    @Override
    public void refreshPlansList(List<ReminderModel> reminderList) {
        showList(reminderList, true);
    }

    @Override
    public void getRepeaterToggle(RepeaterDialogFragment.OnRemainderDetailsListener remainderDetailsListener) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog_toggle");
        if (prev != null) {
            ft.remove(prev);
        }
        RepeaterDialogFragment.newInstance(remainderDetailsListener).show(getActivity().getFragmentManager(), "dialog_toggle");
    }

    @Override
    public Date getSelectedDate() {
        return selectedDate;
    }

    @Override
    public PracticeMainContract.TypeOfBoomButton getTypeOfBoomButton() {
        return PracticeMainContract.TypeOfBoomButton.AddedPractice;
    }

    private void setItemTouchHelper() {
        ItemTouchHelper.SimpleCallback callback =  new SimpleCallbackForTouches(0, ItemTouchHelper.LEFT, new SimpleCallbackForTouches.OnSwipedListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                getPresenter().removeRemainder(((PlansAdapter.PlansHolder) viewHolder).getReminder().getID());
                getPresenter().refreshPlansForDate(selectedDate);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(plansRecyclerView);
    }

    @Override
    public void OnClick(View view, int position, ReminderModel reminder) {
    }

}
