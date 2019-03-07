package in.epochconsulting.erpnext.mprp.implementation;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by pragnya on 9/3/18.
 */

public class VolleyService {
    private static VolleyService volleyUtils;
    private static Context context;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private VolleyService(Context context) {
        VolleyService.context = context;
        requestQueue = getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private LruCache<String, Bitmap> cache = new LruCache<>(20);

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

    public static synchronized VolleyService getVolleyService(Context context) {
        if (volleyUtils == null) {
            volleyUtils = new VolleyService(context);
        }
        return volleyUtils;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

}
