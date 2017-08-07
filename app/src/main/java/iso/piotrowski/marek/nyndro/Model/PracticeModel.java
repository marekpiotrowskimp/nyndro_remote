package iso.piotrowski.marek.nyndro.Model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by marek.piotrowski on 06/08/2017.
 */

@Table(name = "PRACTICE")
public class PracticeModel extends Model {

    @Column(name = "NAME")
    public String name;
    @Column(name = "DESCRIPTION")
    public String description;
    @Column(name = "PRACTICE_IMAGE_ID")
    public int practiceImageId;
    @Column(name = "PROGRESS")
    public int progress;
    @Column(name = "MAX_REPETITION")
    public int maxRepetition;
    @Column(name = "REPETITION")
    public int Repetition;
    @Column(name = "ACTIVE")
    public int active;

    public PracticeModel (){
        super();
    }
}
/*
            db.execSQL("CREATE TABLE PRACTICE (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME TEXT, " +
                    "DESCRIPTION TEXT, " +
                    "PRACTICE_IMAGE_ID INTEGER, " +
                    "PROGRESS INTEGER, " +
                    "MAX_REPETITION INTEGER ," +
                    "REPETITION INTEGER, " +
                    "ACTIVE NUMERIC);");
 */
