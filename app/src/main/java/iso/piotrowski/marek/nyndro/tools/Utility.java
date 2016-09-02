package iso.piotrowski.marek.nyndro.tools;

import java.util.Calendar;
import java.util.Date;

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
}
