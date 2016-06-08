package com.hackerkernel.admin.savlife.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.admin.savlife.R;
import com.hackerkernel.admin.savlife.Util;
import com.hackerkernel.admin.savlife.constant.Constants;
import com.hackerkernel.admin.savlife.constant.EndPoints;
import com.hackerkernel.admin.savlife.network.MyVolley;
import com.hackerkernel.admin.savlife.parser.JsonParser;
import com.hackerkernel.admin.savlife.pojo.SimplePojo;
import com.hackerkernel.admin.savlife.storage.MySP;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddAdminActivity extends AppCompatActivity {
    private static final String TAG = AddAdminActivity.class.getSimpleName();

    @Bind(R.id.admin_username) EditText mUsername;
    @Bind(R.id.admin_password) EditText mPassword;
    @Bind(R.id.add_admin_btn) Button mAddAdmin;
    String username,
            password;
    private RequestQueue mRequestQue;
    private ProgressDialog pd;
    private MySP sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.add_admin);
        }

        //init pd
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.please_Watit));

        //init sp
        sp = MySP.getInstance(this);

        //init volley
        mRequestQue = MyVolley.getInstance().getRequestQueue();

        mAddAdmin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 checkInternet();
             }
         });
    }

    public void checkInternet(){
        if (Util.isNetworkAvailable()){
            username = mUsername.getText().toString().trim();
            password = mPassword.getText().toString().trim();

            //validation
            if (username.isEmpty() || password.isEmpty()){
                Util.showSimpleDialog(this,getString(R.string.oops),"Fill in all the fields");
                return;
            }

            if (username.length() <= 4){
                Util.showSimpleDialog(this,getString(R.string.oops),"Username should be more then 4 characters");
                return;
            }

            if (password.length() <= 4){
                Util.showSimpleDialog(this,getString(R.string.oops),"Password should be more then 4 characters");
                return;
            }

            addAdminInBackground();
        }else {
            Util.showSimpleDialog(this,getString(R.string.oops),getString(R.string.no_internet_available));
        }
    }

    private void addAdminInBackground() {
        pd.show(); //show pd
        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.ADD_ADMIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss(); //show pd
                try {
                    SimplePojo pojo = JsonParser.SimpleParser(response);
                    if (pojo.isReturned()){
                        mUsername.setText("");
                        mPassword.setText("");
                       Util.showSimpleDialog(AddAdminActivity.this,getString(R.string.hurray),pojo.getMessage());
                    } else {
                        Util.showSimpleDialog(AddAdminActivity.this,getString(R.string.oops),pojo.getMessage());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Util.showParsingErrorAlert(AddAdminActivity.this);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss(); //show pd
                Log.d(TAG,"HUS: "+error.getMessage());
                String errorString = MyVolley.handleVolleyError(error);
                Toast.makeText(getApplicationContext(),errorString,Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.COM_APIKEY,Util.generateApiKey(sp.getAdminUsername()));
                params.put(Constants.COM_USERNAME,sp.getAdminUsername());
                params.put(Constants.COM_NEW_USERNAME,username);
                params.put(Constants.COM_PASSWORD,password);
                return params;
            }
        };

        mRequestQue.add(request);
    }
}
