package iso.piotrowski.marek.nyndro.history;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.Model.DataSection;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.R;

/**
 * Created by Marek on 02.08.2016.
 */
public class HistoryRecyclerViewAdapter extends RecyclerView.Adapter<HistoryRecyclerViewAdapter.ViewStatsHolder> {

    private DataSection<HistoryModel> historyList;

    public HistoryRecyclerViewAdapter(DataSection<HistoryModel> historyList) {
        this.historyList = historyList;
    }

    public class ViewStatsHolder extends RecyclerView.ViewHolder {
        @Nullable @BindView(R.id.history_practice_image) ImageView historyPracticeImageId;
        @Nullable @BindView(R.id.history_progress) TextView historyProgress;
        @Nullable @BindView(R.id.history_date) TextView historyDate;
        @Nullable @BindView(R.id.history_repetition) TextView historyRepetition;
        @Nullable @BindView(R.id.history_practice_name_featured) TextView featuredPracticeName;
        @Nullable @BindView(R.id.history_expand_section_button) ImageButton expandSectionButton;
        ViewStatsHolder(CardView cardView) {
            super(cardView);
            ButterKnife.bind(this, cardView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return historyList.getType(position).getValue();
    }

    @Override
    public ViewStatsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView = null;
        switch (DataSection.TypeOfData.values()[viewType]) {
            case Section:
                cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_section_cardview, parent, false);
                break;
            case Data:
                cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_history_cardview, parent, false);
                break;
        }
        return new ViewStatsHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewStatsHolder holder, int position) {
        DataSection<HistoryModel>.SectionResult<HistoryModel> historyResult = historyList.get(position);
        switch (historyResult.getTypeOfData()){
            case Section:
                bindSectionCard(holder, historyResult.getSection());
                holder.expandSectionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isExpanded = historyResult.getSection().isExpanded();
                        historyResult.getSection().setExpanded(!isExpanded);
                        notifyDataSetChanged();
                    }
                });
                break;
            case Data:
                bindDataCard(holder, historyResult.getData());
                break;
        }
    }

    private void bindSectionCard(ViewStatsHolder holder, DataSection section) {
        holder.historyPracticeImageId.setImageDrawable(NyndroApp.getContext().getResources().getDrawable(
                section.getPracticeImageId()));
        holder.featuredPracticeName.setText(section.getName());
        holder.expandSectionButton.setImageResource(section.isExpanded() ? R.mipmap.ic_less : R.mipmap.ic_more);
    }

    private void bindDataCard(ViewStatsHolder holder, HistoryModel history) {
        holder.historyProgress.setText(String.valueOf(history.getProgress()));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(history.getPracticeData());
        holder.historyDate.setText(String.format("Data : %tD", calendar));
        holder.historyRepetition.setText(String.valueOf(history.getRepetition()));
    }

    @Override
    public int getItemCount() {
        return historyList.getSize();
    }
}
