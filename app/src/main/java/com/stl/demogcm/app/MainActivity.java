package com.stl.demogcm.app;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stl.demogcm.R;
import com.stl.demogcm.helper.Utils;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity{
    RegistrationHelper mRegistrationHelper ;
    final String ENTRY_TAG = "ENTRY--->"+this.getClass().getSimpleName()+" " ;
    final String EXIT_TAG = "EXIT--->"+this.getClass().getSimpleName() +" ";
    Button mBtnRegister,mBtnUnregister ;
    Button mBtnNotification ;
    EditText mEtRegDetails ;

    String mRegId ="" ;
    static final String SENDER_ID = "442925186597" ;//Project number
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(ENTRY_TAG + "onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnRegister = (Button)findViewById(R.id.btn_register);
        mBtnUnregister = (Button)findViewById(R.id.btn_unregister);
        mBtnNotification = (Button)findViewById(R.id.btn_notification);
        mEtRegDetails = (EditText)findViewById(R.id.et_registration_details) ;
        mRegistrationHelper = new RegistrationHelper(this) ;


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.checkInternet(v.getContext())){
                    Intent regIntent = new Intent("com.google.android.c2dm.intent.REGISTER");
                    regIntent.putExtra("app", PendingIntent.getBroadcast(v.getContext(),0,new Intent(),0));
                    regIntent.putExtra("sender", SENDER_ID);
                    startService(regIntent);
                    //get the details from sharedpref and set the value

                    /*if(null != mRegistrationHelper && mRegistrationHelper.isDeviceRegistered()){
                        HashMap<String,String> hm = mRegistrationHelper.getDeviceDetails();
                        mRegId = hm.get("RegistrationId");
                        StringBuilder value = new StringBuilder("RegId::"+(null != hm.get("RegistrationId")?hm.get("RegistrationId"):""));
                        value.append("\n"+"unreg::"+(null != hm.get("UnRegistrationId")?hm.get("UnRegistrationId"):""));
                        value.append("\n"+"error::"+(null != hm.get("ErrorId")?hm.get("ErrorId"):""));
                        mEtRegDetails.setText(value);
                    }*/
                    Toast.makeText(v.getContext(),"Registation done sucessfully...",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Check Internet connectivity!!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        mBtnUnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.checkInternet(v.getContext())) {
                    Intent unregIntent = new Intent("com.google.android.c2dm.intent.UNREGISTER");
                    unregIntent.putExtra("app", PendingIntent.getBroadcast(v.getContext(), 0, new Intent(), 0));
                    startService(unregIntent);
                    if (mRegistrationHelper.isDeviceRegistered()) {
                        mRegistrationHelper.unRegisterDevice();
                    }
                    Toast.makeText(MainActivity.this, "Device successfully Unregistered... ", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this, "Check Internet connectivity!!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        mBtnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != mRegId && "" != mRegId) {
                    System.out.println("mRegId::"+mRegId);
                    //Toast.makeText(MainActivity.this, "RegistrationId::"+mRegId, Toast.LENGTH_LONG).show();
                    Intent notifyIntent = new Intent(MainActivity.this,NotificationActivity.class);
                    notifyIntent.putExtra("regId", mRegId) ;
                    startActivity(notifyIntent);

                }else{
                    Toast.makeText(v.getContext(), "First Register the device with GCM then proceed!!!", Toast.LENGTH_LONG).show();
                }
            }
        });
        System.out.println(EXIT_TAG+"onCreate");
    }
    @Override
    protected void onPostResume() {
        System.out.println(ENTRY_TAG+"onPostResume");
        super.onPostResume();


        //########################################################
        //Check for registration details available or not if not
        //#######################################################
        if(mRegistrationHelper.isDeviceRegistered()){
            HashMap<String,String> hm = mRegistrationHelper.getDeviceDetails();
            mRegId = hm.get("RegistrationId");
            StringBuilder value = new StringBuilder("RegId::"+(null != hm.get("RegistrationId")?hm.get("RegistrationId"):""));
            value.append("\n"+"unreg::"+(null != hm.get("UnRegistrationId")?hm.get("UnRegistrationId"):""));
            value.append("\n"+"error::"+(null != hm.get("ErrorId")?hm.get("ErrorId"):""));
            mEtRegDetails.setText(value);
        }else{
            Toast.makeText(getApplicationContext(), "Device Should GCM Registered First!!!", Toast.LENGTH_LONG).show();
            //Intent i = new Intent(SplashActivity.this, RegisterActivity.class);
            //startActivity(i);
            //finish();
        }

        System.out.println(EXIT_TAG+"onPostResume");
    }

}
