package com.kf5.sdk.system.internet;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * author:chosen
 * date:2016/11/16 10:30
 * email:812219713@qq.com
 */

class TrustSSLContext {

    private static SSLContext mSSLContext = null;

    public static SSLSocketFactory getSSLSocketFactory() {

        try {
            mSSLContext = SSLContext.getInstance("TLS");
            mSSLContext.init(null, new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }
            }, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mSSLContext.getSocketFactory();
    }

}
