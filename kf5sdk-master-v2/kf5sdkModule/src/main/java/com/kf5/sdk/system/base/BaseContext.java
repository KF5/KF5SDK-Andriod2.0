package com.kf5.sdk.system.base;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * author:chosen
 * date:2016/10/20 16:52
 * email:812219713@qq.com
 */

public abstract class BaseContext {

    protected Context context;

    public BaseContext(Context context) {
        this.context = context;
    }

    protected  void showToast(String message){
        if (!TextUtils.isEmpty(message) && context != null)
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}