package com.chosen.imageviewer.glide.cache;

import android.util.LruCache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Util;

import java.security.MessageDigest;

/**
 * @author Chosen
 * @create 2018/12/17 11:28
 * @email 812219713@qq.com
 */
public class SafeKeyGenerator {

    private final LruCache<Key, String> loadIdToSafeHash = new LruCache<>(1000);

    public String getSafekey(Key key) {
        String safeKey;
        synchronized (loadIdToSafeHash) {
            safeKey = loadIdToSafeHash.get(key);
        }

        if (safeKey == null) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                key.updateDiskCacheKey(messageDigest);
                safeKey = Util.sha256BytesToHex(messageDigest.digest());
            } catch (Exception e) {
                e.printStackTrace();
            }
            synchronized (loadIdToSafeHash) {
                loadIdToSafeHash.put(key, safeKey);
            }
        }
        return safeKey;
    }
}
