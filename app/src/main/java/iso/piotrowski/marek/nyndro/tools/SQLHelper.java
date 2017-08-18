package iso.piotrowski.marek.nyndro.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.Date;

import iso.piotrowski.marek.nyndro.history.HistoryRecyclerViewAdapter;
import iso.piotrowski.marek.nyndro.DataSource.ConstantsData.Practice;
import iso.piotrowski.marek.nyndro.DataSource.PracticeDatabaseHelper;

/**
 * Created by Marek on 03.08.2016.
 */
public class SQLHelper {

    public static boolean isUpdateDatabase = false;

    public static Cursor getCursorPractice(SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            cursor = db.query("practice", new String[]{"_id", "NAME", "DESCRIPTION", "PRACTICE_IMAGE_ID",
                            "PROGRESS", "MAX_REPETITION", "REPETITION", "ACTIVE"},
                    "active = ?", new String[]{"1"}, null, null, null);
        } catch (SQLiteException e) {
        }
        return cursor;
    }

    public static void insertPractice(Practice practice, Context context) {
        try {
            PracticeDatabaseHelper practiceDatabaseHelper = new PracticeDatabaseHelper(context);
            SQLiteDatabase db = practiceDatabaseHelper.getWritableDatabase();
            ContentValues cv;
            cv = new ContentValues();
            cv.put("NAME", practice.getName());
            cv.put("DESCRIPTION", practice.getDescription());
            cv.put("PRACTICE_IMAGE_ID", practice.getImageResourcesId());
            cv.put("PROGRESS", practice.getProgress());
            cv.put("MAX_REPETITION", practice.getMaxRepetition());
//            cv.put("LAST_PRACTICE_DATE", practice.getLastPracticeDate());
//            cv.put("NEXT_PRACTICE_DATE", practice.getNextPracticeDate());
            cv.put("REPETITION", practice.getRepetition());
            cv.put("ACTIVE", "1");
            db.insert("practice", null, cv);
            db.close();
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
    }


    public static Cursor getRemainderCursor(SQLiteDatabase db, int active) {
        String sqlQuery;
        Cursor cursor = null;
        try {
            sqlQuery = "SELECT remainder._id, remainder.practice_id, remainder.practice_date, " +
                    "remainder.repetition, remainder.active, remainder.repeater, practice.name, practice.practice_image_id, practice.max_repetition FROM remainder INNER JOIN practice ON remainder.practice_id=practice._id WHERE remainder.active=?";
            cursor = db.rawQuery(sqlQuery, new String[]{Integer.toString(active)});
        } catch (SQLiteException e) {
        }
        return cursor;
    }

    public static void updateRemainder(SQLiteDatabase db, int practiceId, long practiceDate, int practiceRepetition, int repeater, int plan_id) {
        try {
            ContentValues cv;
            cv = new ContentValues();
            cv.put("PRACTICE_ID", Integer.toString(practiceId));
            cv.put("PRACTICE_DATE", String.valueOf(practiceDate));
            cv.put("REPETITION", Integer.toString(practiceRepetition));
            cv.put("ACTIVE", "1");
            cv.put("REPEATER", Integer.toString(repeater));
            db.update("remainder", cv, "_id = ?", new String[]{Integer.toString(plan_id)});
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
    }

    public static void insertRemainder(SQLiteDatabase db, int practiceId, long practiceDate, int practiceRepetition, int repeater) {
        try {
            ContentValues cv;
            cv = new ContentValues();
            cv.put("PRACTICE_ID", Integer.toString(practiceId));
            cv.put("PRACTICE_DATE", String.valueOf(practiceDate));
            cv.put("REPETITION", Integer.toString(practiceRepetition));
            cv.put("ACTIVE", "1");
            cv.put("REPEATER", Integer.toString(repeater));
            db.insert("remainder", null, cv);
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
    }

    public static void deletePlan(SQLiteDatabase db, Cursor cursorPlans, int adapterPosition) {
        try {
            cursorPlans.moveToPosition(adapterPosition);
            db.delete("remainder", "_id = ?", new String[]{cursorPlans.getString(0)});
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
    }

    public static void deletePlanDone(SQLiteDatabase db, int _id) {
        try {
            db.delete("remainder", "_id = ?", new String[]{Integer.toString(_id)});
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
    }

    public static void updateRemainderRepeater(SQLiteDatabase db, long timeInMillis, int remainder_id) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("PRACTICE_DATE", String.valueOf(timeInMillis));
            db.update("remainder", cv, "_id = ?", new String[]{Integer.toString(remainder_id)});
            isUpdateDatabase= true;
        } catch (SQLiteException e) {}
    }
}
