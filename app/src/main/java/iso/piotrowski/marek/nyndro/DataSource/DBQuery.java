package iso.piotrowski.marek.nyndro.DataSource;

import com.activeandroid.query.Select;

import java.util.List;

import iso.piotrowski.marek.nyndro.Model.PracticeModel;

/**
 * Created by marek.piotrowski on 06/08/2017.
 */

public class DBQuery {

    public static List<PracticeModel> getAll(){
        return new Select().from(PracticeModel.class).execute();
    }

    public static List<PracticeModel> getPractice(){
        return new Select().from(PracticeModel.class).execute();
    }
}
