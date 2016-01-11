package app.wafaa.popularmovies.Volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;



public class VolleySingleton {

    private static VolleySingleton mVolleyInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mContext;

    private VolleySingleton(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();

        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }


    public static synchronized VolleySingleton getInstance(Context context) {
        if (mVolleyInstance == null) {
            mVolleyInstance = new VolleySingleton(context);
        }
        return mVolleyInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // use the application context
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <t> void addToRequestQueue(Request<t> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
