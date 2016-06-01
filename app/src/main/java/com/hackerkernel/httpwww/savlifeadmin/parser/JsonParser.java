package com.hackerkernel.httpwww.savlifeadmin.parser;

import com.hackerkernel.httpwww.savlifeadmin.constant.Constants;
import com.hackerkernel.httpwww.savlifeadmin.pojo.DonorListPojo;
import com.hackerkernel.httpwww.savlifeadmin.pojo.DonorPojo;
import com.hackerkernel.httpwww.savlifeadmin.pojo.SimplePojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Murtaza on 6/1/2016.
 */
public class JsonParser {
    private static final String TAG = JsonParser.class.getSimpleName();

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
            pojo.setUserId(obj.getString(Constants.COM_ID));
            pojo.setUserName(obj.getString(Constants.COM_FULLNAME));
            pojo.setImageUrl(obj.getString(Constants.COM_IMG));
            pojo.setUserBloodGroup(obj.getString(Constants.COM_BLOOD));
            list.add(pojo);
        }
        return list;
    }

    public static DonorPojo DetailDonorParser(JSONArray dataArray) throws JSONException {
        DonorPojo pojo = new DonorPojo();
        for (int i = 0; i <dataArray.length() ; i++) {
            JSONObject obj = dataArray.getJSONObject(i);
            pojo.setFullName(obj.getString(Constants.COM_FULLNAME));
            pojo.setCity(obj.getString(Constants.LOC_CITY));
            pojo.setAge(obj.getString(Constants.COM_AGE));
            pojo.setBloodGroup(obj.getString(Constants.COM_BLOOD));
            pojo.setGender(obj.getString(Constants.COM_GENDER));
            pojo.setId(obj.getString(Constants.COM_ID));
            pojo.setImageUrl(obj.getString(Constants.COM_IMG));
        }
        return pojo;
    }
}
