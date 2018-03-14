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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity implements View.OnClickListener {

    String TAG = "IntroActivity";
    int CamerPicker = 1;
    int GalleryPicker = 2;
    ImageUtility imageUtility;
    TextView takeImages, submitBtn, mBtnSearch, btn_gallary;
    ImageView iv_1;
    public static ArrayList<String> mEcodedImagesLsit = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        takeImages = (TextView) findViewById(R.id.btn_enrollMe);
        mBtnSearch = (TextView) findViewById(R.id.btn_search);
        btn_gallary = (TextView) findViewById(R.id.btn_gallary);
        iv_1 = (ImageView) findViewById(R.id.iv_1);
        btn_gallary.setOnClickListener(this);
        takeImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(IntroActivity.this, EnrollMeOne.class);
                startActivityForResult(intent, 2);


            }
        });

        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntroActivity.this, SearchActivity.class);
                startActivityForResult(intent, 2);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_gallary:
                mEcodedImagesLsit.clear();
                ImagePicker.create(this)
                        .folderMode(true) // folder mode (false by default)
                        .multi() // multi mode (default mode)
                        .limit(3) // max images can be selected (99 by default)
                        .showCamera(true) // show camera or not (true by default)
                        .enableLog(false) // disabling log
                        .start(GalleryPicker); // start image picker activity with request code

                break;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //userImageEdit.setOnClickListener(this);
        if (resultCode == RESULT_OK && requestCode == GalleryPicker) {


            List<com.esafirm.imagepicker.model.Image> images = ImagePicker.getImages(data);


            List<String> imagesList = new ArrayList<>();

            for (int i = 0; i < images.size(); i++) {
                /*ADDING IMAGE LIST TO IMAGELIST*/
                imagesList.add(images.get(i).getPath());

                /*CONVERTING IMAGE TO BITMAP*/
                String path = images.get(i).getPath();
                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inPreferredConfig = Bitmap.Config.ARGB_8888;
                /*BITMAP PATH IN STRING*/
                String bitmap = String.valueOf(BitmapFactory.decodeFile(path));
                iv_1.setImageBitmap(BitmapFactory.decodeFile(path));
                Log.e(TAG, "BITMAP       " + bitmap);

                /*METHOD TO DECODE IMAGE FROM BITMAP TO BASE64*/
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                BitmapFactory.decodeFile(path).compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteFormat = stream.toByteArray();
                String imgString = Base64.encodeToString(byteFormat, Base64.DEFAULT);
                Log.e(TAG, "IMAGES PATH" + imgString);

                /*ADDING ENCODED IMAGE TO LIST*/
                mEcodedImagesLsit.add(imgString);

            }
            Intent intent = new Intent(IntroActivity.this, SearchResultActivity.class);
            startActivity(intent);
//                                setResult(RESULT_OK,intent);
            finish();//finishing activity
            Log.e(TAG, "ENCODEDLIST     " + mEcodedImagesLsit.size());

        } else if (resultCode == RESULT_OK && requestCode == CamerPicker) {

        }


    }

}
