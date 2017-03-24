package com.kf5.sdk.im.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kf5.sdk.system.utils.MD5Utils;
import com.kf5.sdk.system.utils.SPUtils;

/**
 * author:chosen
 * date:2016/11/3 15:45
 * email:812219713@qq.com
 */

public class SQLManager {

    private static DataBaseHelper dataBaseHelper;

    private static SQLiteDatabase sqLiteDatabase;

    public SQLManager(Context context) {
        openDataBase(context);
    }

    private void openDataBase(Context context) {
        if (dataBaseHelper == null)
            dataBaseHelper = new DataBaseHelper(context, MD5Utils.GetMD5Code("kf5_chat_" + SPUtils.getUserId()) + "v1.db");
        if (sqLiteDatabase == null)
            sqLiteDatabase = dataBaseHelper.getWritableDatabase();
    }

    /**
     * 关闭数据库连接
     */
    public void destroy() {

        try {
            if (dataBaseHelper != null) {
                dataBaseHelper.close();
            }
            closeDB();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    /**
     * 打开数据库连接
     *
     * @param isReadOnly
     */
    private void open(boolean isReadOnly) {

        if (dataBaseHelper == null)
            return;
        if (sqLiteDatabase == null) {
            if (isReadOnly) {
                sqLiteDatabase = dataBaseHelper.getReadableDatabase();
            } else {
                sqLiteDatabase = dataBaseHelper.getWritableDatabase();
            }
        }
    }

    /**
     * 重连数据库
     */
    public final void reOpen() {

        closeDB();
        open(false);

    }

    /**
     * 打开数据库
     *
     * @return
     */
    protected final SQLiteDatabase openSqlDB() {

        open(false);
        return sqLiteDatabase;

    }


    /**
     * 关闭数据库
     */
    private void closeDB() {

        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
            sqLiteDatabase = null;
        }

    }

    protected void release() {
        destroy();
        closeDB();
        dataBaseHelper = null;
    }
}
