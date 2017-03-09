package com.kf5.sdk.ticket.api;

import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.internet.BaseHttpManager;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5Engine.api.KF5API;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * author:chosen
 * date:2016/10/14 18:35
 * email:812219713@qq.com
 * 工单模块相关API
 */

public final class TicketAPI extends BaseHttpManager {

    private TicketAPI() {

    }

    private static TicketAPI mTicketAPI;

    public static TicketAPI getInstance() {
        if (mTicketAPI == null) {
            synchronized (TicketAPI.class) {
                if (mTicketAPI == null)
                    mTicketAPI = new TicketAPI();
            }
        }

        return mTicketAPI;
    }

    /**
     * 获取工单列表
     *
     * @param queryMap 追加参数
     * @param callBack 请求回调
     */
    public void getTicketList(Map<String, String> queryMap, HttpRequestCallBack callBack) {
        queryMap.put(Field.USERTOKEN, SPUtils.getUserToken());
        sendGetRequest(KF5API.getTicketList(SPUtils.getHelpAddress(), queryMap), queryMap, callBack);
    }

    /**
     * 获取工单内容
     *
     * @param queryMap 追加参数
     * @param callBack 请求回调
     */
    public void getTicketDetail(Map<String, String> queryMap, HttpRequestCallBack callBack) {
        queryMap.put(Field.USERTOKEN, SPUtils.getUserToken());
        sendGetRequest(KF5API.getTicketDetail(SPUtils.getHelpAddress(), queryMap), queryMap, callBack);
    }


    /**
     * 更新工单
     *
     * @param fieldMap 更新工单的内容
     * @param callBack 请求回调
     */
    public void updateTicket(Map<String, String> fieldMap, HttpRequestCallBack callBack) {

        sendPostRequest(KF5API.updateTicket(SPUtils.getHelpAddress()), fieldMap, callBack);
    }

    /**
     * 查看工单详细信息（自定义字段的内容）
     *
     * @param queryMap 追加参数
     * @param callBack 请求回调
     */
    public void getTicketAttribute(Map<String, String> queryMap, HttpRequestCallBack callBack) {
        queryMap.put(Field.USERTOKEN, SPUtils.getUserToken());
        sendGetRequest(KF5API.getTicketAttribute(SPUtils.getHelpAddress(), queryMap), queryMap, callBack);
    }

    /**
     * 创建工单
     *
     * @param fieldMap 工单内容
     * @param callBack 请求回调
     */
    public void createTicket(Map<String, String> fieldMap, HttpRequestCallBack callBack) {
        sendPostRequest(KF5API.createTicket(SPUtils.getHelpAddress()), fieldMap, callBack);
    }

    /**
     * 上传附件
     *
     * @param filedMap body参数
     * @param fileList 文件集合
     * @param callBack 请求回调
     */
    public void uploadAttachment(Map<String, String> filedMap, List<File> fileList, HttpRequestCallBack callBack) {
        upload(KF5API.uploadTicketAttachment(SPUtils.getHelpAddress()), filedMap, fileList, callBack);
    }


    /**
     * 满意度评价
     *
     * @param fieldMap 评价参数
     * @param callBack 请求回调
     */
    public void rating(Map<String, String> fieldMap, HttpRequestCallBack callBack) {
        sendPostRequest(KF5API.rating(SPUtils.getHelpAddress()), fieldMap, callBack);
    }

}
