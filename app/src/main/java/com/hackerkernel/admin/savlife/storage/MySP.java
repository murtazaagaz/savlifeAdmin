package com.hackerkernel.admin.savlife.storage;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Singleton class for sharedPreferences
 */
public class MySP {
    //instance field
    private static SharedPreferences mSharedPreference;
    private static MySP mInstance = null;
    private static Context mContext;


    //Shared Preference key
    private String KEY_PREFERENCE_NAME = "savlife_admin";

    //private keyS
    private String KEY_DEFAULT = null;

    //user details keys
    private String KEY_USERNAME = "username";

    public MySP() {
        mSharedPreference = mContext.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static MySP getInstance(Context context) {
        mContext = context;
        if (mInstance == null) {
            mInstance = new MySP();
        }
        return mInstance;
    }

    //Method to set boolean for (AppIntro)
    public void setBooleanKey(String keyname) {
        mSharedPreference.edit().putBoolean(keyname, true).apply();
    }

    /*
    * Method to get boolan key
    * true = means set
    * false = not set (show app intro)
    * */
    public boolean getBooleanKey(String keyname) {
        return mSharedPreference.getBoolean(keyname, false);
    }

    public void setAdminUsername(String username) {
        mSharedPreference.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getAdminUsername() {
        return mSharedPreference.getString(KEY_USERNAME, KEY_DEFAULT);
    }

    /*
    * false = when not loggedin
    * true = loggedin
    * */
    public boolean getLoginStatus() {
        //logged in
        return mSharedPreference.getString(KEY_USERNAME, KEY_DEFAULT) != null;
    }

}