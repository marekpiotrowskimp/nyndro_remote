package iso.piotrowski.marek.nyndro.tools;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
import iso.piotrowski.marek.nyndro.PracticeMain.MainPracticeActivity;
import iso.piotrowski.marek.nyndro.R;

/**
 * Created by Marek on 02.08.2016.
 */
public class Utility {

    public static Date removeTimeFromDate(Date date) {

        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date addDayToDate(Date date) {

        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static String getStringDate (long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return String.format(Locale.UK , "%td-%tm-%tY %tR", calendar, calendar, calendar, calendar);
    }

    public static void setUpPracticeDate(TextView practiceDateView, long date, int practiceDateResId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        practiceDateView.setText(String.format(Locale.UK, "%s %s", getFormatEditFromResources(practiceDateResId),
                date == -1 ? getFormatEditFromResources(R.string.NoDateToShow) : String.format(Locale.UK, " %tD", calendar)));
    }

    public static String getFormatEditFromResources(int resourcesId) {
        return String.format(Locale.UK, "%s", NyndroApp.getContext().getResources().getText(resourcesId));
    }

    public static void startSoundEffect(int resourcesRawId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(NyndroApp.getContext(), resourcesRawId);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });
        mediaPlayer.start();
    }

    public static void SendNotification(String text, int notificationId) {
        Intent intent = new Intent(NyndroApp.getContext(), MainPracticeActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(NyndroApp.getContext());
        stackBuilder.addParentStack(MainPracticeActivity.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new Notification.Builder(NyndroApp.getContext()).setSmallIcon(R.mipmap.icon_nyndro1)
                .setContentTitle(NyndroApp.getContext().getString(R.string.app_name))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(pendingIntent)
                .setContentText(text)
                .build();

        NotificationManager notificationManager = (NotificationManager) NyndroApp.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, notification);
    }

}
