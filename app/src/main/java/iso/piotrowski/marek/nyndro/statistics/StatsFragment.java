package iso.piotrowski.marek.nyndro.statistics;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iso.piotrowski.marek.nyndro.practice.PracticeAdapter;
import iso.piotrowski.marek.nyndro.DataSource.PracticeDatabaseHelper;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;
import iso.piotrowski.marek.nyndro.history.HistoryAnalysis;
import iso.piotrowski.marek.nyndro.history.HistoryRecyclerViewAdapter;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatsFragment extends Fragment {
    private RecyclerView statsRecyclerView;
    private SQLiteDatabase db;
    private Cursor cursorStats;

    public StatsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        statsRecyclerView= (RecyclerView) inflater.inflate(R.layout.fragment_stats, container, false);
        doInBackround();
        return statsRecyclerView;
    }

    private void doInBackround ()
    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    PracticeDatabaseHelper practiceDatabaseHelper = new PracticeDatabaseHelper(getActivity());
                    db = practiceDatabaseHelper.getWritableDatabase();

                    Cursor cursorPractice = SQLHelper.getCursorPractice(db);
                    cursorPractice.moveToFirst();
                    int count = cursorPractice.getCount();
                    HistoryAnalysis[] historyAnalysises = new HistoryAnalysis[count];
                    for (int ind = 0; ind < count; ind++) {
                        cursorPractice.moveToPosition(ind);
                        cursorStats = SQLHelper.getHistoryCursor(db, cursorPractice.getInt(HistoryRecyclerViewAdapter.STATS_ID));
                        historyAnalysises[ind] = new HistoryAnalysis(cursorStats);
                        cursorStats.close();

                        if (historyAnalysises[ind].getAnalysisResult().isEmpty()) {
                            Map<String, HistoryAnalysis.Info> analysisEmpty = new HashMap<String, HistoryAnalysis.Info>();
                            analysisEmpty.put("practice_name", new HistoryAnalysis.Info(cursorPractice.getString(PracticeAdapter.NAME_ID)));
                            analysisEmpty.put("practice_image_id", new HistoryAnalysis.Info(cursorPractice.getInt(PracticeAdapter.PRACTICE_IMAGE_ID_ID)));
                            historyAnalysises[ind].setAnalysisResult(analysisEmpty);
                        }
                    }
                    cursorPractice.close();

                    StatsRecyclerViewAdaprer statsRecyclerViewAdaprer = new StatsRecyclerViewAdaprer();
                    statsRecyclerViewAdaprer.setHistoryAnalysis(historyAnalysises);
                    statsRecyclerViewAdaprer.setDays(getActivity().getResources().getStringArray(R.array.day_of_week));
                    statsRecyclerViewAdaprer.setMonths(getActivity().getResources().getStringArray(R.array.months));

                    statsRecyclerView.setAdapter(statsRecyclerViewAdaprer);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    statsRecyclerView.setLayoutManager(linearLayoutManager);
                }catch (SQLiteException e){}
            }
        },200);
    }

    @Override
    public void onDestroy() {
        if (cursorStats!=null) cursorStats.close();
        if (db!=null) db.close();
        super.onDestroy();
    }

}
