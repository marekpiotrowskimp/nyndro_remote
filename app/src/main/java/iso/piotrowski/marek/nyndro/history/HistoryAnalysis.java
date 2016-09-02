package iso.piotrowski.marek.nyndro.history;

import android.database.Cursor;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Marek on 05.08.2016.
 */
public class HistoryAnalysis {

    public static final int STRING_TYPE =1;
    public static final int NUMBER_TYPE =2;
    public static final int SNBOTH_TYPE =3;
    private Map<String,Info> analysisResult;

    public static class Info{
        private String text;
        private int number;
        private int type;

        public Info (String text)
        {
            this.text = text;
            type=STRING_TYPE;
        }

        public Info (int number)
        {
            this.number=number;
            type=NUMBER_TYPE;
        }

        public Info (String text, int number)
        {
            this.text = text;
            this.number=number;
            type=SNBOTH_TYPE;
        }

        @Override
        public String toString() {
            String result;
            switch (type)
            {
                case STRING_TYPE:
                    result = text;
                    break;
                case NUMBER_TYPE:
                    result = Integer.toString(getNumber());
                    break;
                default:
                    result = text+": "+Integer.toString(getNumber());
                    break;
            }
            return result;
        }

        public int getNumber() {
            return number;
        }
    }

    public HistoryAnalysis (Cursor cursor) {
        setAnalysisResult(new HashMap<String, Info>());
        doAnalysis(cursor);
    }

    private void doAnalysis(Cursor cursor) {
        cursor.moveToFirst();
        if (cursor.getCount()>0) {
            analysisResult.put("practice_name", new Info(cursor.getString(HistoryRecyclerViewAdapter.STATS_PRACTICE_NAME)));
            analysisResult.put("practice_image_id", new Info(cursor.getInt(HistoryRecyclerViewAdapter.STATS_PRACTICE_IMAGE_ID)));
            analysisResult.put("practice_days", new Info(cursor.getCount()));

            //Days of practice
            int[] practiceDays = new int[7];
            for (int ind = 0; ind < cursor.getCount(); ind++) {
                long sec = cursor.getLong(HistoryRecyclerViewAdapter.STATS_DATE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(sec);
                practiceDays[calendar.get(Calendar.DAY_OF_WEEK)-1]++;
                cursor.moveToNext();
            }
            for (int ind = 0; ind < 7; ind++) {
                String key = "day_" + Integer.toString(ind);
                analysisResult.put(key, new Info(practiceDays[ind]));
            }

            //Months of practice
            cursor.moveToFirst();
            int[] practiceMonths = new int[12];
            for (int ind = 0; ind < cursor.getCount(); ind++) {
                long sec = cursor.getLong(HistoryRecyclerViewAdapter.STATS_DATE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(sec);
                practiceMonths[calendar.get(Calendar.MONTH)]++;
                cursor.moveToNext();
            }
            for (int ind = 0; ind < 12; ind++) {
                String key = "month_" + Integer.toString(ind);
                analysisResult.put(key, new Info(practiceMonths[ind]));
            }

            //Averages in weeks and months

            cursor.moveToFirst();
            int week = -1, month=-1;
            int countWeeks=0, countMonth=0;
            int sum=0;
            for (int ind = 0; ind < cursor.getCount(); ind++) {
                long sec = cursor.getLong(HistoryRecyclerViewAdapter.STATS_DATE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(sec);
                int progress =cursor.getInt(HistoryRecyclerViewAdapter.STATS_REPETITON);
                if (progress<10000) {
                    if (week!=calendar.get(Calendar.WEEK_OF_YEAR)) {
                        week=calendar.get(Calendar.WEEK_OF_YEAR);
                        countWeeks++;
                    }
                    if (month!=calendar.get(Calendar.WEEK_OF_YEAR)) {
                        month=calendar.get(Calendar.WEEK_OF_YEAR);
                        countMonth++;
                    }

                    sum += progress;
                }
                cursor.moveToNext();
            }

            cursor.moveToFirst();
            long firstDate = cursor.getLong(HistoryRecyclerViewAdapter.STATS_DATE);
            cursor.moveToLast();
            long lastDate = cursor.getLong(HistoryRecyclerViewAdapter.STATS_DATE);
            int days = (int)TimeUnit.MILLISECONDS.toDays(lastDate-firstDate);
            int max_repetition = cursor.getInt(HistoryRecyclerViewAdapter.STATS_PRACTICE_MAX_REPETITION);
            int progress = cursor.getInt(HistoryRecyclerViewAdapter.STATS_PROGRESS);

            analysisResult.put("average_week",new Info(countWeeks>0?sum/countWeeks:0));
            analysisResult.put("average_month",new Info(countMonth>0?sum/countMonth:0));
            int averageDays = days>0?sum/days:0;
            analysisResult.put("average_days",new Info(averageDays));
            int daysToEnd = averageDays>0?(max_repetition-progress)/averageDays:0;
            analysisResult.put("finish_practice",new Info(daysToEnd));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(new Date().getTime());
            calendar.add(Calendar.SECOND,(int)(daysToEnd*3600*24));

            analysisResult.put("finish_practice_date",new Info(String.format(" %tD",calendar)));
        }
    }

    public Map<String, Info> getAnalysisResult() {
        return analysisResult;
    }
    public void setAnalysisResult(Map<String, Info> analysisResult) {
        this.analysisResult = analysisResult;
    }

}
