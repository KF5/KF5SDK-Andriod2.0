package com.kf5.sdk.system.internet;


import android.support.v4.util.ArrayMap;

import com.kf5.sdk.system.entity.Field;
import com.kf5.sdk.system.utils.SPUtils;
import com.kf5Engine.okhttp.Call;
import com.kf5Engine.okhttp.Callback;
import com.kf5Engine.okhttp.FormBody;
import com.kf5Engine.okhttp.Headers;
import com.kf5Engine.okhttp.MediaType;
import com.kf5Engine.okhttp.MultipartBody;
import com.kf5Engine.okhttp.Request;
import com.kf5Engine.okhttp.RequestBody;
import com.kf5Engine.okhttp.Response;
import com.kf5Engine.system.HeaderUtils;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * author:chosen
 * date:2016/10/14 17:34
 * email:812219713@qq.com
 */

public abstract class BaseHttpManager {

    /**
     * POST请求
     *
     * @param url
     * @param map
     * @param callBack
     */
    protected void sendPostRequest(String url, Map<String, String> map, HttpRequestCallBack callBack) {
        map.put(Field.USERTOKEN, SPUtils.getUserToken());
        Param[] paramsArr = map2Params(map);
        Request request = buildPostRequest(url, paramsArr, map);
        deliveryResult(request, callBack);
    }

    /**
     * GET请求
     *
     * @param url
     * @param callBack
     */
    protected void sendGetRequest(String url, Map<String, String> map, HttpRequestCallBack callBack) {
        Request.Builder request = new Request.Builder();
        addHeader(request);
        deliveryResult(request.url(url).build(), callBack);
    }

    /**
     * 上传附件
     *
     * @param url      路径
     * @param map      body参数
     * @param fileList 文件列表
     * @param callBack 回调
     */
    protected void upload(String url, Map<String, String> map, List<File> fileList, HttpRequestCallBack callBack) {
        map.put(Field.USERTOKEN, SPUtils.getUserToken());
        Param[] params = map2Params(map);
        params = validateParam(params);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        RequestBody fileBody;
        for (File file : fileList) {
            String fileName = file.getName();
            fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name= upload_" + fileList.indexOf(file)
                            + "; filename=\"" + fileName + "\""),
                    fileBody);
        }
        RequestBody requestBody = builder.build();
        Request.Builder request = new Request.Builder();
        addHeader(request);
        deliveryResult(request.url(url).post(requestBody).build(), callBack);

    }

    /**
     * IM模块上传附件接口
     *
     * @param url
     * @param map
     * @param fileList
     * @param callBack
     */
    protected void uploadIMAttachment(String url, Map<String, String> map, List<File> fileList, HttpRequestCallBack callBack) {
        map.put(Field.USERTOKEN, SPUtils.getUserToken());
        Param[] params = map2Params(map);
        params = validateParam(params);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        RequestBody fileBody;
        for (File file : fileList) {
            String fileName = file.getName();
            fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
            builder.addPart(Headers.of("Content-Disposition",
                    "form-data; name= \"filename\"; filename=\"" + fileName + "\""),
                    fileBody);
        }
        RequestBody requestBody = builder.build();
        Request.Builder request = new Request.Builder();
        addHeader(request);
        deliveryResult(request.url(url).post(requestBody).build(), callBack);
    }


    /**
     * 拆分数据
     *
     * @param map
     * @return
     */
    private Param[] map2Params(Map<String, String> map) {
        if (map == null)
            map = new ArrayMap<>();
        int size = map.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = map.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    /**
     * 建立必要model
     *
     * @param url
     * @param params
     * @return
     */
    private Request buildPostRequest(String url, Param[] params, Map<String, String> map) {
        if (params == null)
            params = new Param[0];
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        Request.Builder request = new Request.Builder();
        addHeader(request);
        return request.url(url).post(builder.build()).build();
    }


    private void deliveryResult(final Request request, final HttpRequestCallBack callBack) {
        OkHttpManager.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callBack != null)
                    callBack.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callBack != null)
                    callBack.onSuccess(response.body().string());
            }
        });
    }

    /**
     * 初始化参数
     *
     * @param params
     * @return
     */
    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private void addHeader(Request.Builder request) {
        Map<String, String> map = HeaderUtils.getHeaderMap(SPUtils.getAppid());
        Iterator<String> iterator = map.keySet().iterator();
        int i = 1;
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (i == 1) {
                request.header(key, value);
            } else {
                request.addHeader(key, value);
            }
            i++;
        }
    }

}
