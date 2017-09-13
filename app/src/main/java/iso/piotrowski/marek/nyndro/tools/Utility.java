package iso.piotrowski.marek.nyndro.tools;

import android.media.MediaPlayer;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import iso.piotrowski.marek.nyndro.Application.NyndroApp;
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

    public static void setUpPracticeDate(TextView practiceDateView, long date, int practiceDateResId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        practiceDateView.setText(String.format("%s %s", getFormatEditFromResources(practiceDateResId),
                date == -1 ? getFormatEditFromResources(R.string.NoDateToShow) : String.format(" %tD", calendar)));
    }

    public static String getFormatEditFromResources(int resourcesId) {
        return String.format("%s", NyndroApp.getContext().getResources().getText(resourcesId));
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
}
