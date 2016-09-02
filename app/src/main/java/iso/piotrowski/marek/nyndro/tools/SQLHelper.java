package iso.piotrowski.marek.nyndro.tools;

import android.app.backup.BackupManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.Date;

import iso.piotrowski.marek.nyndro.history.HistoryRecyclerViewAdapter;
import iso.piotrowski.marek.nyndro.practice.Practice;
import iso.piotrowski.marek.nyndro.practice.PracticeDatabaseHelper;

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


    public static void deleteRealPractice(SQLiteDatabase db, Cursor cursorPractice, int position) {
        try {
            cursorPractice.moveToPosition(position);
            db.delete("practice", "_id = ?", new String[]{cursorPractice.getString(0)});
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
    }

    public static void deletePractice(SQLiteDatabase db, Cursor cursorPractice, int position) {
        try {
            cursorPractice.moveToPosition(position);
            // db.delete("practice","_id = ?",new String[]{cursorPractice.getString(0)});
            ContentValues cv = new ContentValues();
            cv.put("ACTIVE", "0");
            db.update("practice", cv, "_id = ?", new String[]{cursorPractice.getString(0)});
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
    }

    public static void recoverPracticeHistoryRemainder(SQLiteDatabase db) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("ACTIVE", "1");
            db.update("practice", cv, "active = ?", new String[]{"0"});
            db.update("history", cv, "active = ?", new String[]{"0"});
            db.update("remainder", cv, "active = ?", new String[]{"0"});
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
    }

    public static void updatePractice(SQLiteDatabase db, int _id, int progressAdd) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("PROGRESS", String.valueOf(progressAdd));
            //  cv.put("LAST_PRACTICE_DATE", String.valueOf(new Date().getTime()));
            db.update("practice", cv, "_id = ?", new String[]{Integer.toString(_id)});
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
    }

    public static void updatePracticeAll(SQLiteDatabase db, int _id, String name, String description, String progress, String maxRepetition, String repetition) {
        try {
            ContentValues cv;
            cv = new ContentValues();
            cv.put("NAME", name);
            cv.put("DESCRIPTION", description);
            cv.put("PROGRESS", progress);
            cv.put("MAX_REPETITION", maxRepetition);
            cv.put("REPETITION", repetition);
            db.update("practice", cv, "_id = ?", new String[]{Integer.toString(_id)});
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
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

    public static void insertHistory(SQLiteDatabase db, int practiceId, int practiceProgress, long practiceDate, int practiceRepetition) {
        try {
            ContentValues cv;
            Date date = Utility.removeTimeFromDate(new Date(practiceDate));
            Cursor testCursor = getHistoryCursor(db, 2, date.getTime(), practiceId);
            if (testCursor.getCount() > 0) {
                testCursor.moveToFirst();
                int _id = testCursor.getInt(0);
                cv = new ContentValues();
                cv.put("PRACTICE_ID", Integer.toString(practiceId));
                cv.put("PROGRESS", Integer.toString(practiceProgress));
                cv.put("PRACTICE_DATE", String.valueOf(date.getTime()));
                cv.put("REPETITION", Integer.toString(practiceRepetition + testCursor.getInt(HistoryRecyclerViewAdapter.STATS_REPETITON)));
                cv.put("ACTIVE", "1");
                db.update("history", cv, "_id = ?", new String[]{Integer.toString(_id)});
            } else {
                cv = new ContentValues();
                cv.put("PRACTICE_ID", Integer.toString(practiceId));
                cv.put("PROGRESS", Integer.toString(practiceProgress));
                cv.put("PRACTICE_DATE", String.valueOf(date.getTime()));
                cv.put("REPETITION", Integer.toString(practiceRepetition));
                cv.put("ACTIVE", "1");
                db.insert("history", null, cv);
            }
            testCursor.close();
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
    }

    public static void updateHistoryInactive(SQLiteDatabase db, int practiceId, int active) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("ACTIVE", Integer.toString(active));
            db.update("history", cv, "practice_id = ?", new String[]{Integer.toString(practiceId)});
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
    }

    public static Cursor getHistoryCursor(SQLiteDatabase db) {
        return getHistoryCursor(db, 1, 0, 0);
    }

    public static Cursor getHistoryCursor(SQLiteDatabase db, int practiceId) {
        return getHistoryCursor(db, 3, 0, practiceId);
    }

    public static Cursor getHistoryCursor(SQLiteDatabase db, int typeCursor, long date, int practiceId) {
        String sqlQuery;
        Cursor cursor = null;
        try {
            switch (typeCursor) {
                case 1:
                    sqlQuery = "SELECT history._id, history.practice_id, history.progress, history.practice_date, " +
                            "history.repetition, practice.name, practice.practice_image_id FROM history INNER JOIN practice ON history.practice_id=practice._id WHERE history.active = ?";
                    cursor = db.rawQuery(sqlQuery, new String[]{"1"});
                    break;
                case 2:
                    sqlQuery = "SELECT history._id, history.practice_id, history.progress, history.practice_date, " +
                            "history.repetition, practice.name, practice.practice_image_id FROM history INNER JOIN practice ON history.practice_id=practice._id WHERE history.practice_date=? and history.practice_id=? and history.active = ?";
                    cursor = db.rawQuery(sqlQuery, new String[]{Long.toString(date), Integer.toString(practiceId), "1"});
                    break;
                case 3:
                    sqlQuery = "SELECT history._id, history.practice_id, history.progress, history.practice_date, " +
                            "history.repetition, practice.name, practice.practice_image_id, practice.max_repetition FROM history INNER JOIN practice ON history.practice_id=practice._id WHERE history.practice_id=? and history.active = ?";
                    cursor = db.rawQuery(sqlQuery, new String[]{Integer.toString(practiceId), "1"});
                    break;
            }
        } catch (SQLiteException e) {
        }
        return cursor;
    }


    private static void insertInitialData(SQLiteDatabase db) {
        try {
            ContentValues cv;
            for (int i = 0; i < Practice.practices.length; i++) {
                cv = new ContentValues();
                cv.put("NAME", Practice.practices[i].getName());
                cv.put("DESCRIPTION", Practice.practices[i].getDescription());
                cv.put("PRACTICE_IMAGE_ID", Practice.practices[i].getImageResourcesId());
                cv.put("PROGRESS", Practice.practices[i].getProgress());
                cv.put("MAX_REPETITION", Practice.practices[i].getMaxRepetition());
//                cv.put("LAST_PRACTICE_DATE", Practice.practices[i].getLastPracticeDate());
//                cv.put("NEXT_PRACTICE_DATE", Practice.practices[i].getNextPracticeDate());
                cv.put("REPETITION", Practice.practices[i].getRepetition());
                cv.put("ACTIVE", true);
                db.insert("practice", null, cv);
                isUpdateDatabase= true;
            }
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

    public static void updateRemainderActive(SQLiteDatabase db, int practice_id) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("ACTIVE", "0");
            db.update("remainder", cv, "practice_id = ?", new String[]{Integer.toString(practice_id)});
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
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

    public static void deleteAllInactive(SQLiteDatabase db) {
        try {
            db.delete("remainder", "active = ?", new String[]{"0"});
            db.delete("history", "active = ?", new String[]{"0"});
            db.delete("practice", "active = ?", new String[]{"0"});
            isUpdateDatabase= true;
        } catch (SQLiteException e) {
        }
    }

    public static long lastPractice(SQLiteDatabase db, int practice_id) {
        Cursor cursor;
        long lastDate = -1;
        try {
            cursor = db.query("history", new String[]{"_id", "practice_date"}, "practice_id=?", new String[]{String.valueOf(practice_id)}, null, null, "practice_date DESC");  //db.rawQuery(sqlQuery, new String[]{Integer.toString(practice_id)});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                lastDate = cursor.getLong(1);
            }
        } catch (SQLiteException e) {
        }

        return lastDate;
    }

    public static long nextPractice(SQLiteDatabase db, int practice_id) {
        Cursor cursor;
        long nextDate = -1;
        try {
            cursor = db.query("remainder", new String[]{"_id", "practice_date"}, "practice_id=?", new String[]{String.valueOf(practice_id)}, null, null, "practice_date");  //db.rawQuery(sqlQuery, new String[]{Integer.toString(practice_id)});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                nextDate = cursor.getLong(1);
            }
        } catch (SQLiteException e) {
        }
        return nextDate;
    }
}
