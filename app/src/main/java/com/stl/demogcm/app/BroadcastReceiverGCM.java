package com.stl.demogcm.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import com.stl.demogcm.R;

/**
 * Created by office on 4/10/2016.
 */
public class BroadcastReceiverGCM extends BroadcastReceiver {
    final String ENTRY_TAG = "ENTRY--->"+this.getClass().getSimpleName()+" " ;
    final String EXIT_TAG = "EXIT--->"+this.getClass().getSimpleName()+" " ;
    String mRegId,mUnregId,mError;
    String data ;
    RegistrationHelper mRegistrationHelper ;
    Notification notification ;
    Notification.Builder builder ;
    NotificationManager notificationManager ;
    Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println(ENTRY_TAG+"onReceive");
        mContext = context;
        try{
            String action = intent.getAction();
            if(action.equals("com.google.android.c2dm.intent.REGISTRATION")){
                System.out.println(ENTRY_TAG+"action::"+action);

                mRegId = intent.getStringExtra("registration_id");
                mError = intent.getStringExtra("error");
                mUnregId = intent.getStringExtra("unregistered");

                if(null == mRegId) {
                    mRegId = "" ;
                }else if(null == mUnregId) {
                    mUnregId = "" ;
                }else if(null == mError) {
                    mError = "" ;
                }
                if(null != mError){
                    getErrorValue(mError);
                }
                // Save the Data in Local
                mRegistrationHelper = new RegistrationHelper(context) ;
                mRegistrationHelper.registerDeviceLocal(mRegId, mUnregId, mError);

            }else if(action.equals("com.google.android.c2dm.intent.RECEIVE")){
                System.out.println(ENTRY_TAG+"action::"+action);

                data = intent.getStringExtra("data");
                notifyNow("GCM notification",data);
                //Toast.makeText(context,"Data notified::"+data,Toast.LENGTH_LONG).show();
            }
        }finally {

        }
        System.out.println(EXIT_TAG+"onReceive");
    }
    private void notifyNow(String notificationTitle, String notificationMessage) {
        System.out.println(ENTRY_TAG+"notifyNow");

        notificationManager = (NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
        // prepare intent which is triggered if the
        // notification is selected
        //Intent notificationIntent = new Intent(this,NotificationDataActivity.class);
        //notificationIntent.putExtra("messages",alMessage) ;
        //notificationIntent.putExtra("last_read_time",strLastMessageTime) ;
        // use System.currentTimeMillis() to have a unique ID for the pending intent
        //PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(),notificationIntent, 0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // build notification
        // the addAction re-use the same intent to keep the example short
        notification  = new Notification.Builder(mContext)
                .setContentTitle(notificationTitle)
                .setContentText(notificationMessage)
                .setTicker("new gcm notification")
                //.setNumber(iMsgCount)
                .setSmallIcon(R.drawable.msg_notify)
                .setSound(alarmSound)
                //.setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();

        notificationManager.notify(0, notification);

        System.out.println(EXIT_TAG+"notifyNow");
    }//end of notifyNow
    private void getErrorValue(String error) {
        if(error.equals("SERVICE_NOT_AVAILABLE")){
            mError = "Cann't Read Response 500/503 error" ;
        }else if(error.equals("ACCOUNT_MISSING")){
            mError = "No Google account in Phone" ;
        }else if(error.equals("AUTHENTICATION_FAILED")){
            mError = "Bad password credential" ;
        }else if(error.equals("INVALID_SENDER")){
            mError = "Sender Account not Recognized" ;
        }else if(error.equals("PHONE_REGISTRATION_ERROR")){
            mError = "Phone not supporting" ;
        }else if(error.equals("GCM.INVALID_PARAMETERS")){
            mError = "Request Parameter Error/ Phone GCM not supporting" ;
        }
    }
}
