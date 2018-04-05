package com.samplemakingapp.signup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.samplemakingapp.R;
import com.samplemakingapp.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.ivarrow)
    ImageView ivarrow;
    @BindView(R.id.txHeader)
    TextView txHeader;
    @BindView(R.id.edName)
    EditText edName;
    @BindView(R.id.rbFemale)
    RadioButton rbFemale;
    @BindView(R.id.rbmale)
    RadioButton rbmale;
    @BindView(R.id.edAge)
    EditText edAge;
    @BindView(R.id.address)
    EditText address;
    @BindView(R.id.txNext)
    TextView txNext;
    @BindView(R.id.ivgCamera)
    ImageView ivgCamera;

    String gender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        txHeader.setText(R.string.signup);
    }

    @OnClick({R.id.ivarrow, R.id.txHeader, R.id.edName, R.id.rbFemale, R.id.rbmale, R.id.edAge, R.id.address, R.id.txNext, R.id.ivgCamera})
    public void onViewClicked(View view) {

        Intent intent;
        switch (view.getId()) {
            case R.id.ivarrow:
                finish();
                break;
            case R.id.txHeader:
                break;
            case R.id.edName:
                break;
            case R.id.rbFemale:

                break;
            case R.id.rbmale:
                break;
            case R.id.edAge:
                break;
            case R.id.address:
                break;
            case R.id.txNext:

                break;
            case R.id.ivgCamera:

                if (rbmale.isChecked()) {

                    gender = "male";
                }
                if (rbFemale.isChecked()) {

                    gender = "female";
                }

                if (edName.getText().toString().isEmpty()) {

                    Toast.makeText(SignupActivity.this, getString(R.string.name_validation) + " " + getString(R.string.before_proceeding),
                            Toast.LENGTH_SHORT).show();
                } else if (gender.isEmpty()) {

                    Toast.makeText(SignupActivity.this, getString(R.string.gender_validation) + " " + getString(R.string.before_proceeding),
                            Toast.LENGTH_SHORT).show();
                } else if (edAge.getText().toString().isEmpty()) {

                    Toast.makeText(SignupActivity.this, getString(R.string.age_validation) + " " + getString(R.string.before_proceeding),
                            Toast.LENGTH_SHORT).show();
                } else if (address.getText().toString().isEmpty()) {

                    Toast.makeText(SignupActivity.this, getString(R.string.address_validation) + " " + getString(R.string.before_proceeding),
                            Toast.LENGTH_SHORT).show();
                } else {

                    intent = new Intent(SignupActivity.this, LoginActivity.class);
                    intent.putExtra("key", "signup");
                    intent.putExtra("name", edName.getText().toString());
                    intent.putExtra("gender", gender);
                    intent.putExtra("age", edAge.getText().toString());
                    intent.putExtra("address", address.getText().toString());
                    startActivity(intent);

                }
                break;
        }
    }

}
