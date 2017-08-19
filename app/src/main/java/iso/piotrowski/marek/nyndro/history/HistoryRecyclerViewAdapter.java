package iso.piotrowski.marek.nyndro.history;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.R;

/**
 * Created by Marek on 02.08.2016.
 */
public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewStatsHolder> {

    private List<HistoryModel> historyList;

    public HistoryRecyclerViewAdapter(List<HistoryModel> historyList) {
        this.historyList = historyList;
    }

    public class ViewStatsHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.stats_image) ImageView statePracticeImageId;
        @BindView(R.id.stats_practice_name) TextView statsPracticeName;
        @BindView(R.id.stats_progress) TextView statsProgress;
        @BindView(R.id.stats_date) TextView statsDate;
        @BindView(R.id.stats_repetition) TextView statsRepetition;
        CardView cardView;

        ViewStatsHolder(CardView cardView) {
            super(cardView);
            this.cardView = cardView;
            ButterKnife.bind(this, this.cardView);
        }
    }

    @Override
    public ViewStatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView;
        cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_history_cardview, parent, false);
        return new ViewStatsHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewStatsHolder holder, int position) {
        HistoryModel history = historyList.get(position);
        holder.statsProgress.setText(String.valueOf(history.getProgress()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(history.getPracticeData());
        holder.statsDate.setText(String.format("Data : %tD", calendar));
        holder.statsRepetition.setText(String.valueOf(history.getRepetition()));
        holder.statePracticeImageId.setImageDrawable(holder.cardView.getResources().getDrawable(
                history.getPractice().getPracticeImageId()));
        holder.statsPracticeName.setText(history.getPractice().getName());
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }
}
