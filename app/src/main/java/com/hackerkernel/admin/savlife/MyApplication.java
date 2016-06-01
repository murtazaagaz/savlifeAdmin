package com.hackerkernel.admin.savlife;

import android.app.Application;
import android.content.Context;

/**
 * Created by Murtaza on 6/1/2016.
 */
public class MyApplication extends Application {
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mInstance.getApplicationContext();
    }

}
