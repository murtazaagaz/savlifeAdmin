package com.hackerkernel.admin.savlife.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.hackerkernel.admin.savlife.Util;
import com.hackerkernel.admin.savlife.constant.Constants;
import com.hackerkernel.admin.savlife.network.MyVolley;
import com.hackerkernel.admin.savlife.parser.JsonParser;
import com.hackerkernel.admin.savlife.pojo.SimplePojo;
import com.hackerkernel.admin.savlife.R;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddDealsActivity extends AppCompatActivity {

    //TODO add api url ,apikey and deal image url

    @Bind(R.id.upload_deals_image)
    Button uploadImageButton;
    @Bind(R.id.deal_description)
    EditText descriptionEdit;
    @Bind(R.id.deal_timings) EditText timingEdit;
    @Bind(R.id.deal_orignal_prize) EditText orignalEdit;
    @Bind(R.id.deal_special_prize) EditText specialEdit;
    @Bind(R.id.deal_off)  EditText offEdit;
    @Bind(R.id.deal_add_deal) Button mAddDealBtn;
    @Bind(R.id.linear)
    LinearLayout linear;
    private String image, descriotion, timing, orignalPrize,
            specialPrize, off;
    private RequestQueue mRequestQue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deals);
        ButterKnife.bind(this);
        descriotion = descriptionEdit.getText().toString();
        timing = timingEdit.getText().toString();
        orignalPrize = orignalEdit.getText().toString();
        specialPrize = specialEdit.getText().toString();
        off = offEdit.getText().toString();
        mRequestQue = MyVolley.getInstance().getRequestQueue();
         mAddDealBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 checkIfInternetAvailable();
             }
         });


    }

    private void checkIfInternetAvailable() {
        if (Util.isNetworkAvailable()){
            fetchDataInBackground();
        }

    }

    private void fetchDataInBackground() {
        StringRequest request = new StringRequest(Request.Method.POST, "URL", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    SimplePojo pojo = JsonParser.SimpleParser(response);
                    if (pojo.isReturned()){
                        Toast.makeText(getApplicationContext(),"Deal Added",Toast.LENGTH_LONG).show();

                    }
                    else {
                        Util.showParsingErrorAlert(getApplicationContext());
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String volleyError = MyVolley.handleVolleyError(error);
                Util.showRedSnackbar(linear,volleyError);


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Constants.COM_DESCRIPTION,descriotion);
                params.put(Constants.COM_APIKEY,"");
                params.put(Constants.COM_ORIGNAL_PRIZE,orignalPrize);
                params.put(Constants.COM_SPECIAL_PRIZE,specialPrize);
                params.put(Constants.COM_TIME,timing);
                params.put(Constants.COM_OFF,off);
                params.put(Constants.COM_IMG,"");
                return params;
            }
        };
        mRequestQue.add(request);
    }
}


