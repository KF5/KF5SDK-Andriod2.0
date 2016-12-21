package com.kf5.sdk.im.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * author:chosen
 * date:2016/11/3 15:45
 * email:812219713@qq.com
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static String DB_TABLE = "kf5chat_message";

    private static final int VERSION = 2;

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
        createTable(db);
        isCreateDb = true;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
                + DataBaseColumn.IS_COM + " INTEGER DEFAULT 0, "
                + DataBaseColumn.MESSAGE + " TEXT, "
                + DataBaseColumn.CREATED_DATE + " INTEGER, "
                + DataBaseColumn.FILE_URL + " TEXT, "
                + DataBaseColumn.LOCAL_PATH + " TEXT, "
                + DataBaseColumn.FILE_TYPE + " TEXT, "
                + DataBaseColumn.FILE_NAME + " TEXT, "
                + DataBaseColumn.SEND_STATUS + " INTEGER DEFAULT 0, "
                + DataBaseColumn.MESSAGE_TYPE + " TEXT, "
                + DataBaseColumn.IS_READ + "  INTEGER DEFAULT 0, "
                + DataBaseColumn.MARK + " TEXT "
                + ");";
        database.execSQL(sql);
    }

    private void alertAddField(SQLiteDatabase database) {
        addChatField(database);
    }


    private void addChatField(SQLiteDatabase database) {
        String SQL = "ALTER TABLE " + DB_TABLE + " ADD " + DataBaseColumn.LOCAL_PATH + " TEXT";
        database.execSQL(SQL);
    }


}