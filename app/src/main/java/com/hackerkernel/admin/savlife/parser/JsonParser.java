package com.hackerkernel.admin.savlife.parser;

import com.hackerkernel.admin.savlife.constant.Constants;
import com.hackerkernel.admin.savlife.pojo.DonorListPojo;
import com.hackerkernel.admin.savlife.pojo.DonorPojo;
import com.hackerkernel.admin.savlife.pojo.SimplePojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Murtaza on 6/1/2016.
 */
public class JsonParser {

    public static SimplePojo SimpleParser(String response) throws JSONException {
        JSONObject jo = new JSONObject(response);
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.setMessage(jo.getString(Constants.COM_MESSAGE));
        simplePojo.setReturned(jo.getBoolean(Constants.COM_RETURN));
        return simplePojo;
    }

    public static List<DonorListPojo> DonorListParser(JSONArray dataArray) throws JSONException {
        List<DonorListPojo> list = new ArrayList<>();
        for (int i = 0; i <dataArray.length() ; i++) {
            JSONObject obj = dataArray.getJSONObject(i);
            DonorListPojo pojo = new DonorListPojo();
            pojo.setFullname(obj.getString(Constants.COM_FULLNAME));
            pojo.setImgUrl(obj.getString(Constants.COM_IMG));
            pojo.setMobile(obj.getString(Constants.COM_MOBILE));
            pojo.setGender(obj.getString(Constants.COM_GENDER));
            pojo.setAge(obj.getString(Constants.COM_AGE));
            pojo.setBlood(obj.getString(Constants.COM_BLOOD));
            pojo.setCity(obj.getString(Constants.COM_CITY));
            pojo.setLastDontaion(obj.getString(Constants.COM_DATE));
            list.add(pojo);
        }
        return list;
    }

    public static DonorPojo DetailDonorParser(JSONArray dataArray) throws JSONException {
        DonorPojo pojo = new DonorPojo();
        for (int i = 0; i <dataArray.length() ; i++) {
            JSONObject obj = dataArray.getJSONObject(i);
            pojo.setFullName(obj.getString(Constants.COM_FULLNAME));
            pojo.setCity(obj.getString(Constants.COM_CITY));
            pojo.setAge(obj.getString(Constants.COM_AGE));
            pojo.setBloodGroup(obj.getString(Constants.COM_BLOOD));
            pojo.setGender(obj.getString(Constants.COM_GENDER));
            pojo.setId(obj.getString(Constants.COM_ID));
            pojo.setImageUrl(obj.getString(Constants.COM_IMG));
        }
        return pojo;
    }
}
