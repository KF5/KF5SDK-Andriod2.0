package com.kf5.sdk.im.db;

/**
 * author:chosen
 * date:2016/11/3 15:43
 * email:812219713@qq.com
 */

class DataBaseColumn {

    //主key id.
    static final String ID = "id";

    //消息id
    static final String MESSAGE_ID = "message_id";

    //消息类型
    static final String MESSAGE_TYPE = "message_type";

    //角色
    static final String ROLE = "role";

    //会话id
    static final String CHAT_ID = "chat_id";

    //是否已读
    static final String IS_READ = "is_read";

    //发送状态 -1 发送失败，0发送成功，1发送中(默认为0,接收的消息)
    static final String SEND_STATUS = "state";

    //创建时间
    static final String CREATED_DATE = "server_time";

    //下载路径
    static final String FILE_URL = "url";

    //本地路径
    static final String FILE_NAME = "file_name";

    //消息内容
    static final String MESSAGE = "message";

    //文件类型
    static final String FILE_TYPE = "file_type";

    //消息标记
    static final String MARK = "mark";

    //附件本地路径
    static final String LOCAL_PATH = "local_path";


    static final String USER_ID = "user_id";

    static final String NAME = "name";

    //撤回标记
    static final String RECALLED = "recalled";

    static final String IMG_WIDTH = "img_width";
    static final String IMG_HEIGHT = "img_height";
    static final String VOICE_DURATION = "voice_duration";
}
