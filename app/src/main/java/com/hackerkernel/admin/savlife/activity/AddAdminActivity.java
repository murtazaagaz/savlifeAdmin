package com.hackerkernel.admin.savlife.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.hackerkernel.admin.savlife.network.MyVolley;
import com.hackerkernel.admin.savlife.parser.JsonParser;
import com.hackerkernel.admin.savlife.pojo.SimplePojo;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

public class AddAdminActivity extends AppCompatActivity {
    //TODO add api link and api key




    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.linear)
    LinearLayout linear;
    @Bind(R.id.add_name)
    EditText editName;
    @Bind(R.id.add_username) EditText editUsername;
    @Bind(R.id.add_password) EditText editPassword;
    @Bind(R.id.add_mobile) EditText editMobile;
    @Bind(R.id.add_admin_btn)
    Button addBtn;
    String name, username, password, mobile;
    private RequestQueue mRequestQue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Add Admin");
        name = editName.getText().toString();
        username = editUsername.getText().toString();
        password = editPassword.getText().toString();
        mobile = editMobile.getText().toString();
        mRequestQue = MyVolley.getInstance().getRequestQueue();
         addBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 checkInternet();

             }
         });
    }
    public void checkInternet(){
        if (Util.isNetworkAvailable()){
            parseDataInBackground();
        }
    }

    private void parseDataInBackground() {
        StringRequest request = new StringRequest(Request.Method.POST, "URL", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    SimplePojo pojo = JsonParser.SimpleParser(response);
                    if (pojo.isReturned()){
                        Toast.makeText(getApplicationContext(),"Admin Added",Toast.LENGTH_LONG).show();

                    }
                    else {
                        Util.showRedSnackbar(linear,pojo.getMessage());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.COM_FULLNAME,name);
                params.put(Constants.COM_PASSWORD,password);
                params.put(Constants.COM_MOBILE,mobile);
                params.put(Constants.COM_USERNAME,username);
                return params;
            }
        };
        mRequestQue.add(request);
    }
}
