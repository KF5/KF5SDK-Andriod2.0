package com.kf5.sdk.im.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.kf5.sdk.system.utils.LogUtil;

/**
 * author:chosen
 * date:2016/11/3 15:45
 * email:812219713@qq.com
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static String DB_TABLE = "kf5chat_message";

    public static final String DB_USER = "kf5_chat_user";

    private static final int VERSION = 4;

    public static boolean isCreateDb;

    public DataBaseHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public DataBaseHelper(Context context, String dbName) {
        this(context, dbName, null, VERSION);
        isCreateDb = false;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtil.printf("创建数据库");
        createTable(db);
        createUserTable(db);
        isCreateDb = true;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtil.printf("更新数据库");
        alertAddField(db);
    }


    /**
     * 创建聊天内容表
     */
    private void createTable(SQLiteDatabase database) {

        String sql = "CREATE TABLE IF NOT EXISTS "
                + DB_TABLE
                + " ("
                + DataBaseColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataBaseColumn.CHAT_ID + " INTEGER, "
                + DataBaseColumn.MESSAGE_ID + " INTEGER, "
                + DataBaseColumn.MESSAGE + " TEXT, "
                + DataBaseColumn.CREATED_DATE + " INTEGER, "
                + DataBaseColumn.FILE_URL + " TEXT, "
                + DataBaseColumn.LOCAL_PATH + " TEXT, "
                + DataBaseColumn.FILE_TYPE + " TEXT, "
                + DataBaseColumn.FILE_NAME + " TEXT, "
                + DataBaseColumn.SEND_STATUS + " INTEGER DEFAULT 0, "
                + DataBaseColumn.MESSAGE_TYPE + " TEXT, "
                + DataBaseColumn.ROLE + " TEXT, "
                + DataBaseColumn.IS_READ + "  INTEGER DEFAULT 0, "
                + DataBaseColumn.MARK + " TEXT, "
                + DataBaseColumn.USER_ID + " TEXT, "
                + DataBaseColumn.NAME + " TEXT, "
                + DataBaseColumn.RECALLED + " INTEGER "
                + ");";
        database.execSQL(sql);
    }

    private void createUserTable(SQLiteDatabase database) {
        String sql = "CREATE TABLE IF NOT EXISTS "
                + DB_USER
                + " ("
                + UserColumn.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + UserColumn.USER_ID + " INTEGER, "
                + UserColumn.NAME + " TEXT, "
                + UserColumn.DISPLAY_NAME + " TEXT, "
                + UserColumn.PHOTO_URL + " TEXT "
                + ");";
        database.execSQL(sql);
        LogUtil.printf("创建User表");
    }

    private void alertAddField(SQLiteDatabase database) {
        addChatField(database);
    }


    private void addChatField(SQLiteDatabase database) {
        addField(database, DB_TABLE, DataBaseColumn.LOCAL_PATH);
        addField(database, DB_TABLE, DataBaseColumn.USER_ID);
        addField(database, DB_TABLE, DataBaseColumn.NAME);
        addField(database, DB_TABLE, DataBaseColumn.RECALLED);
        if (!isTableExist(database, DB_USER)) {
            createUserTable(database);
        }
    }

    private void addField(SQLiteDatabase database, String tableName, String filedName) {
        if (!isFieldExist(database, tableName, filedName)) {
            String SQL = "ALTER TABLE " + tableName + " ADD " + filedName + " TEXT";
            database.execSQL(SQL);
        }
    }


    /**
     * 判断字段是否存在
     *
     * @param db
     * @param tableName
     * @param fieldName
     * @return
     */
    private boolean isFieldExist(SQLiteDatabase db, String tableName, String fieldName) {
        String queryStr = "select sql from sqlite_master where type = 'table' and name = '%s'";
        queryStr = String.format(queryStr, tableName);
        Cursor c = db.rawQuery(queryStr, null);
        String tableCreateSql = null;
        try {
            if (c != null && c.moveToFirst()) {
                tableCreateSql = c.getString(c.getColumnIndex("sql"));
            }
        } finally {
            if (c != null)
                c.close();
        }
        if (tableCreateSql != null && tableCreateSql.contains(fieldName))
            return true;
        return false;
    }

    /**
     * 判断表是否存在
     *
     * @param database
     * @param tableName
     * @return
     */
    private static boolean isTableExist(SQLiteDatabase database, String tableName) {

        if (TextUtils.isEmpty(tableName))
            return false;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("SELECT DISTINCT tbl_name FROM sqlite_master WHERE tbl_name = '" + tableName.trim() + "'", null);
            if (cursor.getCount() > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return false;
    }

}