package com.samplemakingapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.samplemakingapp.login.LoginActivity;
import com.samplemakingapp.signup.SignupActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SplashActivity extends AppCompatActivity {


    @BindView(R.id.tvLogin)
    TextView tvLogin;
    @BindView(R.id.tvSignup)
    TextView tvSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        checkPermission();
    }

    @OnClick({R.id.tvLogin, R.id.tvSignup})
    public void onViewClicked(View view) {

        Intent intent;

        switch (view.getId()) {
            case R.id.tvLogin:

                intent =new Intent(SplashActivity.this, LoginActivity.class);
                intent.putExtra("key","login");
                startActivity(intent);

                break;
            case R.id.tvSignup:

                intent =new Intent(SplashActivity.this, SignupActivity.class);
                intent.putExtra("key","signup");
                startActivity(intent);

                break;
        }
    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= 23) {

            int hasInternetPermission = checkSelfPermission(Manifest.permission.INTERNET);
            int hasReadPermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int hasWritePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasWritePermissioncamra = checkSelfPermission(Manifest.permission.CAMERA);
            int hasWifiState = checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE);

            ArrayList<String> permissionList = new ArrayList<String>();

            if (hasInternetPermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.INTERNET);

            }
            if (hasReadPermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (hasWritePermissioncamra != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.CAMERA);
            }

            if (hasWifiState != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_WIFI_STATE);
            }
            if (!permissionList.isEmpty()) {
                requestPermissions(permissionList.toArray(new String[permissionList.size()]), 4);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case 2:

                for (int i = 0; i < permissions.length; i++) {

                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {

                    } else if (grantResults[i] == PackageManager.PERMISSION_DENIED)
                    {
                    }
                }

                break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

}

