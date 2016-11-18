package com.kf5.sdk.system.internet;

import com.kf5Engine.okhttp.Interceptor;
import com.kf5Engine.okhttp.Request;
import com.kf5Engine.okhttp.Response;

import java.io.IOException;

/**
 * author:chosen
 * date:2016/10/14 17:26
 * email:812219713@qq.com
 */

final class HttpInterceptor implements Interceptor {

    private final String userAgent;

    HttpInterceptor(String userAgent) {
        this.userAgent = userAgent;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request compressRequest = originalRequest.newBuilder()
                .addHeader("User-Agent", encodeHeadInfo(userAgent))
                .build();
        return chain.proceed(compressRequest);
    }


    private static String encodeHeadInfo(String headInfo) {
        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0, length = headInfo.length(); i < length; i++) {
            char c = headInfo.charAt(i);
            if (c <= '\u001f' || c >= '\u007f') {
                stringBuffer.append(String.format("\\u%04x", (int) c));
            } else {
                stringBuffer.append(c);
            }
        }
        return stringBuffer.toString();
    }


}
