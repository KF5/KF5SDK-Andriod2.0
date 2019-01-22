package com.kf5.sdk.helpcenter.api;


import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.internet.BaseHttpManager;
import com.kf5.sdk.system.internet.HttpRequestCallBack;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5Engine.api.KF5API;

import java.util.Map;

/**
 * author:chosen
 * date:2016/10/14 17:30
 * email:812219713@qq.com
 * 帮助中心API
 */

public class HelpCenterHttpAPI extends BaseHttpManager {

    private static final String FULL_SEARCH = "full_search";

    private HelpCenterHttpAPI() {

    }

    private static HelpCenterHttpAPI sHelpCenterHttpManager;

    public static HelpCenterHttpAPI getInstance() {
        if (sHelpCenterHttpManager == null) {
            synchronized (HelpCenterHttpAPI.class) {
                if (sHelpCenterHttpManager == null)
                    sHelpCenterHttpManager = new HelpCenterHttpAPI();
            }
        }
        return sHelpCenterHttpManager;
    }

    /**
     * 查看文档分区列表
     *
     * @param queryMap 追加参数
     * @param callBack 请求回调
     */
    public void getCategoriesList(Map<String, String> queryMap, HttpRequestCallBack callBack) {
        queryMap.put(Field.USERTOKEN, SPUtils.getUserToken());
        sendGetRequest(KF5API.getCategoriesList(SPUtils.getHelpAddress(), queryMap), queryMap, callBack);
    }

    /**
     * 获取文档分类列表
     *
     * @param queryMap 追加参数
     * @param callBack 回调接口
     */
    public void getForumsList(Map<String, String> queryMap, HttpRequestCallBack callBack) {
        queryMap.put(Field.USERTOKEN, SPUtils.getUserToken());
        sendGetRequest(KF5API.getForumsList(SPUtils.getHelpAddress(), queryMap), queryMap, callBack);
    }


    /**
     * 获取文档列表
     * GET请求
     *
     * @param queryMap 追加参数
     * @param callBack 请求回调
     */
    public void getPostList(Map<String, String> queryMap, HttpRequestCallBack callBack) {
        queryMap.put(Field.USERTOKEN, SPUtils.getUserToken());
        sendGetRequest(KF5API.getPostList(SPUtils.getHelpAddress(), queryMap), queryMap, callBack);
    }

    /**
     * * 查看文档
     *
     * @param queryMap 追加参数
     * @param callBack 请求回调
     */
    public void viewPost(Map<String, String> queryMap, HttpRequestCallBack callBack) {
        queryMap.put(Field.USERTOKEN, SPUtils.getUserToken());
        sendGetRequest(KF5API.viewPost(SPUtils.getHelpAddress(), queryMap), queryMap, callBack);
    }

    /**
     * 搜索文档
     *
     * @param queryMap 请求参数
     * @param callBack 请求回调
     */
    public void searchPost(Map<String, String> queryMap, HttpRequestCallBack callBack) {
//        queryMap.put(FULL_SEARCH, "1"); //添加当前参数，将支持全文搜索
        queryMap.put(Field.USERTOKEN, SPUtils.getUserToken());
        sendGetRequest(KF5API.searchPost(SPUtils.getHelpAddress(), queryMap), queryMap, callBack);
    }

}
