package iso.piotrowski.marek.nyndro.plans;


import android.support.v4.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.DataSource.PracticeDatabaseHelper;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlansFragment extends Fragment {

    private RecyclerView plansRecyclerView;
    private SQLiteDatabase db;
    private Cursor cursorPlans;

    public PlansFragment() {
    }

    private class OnRemainderItemClickListener implements PlansAdapter.OnRemainderItemClickListener{
        @Override
        public void OnClick(View view, int position) {
            Fragment fragment = new AddRemainderFragment();
            Bundle bundle = new Bundle();
            cursorPlans.moveToPosition(position);
            bundle.putInt("plan_id",cursorPlans.getInt(PlansAdapter.REMAINDER_ID));
            bundle.putInt("practice_id",cursorPlans.getInt(PlansAdapter.REMAINDER_PRACTICE_ID));
            bundle.putLong("date",cursorPlans.getLong(PlansAdapter.REMAINDER_DATE));
            bundle.putInt("repeat",cursorPlans.getInt(PlansAdapter.REMAINDER_REPEATER));
            fragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, fragment,"visible_tag");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            PracticeDatabaseHelper databaseHelper = new PracticeDatabaseHelper(getActivity());
            db = databaseHelper.getWritableDatabase();
            plansRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_plans, container, false);
            PlansAdapter plansAdapter = new PlansAdapter();
            cursorPlans = SQLHelper.getRemainderCursor(db, 1);
            plansAdapter.setCursorPlans(cursorPlans);
            plansAdapter.setOnRemainderItemClickListener(new OnRemainderItemClickListener());
            plansRecyclerView.setAdapter(plansAdapter);
        } catch (SQLiteException e) {
        }

        plansRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return plansRecyclerView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setItemTouchHelper();
    }

    @Override
    public void onDestroy() {
        if (db != null) db.close();
        if (cursorPlans != null) cursorPlans.close();
        super.onDestroy();
    }

    private void setItemTouchHelper() {
        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                SQLHelper.deletePlan(db, cursorPlans, viewHolder.getAdapterPosition());
                PlansAdapter plansAdapter = new PlansAdapter();
                cursorPlans = SQLHelper.getRemainderCursor(db, 1);
                plansAdapter.setCursorPlans(cursorPlans);
                plansAdapter.setOnRemainderItemClickListener(new OnRemainderItemClickListener());
                plansRecyclerView.swapAdapter(plansAdapter, false);
            }


            @Override
            public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
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

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
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

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(plansRecyclerView);
    }


    private void setAnimationFab() {
        FloatingActionButton actionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab_add_new_practice);


    }
}
