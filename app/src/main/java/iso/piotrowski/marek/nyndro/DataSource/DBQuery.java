package iso.piotrowski.marek.nyndro.DataSource;

import com.activeandroid.query.Select;

import java.util.List;

import iso.piotrowski.marek.nyndro.Model.HistoryModel;
import iso.piotrowski.marek.nyndro.Model.PracticeModel;
import iso.piotrowski.marek.nyndro.Model.ReminderModel;

/**
 * Created by marek.piotrowski on 06/08/2017.
 */

public class DBQuery {

    public static List<PracticeModel> getPractices(){
        return new Select().from(PracticeModel.class).execute();
    }

    public static List<PracticeModel> getPractice(Integer id){
        return new Select().from(PracticeModel.class).where("_id = ?", id).execute();
    }

    public static List<HistoryModel> getHistory(){
        return new Select().from(HistoryModel.class).execute();
    }

    public static List<ReminderModel> getReminders(){
        return new Select().from(ReminderModel.class).execute();
    }

}
