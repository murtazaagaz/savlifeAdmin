package com.hackerkernel.admin.savlife.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.hackerkernel.admin.savlife.pojo.DealListPojo;
import com.hackerkernel.admin.savlife.pojo.DonorListPojo;
import com.hackerkernel.admin.savlife.pojo.SimplePojo;
import com.hackerkernel.admin.savlife.util.PayBillDialogActivity;
import com.hackerkernel.admin.savlife.util.Util;
import com.hackerkernel.admin.savlife.adapter.DonorListAdapter;
import com.hackerkernel.admin.savlife.constant.Constants;
import com.hackerkernel.admin.savlife.constant.EndPoints;
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

public class HomeActivity extends AppCompatActivity{
    private static final String TAG = HomeActivity.class.getSimpleName();
    private RequestQueue mRequestQue;
    private ProgressDialog progressDialog;
    private MySP sp;

    @Bind(R.id.bill_due_next_date) TextView mBillNextDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        //init volley
        mRequestQue = MyVolley.getInstance().getRequestQueue();

        //init sp
        sp = MySP.getInstance(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_Watit));
        progressDialog.setCancelable(true);

        checkInternetGetNextBilldate();
    }

    private void checkInternetGetNextBilldate() {
        if (Util.isNetworkAvailable()){
            //method to get the next bill date
            getNextBillDateInBackground();
            //method to check bill date valid or not
            checkNextBillDateInBackground();
        }
    }

    private void getNextBillDateInBackground() {
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, EndPoints.GET_NEXT_BILL_DATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                mBillNextDate.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG,"HUS: "+error.getMessage());
                error.printStackTrace();
                String errorString = MyVolley.handleVolleyError(error);
                Toast.makeText(getApplicationContext(),errorString,Toast.LENGTH_LONG).show();
            }
        });
        mRequestQue.add(request);
    }

    private void checkNextBillDateInBackground() {
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, EndPoints.CHECK_BILL_DATE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                parseNextBillDateResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e(TAG,"HUS: "+error.getMessage());
                error.printStackTrace();
                String errorString = MyVolley.handleVolleyError(error);
                Toast.makeText(getApplicationContext(),errorString,Toast.LENGTH_LONG).show();
            }
        });
        mRequestQue.add(request);
    }


    private void parseNextBillDateResponse(String response) {
        try {
            SimplePojo p = JsonParser.SimpleParser(response);
            if (!p.isReturned()){
                //show dialog that you have to pay your bill else
                showPayBillDialog();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Util.showParsingErrorAlert(this);
        }
    }

    private void showPayBillDialog() {
        FragmentManager manager = getSupportFragmentManager();
        PayBillDialogActivity dialogActivity = new PayBillDialogActivity();
        dialogActivity.show(manager,"PayBillDialogActivity");
    }


    public void openSearchDonor(View view) {
        startActivity(new Intent(this,SearchDonorActivity.class));
    }

    public void openPayBill(View view) {
        startActivity(new Intent(this,PayBillActivity.class));
    }

    public void openAddDonor(View view) {
        startActivity(new Intent(this,AddDonorActivity.class));
    }

    public void openAddDeal(View view) {
        startActivity(new Intent(this,AddDealsActivity.class));
    }

    public void openAddAdmin(View view) {
        startActivity(new Intent(this,AddAdminActivity.class));
    }

    public void openBookedDealList(View view) {
        startActivity(new Intent(this,DealsListActivity.class));
    }

    public void openSendNotification(View view) {
        startActivity(new Intent(this,SendNotificationActivity.class));
    }

    public void logoutTheUser(View view) {
        sp.logout();
        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
