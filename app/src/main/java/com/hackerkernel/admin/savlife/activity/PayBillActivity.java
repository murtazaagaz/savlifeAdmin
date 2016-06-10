package com.hackerkernel.admin.savlife.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hackerkernel.admin.savlife.R;

public class PayBillActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bill);
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.pay_bill);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }
}
