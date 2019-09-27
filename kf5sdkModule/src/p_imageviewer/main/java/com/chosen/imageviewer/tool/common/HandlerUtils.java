package com.chosen.imageviewer.tool.common;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * @author Chosen
 * @create 2018/12/17 11:36
 * @email 812219713@qq.com
 */
public final class HandlerUtils {

    private HandlerUtils() {
        throw new UnsupportedOperationException("no instance!");
    }

    public static class HandlerHolder extends Handler {

        private WeakReference<Callback> mListenerWeakReference;


        /**
         * 使用必读：推荐在Activity或者Activity内部持有类中实现该接口，不要使用匿名类，可能会被GC
         *
         * @param callback
         */
        public HandlerHolder(Callback callback) {
            mListenerWeakReference = new WeakReference<>(callback);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mListenerWeakReference != null && mListenerWeakReference.get() != null) {
                mListenerWeakReference.get().handleMessage(msg);
            }
        }
    }
}
