package com.kf5.sdk.system.internet;

import com.kf5.sdk.im.api.FileDownLoadCallBack;
import com.kf5Engine.okhttp.Call;
import com.kf5Engine.okhttp.Callback;
import com.kf5Engine.okhttp.Request;
import com.kf5Engine.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * author:chosen
 * date:2016/11/18 18:27
 * email:812219713@qq.com
 */

public class DownLoadManager {

    private static DownLoadManager sDownLoadManager;

    private DownLoadManager() {
    }

    public static DownLoadManager getInstance() {
        if (sDownLoadManager == null) {
            synchronized (DownLoadManager.class) {
                if (sDownLoadManager == null)
                    sDownLoadManager = new DownLoadManager();
            }
        }

        return sDownLoadManager;
    }


    public void downloadFile(final String url, final String parent, final String fileName, final FileDownLoadCallBack callBack) {

        final Request request = new Request.Builder().url(url).build();
        OkHttpManager.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callBack != null)
                    callBack.onResult(FileDownLoadCallBack.Status.FAILED, e.getMessage(), fileName);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(parent, fileName);
                    if (!file.exists())
                        file.getParentFile().mkdirs();
                    file.createNewFile();
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    if (callBack != null)
                        callBack.onResult(FileDownLoadCallBack.Status.SUCCESS, "下载成功",fileName);
                } catch (Exception e) {
                    e.printStackTrace();
                    if (callBack != null)
                        callBack.onResult(FileDownLoadCallBack.Status.FAILED, e.getMessage(),fileName);
                } finally {
                    try {
                        if (is != null) is.close();
                        if (fos != null) fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (callBack != null)
                            callBack.onResult(FileDownLoadCallBack.Status.FAILED, e.getMessage(),fileName);
                    }
                }
            }
        });
    }


}
