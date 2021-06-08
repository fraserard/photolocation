package ca.nait.fard1.photolocation.Service;

import android.graphics.Bitmap;
import androidx.collection.LruCache;

/**
 *  Stores, gets, and releases images from cache.
 */
public class MemoryCache {

    private static LruCache<String, Bitmap> memoryCache;

    //Singleton
    private static MemoryCache instance;
    public static synchronized MemoryCache getInstance() {
        if (instance == null) {
            instance = new MemoryCache();
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 2;

            memoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getByteCount() / 1024;
                }
            };
        }
        return instance;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return memoryCache.get(key);
    }

    public Bitmap removeBitmapFromMemCache(String key){
        return memoryCache.remove(key);
    }

}