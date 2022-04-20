package com.aemyfiles.musicapp.External.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.widget.ImageView;

public class ThumbnailManager {
    private static final int MAX_THREAD = Runtime.getRuntime().availableProcessors() / 2;
    private static final String THUMBNAIL_THREAD_NAME = "thumbnail_thread";
    private int mCurThreadIndex = 0;
    private static volatile ThumbnailManager sInstance;
    private ThumbnailHandler[] mThumbnailHandler;
    private UiUpdateHandler mUpdateHandler = new UiUpdateHandler();

    public ThumbnailManager() {
        HandlerThread[] mThumbnailThread = new HandlerThread[MAX_THREAD];
        mThumbnailHandler = new ThumbnailHandler[MAX_THREAD];

        for (int i = 0; i < MAX_THREAD; i++) {
            mThumbnailThread[i] = new HandlerThread(THUMBNAIL_THREAD_NAME + i, Process.THREAD_PRIORITY_BACKGROUND);
            mThumbnailThread[i].start();

            Looper looper = mThumbnailThread[i].getLooper();

            if (looper != null) {
                mThumbnailHandler[i] = new ThumbnailHandler(looper);
            }
        }
    }

    public static ThumbnailManager getInstance() {
        if (sInstance == null) {
            synchronized (ThumbnailManager.class) {
                if (sInstance == null) {
                    sInstance = new ThumbnailManager();
                }
            }
        }
        return sInstance;
    }

    public void loadThumbnail(ThumbnailInfo info) {
        Object bmp = MemoryCache.getInstance().getCache(info.mKey);
        if (bmp instanceof Bitmap) {
            if (info.mView != null) info.mView.setImageBitmap((Bitmap) bmp);
            info.mBmp = (Bitmap) bmp;
            if (info.mCallback != null) info.mCallback.onSuccess(info.mKey, info.mPath);
        } else {
            if (bmp instanceof Boolean) {
                if (info.mView != null)
                    info.mView.setImageResource(ItemType.Companion.getIdByItemType(info.mItemType));
            } else {
                mThumbnailHandler[mCurThreadIndex].sendMessageAtFrontOfQueue(mThumbnailHandler[mCurThreadIndex].obtainMessage(0, info));
                mCurThreadIndex++;
                if (mCurThreadIndex >= MAX_THREAD) {
                    mCurThreadIndex = 0;
                }
            }
        }
    }

    public interface ThumbnailCallback {
        void onSuccess(int albumId, String path);
    }

    private final class ThumbnailHandler extends Handler {
        public ThumbnailHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg != null) {
                ThumbnailInfo info = (ThumbnailInfo) msg.obj;
                if (info != null && info.mPath != null) {
                    AudioThumbnail audioThumbnail = new AudioThumbnail(info.mPath, info.mSize);
                    info.mBmp = audioThumbnail._createThumbnail();
                }
                mUpdateHandler.sendMessageAtFrontOfQueue(mUpdateHandler.obtainMessage(0, info));
            }
        }
    }

    private static class UiUpdateHandler extends Handler {
        public void handleMessage(Message msg) {
            if (msg != null) {
                ThumbnailInfo info = (ThumbnailInfo) msg.obj;
                if (info != null) {
                    if (info.mBmp != null) {
                        MemoryCache.getInstance().addCache(info.mKey, info.mBmp);
                        if (info.mView != null) info.mView.setImageBitmap(info.mBmp);
                        if (info.mCallback != null) info.mCallback.onSuccess(info.mKey, info.mPath);
                    } else {
                        MemoryCache.getInstance().addCache(info.mKey, false);
                        if (info.mView != null)
                            info.mView.setImageResource(ItemType.Companion.getIdByItemType(info.mItemType));
                    }
                }
            }
        }
    }

    public static class ThumbnailInfo {
        public int mKey;
        public ImageView mView;
        public String mPath;
        public Bitmap mBmp;
        public int mSize;
        public ItemType mItemType;
        public ThumbnailCallback mCallback;

        public ThumbnailInfo(ImageView view, int key, String path, int size, ItemType itemType) {
            mView = view;
            mPath = path;
            mKey = key;
            mSize = size;
            mItemType = itemType;
        }

        public ThumbnailInfo(int key, String path, int size, ItemType itemType, ThumbnailCallback callback) {
            mPath = path;
            mKey = key;
            mSize = size;
            mItemType = itemType;
            mCallback = callback;
        }
    }
}
