package com.kf5.sdk.im.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.kf5.sdk.im.entity.IMMessage;
import com.kf5.sdk.im.entity.MessageType;
import com.kf5.sdk.im.entity.Status;
import com.kf5.sdk.im.entity.Upload;
import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * author:chosen
 * date:2016/11/3 15:46
 * email:812219713@qq.com
 */

public class IMSQLManager extends SQLManager {

    private static IMSQLManager instance;

    private final static String TAG = "KF5";

    private IMSQLManager(Context context) {
        super(context);
    }

    private static IMSQLManager getInstance(Context context) {
        if (instance == null) {
            synchronized (IMSQLManager.class) {
                if (instance == null)
                    instance = new IMSQLManager(context.getApplicationContext());
            }
        }
        return instance;
    }


    /**
     * 插入数据
     *
     * @param context
     * @param iMMessage
     */
    public static void insertMessage(Context context, IMMessage iMMessage) {

        Log.i(Utils.KF5_TAG, "插入消息");
        if (iMMessage == null) return;
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(DataBaseColumn.CHAT_ID, iMMessage.getChatId());
            contentValues.put(DataBaseColumn.MESSAGE_TYPE, iMMessage.getType());
            contentValues.put(DataBaseColumn.IS_READ, iMMessage.getIsRead());
            contentValues.put(DataBaseColumn.MESSAGE, iMMessage.getMessage());
            contentValues.put(DataBaseColumn.CREATED_DATE, iMMessage.getCreated());
            contentValues.put(DataBaseColumn.MESSAGE_ID, iMMessage.getId());
            contentValues.put(DataBaseColumn.IS_COM, iMMessage.isCom() ? 0 : 1);
            contentValues.put(DataBaseColumn.MARK, iMMessage.getTimeStamp());
            switch (iMMessage.getStatus()) {
                case FAILED:
                    contentValues.put(DataBaseColumn.SEND_STATUS, -1);
                    break;
                case SUCCESS:
                    contentValues.put(DataBaseColumn.SEND_STATUS, 0);
                    break;
                case SENDING:
                    contentValues.put(DataBaseColumn.SEND_STATUS, 1);
                    break;
            }
            Upload upload = iMMessage.getUpload();
            if (upload != null) {
                contentValues.put(DataBaseColumn.FILE_TYPE, upload.getType());
                contentValues.put(DataBaseColumn.FILE_URL, upload.getUrl());
                contentValues.put(DataBaseColumn.FILE_NAME, upload.getName());
                contentValues.put(DataBaseColumn.LOCAL_PATH, upload.getLocalPath());
            }
            getInstance(context).openSqlDB().insert(DataBaseHelper.DB_TABLE, null, contentValues);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            contentValues.clear();
        }

    }


    /**
     * 分页查询历史数据
     *
     * @param context
     * @return
     */
    public static List<IMMessage> getPageMessages(Context context, long count) {
        int limit = 18;
        long offCount = count - limit > 0 ? count - limit : 0;
        String sql = "SELECT * FROM " + DataBaseHelper.DB_TABLE + " ORDER BY " + DataBaseColumn.ID + " ASC LIMIT ? , ?";
        Cursor cursor = getInstance(context).openSqlDB().rawQuery(sql, new String[]{String.valueOf(offCount), String.valueOf(limit)});
        return convertToStorage(cursor);
    }


    /**
     * 更新消息发送状态
     *
     * @param context
     * @param tag
     * @param status
     */
    public static void updateMessageSendStatus(Context context, Status status, String tag) {

        ContentValues contentValues = new ContentValues();
        try {
            switch (status) {
                case FAILED:
                    contentValues.put(DataBaseColumn.SEND_STATUS, -1);
                    break;
                case SUCCESS:
                    contentValues.put(DataBaseColumn.SEND_STATUS, 0);
                    break;
                case SENDING:
                    contentValues.put(DataBaseColumn.SEND_STATUS, 1);
                    break;
            }
            getInstance(context).openSqlDB().update(DataBaseHelper.DB_TABLE, contentValues, DataBaseColumn.MARK + " = ?", new String[]{tag});
        } finally {
            contentValues.clear();
        }

    }

    /**
     * 通过标签将附件的token保存在DataBaseCloum的message字段
     *
     * @param context
     * @param tag
     * @param token
     */
    public static void updateUploadTokenByTag(Context context, String tag, String token) {

        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(DataBaseColumn.MESSAGE, token);
            getInstance(context).openSqlDB().update(DataBaseHelper.DB_TABLE, contentValues, DataBaseColumn.MARK + " = ?", new String[]{tag});
        } finally {
            contentValues.clear();
        }

    }


    /**
     * 根据timeStamp本地路径
     *
     * @param context
     * @param localPath
     * @param timeStamp
     */
    public static void updateLocalPathByTimeStamp(Context context, String localPath, String timeStamp) {
        if (!TextUtils.isEmpty(localPath)) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DataBaseColumn.LOCAL_PATH, localPath);
            getInstance(context).openSqlDB().update(DataBaseHelper.DB_TABLE, contentValues, DataBaseColumn.MARK + " = ?", new String[]{timeStamp});
        }
    }


    /**
     * 根据唯一标识删除某条消息
     *
     * @param context
     * @param timeStamp
     */
    public static void deleteMessageByTimeStamp(Context context, String timeStamp) {
        try {
            getInstance(context).openSqlDB().delete(DataBaseHelper.DB_TABLE, DataBaseColumn.MARK + " = ?", new String[]{timeStamp});
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /**
     * 通过创建时间与标签删除某一时间栏目
     *
     * @param context
     * @param tag
     * @param created
     */
    public static void deleteDateByTypeAndCreated(Context context, String tag, long created) {

        try {
            getInstance(context).openSqlDB().delete(DataBaseHelper.DB_TABLE, DataBaseColumn.MARK + " = ? and " + DataBaseColumn.CREATED_DATE + " = ?", new String[]{tag, created + ""});
        } catch (Exception e) {
            // TODO: handle exception
        }
    }


    /**
     * 删除某个消息类型
     *
     * @param context
     */
    public static void deleteMessageByMsgType(Context context) {

        try {
            getInstance(context).openSqlDB().delete(DataBaseHelper.DB_TABLE, DataBaseColumn.FILE_TYPE + " = ?", new String[]{Field.AMR});
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    /**
     * 通过标签删除旧的消息
     *
     * @param context
     * @param tag
     */
    public static void deleteMessageByTag(Context context, String tag) {

        try {
            getInstance(context).openSqlDB().delete(DataBaseHelper.DB_TABLE, DataBaseColumn.MARK + " = ?", new String[]{tag});
        } catch (Exception e) {
            // TODO: handle exception
        }

    }


    /**
     * 删除发送失败的某条消息，通过内容与标签删除
     *
     * @param context
     * @param content
     * @param mark
     */
    public static void deleteMessageByMarkAndContent(Context context, String content, String mark) {

        try {
            getInstance(context).openSqlDB().delete(DataBaseHelper.DB_TABLE, DataBaseColumn.MARK + " = ? and " + DataBaseColumn.MESSAGE + " = ?", new String[]{mark, content});
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }


    /**
     * 更新消息储存状态
     *
     * @param context
     * @param iMMessage
     * @param tag
     */

    public static void updateMessageByTag(Context context, IMMessage iMMessage, String tag) {

        ContentValues contentValues = new ContentValues();
        try {
            switch (iMMessage.getStatus()) {
                case FAILED:
                    contentValues.put(DataBaseColumn.SEND_STATUS, -1);
                    break;
                case SENDING:
                    contentValues.put(DataBaseColumn.SEND_STATUS, 1);
                    break;
                case SUCCESS:
                    contentValues.put(DataBaseColumn.SEND_STATUS, 0);
                    contentValues.put(DataBaseColumn.CHAT_ID, iMMessage.getChatId());
                    contentValues.put(DataBaseColumn.MESSAGE_ID, iMMessage.getId());
                    contentValues.put(DataBaseColumn.MARK, iMMessage.getTimeStamp());
                    contentValues.put(DataBaseColumn.CREATED_DATE, iMMessage.getCreated());
                    if (iMMessage.getUploadId() > 0) {
                        Upload upload = iMMessage.getUpload();
                        if (upload != null) {
                            contentValues.put(DataBaseColumn.FILE_TYPE, upload.getType());
                            contentValues.put(DataBaseColumn.FILE_URL, upload.getUrl());
                        }
                    }
                    break;
            }
            getInstance(context).openSqlDB().update(DataBaseHelper.DB_TABLE, contentValues, DataBaseColumn.MARK + " = ?", new String[]{tag});
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            contentValues.clear();
        }

    }


    /**
     * 分页ͨ查询历史数据内容
     *
     * @param cursor
     * @return
     */
    private static List<IMMessage> convertToStorage(Cursor cursor) {

        List<IMMessage> list = new ArrayList<>();
        if (cursor == null)
            return list;
        int count = cursor.getCount();
        if (count == 0 || !cursor.moveToFirst()) {
            return list;
        }
        try {
            for (int i = 0; i < count; i++) {
                IMMessage iMMessage = new IMMessage();
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseColumn.MESSAGE_ID));
                iMMessage.setId(id);
                iMMessage.setChatId(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseColumn.CHAT_ID)));
                iMMessage.setCreated(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseColumn.CREATED_DATE)));
                iMMessage.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseColumn.MESSAGE)));
                iMMessage.setIsRead(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseColumn.IS_READ)));
                iMMessage.setCom(cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseColumn.IS_COM)) == 0);
                iMMessage.setTimeStamp(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseColumn.MARK)));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseColumn.MESSAGE_TYPE));
                iMMessage.setType(type);
                int status = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseColumn.SEND_STATUS));
                switch (status) {
                    case -1:
                        iMMessage.setStatus(Status.FAILED);
                        break;
                    case 0:
                        iMMessage.setStatus(Status.SUCCESS);
                        break;
                    case 1:
                        iMMessage.setStatus(Status.SENDING);
                        break;
                }
                if (TextUtils.equals(Field.CHAT_UPLOAD, type)) {
                    Upload upload = new Upload();
                    String fileType = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseColumn.FILE_TYPE));
                    upload.setType(fileType);
                    upload.setUrl(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseColumn.FILE_URL)));
                    upload.setName(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseColumn.FILE_NAME)));
                    upload.setLocalPath(cursor.getString(cursor.getColumnIndexOrThrow(DataBaseColumn.LOCAL_PATH)));
                    iMMessage.setUpload(upload);
                    if (TextUtils.equals(Field.AMR, fileType))
                        iMMessage.setMessageType(MessageType.VOICE);
                    else if (Utils.isImage(fileType))
                        iMMessage.setMessageType(MessageType.IMAGE);
                    else
                        iMMessage.setMessageType(MessageType.FILE);
                } else if (TextUtils.equals(Field.CHAT_SYSTEM, type)) {
                    iMMessage.setMessageType(MessageType.SYSTEM);
                } else {
                    iMMessage.setMessageType(MessageType.TEXT);
                }
                list.add(iMMessage);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 重新排序
     *
     * @param list 数据集合
     * @return
     */
    private static List<IMMessage> sortList(List<IMMessage> list) {

        List<IMMessage> imMessageList = new ArrayList<>();
        imMessageList.addAll(list);
        list.clear();
        for (int i = imMessageList.size() - 1; i >= 0; i--) {
            list.add(imMessageList.get(i));
        }
        return list;
    }

    /**
     * 获取最后一条消息的时间
     *
     * @param context
     * @return
     */
    public static long getLastMessageTime(Context context) {

        try {
            String sql = "select * from " + DataBaseHelper.DB_TABLE + " order by " + DataBaseColumn.ID + " DESC limit 1";
            Cursor cursor = getInstance(context).openSqlDB().rawQuery(sql, null);
            if (cursor == null)
                return 0;
            int count = cursor.getCount();
            if (count == 0 || !cursor.moveToFirst())
                return 0;
            long time = cursor.getLong(cursor.getColumnIndexOrThrow(DataBaseColumn.CREATED_DATE));
            cursor.close();
            return time;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;
    }

    /**
     * 是否包含当前消息
     *
     * @param context
     * @param tag
     * @return
     */
    public static boolean isContainThisMessageByTag(Context context, String tag) {

        String sql = "select * from " + DataBaseHelper.DB_TABLE + " where " + DataBaseColumn.MARK + "=" + tag;
        try {
            Cursor cursor = getInstance(context).openSqlDB().rawQuery(sql, null);
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
        }
        return false;
    }

    /**
     * 获取消息的总条数
     *
     * @param context
     * @return
     */
    public static long getMessageCount(Context context) {
        String sql = "SELECT count(*) from " + DataBaseHelper.DB_TABLE;
        Cursor cursor = getInstance(context).openSqlDB().rawQuery(sql, null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        return count;
    }


    /**
     * 获取最后一条消息message_id
     *
     * @param context
     * @return
     */
    public static int getLastMessageId(Context context) {

        int id = 0;
        try {
            String sql = "select " + DataBaseColumn.MESSAGE_ID + " from " + DataBaseHelper.DB_TABLE + " order by " + DataBaseColumn.MESSAGE_ID + " DESC limit 1";
            Cursor cursor = getInstance(context).openSqlDB().rawQuery(sql, null);
            if (cursor == null)
                return 0;
            int count = cursor.getCount();
            if (count == 0 || !cursor.moveToFirst())
                return 0;
            id = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseColumn.MESSAGE_ID));
            cursor.close();
        } catch (Exception e) {
        }
        return id;
    }


    @Override
    protected void release() {
        // TODO Auto-generated method stub
        super.release();
        instance = null;
    }

    public static void reset(Context context) {
        getInstance(context).release();

    }

}