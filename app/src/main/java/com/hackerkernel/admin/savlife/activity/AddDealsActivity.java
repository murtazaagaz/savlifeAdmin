package com.hackerkernel.admin.savlife.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hackerkernel.admin.savlife.R;
import com.hackerkernel.admin.savlife.Util;
import com.hackerkernel.admin.savlife.constant.Constants;
import com.hackerkernel.admin.savlife.constant.EndPoints;
import com.hackerkernel.admin.savlife.parser.JsonParser;
import com.hackerkernel.admin.savlife.pojo.SimplePojo;
import com.hackerkernel.admin.savlife.storage.MySP;
import com.hackerkernel.admin.savlife.util.ImageUtil;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddDealsActivity extends AppCompatActivity {
    private static final int SELECT_IMAGE_CODE = 100;
    @Bind(R.id.upload_deals_image) ImageView mImage;
    @Bind(R.id.upload_deals_image_btn) Button mSelectImageBtn;
    @Bind(R.id.upload_deals_labname) EditText mLabname;
    @Bind(R.id.upload_deals_description) EditText mDescription;
    @Bind(R.id.upload_deals_code) EditText mCode;
    @Bind(R.id.upload_deals_original_price) EditText mOriginalPrice;
    @Bind(R.id.upload_deals_special_price) EditText mSpecialPrice;
    @Bind(R.id.upload_deals_add) Button mAddDeal;
    private String imageBase64 = null,
            labname,
            code,
            description,
            originalPrice,
            specialPrice;

    private MySP sp;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deals);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(R.string.add_deal);
        }

        //init pd
        pd = new ProgressDialog(this);
        pd.setMessage(getString(R.string.please_Watit));

        //init sp
        sp = MySP.getInstance(this);

        mSelectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //select image
                Intent openGallery = new Intent(Intent.ACTION_PICK);
                openGallery.setType("image/*");
                startActivityForResult(openGallery, SELECT_IMAGE_CODE);
            }
        });

        mAddDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfInternetAvailable();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_IMAGE_CODE && resultCode == RESULT_OK && data != null){
            Uri imageuri = data.getData();
            //compress image
            String imagePath = ImageUtil.getFilePathFromUri(this,imageuri);
            Bitmap imageBitmap = ImageUtil.decodeBitmapFromFilePath(imagePath,300,300);
            if (imageBitmap != null){
                mImage.setVisibility(View.VISIBLE);
                mImage.setImageBitmap(imageBitmap);
                //compress image to base64
                imageBase64 = ImageUtil.compressImageToBase64(imageBitmap);
            }else {
                Toast.makeText(getApplicationContext(),"Unable to select image. Try again lat",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkIfInternetAvailable() {
        if (Util.isNetworkAvailable()){

            labname = mLabname.getText().toString().trim();
            description = mDescription.getText().toString().trim();
            code = mCode.getText().toString().trim();
            originalPrice = mOriginalPrice.getText().toString().trim();
            specialPrice = mSpecialPrice.getText().toString().trim();

            //validation
            if (labname.isEmpty() || description.isEmpty() || code.isEmpty() || originalPrice.isEmpty() || specialPrice.isEmpty()){
                Util.showSimpleDialog(this,getString(R.string.oops),"Fill in all the required fields");
                return;
            }

            if (Integer.parseInt(specialPrice) > Integer.parseInt(originalPrice)){
                Util.showSimpleDialog(this,getString(R.string.oops),"Special price cannot be greater then original price");
                return;
            }

            fetchDataInBackground();
        }else {
            Util.showSimpleDialog(this,getString(R.string.oops),getString(R.string.no_internet_available));
        }
    }

    private void fetchDataInBackground() {
        UploadDeal uploadDeal = new UploadDeal();
        uploadDeal.execute();
    }

    /*
    * Asynctask to upload status image to server
    * */
    public class UploadDeal extends AsyncTask<String,Integer,String> {
        public UploadDeal(){
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(EndPoints.ADD_DEAL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os,"UTF-8"));

                //set post data
                HashMap<String,String> postParam = new HashMap<>();
                postParam.put(Constants.COM_APIKEY,Util.generateApiKey(sp.getAdminUsername()));
                postParam.put(Constants.COM_USERNAME,sp.getAdminUsername());
                postParam.put(Constants.COM_LABNAME,labname);
                postParam.put(Constants.COM_CODE,code);
                postParam.put(Constants.COM_DESCRIPTION,description);
                postParam.put(Constants.COM_ORIGNAL_PRIZE,originalPrice);
                postParam.put(Constants.COM_SPECIAL_PRIZE,specialPrice);
                if (imageBase64 != null){
                    postParam.put(Constants.COM_IMG,imageBase64);
                }

                writer.write(Util.getPostDataFromHashmap(postParam));
                writer.flush();
                writer.close();
                os.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK){
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String response;
                    while ((response = reader.readLine()) != null){
                        sb.append(response);
                    }
                    return sb.toString();
                }

                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            parseUploadDealResponse(s);
        }
    }

    /*
    * Method to parse upload deal response
    * */
    private void parseUploadDealResponse(String s) {
        try {
            SimplePojo pojo = JsonParser.SimpleParser(s);
            if (pojo.isReturned()){
                Util.showSimpleDialog(this,getString(R.string.hurray),pojo.getMessage());
            }else {
                Util.showSimpleDialog(this,getString(R.string.oops),pojo.getMessage());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Util.showParsingErrorAlert(this);
        }
    }
}


