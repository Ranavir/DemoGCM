package com.stl.demogcm.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by office on 4/10/2016.
 */
public class Utils {
    /**
     * @param context
     * @return
     */
    public static boolean checkInternet(Context context) {
        // check Internet connection is available
        ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable() && netInfo.isConnected()) {
            return true;
        } else {
            System.out.println("Internet Connection Not Present");
            return false;
        }
    }
}
