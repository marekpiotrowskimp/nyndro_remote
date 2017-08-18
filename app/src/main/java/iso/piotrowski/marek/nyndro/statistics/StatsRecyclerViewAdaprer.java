package iso.piotrowski.marek.nyndro.statistics;

import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.Model.AnalysisInfo;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.UIComponents.BarChart;

/**
 * Created by Marek on 05.08.2016.
 */
public class StatsRecyclerViewAdaprer extends RecyclerView.Adapter<StatsRecyclerViewAdaprer.StatsCardViewHolder> {

    private HistoryAnalysis[] historyAnalysis;
    private String[] days;
    private String[] months;
    private int colors[] = {0xa52a2a, 0x7fff00, 0x6495ed, 0xdc143c, 0xff1493, 0x228b22, 0xffd700, 0x008000, 0xff4500, 0xFFA07A, 0x00FFFF, 0x90EE90};
    private enum TypeOfStats {
        Full(0), Empty(1);
        private int value;
        TypeOfStats (int value){
            this.value = value;
        }
        public int getValue(){
            return this.value;
        }
    }

    @Override
    public StatsCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cardView;
        if (TypeOfStats.values()[viewType] == TypeOfStats.Empty)
            cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_cardview_layout_empty, parent, false);
        else
            cardView = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_cardview_layout, parent, false);
        return new StatsCardViewHolder(cardView, viewType);
    }

    public void setHistoryAnalysis(HistoryAnalysis[] historyAnalysis) {
        this.historyAnalysis = historyAnalysis;
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    public String[] getMonths() {
        return months;
    }

    public void setMonths(String[] months) {
        this.months = months;
    }

    public class StatsCardViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.statystics_practice_image_id) ImageView practiceImageId;
        @BindView(R.id.statystics_practice_name) TextView practiceName;
        @BindView(R.id.statystics_practice_day) @Nullable TextView practiceDays;
        @BindView(R.id.statystics_practice_average_week) @Nullable TextView practiceAverageWeeks;
        @BindView(R.id.statystics_practice_average_month) @Nullable TextView practiceAverageMonths;
        @BindView(R.id.statystics_practice_average_days) @Nullable TextView practiceAverageDays;
        @BindView(R.id.statystics_practice_expected_end) @Nullable TextView practiceExpectedDay;
        @BindView(R.id.statystics_bar_chart) @Nullable BarChart practiceBarChart;
        @BindView(R.id.statystics_bar_chart_month) @Nullable BarChart practiceBarChartMonth;
        CardView cardView;
        int viewType;

        StatsCardViewHolder(CardView cardView, int viewType) {
            super(cardView);
            this.cardView = cardView;
            this.viewType = viewType;
            ButterKnife.bind(this, this.cardView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (historyAnalysis[position].getAnalysisResult().size() < 3)
            return TypeOfStats.Empty.getValue();
        else
            return TypeOfStats.Full.getValue();
    }

    @Override
    public void onBindViewHolder(StatsCardViewHolder holder, int position) {

        if (TypeOfStats.values()[holder.viewType] == TypeOfStats.Full) {

            Map<String, AnalysisInfo> historyAnalysisResult = historyAnalysis[position].getAnalysisResult();

            if (!historyAnalysisResult.isEmpty()) {
                holder.practiceImageId.setImageDrawable(holder.cardView.getResources().getDrawable(historyAnalysisResult.get("practice_image_id").getNumber()));
                holder.practiceName.setText(String.format(" %s", historyAnalysisResult.get("practice_name").toString()));
                if (holder.practiceDays != null) holder.practiceDays.setText(String.format(" %s", historyAnalysisResult.get("practice_days").toString()));
                if (holder.practiceAverageWeeks != null) holder.practiceAverageWeeks.setText(String.format(" %s", historyAnalysisResult.get("average_week").toString()));
                if (holder.practiceAverageMonths != null) holder.practiceAverageMonths.setText(String.format(" %s", historyAnalysisResult.get("average_month").toString()));
                if (holder.practiceAverageDays != null) holder.practiceAverageDays.setText(String.format(" %s", historyAnalysisResult.get("average_days").toString()));
                if (holder.practiceExpectedDay != null) holder.practiceExpectedDay.setText(String.format(" %s dni\n%s", historyAnalysisResult.get("finish_practice").toString(),
                        historyAnalysisResult.get("finish_practice_date").toString()));

                List<BarChart.DataObj> list = new ArrayList<BarChart.DataObj>();
                for (int ind = 0; ind < 7; ind++) {
                    String key = "day_" + Integer.toString(ind);
                    int number = historyAnalysisResult.get(key).getNumber();
                    list.add(new BarChart.DataObj(days[ind] + " > " + Integer.toString(number), number, ind < colors.length ? colors[ind] : 0xAAAAAA));
                }
                if (holder.practiceBarChart != null) holder.practiceBarChart.setDataChart(list);

                List<BarChart.DataObj> listMonth = new ArrayList<BarChart.DataObj>();
                for (int ind = 0; ind < 12; ind++) {
                    String key = "month_" + Integer.toString(ind);
                    int number = historyAnalysisResult.get(key).getNumber();
                    listMonth.add(new BarChart.DataObj(months[ind] + " > " + Integer.toString(number), number, ind < colors.length ? colors[ind] : 0xAAAAAA));
                }
                if (holder.practiceBarChartMonth != null) holder.practiceBarChartMonth.setDataChart(listMonth);

            }
        } else {
            Map<String, AnalysisInfo> historyAnalysisResult = historyAnalysis[position].getAnalysisResult();
            holder.practiceImageId.setImageDrawable(holder.cardView.getResources().getDrawable(historyAnalysisResult.get("practice_image_id").getNumber()));
            holder.practiceName.setText(String.format(" %s", historyAnalysisResult.get("practice_name").toString()));
        }
    }

    @Override
    public int getItemCount() {
        return historyAnalysis.length;
    }
}
