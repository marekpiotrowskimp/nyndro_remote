package iso.piotrowski.marek.nyndro.statistics;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.Model.AnalysisInfo;
import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.R;

/**
 * Created by Marek on 05.08.2016.
 */
public class HistoryAnalysis {

    private Map<String, AnalysisInfo> analysisResult;

    public HistoryAnalysis(List<HistoryModel> historyList) {
        analysisResult = new HashMap<>();
        doAnalysis(historyList);
    }

    private void doAnalysis(List<HistoryModel> historyList) {
        if (historyList.size() > 0) {
            //Base information about practice
            analysisResult = getPracticeInformation(historyList, analysisResult);

            //Days of practice
            analysisResult = getDaysOfPractice(historyList, analysisResult);

            //Months of practice
            analysisResult = getMonthsOfPractice(historyList, analysisResult);

            //Averages in weeks and months
            analysisResult = getAverages(historyList, analysisResult);
        }
    }

    private Map<String, AnalysisInfo> getAverages(List<HistoryModel> historyList, Map<String, AnalysisInfo> analysisResult) {
        int week = -1, month = -1;
        int countWeeks = 0, countMonth = 0;
        int sum = 0;
        for (HistoryModel history : historyList) {
            long sec = history.getPracticeData();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(sec);
            int progress = history.getRepetition();
            if (progress < 10000) {
                int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
                if (week != weekOfYear) {
                    week = weekOfYear;
                    countWeeks++;
                }
                int monthOfYear = calendar.get(Calendar.MONTH);
                if (month != monthOfYear) {
                    month = monthOfYear;
                    countMonth++;
                }
                sum += progress;
            }
        }

        long firstDate = historyList.get(0).getPracticeData();
        long lastDate = historyList.get(historyList.size() - 1).getPracticeData();
        int days = (int) TimeUnit.MILLISECONDS.toDays(firstDate - lastDate);
        int max_repetition = historyList.get(0).getPractice().getMaxRepetition();
        int progress = historyList.get(0).getPractice().getProgress();

        analysisResult.put("average_week", new AnalysisInfo(countWeeks > 0 ? sum / countWeeks : 0));
        analysisResult.put("average_month", new AnalysisInfo(countMonth > 0 ? sum / countMonth : 0));
        int averageDays = days > 0 ? sum / days : 0;
        analysisResult.put("average_days", new AnalysisInfo(averageDays));
        int daysToEnd = (averageDays > 0) && (max_repetition > progress) ? (max_repetition - progress) / averageDays : 0;
        analysisResult.put("finish_practice", new AnalysisInfo(daysToEnd));

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.SECOND, daysToEnd * 3600 * 24);
        String calculatedEnd = String.format(Locale.UK, " %tD", calendar);
        String dateOfEndText = daysToEnd > 1026 ?
                NyndroApp.getContext().getString(R.string.infinity) : calculatedEnd;
        analysisResult.put("finish_practice_date", new AnalysisInfo(daysToEnd == 0 ? NyndroApp.getContext().getString(R.string.undefined) : dateOfEndText));
        return analysisResult;
    }

    private Map<String, AnalysisInfo> getPracticeInformation(List<HistoryModel> historyList, Map<String, AnalysisInfo> analysisResult) {
        HistoryModel historyFirst = historyList.get(0);
        analysisResult.put("practice_name", new AnalysisInfo(historyFirst.getPractice().getName()));
        analysisResult.put("practice_image_id", new AnalysisInfo(historyFirst.getPractice().getPracticeImageId()));
        analysisResult.put("practice_days", new AnalysisInfo(historyList.size()));
        return analysisResult;
    }

    private Map<String, AnalysisInfo> getMonthsOfPractice(List<HistoryModel> historyList, Map<String, AnalysisInfo> analysisResult) {
        int[] practiceMonths = new int[12];
        Calendar calendar = Calendar.getInstance();
        for (HistoryModel history : historyList) {
            calendar.setTimeInMillis(history.getPracticeData());
            practiceMonths[calendar.get(Calendar.MONTH)]++;
        }
        for (int ind = 0; ind < 12; ind++) {
            String key = "month_" + Integer.toString(ind);
            analysisResult.put(key, new AnalysisInfo(practiceMonths[ind]));
        }
        return analysisResult;
    }

    private Map<String, AnalysisInfo> getDaysOfPractice(List<HistoryModel> historyList, Map<String, AnalysisInfo> analysisResult) {
        int[] practiceDays = new int[7];
        for (HistoryModel history : historyList) {
            long sec = history.getPracticeData();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(sec);
            practiceDays[calendar.get(Calendar.DAY_OF_WEEK) - 1]++;
        }
        for (int ind = 0; ind < 7; ind++) {
            String key = "day_" + Integer.toString(ind);
            analysisResult.put(key, new AnalysisInfo(practiceDays[ind]));
        }
        return analysisResult;
    }

    public Map<String, AnalysisInfo> getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(Map<String, AnalysisInfo> analysisResult) {
        this.analysisResult = analysisResult;
    }

}
