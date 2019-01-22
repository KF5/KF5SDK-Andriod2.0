package com.kf5.sdk.system.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * author:chosen
 * date:2016/10/19 11:52
 * email:812219713@qq.com
 */

public class ToastUtil {

    private static String oldMsg;
    protected static Toast toast   = null;
    private static long oneTime=0;
    private static long twoTime=0;

    public static void showToast(Context context, String s){
        if(toast==null){
            toast =Toast.makeText(context, s, Toast.LENGTH_SHORT);
            toast.show();
            oneTime=System.currentTimeMillis();
        }else{
            twoTime=System.currentTimeMillis();
            if(TextUtils.equals(s , oldMsg)){
                if(twoTime-oneTime>Toast.LENGTH_SHORT)
                    toast.show();
            }else{
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime=twoTime;
    }

}
