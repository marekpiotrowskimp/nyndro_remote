package iso.piotrowski.marek.nyndro.practice;


import android.app.backup.BackupManager;
import android.media.MediaPlayer;
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
    private boolean canceledDelete;
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

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder.getItemViewType() == PracticeAdapter.STANDARD_TYPE) {
                    deletePractice(viewHolder.getAdapterPosition(), viewHolder.itemView);
                }

            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (viewHolder.getItemViewType() == PracticeAdapter.STANDARD_TYPE) {
                    if (isCurrentlyActive) {
                        final int margine = 10;
                        View itemView = viewHolder.itemView;
                        int x, y, width, height;
                        x = itemView.getLeft() + margine;
                        y = itemView.getTop() + margine;
                        width = itemView.getWidth() - margine * 2;
                        height = itemView.getHeight() - margine * 2;

                        Paint paint = new Paint();

                        paint.setARGB(220, 10, 10, 10);
                        paint.setTextSize(40);
                        String deleteText = getActivity().getString(R.string.delete_practice);
                        c.drawText(deleteText, x + width - paint.measureText(deleteText), y + height / 2 + 8, paint);
                    }

                    super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (viewHolder.getItemViewType() == PracticeAdapter.STANDARD_TYPE) {
                    if (isCurrentlyActive) {
                        final int margine = 10;
                        View itemView = viewHolder.itemView;
                        int x, y, width, height;
                        x = itemView.getLeft() + margine;
                        y = itemView.getTop() + margine;
                        width = itemView.getWidth() - margine * 2;
                        height = itemView.getHeight() - margine * 2;

                        Paint paint = new Paint();

                        paint.setARGB(255, 255, 50, 50);
                        c.drawRect(new Rect(x, y, x + width, y + height), paint);
                        paint.setARGB(220, 250, 250, 255);
                        paint.setTextSize(40);
                        String deleteText = getActivity().getString(R.string.delete_practice);
                        c.drawText(deleteText, x + width - paint.measureText(deleteText), y + height / 2 + 8, paint);
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(practiceMainRecycleView);
    }

    public void refreshPracticeRecyclerView() {
        presenter.loadPracticeData();
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

    void deletePractice(final int position, View view) {
//        SQLHelper.deletePractice(db, cursorPractice, position);
//        cursorPractice.moveToPosition(position);
//        SQLHelper.updateHistoryInactive(db, cursorPractice.getInt(0), 0);
//        SQLHelper.updateRemainderActive(db, cursorPractice.getInt(0));
//        canceledDelete = false;
//        Snackbar.make(view, getActivity().getResources().getString(R.string.deleted_practice_info) + " " + cursorPractice.getString(PracticeAdapter.NAME_ID), 10000)
//                .setAction(R.string.cancel_action, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        canceledDelete = true;
//                        SQLHelper.recoverPracticeHistoryRemainder(db);
//                        refreshPracticeRecyclerView();
//                    }
//                }).show();
//
//        Handler deleteHandler = new Handler();
//        deleteHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!canceledDelete) {
//                    SQLHelper.deleteAllInactive(db);
//                    requestBackup();
//                }
//            }
//        }, 11000);

        refreshPracticeRecyclerView();
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
