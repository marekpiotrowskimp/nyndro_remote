package iso.piotrowski.marek.nyndro.RemainderService;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import iso.piotrowski.marek.nyndro.DataSource.DBQuery;
import iso.piotrowski.marek.nyndro.PracticeMain.MainPracticeActivity;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.R;

public class RemainderService extends IntentService {

    public static final int NOTIFICATION_ID = 6669;
    private static final String TAG = "RemainderService";
    private final IBinder binder = new RemainderBinder();
    private BroadcastReceiver receiver;
    private int runnerCount = 0;

    public RemainderService() {
        super("RemainderService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    public class RemainderBinder extends Binder {
        public RemainderService getRemainderService() {
            return RemainderService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        doRemind();
        setUpBroadcastReceiver();
    }

    private void setUpBroadcastReceiver() {
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                runnerCount++;
                doRemind();
            }
        };

        registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
    }

    private class ReminderRunnable implements Runnable {

        private final int fixRunnerId;
        private Handler handler;
        ReminderRunnable (Handler handler, int runnerId){
            this.handler = handler;
            this.fixRunnerId = runnerId;
        }
        @Override
        public void run() {
            Log.d(TAG, "[service] run: " + runnerCount + " " + fixRunnerId);
            List<ReminderModel> reminderList = DBQuery.getReminders();
            for (ReminderModel reminder : reminderList) {
                Calendar actualCalendar = getCalendarFor(new Date().getTime());
                Calendar remainderCalendar = getCalendarFor(reminder.getPracticeDate());
                if (actualCalendar.compareTo(remainderCalendar) > 0) {
                    SendNotification(getNotificationMessageFor(reminder).toString(), (int) reminder.getPracticeId());
                    if (reminder.getRepeater() == 0) {
                        DBQuery.deleteReminder(reminder.getID());
                    } else {
                        calculateNewDateOfRemainder(reminder, actualCalendar);
                    }
                }
            }
            if (fixRunnerId == runnerCount) handler.postDelayed(this, 30000);
        }
    }

    private void doRemind() {
        final Handler handlerRemind = new Handler();
        handlerRemind.postDelayed(new ReminderRunnable(handlerRemind, runnerCount), 30000);
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

    @NonNull
    private StringBuilder getNotificationMessageFor(ReminderModel reminder) {
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.remaind_text_notificator));
        builder.append(reminder.getPractice().getName());
        return builder;
    }

    @NonNull
    private Calendar getCalendarFor(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar;
    }

    private void SendNotification(String text, int notificationId) {
        Intent intent = new Intent(this, MainPracticeActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainPracticeActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(this).setSmallIcon(R.mipmap.icon_nyndro1)
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentText(text)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID + notificationId, notification);
    }

    @Override
    public void onDestroy() {
        Intent intent = new Intent("iso.piotrowski.marek.nyndro.restart");
        intent.putExtra("information", "restore");
        sendBroadcast(intent);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
