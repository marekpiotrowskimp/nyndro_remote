package iso.piotrowski.marek.nyndro.practice;


import android.app.backup.BackupManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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

import java.util.Date;

import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class PracticeMainFragment extends Fragment {

    private SQLiteDatabase db;
    private Cursor cursorPractice;
    private PracticeAdapter practiceAdapter;
    private RecyclerView practiceMainRecycleView;
    private boolean canceledDelete;

    private class ImageButtonListener implements PracticeAdapter.ImageCardViewListener
    {
        @Override
        public void onClick(View view, int position, int multiple) {
            if (cursorPractice.moveToPosition(position)) {
                int progressAdd = cursorPractice.getInt(PracticeAdapter.PROGRESS_ID)+cursorPractice.getInt(PracticeAdapter.REPETITION_ID)*multiple;
                SQLHelper.updatePractice (db, cursorPractice.getInt(0), progressAdd);
                SQLHelper.insertHistory(db,cursorPractice.getInt(0),progressAdd,new Date().getTime(), cursorPractice.getInt(PracticeAdapter.REPETITION_ID));
                requestBackup();
                startEffect(view);
            }
        }

        public void startSoundEffect(){
            MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.tweet);
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (mediaPlayer != null) {
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
            });
            mediaPlayer.start();
        }

        public void startEffect(final View view){
            startSoundEffect();
            Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.add_button_animation);
            view.clearAnimation();
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    refreshPracticeRecyclerView();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            ((View)view.getParent().getParent().getParent()).startAnimation(AnimationUtils.loadAnimation(getContext(),R.anim.color_blinking_cardview));
            view.startAnimation(animation);

        }
    }
    public void requestBackup() {
        if (SQLHelper.isUpdateDatabase) {
            try {
                SQLHelper.isUpdateDatabase = false;
                BackupManager backupManager = new BackupManager(getActivity());
                backupManager.dataChanged();
            } catch (Exception e) {
            }
        }
    }

    private class PracticeCardViewListener implements PracticeAdapter.CardViewListener{
        @Override
        public void onClick(View view, int position) {
            PracticeDetailFragment practiceDetailFragment = new PracticeDetailFragment();
            practiceDetailFragment.setPosition(position);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment_container,practiceDetailFragment,"visible_tag");
            ft.addToBackStack(null);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
         }
    }

    public PracticeMainFragment() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        practiceMainRecycleView = (RecyclerView)inflater.inflate(R.layout.fragment_practice_main,container, false);
        return practiceMainRecycleView;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {

            PracticeDatabaseHelper practiceDatabaseHelper = new PracticeDatabaseHelper(getActivity());
            db = practiceDatabaseHelper.getReadableDatabase();
            cursorPractice = SQLHelper.getCursorPractice(db);

            practiceAdapter = new PracticeAdapter(cursorPractice,db);
            practiceAdapter.setImageButtonListener(new ImageButtonListener());
            practiceAdapter.setCardViewListener(new PracticeCardViewListener());
            practiceMainRecycleView.setAdapter(practiceAdapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

            layoutManager.setOrientation(LinearLayout.VERTICAL);
            practiceMainRecycleView.setLayoutManager(layoutManager);
            setUpItemTouchHelper();

        } catch (SQLException e)
        {

        }

    }

    @Override
    public void onDestroy() {
        if (cursorPractice!=null) cursorPractice.close();
        if (db!=null) db.close();
        super.onDestroy();
    }


    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (viewHolder.getItemViewType()==PracticeAdapter.STANDARD_TYPE) {
                    deletePractice(viewHolder.getAdapterPosition(), viewHolder.itemView);
                }

            }

            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (viewHolder.getItemViewType()==PracticeAdapter.STANDARD_TYPE) {
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
                if (viewHolder.getItemViewType()==PracticeAdapter.STANDARD_TYPE) {
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


    private void refreshPracticeRecyclerView ()
    {
        cursorPractice = SQLHelper.getCursorPractice(db);
        practiceAdapter = new PracticeAdapter(cursorPractice,db);
        practiceAdapter.setImageButtonListener(new ImageButtonListener());
        practiceAdapter.setCardViewListener(new PracticeCardViewListener());
        practiceMainRecycleView.swapAdapter(practiceAdapter,false);
    }

    void deletePractice (final int position, View view)
    {
        SQLHelper.deletePractice(db,cursorPractice,position);
        cursorPractice.moveToPosition(position);
        SQLHelper.updateHistoryInactive(db,cursorPractice.getInt(0),0);
        SQLHelper.updateRemainderActive(db,cursorPractice.getInt(0));
        canceledDelete = false;
        Snackbar.make(view,getActivity().getResources().getString(R.string.deleted_practice_info)+" "+cursorPractice.getString(PracticeAdapter.NAME_ID),10000)
                .setAction(R.string.cancel_action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        canceledDelete= true;
                        SQLHelper.recoverPracticeHistoryRemainder(db);
                        refreshPracticeRecyclerView();                    }
                }).show();

        Handler deleteHandler = new Handler();
        deleteHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!canceledDelete){
                    SQLHelper.deleteAllInactive(db);
                    requestBackup();
                }
            }
        }, 11000);

        refreshPracticeRecyclerView();
    }
}
