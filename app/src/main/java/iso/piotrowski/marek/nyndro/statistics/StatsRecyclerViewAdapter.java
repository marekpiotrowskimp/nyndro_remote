package iso.piotrowski.marek.nyndro.statistics;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.Model.AnalysisInfo;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.UITool;

/**
 * Created by Marek on 05.08.2016.
 */
public class StatsRecyclerViewAdapter extends RecyclerView.Adapter<StatsRecyclerViewAdapter.StatsCardViewHolder> implements OnChartValueSelectedListener {

    private HistoryAnalysis[] historyAnalysis;
    private String[] days;
    private String[] months;
    private Typeface mTfLight = Typeface.createFromAsset(NyndroApp.getContext().getAssets(), "OpenSans-Light.ttf");
    private Typeface mTfExtraBoldItalic = Typeface.createFromAsset(NyndroApp.getContext().getAssets(), "OpenSans-ExtraBoldItalic.ttf");

    private enum TypeOfStats {
        Full(0), Empty(1);
        private int value;

        TypeOfStats(int value) {
            this.value = value;
        }

        public int getValue() {
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
        @BindView(R.id.pie_chart_days_stats) @Nullable PieChart pieChartDays;
        @BindView(R.id.radar_chart_months_stats) @Nullable RadarChart radarChartMonths;
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
                holder.practiceImageId.setImageBitmap(UITool.makeRoundCorners(historyAnalysisResult.get(HistoryAnalysis.PRACTICE_IMAGE_ID).getNumber(),16));
                holder.practiceName.setText(String.format(Locale.UK, "%s", historyAnalysisResult.get(HistoryAnalysis.PRACTICE_NAME).toString()));
                if (holder.practiceDays != null)
                    holder.practiceDays.setText(String.format(Locale.UK, " %s", historyAnalysisResult.get(HistoryAnalysis.PRACTICE_DAYS).toString()));
                if (holder.practiceAverageWeeks != null)
                    holder.practiceAverageWeeks.setText(String.format(Locale.UK, " %s", historyAnalysisResult.get(HistoryAnalysis.AVERAGE_WEEK).toString()));
                if (holder.practiceAverageMonths != null)
                    holder.practiceAverageMonths.setText(String.format(Locale.UK, " %s", historyAnalysisResult.get(HistoryAnalysis.AVERAGE_MONTH).toString()));
                if (holder.practiceAverageDays != null)
                    holder.practiceAverageDays.setText(String.format(Locale.UK, " %s", historyAnalysisResult.get(HistoryAnalysis.AVERAGE_DAYS).toString()));
                if (holder.practiceExpectedDay != null)
                    holder.practiceExpectedDay.setText(String.format(Locale.UK, " %s %s\n%s", historyAnalysisResult.get(HistoryAnalysis.FINISH_PRACTICE).toString(),
                            NyndroApp.getContext().getResources().getString(R.string.days_name),
                            historyAnalysisResult.get(HistoryAnalysis.FINISH_PRACTICE_DATE).toString()));

                setUpPieChart(holder.pieChartDays, historyAnalysisResult);
                setUpRadarChart(holder.radarChartMonths, historyAnalysisResult);

            }
        } else {
            Map<String, AnalysisInfo> historyAnalysisResult = historyAnalysis[position].getAnalysisResult();
            if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                holder.practiceImageId.setImageDrawable(holder.cardView.getResources().getDrawable(historyAnalysisResult.get(HistoryAnalysis.PRACTICE_IMAGE_ID).getNumber()));
            } else {
                holder.practiceImageId.setImageDrawable(holder.cardView.getResources().getDrawable(historyAnalysisResult.get(HistoryAnalysis.PRACTICE_IMAGE_ID).getNumber(), null));
            }
            holder.practiceName.setText(String.format(Locale.UK, " %s", historyAnalysisResult.get(HistoryAnalysis.PRACTICE_NAME).toString()));
        }
    }

    private void setUpRadarChart (RadarChart radarChart, Map<String, AnalysisInfo> historyAnalysisResult) {
        radarChart.setBackgroundColor(Color.TRANSPARENT);
        radarChart.setExtraOffsets(-50, 2, 2, 2);

        radarChart.getDescription().setEnabled(false);

        radarChart.setWebLineWidth(1f);
        radarChart.setWebColor(Color.BLACK);
        radarChart.setWebLineWidthInner(1f);
        radarChart.setWebColorInner(Color.BLACK);
        radarChart.setWebAlpha(100);

        setData(radarChart, historyAnalysisResult);

        radarChart.animateXY(
                2400, 2400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = radarChart.getXAxis();
        xAxis.setTypeface(mTfExtraBoldItalic);
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            private String[] mActivities = months;
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.GRAY);

        YAxis yAxis = radarChart.getYAxis();
        yAxis.setTypeface(mTfExtraBoldItalic);
        yAxis.setLabelCount(12, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        Legend l = radarChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setTypeface(mTfLight);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
        l.setTextColor(Color.WHITE);
        l.setEnabled(false);
    }

    public void setData(RadarChart radarChart, Map<String, AnalysisInfo> historyAnalysisResult) {

        ArrayList<RadarEntry> entries = getRadarEntries(historyAnalysisResult);

        RadarDataSet set2 = new RadarDataSet(entries, "");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(false);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set2);

        RadarData data = new RadarData(sets);
        data.setValueTypeface(mTfExtraBoldItalic);
        data.setValueTextSize(8f);
        data.setDrawValues(true);
        data.setValueTextColor(Color.BLACK);

        radarChart.setData(data);
        radarChart.invalidate();
    }

    @NonNull
    private ArrayList<RadarEntry> getRadarEntries(Map<String, AnalysisInfo> historyAnalysisResult) {
        int max = 1;
        for (int i = 0; i < 12; i++) {
            String key = HistoryAnalysis.PREFIX_MONTH + Integer.toString(i);
            int number = historyAnalysisResult.get(key).getNumber();
            max = number > max ? number : max;
        }

        ArrayList<RadarEntry> entries = new ArrayList<RadarEntry>();
        for (int i = 0; i < 12; i++) {
            String key = HistoryAnalysis.PREFIX_MONTH + Integer.toString(i);
            int number = historyAnalysisResult.get(key).getNumber();
            entries.add(new RadarEntry(((float)number / (float)max) * (float)100));
        }
        return entries;
    }

    private void setUpPieChart (PieChart pieChart, Map<String, AnalysisInfo> historyAnalysisResult) {
        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(-50, 2, 2, 2);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setCenterTextTypeface(mTfExtraBoldItalic);
        pieChart.setCenterText(NyndroApp.getContext().getResources().getString(R.string.number_of_practices_in_days));

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.DKGRAY);

        pieChart.setTransparentCircleColor(Color.GRAY);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(36f);
        pieChart.setTransparentCircleRadius(41f);

        pieChart.setDrawCenterText(true);
        pieChart.setCenterTextColor(Color.WHITE);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        // pieChart.setUnit(" €");
        // pieChart.setDrawUnitsInChart(true);

        // add a selection listener
        pieChart.setOnChartValueSelectedListener(this);

        setData(pieChart, historyAnalysisResult);

        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(17f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setForm(Legend.LegendForm.CIRCLE);

        // entry label styling
        pieChart.setEntryLabelColor(Color.DKGRAY);
//        pieChart.setEntryLabelTypeface(mTfRegular);
        pieChart.setEntryLabelTextSize(12f);
    }

    private void setData(PieChart pieChart, Map<String, AnalysisInfo> historyAnalysisResult) {

        ArrayList<PieEntry> entries = getDaysPieEntries(historyAnalysisResult);

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
//        dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new LargeValueFormatter());
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(mTfExtraBoldItalic);
        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);
        pieChart.setEntryLabelTypeface(mTfExtraBoldItalic);
        pieChart.invalidate();
    }

    @NonNull
    private ArrayList<PieEntry> getDaysPieEntries(Map<String, AnalysisInfo> historyAnalysisResult) {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < 7; i++) {
            String key = HistoryAnalysis.PREFIX_DAY + Integer.toString(i);
            int number = historyAnalysisResult.get(key).getNumber();
            entries.add(new PieEntry((float) (number),
                    days[i]));
        }
        return entries;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public int getItemCount() {
        return historyAnalysis.length;
    }
}
