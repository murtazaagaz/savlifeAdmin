package com.hackerkernel.admin.savlife.parser;

import com.hackerkernel.admin.savlife.constant.Constants;
import com.hackerkernel.admin.savlife.pojo.DealListPojo;
import com.hackerkernel.admin.savlife.pojo.DonorListPojo;
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

    public static List<DealListPojo> DealListParser(JSONArray data) throws JSONException {
        List<DealListPojo> list = new ArrayList<>();
        for (int i = 0; i < data.length(); i++) {
            JSONObject jo = data.getJSONObject(i);
            DealListPojo pojo = new DealListPojo();
            pojo.setId(jo.getString(Constants.COM_ID));
            pojo.setImageUrl(jo.getString(Constants.COM_IMAGE));
            pojo.setLabName(jo.getString(Constants.COM_LABNAME));
            list.add(pojo);
        }
        return list;
    }
}
