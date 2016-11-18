package com.kf5.sdk.ticket.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.kf5.sdk.system.utils.MD5Utils;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5.sdk.ticket.entity.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * author:chosen
 * date:2016/10/20 14:06
 * email:812219713@qq.com
 */

public class KF5SDKtoHelper {

//    private final static String DATABASE_NAME = "kf5sdk.db";

    private final static int VERSION = 1;

    private static final String KEY_ID = "_id";

    private static final String KEY_UPDATE = "update_time";

    private static final String KEY_ORDER_NUM = "order_num";

    private static final String KEY_READ = "bool_read";

    private static String DB_TABLE;

    private SQLiteDatabase db;

    private DBOpenHelper openHelper;

    private Context context;

    private String DB_CREATE;

    public KF5SDKtoHelper(Context context) {
        super();
        this.context = context;
        DB_TABLE = "kf5_ticket";
        DB_CREATE = "create table " +
                DB_TABLE + " (" + KEY_ID + " integer primary key autoincrement, "
                + KEY_READ + " integer not null,"
                + KEY_UPDATE + " text null);";
    }


    /**
     * @throws SQLiteException
     */
    public void openDatabase() throws SQLiteException {

        openHelper = new DBOpenHelper(context, MD5Utils.GetMD5Code("kf5_ticket_" + SPUtils.getUserId()) + ".db", null, VERSION);
        try {
            db = openHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            e.printStackTrace();
            db = openHelper.getReadableDatabase();
        }
        boolean isTableExit = tableIsExist(openHelper, DB_TABLE);
        if (!isTableExit) {
            db.execSQL(DB_CREATE);
        }

    }

    /**
     *
     */
    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }


    /**
     * @return
     */
    public List<Message> queryAllData() {
        Cursor cursor = db.query(DB_TABLE, new String[]{KEY_ID, KEY_UPDATE}, null, null, null, null, null);
        return convertToStorage(cursor);
    }

    /**
     * @return
     */
    public List<Message> queryAllDataOrderByID() {
        Cursor cursor = db.query(DB_TABLE, new String[]{KEY_ID, KEY_UPDATE}, null, null, null, null, KEY_ID + " DESC");
        return convertToStorage(cursor);
    }


    /**
     * @param message
     */
    public void updateDataByID(Message message) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_UPDATE, message.getLastCommentId());
        contentValues.put(KEY_READ, message.isRead() ? 1 : 0);
        db.update(DB_TABLE, contentValues, KEY_ID + "=?", new String[]{message.getId()});
    }


    /**
     * @param message
     */
    public void updateBoolRead(Message message) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_READ, message.isRead() ? 1 : 0);
        db.update(DB_TABLE, contentValues, KEY_ID + "=?", new String[]{message.getId()});
    }


    /**
     * @param id
     * @return
     */
    public Message queryOneData(String id) {

        Cursor results = db.query(DB_TABLE, new String[]{KEY_ID, KEY_UPDATE, KEY_READ},
                KEY_ID + "=" + id, null, null, null, null);
        return getMessage(results);

    }

    /**
     *
     */
    public void dropTable() {

        boolean isTableExit = tableIsExist(openHelper, DB_TABLE);
        String sql = "DROP TABLE " + DB_TABLE;
        if (isTableExit) {
            db.execSQL(sql);
        }
    }


    /**
     * @param message
     * @return
     */
    public long insert(Message message) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_ID, message.getId());
        contentValues.put(KEY_UPDATE, message.getLastCommentId());
        contentValues.put(KEY_READ, message.isRead() ? 1 : 0);
        return db.insert(DB_TABLE, null, contentValues);
    }

    /**
     * @param cursor
     * @return
     */
    private List<Message> convertToStorage(Cursor cursor) {

        int count = cursor.getCount();
        if (count == 0 || !cursor.moveToFirst()) {
            return null;
        }

        List<Message> list = new ArrayList<Message>();
        for (int i = 0; i < count; i++) {
            Message message = new Message();
            message.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
            message.setLastCommentId(cursor.getString(cursor.getColumnIndex(KEY_UPDATE)));
            list.add(message);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    /**
     * @param cursor
     * @return
     */
    private Message getMessage(Cursor cursor) {

        Message message = new Message();
        int count = cursor.getCount();
        if (count == 0 || !cursor.moveToFirst()) {
            return null;
        }
        message.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
        message.setLastCommentId(cursor.getString(cursor.getColumnIndex(KEY_UPDATE)));
        message.setRead(cursor.getInt(cursor.getColumnIndex(KEY_READ)) == 1);
        cursor.close();
        return message;
    }


    /**
     * 是否包含当前信息
     *
     * @param tableName   表名
     * @param custom_name 字段名称
     * @param id          字段id
     * @return
     */
    private boolean isContainThisObj(String tableName, String custom_name, int id) {

        String sql = "SELECT * FROM " + tableName + " WHERE " + custom_name + "=" + id;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            if (cursor == null)
                return false;
            int count = cursor.getCount();
            if (count == 0 || !cursor.moveToFirst())
                return false;
            else
                return true;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }


    /**
     * @param id
     * @return
     */
    public long deleteOneData(String id) {

        return db.delete(DB_TABLE, KEY_ORDER_NUM + "=" + id, null);

    }


    /**
     *
     */
    public void deleteFirstData() {

        String sql = "delete from " + DB_TABLE + " limit 1;";
        db.execSQL(sql);
    }


    /**
     *
     */
    private static class DBOpenHelper extends SQLiteOpenHelper {

        public DBOpenHelper(Context context, String name,
                            SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // TODO Auto-generated method stub
            //			db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }

    }

    /**
     * @param openHelper
     * @param tableName
     * @return
     */

    public static boolean tableIsExist(DBOpenHelper openHelper, String tableName) {
        boolean result = false;
        if (tableName == null) {
            return false;
        }
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            db = openHelper.getReadableDatabase();
            cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName.trim() + "'", null);
            if (cursor.getCount() > 0) {
                cursor.close();
                return true;
            }
            cursor.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return result;
    }

}
