package iso.piotrowski.marek.nyndro.plans;

import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.plans.AddNewPlans.AddRemainderFragment;
import iso.piotrowski.marek.nyndro.practice.SimpleCallbackForTouches;
import iso.piotrowski.marek.nyndro.tools.Fragments.FragmentsFactory;
import iso.piotrowski.marek.nyndro.tools.Fragments.IFragmentParams;

public class PlansFragment extends Fragment implements PlansContract.IViewer, IFragmentParams {

    private RecyclerView plansRecyclerView;
    private PlansContract.IPresenter presenter;

    public PlansFragment() {
    }

    public static PlansFragment getInstance(){
        return new PlansFragment();
    }

    @Override
    public void setPresenter(PlansContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPlans(List<ReminderModel> reminderList) {
        showPlans(reminderList, false);
    }

    @Override
    public void refreshPlans(List<ReminderModel> reminderList) {
        showPlans(reminderList, true);
    }

    public void showPlans(List<ReminderModel> reminderList, boolean refresh) {
        PlansAdapter plansAdapter = new PlansAdapter(reminderList);
        plansAdapter.setOnRemainderItemClickListener(new MyOnRemainderItemClickListener(reminderList));
        if (refresh)
            plansRecyclerView.swapAdapter(plansAdapter, false);
        else
            plansRecyclerView.setAdapter(plansAdapter);
        plansRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new PlanPresenter(this, DataSource.getInstance());
        plansRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_plans, container, false);
        return plansRecyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setItemTouchHelper();
        presenter.loadPlansData();
    }

    private void setItemTouchHelper() {
        ItemTouchHelper.SimpleCallback callback =  new SimpleCallbackForTouches(0, ItemTouchHelper.LEFT, new SimpleCallbackForTouches.OnSwipedListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder instanceof PlansAdapter.PlansHolder) {
                    presenter.removeRemainder(((PlansAdapter.PlansHolder) viewHolder).getRemainderId());
                }
                presenter.refreshPlansData();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(plansRecyclerView);
    }

    @Override
    public String getFragmentName() {
        return getResources().getString(R.string.app_label_plans);
    }

    @Override
    public FragmentsFactory.TypeOfFragment getTypeOf() {
        return FragmentsFactory.TypeOfFragment.Plans;
    }

    @Override
    public boolean isButtonVisible() {
        return true;
    }

    private class MyOnRemainderItemClickListener implements PlansAdapter.OnRemainderItemClickListener {
        private final List<ReminderModel> reminderList;

        public MyOnRemainderItemClickListener(List<ReminderModel> reminderList) {
            this.reminderList = reminderList;
        }

        @Override
        public void OnClick(View view, int position) {
            ReminderModel reminder = reminderList.get(position);
            Fragment fragment = AddRemainderFragment.getInstance(reminder);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, fragment,"visible_tag");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
    }
}
