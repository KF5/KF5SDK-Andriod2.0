package com.kf5sdk.exam.app;

import android.app.Application;
import android.os.Debug;

import com.kf5.sdk.system.init.KF5SDKInitializer;
import com.kf5sdk.exam.AboutUsActivity;

/**
 * author:chosen
 * date:2016/10/18 11:24
 * email:812219713@qq.com
 */

public class KF5Application extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        KF5SDKInitializer.init(getApplicationContext());
//        LeakCanary.install(this);
    }
}
