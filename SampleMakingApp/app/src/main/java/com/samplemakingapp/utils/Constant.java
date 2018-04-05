package com.samplemakingapp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;

/**
 * Created by pro22 on 24/6/17.
 */

public class Constant {

    static  ProgressDialog progressDialog;
    public static final String BASE_URL = "http://100.16.174.227:5000/";

    public static final String SignUp = BASE_URL + "signup";
    public static final int REQ_SignUp = 1;


    public static boolean isConnectingToInternet(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }

        return false;

    }

    public static boolean checkActivation(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);

        boolean isActivityFound = false;

        if (services.get(0).topActivity.getPackageName().toString().equalsIgnoreCase(context.getPackageName().toString())) {
            isActivityFound = true;
        }
        Log.e(" FCM ", "Activity open: " + isActivityFound);  // true  foreground n false background

        return isActivityFound;
    }

    public static void alertDialog(Context context,String mTitle,String mPositiveButton, String msg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(mTitle);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton(mPositiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static void alertDialogSuccess(final Context context, String mTitle, String mPositiveButton, String msg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(mTitle);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setPositiveButton(mPositiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.dismiss();
                ((Activity)context).finish();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public static void alertWithIntent(final Context context,String mPositiveButton, String mNegativeButton,
                                       String mTitle, String msg, final Class className, final Bundle bundle) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(mTitle);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(mPositiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1)
            {
                arg0.dismiss();
                Intent intent=new Intent(context,className);
                if(bundle != null){
                    intent.putExtras(bundle);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

            }
        });
        if(!mNegativeButton.trim().isEmpty()){
            alertDialogBuilder.setNegativeButton(mNegativeButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1)
                {
                    arg0.dismiss();

                }
            });
        }
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public static void hideKeyboard(Context context, View view){
        InputMethodManager inputManager = (InputMethodManager)
                context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (view!=null)
        {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void Progressdialog(Context context){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(new ContextThemeWrapper(context, android.R.style.Theme_Holo_Light_Dialog));
        } else {
            progressDialog = new ProgressDialog(context);
        }
        progressDialog.setMessage("Sending request to server");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public static void progressDismiss(){

        progressDialog.dismiss();
    }
}