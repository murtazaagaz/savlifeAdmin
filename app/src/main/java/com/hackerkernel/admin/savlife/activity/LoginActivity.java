package com.hackerkernel.admin.savlife.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.hackerkernel.admin.savlife.util.Util;
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

public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.login_btn) Button loginBtn;
    @Bind(R.id.login_username) EditText mLoginUsername;
    @Bind(R.id.login_password) EditText mLoginPassword;

    private ProgressDialog pd;
    private RequestQueue mRequestQueue;
    private MySP sp;
    private String mUsername;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init sp
        sp = MySP.getInstance(this);
        //send user to home activity if he is login
        if (sp.getLoginStatus()){
            Util.goToHomeActivity(this);
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        //init pd
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.please_Watit));
        pd.setCancelable(true);

        //init volley
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            checkInternetAndDoLogin();
            }
        });

    }

    /*
    * Method to check internet and do login
    * */
    private void checkInternetAndDoLogin() {
        if (Util.isNetworkAvailable()){
            mUsername = mLoginUsername.getText().toString().trim();
            mPassword = mLoginPassword.getText().toString().trim();
            if (mUsername.isEmpty() || mPassword.isEmpty()){
                Toast.makeText(getApplicationContext(),"Invalid username & password",Toast.LENGTH_LONG).show();
                return;
            }
            doLoginInBackground();
        }else {
            Toast.makeText(getApplicationContext(),R.string.no_internet_available,Toast.LENGTH_LONG).show();
        }
    }

    /*
    * Method to do login in bg
    * */
    private void doLoginInBackground() {
        pd.show();
        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss(); //hide it bitch
                parseLoginResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                error.printStackTrace();
                String stringError = MyVolley.handleVolleyError(error);
                if (stringError != null){
                    Toast.makeText(getApplicationContext(), stringError,Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(Constants.COM_APIKEY,Util.generateApiKey(mUsername));
                params.put(Constants.COM_USERNAME,mUsername);
                params.put(Constants.COM_PASSWORD,mPassword);
                return params;
            }
        };
        mRequestQueue.add(request);
    }

    /*
    *
    * */
    private void parseLoginResponse(String response) {
        try {
            SimplePojo pojo = JsonParser.SimpleParser(response);
            if (pojo.isReturned()){
                //save in sp
                sp.setAdminUsername(mUsername);

                //go to home activity
                Util.goToHomeActivity(this);
            }else {
                Toast.makeText(getApplicationContext(),pojo.getMessage(),Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Util.showParsingErrorAlert(this);
        }
    }
}
