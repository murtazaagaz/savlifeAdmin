package com.hackerkernel.admin.savlife.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.admin.savlife.R;
import com.hackerkernel.admin.savlife.constant.Constants;
import com.hackerkernel.admin.savlife.constant.EndPoints;
import com.hackerkernel.admin.savlife.network.MyVolley;
import com.hackerkernel.admin.savlife.parser.JsonParser;
import com.hackerkernel.admin.savlife.pojo.SimplePojo;
import com.hackerkernel.admin.savlife.storage.MySP;
import com.hackerkernel.admin.savlife.util.Util;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SendNotificationActivity extends AppCompatActivity {
    @Bind(R.id.notification_title) TextView mTitle;
    @Bind(R.id.notification_message) TextView mMessage;
    @Bind(R.id.notification_btn) Button mBtn;

    private RequestQueue mRequestQueue;
    private MySP sp;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.send_notification);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        //init volley & sp
        mRequestQueue = MyVolley.getInstance().getRequestQueue();
        sp = MySP.getInstance(this);
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.please_Watit));
        pd.setCancelable(true);

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetAndSendNotification();
            }
        });
    }

    /*
    * Method check internet, validate and send request to the api
     * to send push notification
    * */
    private void checkInternetAndSendNotification() {
        if (Util.isNetworkAvailable()){
            //validate
            String title = mTitle.getText().toString().trim();
            String message = mMessage.getText().toString().trim();

            if (title.isEmpty() || message.isEmpty()){
                Util.showSimpleDialog(this,getString(R.string.oops),"FIll in all the fields");
                return;
            }

            sendNotificationInBackground(title,message);
        }else {
            Toast.makeText(getApplicationContext(),R.string.no_internet_available,Toast.LENGTH_LONG).show();
        }
    }


    private void sendNotificationInBackground(final String title, final String message) {
        pd.show();
        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.SEND_NOTIFICATION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    SimplePojo pojo = JsonParser.SimpleParser(response);
                    if (pojo.isReturned()){
                        mTitle.setText("");
                        mMessage.setText("");
                        Toast.makeText(getApplicationContext(),pojo.getMessage(),Toast.LENGTH_LONG).show();
                    }else {
                        Util.showSimpleDialog(getApplication(),getString(R.string.oops),pojo.getMessage());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Util.showParsingErrorAlert(SendNotificationActivity.this);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                error.printStackTrace();
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(Constants.COM_APIKEY,Util.generateApiKey(sp.getAdminUsername()));
                params.put(Constants.COM_USERNAME,sp.getAdminUsername());
                params.put(Constants.COM_TITLE,title);
                params.put(Constants.COM_MESSAGE,message);
                return params;
            }
        };
        mRequestQueue.add(request);
    }
}
