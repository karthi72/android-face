package com.samplemakingapp.myaccount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.samplemakingapp.R;
import com.samplemakingapp.contactus.ContactUs;
import com.samplemakingapp.help.HelpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAccount extends AppCompatActivity {

    @BindView(R.id.txHeader)
    TextView txHeader;
    @BindView(R.id.ivarrow)
    ImageView ivarrow;
    @BindView(R.id.txNext)
    TextView txNext;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvGender)
    TextView tvGender;
    @BindView(R.id.tvAge)
    TextView tvAge;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.ivgHelp)
    ImageView ivgHelp;
    @BindView(R.id.ivgContactus)
    ImageView ivgContactus;
    @BindView(R.id.ivgFQ)
    ImageView ivgFQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        ButterKnife.bind(this);

        txHeader.setText(R.string.myaccount);
        ivarrow.setVisibility(View.GONE);

        /*Bundle bundle =getIntent().getExtras();

        tvName.setText(bundle.getString("name"));*/
        tvName.setText(getIntent().getStringExtra("name"));

        tvAddress.setText(getIntent().getStringExtra("address"));
        tvGender.setText(getIntent().getStringExtra("gender"));


        Log.e("age",getIntent().getStringExtra("age"));
        tvAge.setText(getIntent().getStringExtra("age").trim());

    }

    @OnClick({R.id.txHeader, R.id.ivarrow, R.id.txNext, R.id.tvName, R.id.tvGender, R.id.tvAge, R.id.tvAddress, R.id.ivgHelp, R.id.ivgContactus, R.id.ivgFQ})
    public void onViewClicked(View view) {

        Intent intent;
        switch (view.getId()) {
            case R.id.txHeader:

                break;
            case R.id.ivarrow:
                break;
            case R.id.txNext:
                break;
            case R.id.tvName:
                break;
            case R.id.tvGender:
                break;
            case R.id.tvAge:
                break;
            case R.id.tvAddress:
                break;
            case R.id.ivgHelp:

                intent=new Intent(MyAccount.this,HelpActivity.class);
                startActivity(intent);

                break;
            case R.id.ivgContactus:

                 intent=new Intent(MyAccount.this,ContactUs.class);
                intent.putExtra("key","contact_us");
                startActivity(intent);

                break;
            case R.id.ivgFQ:

                 intent=new Intent(MyAccount.this,ContactUs.class);
                intent.putExtra("key","faq");
                startActivity(intent);
                break;
        }
    }
}
