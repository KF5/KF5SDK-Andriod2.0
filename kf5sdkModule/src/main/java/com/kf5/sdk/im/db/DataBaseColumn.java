package com.kf5.sdk.im.db;

/**
 * author:chosen
 * date:2016/11/3 15:43
 * email:812219713@qq.com
 */

public class DataBaseColumn {

    //主key id.
    public static final String ID = "id";

    //消息id
    public static final String MESSAGE_ID = "message_id";

    //消息类型
    public static final String MESSAGE_TYPE = "message_type";

    //角色
    public static final String ROLE = "role";

    //会话id
    public static final String CHAT_ID = "chat_id";

    //是否已读
    public static final String IS_READ = "is_read";

    //发送状态 -1 发送失败，0发送成功，1发送中(默认为0,接收的消息)
    public static final String SEND_STATUS = "state";

    //创建时间
    public static final String CREATED_DATE = "server_time";

    //下载路径
    public static final String FILE_URL = "url";

    //本地路径
    public static final String FILE_NAME = "file_name";

    //消息内容
    public static final String MESSAGE = "message";

    //文件类型
    public static final String FILE_TYPE = "file_type";

    //消息标记
    public static final String MARK = "mark";

    //附件本地路径
    public static final String LOCAL_PATH = "local_path";


    public static final String USER_ID = "user_id";

    public static final String NAME = "name";

    //撤回标记
    public static final String RECALLED = "recalled";
}
