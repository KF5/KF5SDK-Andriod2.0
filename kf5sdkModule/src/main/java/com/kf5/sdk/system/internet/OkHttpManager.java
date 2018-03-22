package com.kf5.sdk.system.internet;

import com.kf5.sdk.system.utils.SPUtils;
import com.kf5Engine.okhttp.OkHttpClient;
import com.kf5Engine.okhttp.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

/**
 * author:chosen
 * date:2016/10/14 16:48
 * email:812219713@qq.com
 */

class OkHttpManager {

    private OkHttpManager() {

    }

    private static OkHttpClient sOkHttpClient;

    private static OkHttpManager sOkHttpManager;

    public static OkHttpManager getInstance() {
        if (sOkHttpManager == null) {
            synchronized (OkHttpManager.class) {
                if (sOkHttpManager == null) {
                    sOkHttpManager = new OkHttpManager();
                    sOkHttpClient = new OkHttpClient.Builder()
                            .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .retryOnConnectionFailure(true)
                            .addNetworkInterceptor(new HttpInterceptor(SPUtils.getUserAgent()))
//                            .hostnameVerifier(new HostnameVerifier() {
//                                @Override
//                                public boolean verify(String hostname, SSLSession session) {
//                                    return true;
//                                }
//                            })
//                            .sslSocketFactory(TrustSSLContext.getSSLSocketFactory())
                            .build();
                }
            }
        }
        return sOkHttpManager;
    }

    public static void releaseOkHttp() {
        sOkHttpManager = null;
        sOkHttpClient = null;
    }


    public OkHttpClient getOkHttpClient() {
        return sOkHttpClient;
    }

}
