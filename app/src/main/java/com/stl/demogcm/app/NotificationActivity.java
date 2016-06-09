package com.stl.demogcm.app;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stl.demogcm.R;
import com.stl.demogcm.helper.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {
    final String ENTRY_TAG = "ENTRY--->"+this.getClass().getSimpleName()+" " ;
    final String EXIT_TAG = "EXIT--->"+this.getClass().getSimpleName()+" " ;
    EditText mEtData ;
    Button mBtnNotify ;
    String mRegId ;
    String mData ;
    //Required refs for AsyncTask
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println(ENTRY_TAG+"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        mEtData = (EditText)findViewById(R.id.et_data);
        mBtnNotify = (Button)findViewById(R.id.btn_send_notification);
        if(null != getIntent()){
                mRegId = getIntent().getStringExtra("regId") ;
                Toast.makeText(this, "RegistrationId::"+mRegId, Toast.LENGTH_LONG).show();
        }
        mBtnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData = mEtData.getText().toString();
                if(null != mData || "" != mData) {
                    if(Utils.checkInternet(v.getContext())){
                        //do notify
                        doNotify();
                    }else{
                        Toast.makeText(v.getContext(), "Check Internet connectivity...", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(v.getContext(), "Enter data to proceed...", Toast.LENGTH_LONG).show();
                }
            }
        });
        System.out.println(EXIT_TAG+"onCreate");
    }

    private void doNotify() {
        System.out.println(ENTRY_TAG+"doNotify");
        new NotificationTask().execute();


        System.out.println(EXIT_TAG + "doNotify");
    }
    /**
     *This is used getting history asynchronously in background and then navigate to HistoryActivity on success
     */
    private class NotificationTask extends AsyncTask<Void, Void, Void> {
        String res ="" ;
        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(NotificationActivity.this);
            pd.setTitle("Pushing notification data");
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... datas) {

            HttpClient client = new DefaultHttpClient();
            HttpPost post = new HttpPost("https://android.googleapis.com/gcm/send");
            try {
                System.out.println(ENTRY_TAG+"mRegId::"+mRegId);
                System.out.println(ENTRY_TAG+"data::"+mData);
                ArrayList<NameValuePair> namevaluepair = new ArrayList<NameValuePair>();
                namevaluepair.add(new BasicNameValuePair("registration_id", mRegId));
                namevaluepair.add(new BasicNameValuePair("data", mData));//This key value will be received at BroadcastReceiver

                post.setHeader("Authorization", "key=AIzaSyDxQpCFnbmzmA0JswYh7S_QhE7Iwdx94No");//API KEY
                post.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

                post.setEntity(new UrlEncodedFormEntity(namevaluepair));
                HttpResponse response = client.execute(post);
                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        System.out.println(ENTRY_TAG + "GCM HttpResponse:: ======  " + line);

                    }
                    res = line;
                }else{
                    res = "FAILURE";
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (pd != null) {
                pd.dismiss();
            }
            mEtData.setText("");
            System.out.println(ENTRY_TAG + "Response:: ======  " + res);
            //Toast.makeText(NotificationActivity.this,"Response::"+res,Toast.LENGTH_LONG).show();

        }
    }//end of NotificationTask
}
