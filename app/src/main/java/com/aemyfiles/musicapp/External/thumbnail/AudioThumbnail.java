package com.aemyfiles.musicapp.External.thumbnail;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;

public class AudioThumbnail {
    private int mThumbSize = 320;
    private String mPath;

    public AudioThumbnail(String path, int size) {
        mPath = path;
        mThumbSize = size;
    }

    public Bitmap _createThumbnail() {
        Bitmap retBmp = null;
        try (MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever()) {
            File file = new File(mPath);
            if (file.exists() && file.length() > 0) {
                mediaMetadataRetriever.setDataSource(mPath);
                byte[] embedPic = mediaMetadataRetriever.getEmbeddedPicture();
                if (embedPic != null) {
                    BitmapFactory.Options options = getBmpFactoryOption(mThumbSize);
                    retBmp = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length, options);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap ret = null;
        try {
            if (retBmp != null && (retBmp.getWidth() != mThumbSize || retBmp.getHeight() != mThumbSize)) {
                ret = getSizedCenterCropBmp(retBmp, mThumbSize);
                retBmp.recycle();
            } else {
                ret = retBmp;
            }
        } catch (OutOfMemoryError e) {
            Log.e("AudioThumbnail", "OutOfMemoryError:" + e.toString());
        } catch (IllegalArgumentException e) {
            Log.e("AudioThumbnail", "IllegalArgumentException:" + e.toString());
        }
        return ret;
    }

    private Bitmap getSizedCenterCropBmp(Bitmap bmpSrc, int nSize) {
        Bitmap croppedBitmap = null;

        if (bmpSrc != null) {
            try {
                int width = bmpSrc.getWidth();
                int height = bmpSrc.getHeight();
                int x, y;
                int sourceSize;
                if (width > height) {
                    x = (width / 2) - (height / 2);
                    y = 0;
                    sourceSize = height;
                } else {
                    x = 0;
                    y = (height / 2) - (width / 2);
                    sourceSize = width;
                }
                Rect srcRect = new Rect(x, y, x + sourceSize, y + sourceSize);
                Rect dstRect = new Rect(0, 0, nSize, nSize);

                Bitmap bitmap = Bitmap.createBitmap(nSize, nSize, getConfig(bmpSrc));

                Paint paint = new Paint();
                paint.setFilterBitmap(true);

                Canvas canvas = new Canvas();
                canvas.setBitmap(bitmap);
                canvas.drawBitmap(bmpSrc, srcRect, dstRect, paint);
                canvas.setBitmap(null);
                croppedBitmap = bitmap;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        return croppedBitmap;
    }

    @NonNull
    private static Bitmap.Config getConfig(Bitmap bmpSrc) {
        Bitmap.Config newConfig = Bitmap.Config.ARGB_8888;
        final Bitmap.Config config = bmpSrc.getConfig();
        // GIF files generate null configs, assume ARGB_8888
        if (config != null) {
            switch (config) {
                case RGB_565:
                    newConfig = Bitmap.Config.RGB_565;
                    break;
                case ALPHA_8:
                    newConfig = Bitmap.Config.ALPHA_8;
                    break;
            }
        }
        return newConfig;
    }

    private BitmapFactory.Options getBmpFactoryOption(int thumbnailSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.outHeight = thumbnailSize;
        options.outWidth = thumbnailSize;
//        options.semIsPreview = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        return options;
    }
}
