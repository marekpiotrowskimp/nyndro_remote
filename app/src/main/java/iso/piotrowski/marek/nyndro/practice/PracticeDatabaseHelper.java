package iso.piotrowski.marek.nyndro.practice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.format.DateUtils;
import android.util.Log;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Marek on 25.07.2016.
 */
public class PracticeDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "practice";
    private static final int DB_VERSION = 1;

    public PracticeDatabaseHelper (Context context){
        super(context,DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updatePracticeDatabase(sqLiteDatabase,0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldV, int newV) {
        updatePracticeDatabase(sqLiteDatabase, oldV, newV);
    }

    private void updatePracticeDatabase (SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion<1)
        {
            db.execSQL("CREATE TABLE PRACTICE (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NAME TEXT, " +
                    "DESCRIPTION TEXT, " +
                    "PRACTICE_IMAGE_ID INTEGER, " +
                    "PROGRESS INTEGER, " +
                    "MAX_REPETITION INTEGER ," +
                    "REPETITION INTEGER, " +
                    "ACTIVE NUMERIC);");

            db.execSQL("CREATE TABLE HISTORY (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "PRACTICE_ID INTEGER, " +
                    "PROGRESS INTEGER, " +
                    "PRACTICE_DATE INTEGER, " +
                    "REPETITION INTEGER, " +
                    "ACTIVE NUMERIC);");

            db.execSQL("CREATE TABLE REMAINDER (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "PRACTICE_ID INTEGER, " +
                    "PRACTICE_DATE INTEGER, " +
                    "REPETITION INTEGER, " +
                    "ACTIVE NUMERIC, " +
                    "REPEATER INTEGER);");
        }

        if (oldVersion<DB_VERSION)
        {
        }

    }

}
