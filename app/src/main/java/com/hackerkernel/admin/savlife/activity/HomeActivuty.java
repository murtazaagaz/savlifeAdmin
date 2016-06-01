package com.hackerkernel.admin.savlife.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hackerkernel.com.savlifeadmin.R;
import com.hackerkernel.admin.savlife.Util;
import com.hackerkernel.admin.savlife.adapter.DonorListAdapter;
import com.hackerkernel.admin.savlife.constant.Constants;
import com.hackerkernel.admin.savlife.constant.EndPoints;
import com.hackerkernel.admin.savlife.network.MyVolley;
import com.hackerkernel.admin.savlife.parser.JsonParser;
import com.hackerkernel.admin.savlife.pojo.DonorListPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivuty extends AppCompatActivity{
    //TODO Add api key and change api url




    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.search_edittext)
    EditText searchEditText;
    @Bind(R.id.Search_recycleView)
    RecyclerView mRecyclerView;
    @Bind(R.id.searchbtn)
    Button searchbtn;
    private RequestQueue mRequestQue;
    TextView mPlaceholder;
    private ProgressDialog progressDialog;
    private String searchId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_activuty);
        searchId = searchEditText.getText().toString();
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Search");
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRequestQue = MyVolley.getInstance().getRequestQueue();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_Watit));
        progressDialog.setCancelable(true);
        LinearLayoutManager linearLayoutManeger = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(linearLayoutManeger);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    public void checkInternetAndDoSearch(){
        if (Util.isNetworkAvailable()){
            doSearchInBackground();
        }
    }

    private void doSearchInBackground() {
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,
                EndPoints.SEARCH_DONOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.d("TAG","MUR:"+response);
                parseBestDonorResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.d("TAG","MUR: "+error.getMessage());
                //TODO:: handle error
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put(Constants.COM_APIKEY,"ADD API KEY");
                params.put(Constants.COM_ID,searchId);
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
                int count = jsonObj.getInt(Constants.COM_COUNT);
                //when no donor found for this place
                if (count <= 0){
                    mRecyclerView.setVisibility(View.GONE);
                    mPlaceholder.setVisibility(View.VISIBLE);
                    mPlaceholder.setText(message);
                }else {
                    //donor found
                    mPlaceholder.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);

                    JSONArray dataArray = jsonObj.getJSONArray(Constants.COM_DATA);
                    List<DonorListPojo> list = JsonParser.DonorListParser(dataArray);
                    setupDonorRecyclerView(list);
                }
            }else {
                //some auth error
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
        DonorListAdapter adapter = new DonorListAdapter(getApplicationContext());
        adapter.setList(list);
        mRecyclerView.setAdapter(adapter);
    }


}
