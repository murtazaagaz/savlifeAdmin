package com.hackerkernel.admin.savlife.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.admin.savlife.R;
import com.hackerkernel.admin.savlife.adapter.DonorListAdapter;
import com.hackerkernel.admin.savlife.constant.Constants;
import com.hackerkernel.admin.savlife.constant.EndPoints;
import com.hackerkernel.admin.savlife.network.MyVolley;
import com.hackerkernel.admin.savlife.parser.JsonParser;
import com.hackerkernel.admin.savlife.pojo.DonorListPojo;
import com.hackerkernel.admin.savlife.storage.MySP;
import com.hackerkernel.admin.savlife.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchDonorActivity extends AppCompatActivity {
    private static final String TAG = SearchDonorActivity.class.getSimpleName();
    @Bind(R.id.search_edittext) EditText mSearchEdittext;
    @Bind(R.id.search_recycleview) RecyclerView mRecyclerView;
    @Bind(R.id.search_btn) Button mSearchBtn;
    @Bind(R.id.search_placeholder) TextView mPlaceholder;

    private RequestQueue mRequestQue;
    private ProgressDialog progressDialog;
    private String mDonorId;
    private MySP sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_donor);

        ButterKnife.bind(this);

        //init volley
        mRequestQue = MyVolley.getInstance().getRequestQueue();

        //init sp
        sp = MySP.getInstance(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_Watit));
        progressDialog.setCancelable(true);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(manager);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetAndDoSearch();
            }
        });

    }

    public void checkInternetAndDoSearch(){
        if (Util.isNetworkAvailable()){
            mDonorId = mSearchEdittext.getText().toString().trim();
            if (mDonorId.isEmpty()){
                Util.showSimpleDialog(this,getString(R.string.oops),"Enter donor Id");
                return;
            }
            doSearchInBackground();
        }else{
            Util.showSimpleDialog(this,getString(R.string.oops),getString(R.string.no_internet_connection));
        }
    }

    private void doSearchInBackground() {
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.SEARCH_DONOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d(TAG,"HUS: "+response);
                parseBestDonorResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d(TAG,"HUS: "+error.getMessage());
                String errorString = MyVolley.handleVolleyError(error);
                Toast.makeText(getApplicationContext(),errorString,Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(Constants.COM_APIKEY,Util.generateApiKey(sp.getAdminUsername()));
                params.put(Constants.COM_USERNAME,sp.getAdminUsername());
                params.put(Constants.COM_ID,mDonorId);
                return params;
            }
        };

        mRequestQue.add(request);

    }

    private void parseBestDonorResponse(String response) {
        try {
            JSONObject jsonObj = new JSONObject(response);
            boolean returned = jsonObj.getBoolean(Constants.COM_RETURN);
            String message = jsonObj.getString(Constants.COM_MESSAGE);
            if (returned){
                //donor found
                mPlaceholder.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);

                JSONArray dataArray = jsonObj.getJSONArray(Constants.COM_DATA);
                List<DonorListPojo> list = JsonParser.DonorListParser(dataArray);
                setupDonorRecyclerView(list);
            }else {
                mRecyclerView.setVisibility(View.GONE);
                mPlaceholder.setVisibility(View.VISIBLE);
                mPlaceholder.setText(message);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Util.showParsingErrorAlert(getApplicationContext());
        }

    }

    private void setupDonorRecyclerView(List<DonorListPojo> list) {
        DonorListAdapter adapter = new DonorListAdapter(this);
        adapter.setList(list);
        mRecyclerView.setAdapter(adapter);
    }
}
