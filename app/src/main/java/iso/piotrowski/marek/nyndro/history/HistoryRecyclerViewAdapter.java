package iso.piotrowski.marek.nyndro.history;

import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import iso.piotrowski.marek.nyndro.R;

/**
 * Created by Marek on 02.08.2016.
 */
public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewStatsHolder> {
   // "_id","PRACTICE_ID","PROGRESS","PRACTICE_DATE","REPETITION"

    public static final int STATS_ID=0;
    public static final int STATS_PRACTICE_ID=1;
    public static final int STATS_PROGRESS=2;
    public static final int STATS_DATE=3;
    public static final int STATS_REPETITON=4;
    public static final int STATS_PRACTICE_NAME=5;
    public static final int STATS_PRACTICE_IMAGE_ID=6;
    public static final int STATS_PRACTICE_MAX_REPETITION=7;
    private Cursor cursorStats;

    public HistoryRecyclerViewAdapter(Cursor cursorStats)
    {
        this.cursorStats=cursorStats;
    }

    public class ViewStatsHolder extends RecyclerView.ViewHolder{
        CardView cv;
        public ViewStatsHolder(CardView cv){
            super(cv);
            this.cv=cv;
        }
    }

    @Override
    public ViewStatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv;
        cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_history_cardview,parent,false);
        return new ViewStatsHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewStatsHolder holder, int position) {

        ImageView statePracticeImageId = (ImageView)holder.cv.findViewById(R.id.stats_image);
        TextView statsPracticeName = (TextView) holder.cv.findViewById(R.id.stats_practice_name);
        TextView statsProgress = (TextView) holder.cv.findViewById(R.id.stats_progress);
        TextView statsDate = (TextView) holder.cv.findViewById(R.id.stats_date);
        TextView statsRepetition = (TextView) holder.cv.findViewById(R.id.stats_repetition);

        cursorStats.moveToPosition(position);
        statsProgress.setText(Integer.toString(cursorStats.getInt(STATS_PROGRESS)));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cursorStats.getLong(STATS_DATE));
        statsDate.setText(String.format("Data : %tD",calendar));
        statsRepetition.setText(Integer.toString(cursorStats.getInt(STATS_REPETITON)));

        statePracticeImageId.setImageDrawable(holder.cv.getResources().getDrawable(cursorStats.getInt(STATS_PRACTICE_IMAGE_ID)));
        statsPracticeName.setText(cursorStats.getString(STATS_PRACTICE_NAME));

    }

    @Override
    public int getItemCount() {
        return cursorStats.getCount();
    }
}
