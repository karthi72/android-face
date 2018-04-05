package com.samplemakingapp.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import com.crashlytics.android.Crashlytics;
import com.samplemakingapp.R;

import io.fabric.sdk.android.Fabric;
import java.io.File;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class App extends Application {

    public static String ENQUIRIES = null, QUOTATIONS = null;
    public static App mInstance;
    public static Context mcontext;
    public static String latitude = "", longitude = "";
    public static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
//        Fabric.with(this, new Crashlytics());
        mInstance = this;
        mcontext = getApplicationContext();
        //calligraphy for fonts
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/RobotoCondensed-Light.ttf")
                .setDefaultFontPath("fonts/Ubuntu_R.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
//        FontsOverride.setDefaultFont(this, "MONOSPACE", "Fonts/Ubuntu_R.ttf");


        File directory = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            directory = new File(Environment.getExternalStorageDirectory() + File.separator + "SampleMakingApp");
            if (!directory.exists())
                directory.mkdirs();
        }
        else {
            directory = getApplicationContext().getDir("SampleMakingApp", Context.MODE_PRIVATE);
            if (!directory.exists())
                directory.mkdirs();
        }

        if (directory != null) {
            File books = new File(directory + File.separator + "Quotations");
            File video = new File(directory + File.separator + "Enquiries");

            if (!books.exists())
                books.mkdirs();

            if (!video.exists())
                video.mkdirs();

            QUOTATIONS = directory + File.separator + "Quotations";
            ENQUIRIES = directory + File.separator + "Enquiries";
        }
    }

    public static SharedPreferences getGlobalPefs() {
        return getContext().getSharedPreferences("SampleMakingApp", 0);
    }

    public static App getInstance() {
        return mInstance;
    }

    public static Context getContext() {
        return mcontext;
    }

    public static SharedPreferences getIdPrefs() {
        return getContext().getSharedPreferences("SampleMakingApp1", 0);
    }

    public static SharedPreferences getIdPref() {
        return getContext().getSharedPreferences("SampleMakingApp2", 0);
    }

    public static String getQUOTATIONS() {
        return QUOTATIONS;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
