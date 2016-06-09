package com.stl.demogcm.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by office on 04/10/2016.
 */
public class RegistrationHelper {
    SharedPreferences mPref; // Shared Preferences reference
    SharedPreferences.Editor mEditor; // Editor reference for Shared preferences
    Context mContext; // Context

    private static final String PREFERENCE_NAME = "gcm_pref_file"; // filename

    // All Shared Preferences Keys
    private static final String IS_DEVICE_REGISTERED = "false";

    public static final String KEY_REG_ID = "RegistrationId"; // Reg id
    public static final String KEY_UNREG_ID = "UnRegistrationId";// Un Reg Id
    public static final String KEY_ERROR_ID = "ErrorId";// error
    final String ENTRY_TAG = "ENTRY--->"+this.getClass().getSimpleName() ;
    final String EXIT_TAG = "EXIT--->"+this.getClass().getSimpleName() ;
    /**
     * @param context
     */
    public RegistrationHelper(Context context) {
        Log.i(ENTRY_TAG, "RegistrationHelper");

        this.mContext = context;
        mPref = mContext.getSharedPreferences(PREFERENCE_NAME, mContext.MODE_PRIVATE);
        mEditor = mPref.edit();
        mEditor.commit();

        Log.i(EXIT_TAG, "RegistrationHelper");
    }
    public void registerDeviceLocal(String regid, String unregid,String error) {
        Log.i(ENTRY_TAG, "registerDeviceLocal");

        System.out.println("************** " + regid + "******************** " + unregid + "***************** " + error);
        // Storing login value as TRUE
        mEditor.putBoolean(IS_DEVICE_REGISTERED, true);

        // Storing regid,unregid,error if any in pref file
        mEditor.putString(KEY_REG_ID, regid);
        mEditor.putString(KEY_UNREG_ID, unregid);
        mEditor.putString(KEY_ERROR_ID, error);


        // commit changes
        mEditor.commit();
        Log.i(EXIT_TAG, "registerDeviceLocal");
    }
    /**
     * Getting stored session data
     *
     * @return
     */
    public HashMap<String, String> getDeviceDetails() {

        System.out.println("Inside Hashmap ++++++++++++++++++++++++++");
        // Use hashmap to store user credentials
        HashMap<String, String> device = new HashMap<String, String>();

        device.put(KEY_REG_ID, mPref.getString(KEY_REG_ID, null)); // loyalty id
        device.put(KEY_UNREG_ID, mPref.getString(KEY_UNREG_ID, null));
        device.put(KEY_ERROR_ID, mPref.getString(KEY_ERROR_ID, null));


        return device;
    }
    public boolean isDeviceRegistered() {
        boolean boolFlag = false ;
        Log.i(ENTRY_TAG, "isDeviceRegistered");

        boolFlag = mPref.getBoolean(IS_DEVICE_REGISTERED, false) ;
        System.out.println("boolRegistrationFlag--->" + boolFlag);
        Log.i(EXIT_TAG, "isDeviceRegistered");
        return boolFlag;

    }
    /**
     * logout user
     */
    public void unRegisterDevice() {

        // delete data from preferences
        mEditor.clear();
        mEditor.commit();

    }
}
