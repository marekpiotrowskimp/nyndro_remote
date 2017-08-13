package iso.piotrowski.marek.nyndro.practice;


import android.app.backup.BackupManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.DataSource.DataSource;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.practice.Details.PracticeDetailFragment;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PracticeMainFragment extends Fragment implements PracticeContract.IViewer, PracticeAdapter.INextAndLastDateOfPractice, PracticeAdapter.ICardViewListener {

    private PracticeAdapter practiceAdapter;
    private RecyclerView practiceMainRecycleView;
    private PracticeContract.IPresenter presenter;

    public PracticeMainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        new PracticePresenter(this, DataSource.getInstance());
        practiceMainRecycleView = (RecyclerView) inflater.inflate(R.layout.fragment_practice_main, container, false);
        ButterKnife.bind(this, practiceMainRecycleView);
        return practiceMainRecycleView;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.loadPracticeData();
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
        practiceAdapter.setImageButtonListener(new ImageButtonListener(presenter));
        practiceAdapter.setCardViewListener(this);
        practiceAdapter.setNextAndLastDateOfPractice(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleCallback = new SimpleCallbackForTouches(0, ItemTouchHelper.LEFT, presenter);
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
        ReminderModel reminder = presenter.getNextPlanedOfPractice(practiceId);
        return reminder != null ? reminder.getPracticeDate() : -1;
    }

    @Override
    public long getLastPractice(long practiceId) {
        HistoryModel history = presenter.getLastHistoryOfPractice(practiceId);
        return history !=null ? history.getPracticeData() : -1;
    }

    @Override
    public void onClickToShowPracticeDetails(View view, int position) {
        PracticeDetailFragment practiceDetailFragment = new PracticeDetailFragment();
        practiceDetailFragment.setPosition(position);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment_container, practiceDetailFragment, "visible_tag");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
