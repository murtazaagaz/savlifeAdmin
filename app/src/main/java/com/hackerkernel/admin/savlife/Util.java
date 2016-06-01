package com.hackerkernel.admin.savlife;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Murtaza on 6/1/2016.
 */
public class Util {
    private static final String TAG = Util.class.getSimpleName();
    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) MyApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() &&
                networkInfo.isConnected();
    }

    public static void showRedSnackbar(View layoutForSnacbar, String message){
        Snackbar snack = Snackbar.make(layoutForSnacbar,message,Snackbar.LENGTH_LONG);
        ViewGroup group = (ViewGroup) snack.getView();
        group.setBackgroundColor(ContextCompat.getColor(MyApplication.getAppContext(), com.hackerkernel.com.savlifeadmin.R.color.primary));
        snack.show();
    }

    public static void showParsingErrorAlert(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(com.hackerkernel.com.savlifeadmin.R.string.oops))
                .setMessage(context.getString(com.hackerkernel.com.savlifeadmin.R.string.dont_worry_engineers_r_working)  )
                .setNegativeButton(context.getString(com.hackerkernel.com.savlifeadmin.R.string.report_issue), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO:: take user to report issue area
                    }
                })
                .setPositiveButton(context.getString(com.hackerkernel.com.savlifeadmin.R.string.try_again), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static void noInternetSnackBar(final Activity activity, View snackBarLayout){
        final Snackbar snackbar = Snackbar.make(snackBarLayout, com.hackerkernel.com.savlifeadmin.R.string.no_internet_connection, Snackbar.LENGTH_INDEFINITE);
        snackbar.setActionTextColor(ContextCompat.getColor(activity, com.hackerkernel.com.savlifeadmin.R.color.primary));
        snackbar.show();


    }
}
