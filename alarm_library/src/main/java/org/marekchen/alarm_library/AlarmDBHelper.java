package org.marekchen.alarm_library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenpei on 2017/12/15.
 */

public class AlarmDBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "alarm";
    private static final String TABLE_NAME = "alarm_info";
    private static final String ROW_ID = "rowid";
    private static final String TIME = "time";
    private static final String MESSAGE = "message";
    private static final String STATUS = "statue";
    private static final String BACK_TIME = "back_time";
    private static final String TYPE = "type";

    public AlarmDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s)", TABLE_NAME,
                TIME + " TEXT," +
                        MESSAGE + " TEXT," +
                        STATUS + " INTEGER," +
                        BACK_TIME + " INTEGER," +
                        TYPE + " INTEGER"
        );
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAlarm(AlarmItem item) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues value = new ContentValues();
            value.put(TIME, item.getTime());
            value.put(MESSAGE, item.getMessage());
            value.put(STATUS, item.getStatus());
            value.put(BACK_TIME, item.getBackTime());
            value.put(TYPE, item.getType());
            db.insert(TABLE_NAME, null, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void deleteAlarm(AlarmItem item) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.delete(TABLE_NAME, "? = ?", new String[]{ROW_ID, item.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public void updateAlarm(AlarmItem item) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            ContentValues value = new ContentValues();
            value.put(TIME, item.getTime());
            value.put(MESSAGE, item.getMessage());
            value.put(STATUS, item.getStatus());
            value.put(BACK_TIME, item.getBackTime());
            value.put(TYPE, item.getType());
            db.update(TABLE_NAME, value, "? = ?", new String[]{ROW_ID, item.getId()});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public List<AlarmItem> queryAlarm() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        List<AlarmItem> list = new ArrayList<>();
        try {
            db = getWritableDatabase();
            cursor = db.query(TABLE_NAME, new String[]{ROW_ID, TIME, MESSAGE, STATUS, BACK_TIME, TYPE}, null, null, null, null, TIME);
            //CrashManager.ItemList list = new CrashManager.ItemList();
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    AlarmItem item = new AlarmItem();
                    item.setId(cursor.getString(0));
                    item.setTime(cursor.getString(1));
                    item.setMessage(cursor.getString(2));
                    item.setStatus(cursor.getInt(3));
                    item.setBackTime(cursor.getInt(4));
                    item.setType(cursor.getInt(5));
                    list.add(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
