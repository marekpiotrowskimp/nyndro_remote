package iso.piotrowski.marek.nyndro.statistics;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.UIComponents.BarChart;
import iso.piotrowski.marek.nyndro.history.HistoryAnalysis;

/**
 * Created by Marek on 05.08.2016.
 */
public class StatsRecyclerViewAdaprer extends RecyclerView.Adapter<StatsRecyclerViewAdaprer.StatsCardViewHolder>{

    public static int TYPE_EMPTY=1;
    public static int TYPE_FULL=2;
    private HistoryAnalysis[] historyAnalysis;
    private String [] days;
    private String [] months;
    private int colors[]={0xa52a2a, 0x7fff00, 0x6495ed, 0xdc143c, 0xff1493, 0x228b22, 0xffd700, 0x008000, 0xff4500, 0xFFA07A, 0x00FFFF, 0x90EE90};

    @Override
    public StatsCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv;
        if (viewType == TYPE_EMPTY)
            cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_cardview_layout_empty,parent,false);
        else
            cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_cardview_layout,parent,false);
        return new StatsCardViewHolder(cv,viewType);
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

    public class StatsCardViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        int viewType;
        StatsCardViewHolder (CardView cv, int viewType){
            super(cv);
            this.cv=cv;
            this.viewType=viewType;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (historyAnalysis[position].getAnalysisResult().size()<3)
            return TYPE_EMPTY;
        else
            return TYPE_FULL;
    }

    @Override
    public void onBindViewHolder(StatsCardViewHolder holder, int position) {

        if (holder.viewType==TYPE_FULL) {
            ImageView practiceImageId = (ImageView) holder.cv.findViewById(R.id.statystics_practice_image_id);
            TextView practiceName = (TextView) holder.cv.findViewById(R.id.statystics_practice_name);
            TextView practiceDays = (TextView) holder.cv.findViewById(R.id.statystics_practice_day);
            TextView practiceAverageWeeks = (TextView) holder.cv.findViewById(R.id.statystics_practice_average_week);
            TextView practiceAverageMonths = (TextView) holder.cv.findViewById(R.id.statystics_practice_average_month);
            TextView practiceAverageDays = (TextView) holder.cv.findViewById(R.id.statystics_practice_average_days);
            TextView practiceExpectedDay = (TextView) holder.cv.findViewById(R.id.statystics_practice_expected_end);
            BarChart practiceBarChart = (BarChart) holder.cv.findViewById(R.id.statystics_bar_chart);
            BarChart practiceBarChartMonth = (BarChart) holder.cv.findViewById(R.id.statystics_bar_chart_month);

            Map<String, HistoryAnalysis.Info> historyAnalysisResult = historyAnalysis[position].getAnalysisResult();

            if (!historyAnalysisResult.isEmpty()) {
                practiceImageId.setImageDrawable(holder.cv.getResources().getDrawable(historyAnalysisResult.get("practice_image_id").getNumber()));
                practiceName.setText(" "+historyAnalysisResult.get("practice_name").toString());
                practiceDays.setText(" "+historyAnalysisResult.get("practice_days").toString());
                practiceAverageWeeks.setText(" "+historyAnalysisResult.get("average_week").toString());
                practiceAverageMonths.setText(" "+historyAnalysisResult.get("average_month").toString());
                practiceAverageDays.setText(" "+historyAnalysisResult.get("average_days").toString());
                practiceExpectedDay.setText(" "+historyAnalysisResult.get("finish_practice").toString()+" dni\n"+
                        historyAnalysisResult.get("finish_practice_date").toString());

                List<BarChart.DataObj> list = new ArrayList<BarChart.DataObj>();
                for (int ind = 0; ind < 7; ind++) {
                    String key = "day_" + Integer.toString(ind);
                    int number = historyAnalysisResult.get(key).getNumber();
                    list.add(new BarChart.DataObj(days[ind] + " > " + Integer.toString(number), number, ind<colors.length? colors[ind]:0xAAAAAA));
                }
                practiceBarChart.setDataChart(list);

                List<BarChart.DataObj> listMonth = new ArrayList<BarChart.DataObj>();
                for (int ind = 0; ind < 12; ind++) {
                    String key = "month_" + Integer.toString(ind);
                    int number = historyAnalysisResult.get(key).getNumber();
                    listMonth.add(new BarChart.DataObj(months[ind] + " > " + Integer.toString(number), number, ind<colors.length? colors[ind]:0xAAAAAA));
                }
                practiceBarChartMonth.setDataChart(listMonth);

            }
        } else
        {
            ImageView practiceImageId = (ImageView) holder.cv.findViewById(R.id.statystics_practice_image_id);
            TextView practiceName = (TextView) holder.cv.findViewById(R.id.statystics_practice_name);
            Map<String, HistoryAnalysis.Info> historyAnalysisResult = historyAnalysis[position].getAnalysisResult();
            practiceImageId.setImageDrawable(holder.cv.getResources().getDrawable(historyAnalysisResult.get("practice_image_id").getNumber()));
            practiceName.setText(" "+historyAnalysisResult.get("practice_name").toString());
        }
    }

    @Override
    public int getItemCount() {
        return historyAnalysis.length;
    }
}
