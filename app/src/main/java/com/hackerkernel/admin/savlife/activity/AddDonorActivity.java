package com.hackerkernel.admin.savlife.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
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

public class AddDonorActivity extends AppCompatActivity {
    private static final String TAG = AddDonorActivity.class.getSimpleName();
    private String mUserName;
    private String mUserMobile;
    private String mUserGender;
    private String mUserAge;
    private String mUserBloodGroup;
    private String mUserCity;

    private RequestQueue mRequestQueue;
    private ProgressDialog pd;
    private MySP sp;

    @Bind(R.id.add_donor_name) EditText mName;
    @Bind(R.id.add_donor_mobile) EditText mMobile;
    @Bind(R.id.add_donor_gender_group) RadioGroup mGenderGroup;
    @Bind(R.id.add_donor_age) EditText mAge;
    @Bind(R.id.add_donor_blood) Spinner mBloodGroup;
    @Bind(R.id.add_donor_btn) Button mRegButton;
    @Bind(R.id.add_donor_city) EditText mCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_donor);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.add_donor);
        }

        //init sp
        sp = MySP.getInstance(this);

        mRequestQueue = MyVolley.getInstance().getRequestQueue();

        //init pd
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.please_Watit));
        pd.setCancelable(true);

        /*
        * When Register button is clicked
        * */
        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInternetAndRegister();
            }
        });
    }
    private void checkInternetAndRegister() {
        if (Util.isNetworkAvailable()){
            mUserName = mName.getText().toString().trim();
            mUserMobile = mMobile.getText().toString().trim();
            mUserCity = mCity.getText().toString().trim().toLowerCase();
            int genderId = mGenderGroup.getCheckedRadioButtonId();
            mUserAge = mAge.getText().toString().trim();
            mUserBloodGroup = (String) mBloodGroup.getSelectedItem();
            if (genderId == R.id.add_donor_gender_male){
                mUserGender = "Male";
            }else {
                mUserGender = "Female";
            }

            if (mUserName.isEmpty() || mUserMobile.isEmpty() || mUserAge.isEmpty() || mUserBloodGroup.isEmpty() || mUserGender.isEmpty()){
                Util.showSimpleDialog(this,getString(R.string.oops),"Fill in all the fields");
                return;
            }

            if (mUserName.length() <= 3){
                Util.showSimpleDialog(this,getString(R.string.oops),"Fullname should be more then 3 character");
                return;
            }

            if (mUserMobile.length() != 10){
                Util.showSimpleDialog(this,getString(R.string.oops),"Invalid mobile number");
                return;
            }

            if (Integer.parseInt(mUserAge) < 18){
                Util.showSimpleDialog(this,getString(R.string.oops),"You must be 18 to register");
                return;
            }

            doRegisterInBackground();

        }else {
            Toast.makeText(getApplicationContext(), R.string.no_internet_available,Toast.LENGTH_LONG).show();
            //go to no internet activity
        }
    }

    /*
    * Method to do register in background
    * */
    private void doRegisterInBackground() {
        pd.show();
        StringRequest request = new StringRequest(Request.Method.POST, EndPoints.ADD_DONOR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                parseRegisterResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                Log.e(TAG,"HUS: doRegisterInBackground: "+error.getMessage());
                error.printStackTrace();
                //handle Volley error
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
                params.put(Constants.COM_FULLNAME,mUserName);
                params.put(Constants.COM_MOBILE,mUserMobile);
                params.put(Constants.COM_AGE,mUserAge);
                params.put(Constants.COM_BLOOD,mUserBloodGroup);
                params.put(Constants.COM_GENDER,mUserGender);
                params.put(Constants.COM_CITY,mUserCity);
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    /*
    * Method to parse register response
    * */
    private void parseRegisterResponse(String response) {
        try {
            SimplePojo current = JsonParser.SimpleParser(response);
            if (current.isReturned()){

                Util.showSimpleDialog(this,getString(R.string.hurray),current.getMessage());
            }else {
                Util.showSimpleDialog(this,getString(R.string.oops),current.getMessage());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG,"HUS: parseRegisterResponse: "+e.getMessage());
            Util.showParsingErrorAlert(this);
        }
    }

}
