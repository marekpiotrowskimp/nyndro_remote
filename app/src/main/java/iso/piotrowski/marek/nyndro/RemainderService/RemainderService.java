package iso.piotrowski.marek.nyndro.RemainderService;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.compat.BuildConfig;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import iso.piotrowski.marek.nyndro.DataSource.DBQuery;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.Utility;

public class RemainderService extends IntentService {

    public static final int NOTIFICATION_ID = 6669;
    private static final String TAG = "RemainderService";
    private BroadcastReceiver receiver;
    private int delayMillis = 5000;


    public RemainderService() {
        super("RemainderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onHandleIntent: [service] receive intend");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onStartCommand: [service] start");
        doRemind();
        return START_NOT_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate: [service] start");
    }

    private void doRemind() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ReminderRunnable reminderRunnable = new ReminderRunnable();
                if (BuildConfig.DEBUG) Log.d(TAG, "[service] run: Thread");
                reminderRunnable.run();
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                stopSelf();
            }
        }).start();
    }

    private void calculateNewDateOfRemainder(ReminderModel reminder, Calendar actualCalendar) {
        int repeater = reminder.getRepeater();
        Calendar calendar = getCalendarFor(reminder.getPracticeDate());
        while (actualCalendar.compareTo(calendar) > 0) {
            switch (repeater) {
                case 1:
                    calendar.add(Calendar.DATE, 1);
                    break;
                case 2:
                    calendar.add(Calendar.DATE, 7);
                    break;
                case 3:
                    calendar.add(Calendar.MONTH, 1);
                    break;
            }
        }
        if (repeater > 0) {
            DBQuery.updateReminders(reminder, calendar.getTimeInMillis(), repeater, reminder.getPractice());
        }
    }

    @Override
    public void onDestroy() {
        if (BuildConfig.DEBUG) Log.d(TAG, "onDestroy: [service] send restart");
        super.onDestroy();
    }

    private class ReminderRunnable implements Runnable {

        ReminderRunnable() {
        }

        @Override
        public void run() {
            if (BuildConfig.DEBUG) Log.d(TAG, "[service] run: ReminderRunner");
            List<ReminderModel> reminderList = DBQuery.getReminders();
            for (ReminderModel reminder : reminderList) {
                Calendar actualCalendar = getCalendarFor(new Date().getTime());
                Calendar remainderCalendar = getCalendarFor(reminder.getPracticeDate());
                if (actualCalendar.compareTo(remainderCalendar) > 0) {
                    Utility.SendNotification(getNotificationMessageFor(reminder).toString(), (int) reminder.getPracticeId() + NOTIFICATION_ID);
                    if (reminder.getRepeater() == 0) {
                        DBQuery.deleteReminder(reminder.getID());
                    } else {
                        calculateNewDateOfRemainder(reminder, actualCalendar);
                    }
                }
            }
        }

        @NonNull
        private StringBuilder getNotificationMessageFor(ReminderModel reminder) {
            StringBuilder builder = new StringBuilder();
            builder.append(getString(R.string.remaind_text_notificator));
            builder.append(reminder.getPractice().getName());
            return builder;
        }
    }

    @NonNull
    private Calendar getCalendarFor(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

}
