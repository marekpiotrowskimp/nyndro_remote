package iso.piotrowski.marek.nyndro.practice;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
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
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.practice.Details.PracticeDetailFragment;
import iso.piotrowski.marek.nyndro.FragmentsFactory.FragmentsFactory;
import iso.piotrowski.marek.nyndro.Navigator.Navigator;
import iso.piotrowski.marek.nyndro.FragmentsFactory.NyndroFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PracticeMainFragment extends NyndroFragment implements PracticeContract.IViewer,
        PracticeAdapter.INextAndLastDateOfPractice, PracticeAdapter.ICardViewListener {

    private PracticeAdapter practiceAdapter;
    @BindView(R.id.practice_main_recycleView) RecyclerView practiceMainRecycleView;

    public PracticeMainFragment() {
    }

    public static PracticeMainFragment getInstance(){
        return new PracticeMainFragment();
    }

    private PracticeContract.IPresenter getPresenter(){
        return (PracticeContract.IPresenter) presenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new PracticePresenter(this, DataSource.getInstance());
        LinearLayout practiceMainLayout = (LinearLayout) inflater.inflate(R.layout.fragment_practice_main, container, false);
        ButterKnife.bind(this, practiceMainLayout);
        return practiceMainLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        getPresenter().loadPracticeData();
    }

    @Override
    public void setUpRecyclerView(List<PracticeModel> practices) {
        setUpPracticeAdapter(practices);
        practiceMainRecycleView.setAdapter(practiceAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayout.VERTICAL);
        practiceMainRecycleView.setLayoutManager(layoutManager);
        setUpItemTouchHelper();
    }

    private void setUpPracticeAdapter(List<PracticeModel> practices) {
        practiceAdapter = new PracticeAdapter(practices);
        practiceAdapter.setImageButtonListener(new ImageButtonListener(getPresenter()));
        practiceAdapter.setCardViewListener(this);
        practiceAdapter.setNextAndLastDateOfPractice(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new SimpleCallbackForTouches(0, ItemTouchHelper.LEFT, new SimpleCallbackForTouches.OnSwipedListener() {
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder.getItemViewType() == PracticeAdapter.TypeOfCardView.Standard.getValue()) {
                    if (viewHolder instanceof PracticeAdapter.ViewPracticeHolder) {
                        getPresenter().deletePractice(((PracticeAdapter.ViewPracticeHolder) viewHolder).getPractice(), viewHolder.itemView);
                    }
                }
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(practiceMainRecycleView);
    }

    @Override
    public void refreshPracticeRecyclerView(List<PracticeModel> practices) {
        setUpPracticeAdapter(practices);
        practiceMainRecycleView.swapAdapter(practiceAdapter, false);
    }

    @Override
    public void setPresenter(PracticeContract.IPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public long getNextPractice(long practiceId) {
        ReminderModel reminder = getPresenter().getNextPlanedOfPractice(practiceId);
        return reminder != null ? reminder.getPracticeDate() : -1;
    }

    @Override
    public long getLastPractice(long practiceId) {
        HistoryModel history = getPresenter().getLastHistoryOfPractice(practiceId);
        return history !=null ? history.getPracticeData() : -1;
    }

    @Override
    public void onClickToShowPracticeDetails(View view, PracticeModel practice) {
        Navigator.getInstance().changeFragmentInContainer(PracticeDetailFragment.getInstance(practice), true);
    }

    @Override
    public String getFragmentName() {
        return NyndroApp.getContext().getResources().getString(R.string.app_name);
    }

    @Override
    public FragmentsFactory.TypeOfFragment getTypeOf() {
        return FragmentsFactory.TypeOfFragment.Main;
    }

    @Override
    public boolean isButtonVisible() {
        return true;
    }

    @Override
    public void showMessage(String message) {
        super.showMessage(message);
        View view = getView();
        if (view != null) Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }
}
