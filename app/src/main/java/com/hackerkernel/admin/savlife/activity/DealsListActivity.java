package com.hackerkernel.admin.savlife.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hackerkernel.admin.savlife.R;
import com.hackerkernel.admin.savlife.adapter.DealsListAdapter;
import com.hackerkernel.admin.savlife.constant.EndPoints;
import com.hackerkernel.admin.savlife.pojo.DealListPojo;
import com.hackerkernel.admin.savlife.pojo.DonorListPojo;
import com.hackerkernel.admin.savlife.util.Util;
import com.hackerkernel.admin.savlife.constant.Constants;
import com.hackerkernel.admin.savlife.network.MyVolley;
import com.hackerkernel.admin.savlife.parser.JsonParser;
import com.hackerkernel.admin.savlife.storage.MySP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DealsListActivity extends AppCompatActivity {
    private static final String TAG = DealsListActivity.class.getSimpleName();
    @Bind(R.id.deals_listview) ListView mListView;

    private RequestQueue mRequestQueue;
    private ProgressDialog pd;
    private MySP sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_detail);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle("Deals list");
        }

        //init vollet
        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.please_Watit));

        //init sp
        sp = MySP.getInstance(this);

        checkNetworkIsAvailable();
    }
    private void checkNetworkIsAvailable(){
        if (Util.isNetworkAvailable()){
            fetchDealListInBackground();
        }else {
            Util.showSimpleDialog(this,getString(R.string.oops),getString(R.string.no_internet_available));
        }
    }

    private void fetchDealListInBackground() {
        pd.show(); //show pd
        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.DEAL_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss(); //hide pd
                parseDetailDonorData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss(); //hide pd
                Log.d(TAG,"HUS:"+error.getMessage());
                error.printStackTrace();
                String errorString = MyVolley.handleVolleyError(error);
                if (errorString != null){
                    Toast.makeText(getApplicationContext(),errorString,Toast.LENGTH_LONG).show();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(Constants.COM_APIKEY,Util.generateApiKey(sp.getAdminUsername()));
                params.put(Constants.COM_USERNAME,sp.getAdminUsername());
                return params;
            }
        };
        mRequestQueue.add(request);

    }
    private void parseDetailDonorData(String response){

        try {
            JSONObject obj = new JSONObject(response);
            boolean returned = obj.getBoolean(Constants.COM_RETURN);
            String message = obj.getString(Constants.COM_MESSAGE);
            if (returned){
                JSONArray data =  obj.getJSONArray(Constants.COM_DATA);
                List<DealListPojo> list = JsonParser.DealListParser(data);
                setupView(list);
            }
            else{
                Util.showSimpleDialog(this,getString(R.string.oops),message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Util.showParsingErrorAlert(this);
        }
    }

    /*
    * Method to setup view
    * */
    private void setupView(List<DealListPojo> list) {
        DealsListAdapter adapter = new DealsListAdapter(this,R.layout.deal_list_row,list);
        mListView.setAdapter(adapter);
    }

}
