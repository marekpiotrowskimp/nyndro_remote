package iso.piotrowski.marek.nyndro.DataSource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.activeandroid.ActiveAndroid;

/**
 * Created by Marek on 25.07.2016.
 */
public class PracticeDatabaseHelper {

    public PracticeDatabaseHelper (Context context) {
    }

    public SQLiteDatabase getReadableDatabase() {
        return ActiveAndroid.getDatabase();
    }

    public SQLiteDatabase getWritableDatabase(){
        return ActiveAndroid.getDatabase();
    }
}
