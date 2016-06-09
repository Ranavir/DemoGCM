package com.stl.demogcm.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.stl.demogcm.helper.LruBitmapCache;

/**
 * To maintain volley core objects and request queue and to make them global
 * with this singleton class which extends Application object
 * Also in AndroidManifest.xml added this singleton class in <application> tag
 * using android:name property to execute the class automatically whenever app launches.
 * Also add INTERNET permission as we are going to make network calls.
 *
 * Created by Ranvir on 4/2/2016.
 *
 */
public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println(TAG + " ### onCreate");
        mInstance = this;
        initSingletons();
    }
    protected void initSingletons()
    {
        // Initialize the instance of GCMReceiveObservable singleton class
    }
    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        System.out.println(TAG+" ### getRequestQueue");
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        System.out.println(TAG+" ### getImageLoader");
        getRequestQueue();
        if (mImageLoader == null) {
            System.out.println(TAG+" ### mImageLoader instantiated with new LruBitmapCache");
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        System.out.println(TAG+" ### addToRequestQueue");
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        System.out.println(TAG+" ### addToRequestQueue");
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        System.out.println(TAG+" ### cancelPendingRequests");
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}