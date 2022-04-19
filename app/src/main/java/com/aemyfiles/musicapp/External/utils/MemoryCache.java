package com.aemyfiles.musicapp.External.utils;

import android.util.LruCache;

public class MemoryCache {

    private static final int CACHE_CNT = 64;
    private static final LruCache<Object, Object> sCache = new LruCache<>(CACHE_CNT);
    private static MemoryCache mInstance = null;


    private MemoryCache() {
    }

    public static synchronized MemoryCache getInstance() {
        if (mInstance == null) {
            mInstance = new MemoryCache();
        }
        return mInstance;
    }

    public void addCache(int id, Object bmp) {
        sCache.put(id, bmp);
    }

    public void clearCache() {
        sCache.evictAll();
    }

    public Object getCache(int id) {
        return sCache.get(id);
    }
}
