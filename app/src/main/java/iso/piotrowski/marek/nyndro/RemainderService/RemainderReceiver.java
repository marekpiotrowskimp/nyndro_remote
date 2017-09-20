package iso.piotrowski.marek.nyndro.RemainderService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Date;

public class RemainderReceiver extends BroadcastReceiver {
    private static final String TAG = "RemainderReceiver";

    public RemainderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "[service] onReceive: restart service");

        if ((intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) ||
            (intent.getAction().equals("iso.piotrowski.marek.nyndro.start"))) {
//            setAlarm(context);
            startService(context);
        }

        if (intent.getAction().equals("iso.piotrowski.marek.nyndro.restart")) {
        }
    }

    private void startService(Context context) {
        Intent intent = new Intent(context, RemainderService.class);
        context.startService(intent);
    }

    private void setAlarm(Context context) {
        AlarmManager alarmMgr;
        PendingIntent alarmIntent;
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intentToService = new Intent(context, RemainderService.class);
        alarmIntent = PendingIntent.getService(context, 0, intentToService, 0);

//        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_FIFTEEN_MINUTES,
//                AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);

        alarmMgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, new Date().getTime() + 5000,
                5000, alarmIntent);
        Log.d(TAG, "setAlarm: [service] ALARM");
    }
}
