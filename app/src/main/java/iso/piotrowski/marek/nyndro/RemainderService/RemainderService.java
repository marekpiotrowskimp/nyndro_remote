package iso.piotrowski.marek.nyndro.RemainderService;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import iso.piotrowski.marek.nyndro.DataSource.DBQuery;
import iso.piotrowski.marek.nyndro.MainPracticsActivity;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;
import iso.piotrowski.marek.nyndro.plans.PlansAdapter;
import iso.piotrowski.marek.nyndro.DataSource.PracticeDatabaseHelper;
import iso.piotrowski.marek.nyndro.R;
import iso.piotrowski.marek.nyndro.tools.SQLHelper;

public class RemainderService extends IntentService {

    public static final int NOTYFICATION_ID = 6669;
    private final IBinder binder = new RemainderBinder();
    private Context context;
    // private BroadcastReceiver receiver;

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
        context = this;
        final Handler handlerRemaind = new Handler();
        handlerRemaind.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v("RemainderService", "running....");
                List<ReminderModel> reminderList = DBQuery.getReminders();
                for (ReminderModel reminder : reminderList) {
//                    for (; (cursor.getCount() > 0) && (!cursor.isAfterLast()); cursor.moveToNext()) {
                    Calendar aktualCalendar = Calendar.getInstance();
                    aktualCalendar.setTimeInMillis(new Date().getTime());
                    Calendar remainderCalendar = Calendar.getInstance();
                    remainderCalendar.setTimeInMillis(reminder.getPracticeDate());

                    if (aktualCalendar.compareTo(remainderCalendar) > 0) {
                        Log.v("RemainderService", "sending message....");
                        StringBuilder builder = new StringBuilder();
                        builder.append(getString(R.string.remaind_text_notificator));
                        builder.append(reminder.getPractice().getName());
                        SendNotification(builder.toString(), (int) reminder.getPracticeId());


                        int repeater = reminder.getRepeater();
                        long dateRemind = reminder.getPracticeDate();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(dateRemind);
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
                            default:
                                DBQuery.deleteReminder(reminder.getID());
                        }
                        if (repeater > 0) {
                            DBQuery.updateReminders(reminder, calendar.getTimeInMillis(), repeater, reminder.getPractice());
                        }
                    }
                }
                handlerRemaind.postDelayed(this, 10000);
            }
        }, 10000);

//        receiver = new BroadcastReceiver() {
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                // Some action
//            }
//        };
//
//        registerReceiver(receiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
    }

    private void SendNotification(String text, int notificationId) {
        Intent intent = new Intent(this, MainPracticsActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainPracticsActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notyfication = new Notification.Builder(this).setSmallIcon(R.mipmap.icon_nyndro1)
                .setContentTitle(getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentText(text)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTYFICATION_ID + notificationId, notyfication);
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
