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

import iso.piotrowski.marek.nyndro.practice.PracticeDatabaseHelper;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private RecyclerView statsRecyclerView;
    private SQLiteDatabase db;
    private Cursor cursorStats;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_history, container, false);

        statsRecyclerView = (RecyclerView)linearLayout.findViewById(R.id.history_recyclerview);

        try {
            PracticeDatabaseHelper practiceDatabaseHelper = new PracticeDatabaseHelper(getActivity());
            db = practiceDatabaseHelper.getWritableDatabase();
            cursorStats = SQLHelper.getHistoryCursor(db);
            HistoryRecyclerViewAdapter historyRecyclerViewAdapter = new HistoryRecyclerViewAdapter(cursorStats);
            statsRecyclerView.setAdapter(historyRecyclerViewAdapter);
        }catch (SQLiteException e){}

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        statsRecyclerView.setLayoutManager(linearLayoutManager);
        return linearLayout;
    }

    @Override
    public void onDestroy() {
        if (cursorStats!=null) cursorStats.close();
        if (db!=null) db.close();
        super.onDestroy();
    }

}
