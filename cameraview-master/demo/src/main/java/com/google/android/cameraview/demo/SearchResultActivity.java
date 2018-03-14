/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.cameraview.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SearchResultActivity extends AppCompatActivity {
    //{"PII":{"address":"7w","age":"54","person_name":"tannu","sex":"female"},"message":"OK","status":0,"subject_id":3,"unable_detect_images":[]}
    TextView takeImages, submitBtn, mBtnSearch;
    TextView name_edit, age_edit, address_edit, gender_edit;
    String name = "", age = "", sex = "", address = "";
    //    String gender[] = {"Sex", "Male", "Female"};
//    MaterialSpinner spniner;
    DataHolderBitmap myHolder;
    JSONObject jsonObjectFinal = new JSONObject();
    RequestQueue mRequestQueue;
//    ArrayList<Bitmap> mImageList = new ArrayList<>();

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        myHolder = new DataHolderBitmap();


        name_edit = (TextView) findViewById(R.id.name_edit);
        age_edit = (TextView) findViewById(R.id.age_edit);
        address_edit = (TextView) findViewById(R.id.address_edit);
        gender_edit = (TextView) findViewById(R.id.gender_edit);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        submitBtn = (TextView) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        takeImages = (TextView) findViewById(R.id.btn_enrollMe);
        mBtnSearch = (TextView) findViewById(R.id.btn_search);
        takeImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = name_edit.getText().toString().trim();
                age = age_edit.getText().toString().trim();
                address = address_edit.getText().toString().trim();

                if (name.isEmpty()) {

                    Toast.makeText(SearchResultActivity.this, R.string.enter_name,
                            Toast.LENGTH_SHORT).show();
                } else if (sex.isEmpty()) {

                    Toast.makeText(SearchResultActivity.this, R.string.enter_sex,
                            Toast.LENGTH_SHORT).show();
                } else if (age.isEmpty()) {

                    Toast.makeText(SearchResultActivity.this, R.string.enter_age,
                            Toast.LENGTH_SHORT).show();
                } else if (address.isEmpty()) {

                    Toast.makeText(SearchResultActivity.this, R.string.enter_address,
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(SearchResultActivity.this, EnrollMeActivity.class);
                    startActivityForResult(intent, 2);

                }

            }
        });

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SearchResultActivity.this, SearchActivity.class);
//                startActivityForResult(intent, 2);
            }
        });

        submitdata();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if (requestCode == 2 && resultCode == RESULT_OK) {


//            Log.e("my list ", "" + EnrollMeActivity.mLsit);
//            mImageList.addAll(EnrollMeActivity.mLsit);
//
//            EnrollMeActivity.mLsit.clear();

        }
    }

    public void submitdata() {
        JSONArray jsonObjectimages = new JSONArray();
        for (int i = 0; i < IntroActivity.mEcodedImagesLsit.size(); i++) {
            JSONArray jsonArray = new JSONArray();
            jsonArray.put("image_" + Calendar.getInstance().getTimeInMillis() + ".jpg");
            jsonArray.put(IntroActivity.mEcodedImagesLsit.get(i));
            jsonObjectimages.put(jsonArray);
        }
        Log.e("arrayblist", "" + jsonObjectimages.length());
        try {
            jsonObjectFinal.put("images", jsonObjectimages);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SearchImages();
    }


    private void SearchImages() {
        progressBar.setVisibility(View.VISIBLE);
        String tag_json_obj = "jobj_req";
        String URL = "http://100.16.174.227:5000/trueid/predict";
        try {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(URL, jsonObjectFinal,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Reg response ->", response.toString());
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                int status = jsonObject.getInt("status");
                                String msg = jsonObject.getString("message");
                                if (jsonObject.has("PII")) {
                                    JSONObject jsonObjectPII = jsonObject.getJSONObject("PII");

                                    if (jsonObjectPII.has("person_name")) {
                                        name_edit.setText(jsonObjectPII.getString("person_name"));
                                    } else if (jsonObjectPII.has("sex")) {
                                        gender_edit.setText(jsonObjectPII.getString("sex"));
                                    } else if (jsonObjectPII.has("age")) {
                                        age_edit.setText(jsonObjectPII.getString("age"));
                                    } else if (jsonObjectPII.has("address")) {
                                        address_edit.setText(jsonObjectPII.getString("address"));
                                    }
                                } else {
                                    Toast.makeText(SearchResultActivity.this, R.string.unable_to_detect_face,
                                            Toast.LENGTH_LONG).show();
                                }
                                progressBar.setVisibility(View.GONE);


                                Log.e("response ", "" + msg);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("TAG", "Error:   " + error.getMessage());
                    try {
                        final int httpStatusCode = error.networkResponse.statusCode;
                        Log.e("status code", "" + httpStatusCode);
                        if (httpStatusCode == 500) {
                            Toast.makeText(SearchResultActivity.this, R.string.error,
                                    Toast.LENGTH_LONG).show();

                            // Http status code 401: Unauthorized.
                        } else {
                            Toast.makeText(SearchResultActivity.this, "error " + httpStatusCode,
                                    Toast.LENGTH_LONG).show();


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    progressBar.setVisibility(View.GONE);

                }
            })

            {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");
                    return headers;
                }
            };
// Adding request to request queue
            jsonObjReq.setRetryPolicy(
                    new DefaultRetryPolicy(36000000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            addToRequestQueue(jsonObjReq,
                    tag_json_obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(com.android.volley.Request<T> req, String tag) {
// set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? "TAG" : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(com.android.volley.Request<T> req) {
        req.setTag("TAG");
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }


    private void clearEditText() {

        name_edit.setText("");
        age_edit.setText("");
        gender_edit.setText("");
        address_edit.setText("");
        EnrollMeActivity.mEcodedImagesLsit.clear();


    }


}
