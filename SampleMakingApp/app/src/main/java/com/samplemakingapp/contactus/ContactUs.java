package com.samplemakingapp.contactus;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;


import com.samplemakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ContactUs extends AppCompatActivity {

    @BindView(R.id.txHeader)
    TextView txHeader;
    @BindView(R.id.ivarrow)
    ImageView ivarrow;
    @BindView(R.id.txNext)
    TextView txNext;
    @BindView(R.id.webview)
    WebView webview;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        ButterKnife.bind(this);

        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        request();

        Log.e("key ",getIntent().getStringExtra("key"));

        if(getIntent().getStringExtra("key").equals("contact_us")){
            txHeader.setText(R.string.contact_us);
            webview.loadUrl("http://www.openinference.com/contact.html");

        }
        else {
            txHeader.setText(R.string.faq);
            webview.loadUrl("http://www.openinference.com/faq.html");
        }

        webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }
            @Override
            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {

                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                // Log.e("payment url4-->",url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //  Log.e("onReceivedSslError-->","yes");
                handler.proceed(); // Ignore SSL certificate errors
            }
        });

    }

    void request() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            progressDialog = new ProgressDialog(new ContextThemeWrapper(ContactUs.this, android.R.style.Theme_Holo_Light_Dialog));
        } else {
            progressDialog = new ProgressDialog(ContactUs.this);
        }
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }


    @OnClick({R.id.txHeader, R.id.ivarrow, R.id.txNext, R.id.webview})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txHeader:
                break;
            case R.id.ivarrow:
                finish();
                break;
            case R.id.txNext:
                break;
            case R.id.webview:
                break;
        }
    }
}
