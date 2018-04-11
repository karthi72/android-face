package com.samplemakingapp.login;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.otaliastudios.cameraview.Size;
import com.samplemakingapp.R;
import com.samplemakingapp.SplashActivity;
import com.samplemakingapp.login.facedetection.CameraSourcePreview;
import com.samplemakingapp.login.facedetection.FaceGraphic;
import com.samplemakingapp.login.facedetection.GraphicOverlay;
import com.samplemakingapp.myaccount.MyAccount;
import com.samplemakingapp.signup.SignupActivity;
import com.samplemakingapp.utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements
        ActivityCompat.OnRequestPermissionsResultCallback {

    @BindView(R.id.txHeader)
    TextView txHeader;
    @BindView(R.id.toolbar)
    RelativeLayout mToolBar;
    @BindView(R.id.ivarrow)
    ImageView ivarrow;
    @BindView(R.id.txNext)
    TextView txNext;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.txt_enable_camera)
    TextView mTxtEnableCamera;
    @BindView(R.id.ivgCamera)
    ImageView mBtnCapture;

    @BindView(R.id.root)
    LinearLayout root;

    public static ArrayList<String> mEcodedImagesLsit = new ArrayList<>();
    private static final String TAG = "LoginActivity";
    @BindView(R.id.faceOverlay)
    GraphicOverlay mGraphicOverlay;
    @BindView(R.id.preview)
    CameraSourcePreview mPreview;
    private Handler mBackgroundHandler;
    Random mRandom = new Random();
    JSONObject jsonObjectFinal = new JSONObject();
    RequestQueue mRequestQueue;
    private CameraSource mCameraSource = null;
    private static final int RC_HANDLE_GMS = 9001;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    CameraSource.ShutterCallback mShutterCallback;
    CameraSource.PictureCallback mPictureCallback;

    String mName = "", mGender = "", mAge = "", mAddress = "";
    Toast mToast;
    int mScreenWidth = 0, mScreenHeight = 0;
    public String mValidFaceFlag = "", mTaskFlag = "";
    boolean mSingupCounterRunning = false;
    boolean mLoginCounterRunning = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mTaskFlag = getIntent().getStringExtra("key");

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mScreenWidth = size.x;
        mScreenHeight = size.y;
        mToast = new Toast(LoginActivity.this);
        if (getIntent().getStringExtra("key").equals("signup")) {
            mName = getIntent().getStringExtra("name");
            mGender = getIntent().getStringExtra("sex");
            mAge = getIntent().getStringExtra("age");
            mAddress = getIntent().getStringExtra("address");
            txHeader.setText(R.string.signup);
        } else {
            txHeader.setText(R.string.login);
            txNext.setVisibility(View.GONE);
        }
        txNext.setVisibility(View.GONE);
        initCamera();

    }
private void initCamera(){
    int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
    if (rc == PackageManager.PERMISSION_GRANTED) {
        createCameraSource();
    } else {
        requestCameraPermission();
    }
    mShutterCallback = new CameraSource.ShutterCallback() {
        @Override
        public void onShutter() {
            Log.e("takePicture", "takePicture");
        }
    };
    mPictureCallback = new CameraSource.PictureCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onPictureTaken(byte[] bytes) {
            Log.e("onPictureTaken", "onPictureTaken");
//                if (mCameraSource != null) {
//                    try {
//                        mCameraSource.release();
//                    } catch (NullPointerException ignored) {  }
//                    mCameraSource = null;
//                }
//                if (cameraSource == null) {
//                    releaseCamera();
//                    return;
//                }
//
//                mCameraSource = cameraSource;
//                mCameraSource.start(mSurfaceView.getHolder());
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                e.printStackTrace();
            }
            onPicture(bytes);
        }
    };
}
    private void onPicture(byte[] data) {
        Log.d(TAG, "onPictureTaken " + data.length);
        try {
            ByteArrayInputStream bs = new ByteArrayInputStream(data);

            android.support.media.ExifInterface exif = new android.support.media.ExifInterface(bs);
            Matrix matrix = new Matrix();

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "E" +
                        "xif: " + orientation);
            }

            Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            bm = Bitmap.createScaledBitmap(bm, 650, 850, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] b = baos.toByteArray();
            String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("LOOK", imageEncoded);
            mEcodedImagesLsit.add(imageEncoded);
            final Bitmap bitmap = bm;
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                            "CameraDemopicture" + Calendar.getInstance().getTimeInMillis() + ".jpg");

                    if (!file.exists())     //check if file already exists
                    {
                        file.mkdirs();     //if not, create it
                    }
                    OutputStream outputStream = null;
                    try {
                        ContentValues values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "picture" + Calendar.getInstance().getTimeInMillis() + ".jpg");
                        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                        Uri filepath = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        outputStream = getContentResolver().openOutputStream(filepath);

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                        outputStream.close();
                    } catch (IOException e) {
                        Log.w(TAG, "Cannot write to " + file, e);
                    } finally {
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private void createCameraSource() {
        Context context = getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());


        if (!detector.isOperational()) {
            Log.w(TAG, "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(30.0f)
                .build();
    }

    private void requestCameraPermission() {
        Log.w(TAG, "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.ok, listener)
                .show();
    }

    private void updateCaptureButton(String mMsg) {
        mBtnCapture.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, mMsg + ", " + getString(R.string.try_again), Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();

    }

    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker(mGraphicOverlay);
        }
    }

    private class GraphicFaceTracker extends Tracker<Face> {
        private GraphicOverlay mOverlay;
        private FaceGraphic mFaceGraphic;

        GraphicFaceTracker(GraphicOverlay overlay) {
            mOverlay = overlay;
            mFaceGraphic = new FaceGraphic(overlay);
        }

        /**
         * Start tracking the detected face instance within the face overlay.
         */
        @Override
        public void onNewItem(int faceId, Face item) {
            mFaceGraphic.setId(faceId);
        }

        /**
         * Update the position/characteristics of the face within the overlay.
         */
        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
            mOverlay.add(mFaceGraphic);
            mFaceGraphic.updateFace(face);

            if (detectionResults.getDetectedItems().size() == 0) {
                mValidFaceFlag = "";
                Log.i(TAG, "No faces detected");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBtnCapture.setVisibility(View.INVISIBLE);
                        mTxtEnableCamera.setVisibility(View.VISIBLE);
                        mTxtEnableCamera.setText(getString(R.string.no_faces_detected));
                        if (mSingupCounterRunning) {
                            mSingupCounterRunning = false;
                            mSignupCounter.cancel();
                            updateCaptureButton(getString(R.string.no_faces_detected));
                        } else if (mLoginCounterRunning) {
                            mLoginCounterRunning = false;
                            mLoginCounter.cancel();
                            updateCaptureButton(getString(R.string.no_faces_detected));
                        }
                    }
                });
            } else if (detectionResults.getDetectedItems().size() > 1) {
                mValidFaceFlag = "more";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBtnCapture.setVisibility(View.INVISIBLE);
                        mTxtEnableCamera.setVisibility(View.VISIBLE);
                        mTxtEnableCamera.setText(getString(R.string.there_are_more_than_one_faces));
                        if (mSingupCounterRunning) {
                            mSingupCounterRunning = false;
                            mSignupCounter.cancel();
                            updateCaptureButton(getString(R.string.there_are_more_than_one_faces));
                        } else if (mLoginCounterRunning) {
                            mLoginCounterRunning = false;
                            mLoginCounter.cancel();
                            updateCaptureButton(getString(R.string.there_are_more_than_one_faces));
                        }
                    }
                });
            } else if (detectionResults.getDetectedItems().size() > 0) {
                if (face.getHeight() > mScreenHeight / 3 && face.getWidth() > mScreenWidth / 3) {
                    mValidFaceFlag = "close";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBtnCapture.setVisibility(View.INVISIBLE);
                            mTxtEnableCamera.setVisibility(View.VISIBLE);
                            mTxtEnableCamera.setText(getString(R.string.your_face_is_too_close_to_camera));
                            if (mSingupCounterRunning) {
                                mSingupCounterRunning = false;
                                mSignupCounter.cancel();
                                updateCaptureButton(getString(R.string.your_face_is_too_close_to_camera));
                            } else if (mLoginCounterRunning) {
                                mLoginCounterRunning = false;
                                mLoginCounter.cancel();
                                updateCaptureButton(getString(R.string.your_face_is_too_close_to_camera));
                            }

                        }
                    });
                } else if (face.getHeight() < mScreenHeight / 3 && face.getWidth() < mScreenWidth / 3) {
                    mValidFaceFlag = "far";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBtnCapture.setVisibility(View.INVISIBLE);
                            mTxtEnableCamera.setVisibility(View.VISIBLE);
                            mTxtEnableCamera.setText(getString(R.string.your_face_is_very_far_to_camera));
                            if (mSingupCounterRunning) {
                                mSingupCounterRunning = false;
                                mSignupCounter.cancel();
                                updateCaptureButton(getString(R.string.your_face_is_very_far_to_camera));
                            } else if (mLoginCounterRunning) {
                                mLoginCounterRunning = false;
                                mLoginCounter.cancel();
                                updateCaptureButton(getString(R.string.your_face_is_very_far_to_camera));
                            }
                        }
                    });
                } else {
                    mValidFaceFlag = "ok";
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mBtnCapture.setVisibility(View.VISIBLE);
                            mTxtEnableCamera.setVisibility(View.GONE);
                            mTxtEnableCamera.setText("");
                        }
                    });
                }
            }
        }

        /**
         * Hide the graphic when the corresponding face was not detected.  This can happen for
         * intermediate frames temporarily (e.g., if the face was momentarily blocked from
         * view).
         */
        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
            mOverlay.remove(mFaceGraphic);
            mValidFaceFlag = "";
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBtnCapture.setVisibility(View.INVISIBLE);
                    mTxtEnableCamera.setVisibility(View.VISIBLE);
                    mTxtEnableCamera.setText(getString(R.string.no_faces_detected));
                    if (mSingupCounterRunning) {
                        mSingupCounterRunning = false;
                        mSignupCounter.cancel();
                        updateCaptureButton(getString(R.string.no_faces_detected));
                    } else if (mLoginCounterRunning) {
                        mLoginCounterRunning = false;
                        mLoginCounter.cancel();
                        updateCaptureButton(getString(R.string.no_faces_detected));
                    }
                }
            });

        }

        @Override
        public void onDone() {
            mOverlay.remove(mFaceGraphic);
        }
    }

    private void startCameraSource() {
        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    @OnClick({R.id.txHeader, R.id.ivarrow, R.id.txNext, R.id.progressBar, R.id.ivgCamera})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.txHeader:
                break;

            case R.id.ivarrow:
                intent = new Intent(LoginActivity.this, SplashActivity.class);
                startActivity(intent);
                break;

            case R.id.txNext:
                if (getIntent().getStringExtra("key").equals("signup")) {
                    submitdata();
                } else {
                    submitdataLogin();
                }
                break;

            case R.id.progressBar:
                break;

            case R.id.ivgCamera:
                if (mValidFaceFlag.trim().equalsIgnoreCase("ok")) {
                    Log.e("key", getIntent().getStringExtra("key"));
                    if (mTaskFlag.equals("signup")) {
                        if (mCameraSource != null) {
                            mEcodedImagesLsit.clear();
                            mBtnCapture.setEnabled(false);
                            mSignupCounter.start();
                        }
                    } else {
                        if (mCameraSource != null) {
                            mBtnCapture.setEnabled(false);
                            mEcodedImagesLsit.clear();
                            mLoginCounter.start();
                        }
                    }
                }
                break;
        }
    }

    public void submitdataLogin() {
        JSONArray jsonObjectimages = new JSONArray();
        int mListSize = mEcodedImagesLsit.size();
        if (mListSize > 3) {
            mListSize = 3;
        }

        for (int i = 0; i < mListSize; i++) {
            JSONArray jsonArray = new JSONArray();
            int i1 = mRandom.nextInt(999 - 1 + 1) + 1;
            jsonArray.put("image_" + +Calendar.getInstance().getTimeInMillis() + i1 + ".jpg");
            jsonArray.put(mEcodedImagesLsit.get(i));
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
        Constant.Progressdialog(LoginActivity.this);
        // progressBar.setVisibility(View.VISIBLE);
        String tag_json_obj = "jobj_req";
        String URL = Constant.BASE_URL + "trueid/predict";
        try {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(URL, jsonObjectFinal,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Reg response ->", response.toString());
                            try {
                                mBtnCapture.setEnabled(true);
                                JSONObject jsonObject = new JSONObject(response.toString());
                                int status = jsonObject.getInt("status");
                                String msg = jsonObject.getString("message") + "\n";
                                if (jsonObject.has("PII")) {
                                    JSONObject jsonObjectPII = jsonObject.getJSONObject("PII");
                                    String name = " ", gender = " ", age = " ", address = " ";
                                    name = (jsonObjectPII.getString("person_name"));
                                    gender = (jsonObjectPII.getString("sex"));
                                    age = (jsonObjectPII.getString("age"));
                                    address = (jsonObjectPII.getString("address"));

                                    Intent intent = new Intent(LoginActivity.this, MyAccount.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", name);
                                    bundle.putString("gender", gender);
                                    bundle.putString("age", age);
                                    bundle.putString("address", address);
                                    intent.putExtras(bundle);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);

                                } else {
                                    if (jsonObject.has("subject_id")) {
                                        if (jsonObject.getString("subject_id").trim().equals("UNKNOWN_PERSON")) {
                                            msg = jsonObject.getString("subject_id") + "\n";
                                        }
                                    }
                                    if (jsonObject.has("too_blurry_pics")) {
                                        msg = msg + " No. of blur images detected: " + jsonObject.getJSONArray("too_blurry_pics").length() + "\n";
                                    }
                                    if (jsonObject.has("too_close_pics")) {
                                        msg = msg + " No. of too close images detected: " + jsonObject.getJSONArray("too_close_pics").length() + "\n";
                                    }
                                    if (jsonObject.has("too_far_pics")) {
                                        msg = msg + " No. of too far images detected: " + jsonObject.getJSONArray("too_far_pics").length() + "\n";
                                    }
                                    if (jsonObject.has("unable_detect_images")) {
                                        msg = msg + " No. of images not detected: " + jsonObject.getJSONArray("unable_detect_images").length() + "\n";
                                    }

                                    Constant.alertDialog(LoginActivity.this, "", getString(R.string.ok), msg);
                                }
                                Constant.progressDismiss();
                                // progressBar.setVisibility(View.GONE);

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
                        mBtnCapture.setEnabled(true);
                        if (httpStatusCode == 500) {
                            Constant.alertDialog(LoginActivity.this, "", getString(R.string.ok),
                                    getString(R.string.error_string) + httpStatusCode);

                            // Http status code 401: Unauthorized.
                        } else {

                            Constant.alertDialog(LoginActivity.this, "", getString(R.string.ok),
                                    getString(R.string.error_string) + httpStatusCode);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Constant.progressDismiss();
                    // progressBar.setVisibility(View.GONE);
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


    public void submitdata() {

        if (getIntent().getStringExtra("key").equals("signup")) {
            JSONArray jsonObjectimages = new JSONArray();

            Log.e("mEcodedImagesLsit", mEcodedImagesLsit.size() + " ");
            if (mEcodedImagesLsit != null && mEcodedImagesLsit.size() > 0) {
                int mListSize = mEcodedImagesLsit.size();
                if (mListSize > 10) {
                    mListSize = 10;
                }
                for (int i = 0; i < mListSize; i++) {
                    JSONArray jsonArray = new JSONArray();
                    int i1 = mRandom.nextInt(999 - 1 + 1) + 1;
                    jsonArray.put("image_" + mName + mAge + getIntent().getStringExtra("name")
                            + getIntent().getStringExtra("age") + Calendar.getInstance().getTimeInMillis() + i1 + ".jpg");
                    jsonArray.put(mEcodedImagesLsit.get(i));
                    jsonObjectimages.put(jsonArray);
                }

                try {
                    JSONObject jsonObjectPII = new JSONObject();
                    jsonObjectPII.put("person_name", getIntent().getStringExtra("name"));
                    jsonObjectPII.put("sex", getIntent().getStringExtra("gender"));
                    jsonObjectPII.put("age", getIntent().getStringExtra("age"));
                    jsonObjectPII.put("address", getIntent().getStringExtra("address"));
                    jsonObjectFinal.put("PII", jsonObjectPII);
                    jsonObjectFinal.put("images", jsonObjectimages);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AddImages();
            }
        }
    }

    private void AddImages() {

        Log.e("onfinish", "onfinish");
        Constant.Progressdialog(LoginActivity.this);
        // progressBar.setVisibility(View.VISIBLE);
        String tag_json_obj = "jobj_req";
        String URL = Constant.BASE_URL + "trueid/enroll";
        try {
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(URL, jsonObjectFinal,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Reg response ->", response.toString());
                            mBtnCapture.setEnabled(true);
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                int status = jsonObject.getInt("status");
                                String msg = jsonObject.getString("message") + "\n";
                                //    progressBar.setVisibility(View.GONE);
                                Constant.progressDismiss();

                                if (jsonObject.has("PII")) {

                                    JSONObject jsonObjectPII = jsonObject.getJSONObject("PII");
                                    String name = " ", gender = " ", age = " ", address = " ";
                                    if (jsonObjectPII.has("person_name")) {
                                        name = (jsonObjectPII.getString("person_name"));
                                    } else if (jsonObjectPII.has("name")) {
                                        name = (jsonObjectPII.getString("name"));
                                    }

                                    if (jsonObjectPII.has("sex"))
                                        gender = (jsonObjectPII.getString("sex"));

                                    if (jsonObjectPII.has("address"))
                                        address = (jsonObjectPII.getString("address"));

                                    if (jsonObjectPII.has("age"))
                                        age = (jsonObjectPII.getString("age"));

                                    Bundle bundle = new Bundle();
                                    bundle.putString("name", name);
                                    bundle.putString("gender", gender);
                                    bundle.putString("age", age);
                                    bundle.putString("address", address);

                                    Constant.alertWithIntent(LoginActivity.this, getString(R.string.yes), getString(R.string.try_another),
                                            getString(R.string.already_enrolled), jsonObject.getString("message") + " " +
                                                    getString(R.string.do_you_want_to_view_pii_for_this_enrollment), MyAccount.class, bundle);


                                } else if (status == -5) {
                                    if (jsonObject.has("too_blurry_pics")) {
                                        msg = msg + " No. of blur images detected: " + jsonObject.getJSONArray("too_blurry_pics").length() + "\n";
                                    }
                                    if (jsonObject.has("too_close_pics")) {
                                        msg = msg + " No. of too close images detected: " + jsonObject.getJSONArray("too_close_pics").length() + "\n";
                                    }
                                    if (jsonObject.has("too_far_pics")) {
                                        msg = msg + " No. of too far images detected: " + jsonObject.getJSONArray("too_far_pics").length() + "\n";
                                    }
                                    if (jsonObject.has("unable_detect_images")) {
                                        msg = msg + " No. of images not detected: " + jsonObject.getJSONArray("unable_detect_images").length() + "\n";
                                    }
                                    Constant.alertDialog(LoginActivity.this, "", getString(R.string.ok), msg);
                                } else {
                                    Constant.alertWithIntent(LoginActivity.this, getString(R.string.ok), "",
                                            getString(R.string.info), getString(R.string.successfully_enroll), SplashActivity.class, null);
                                }
                                clearEditText();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("TAG", "Error:   " + error.getMessage());

                    mBtnCapture.setEnabled(true);
                    clearEditText();
                    try {

                        final int httpStatusCode = error.networkResponse.statusCode;
                        Log.e("status code", "" + httpStatusCode);
                        if (httpStatusCode == 500) {

                            Toast.makeText(LoginActivity.this, "error : Pictures are not taken in correct way. Make sure sure your images are taken correctly",
                                    Toast.LENGTH_LONG).show();

                            // Http status code 401: Unauthorized.
                        } else {
                            Toast.makeText(LoginActivity.this, "error " + httpStatusCode,
                                    Toast.LENGTH_LONG).show();

                            finish();
                            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                            intent.putExtra("key", "signup");
                            startActivity(intent);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Constant.progressDismiss();
                    // progressBar.setVisibility(View.GONE);
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

    @Override
    protected void onPause() {

        mPreview.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    private void clearEditText() {
        mEcodedImagesLsit.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraSource();
    }

    public static class ConfirmationDialogFragment extends DialogFragment {

        private static final String ARG_MESSAGE = "message";
        private static final String ARG_PERMISSIONS = "permissions";
        private static final String ARG_REQUEST_CODE = "request_code";
        private static final String ARG_NOT_GRANTED_MESSAGE = "not_granted_message";

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Bundle args = getArguments();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(args.getInt(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String[] permissions = args.getStringArray(ARG_PERMISSIONS);
                                    if (permissions == null) {
                                        throw new IllegalArgumentException();
                                    }
                                    ActivityCompat.requestPermissions(getActivity(),
                                            permissions, args.getInt(ARG_REQUEST_CODE));
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getActivity(),
                                            args.getInt(ARG_NOT_GRANTED_MESSAGE),
                                            Toast.LENGTH_SHORT).show();
                                }
                            })
                    .create();

        }

        public static ConfirmationDialogFragment newInstance(@StringRes int message,
                                                             String[] permissions, int requestCode, @StringRes int notGrantedMessage) {
            ConfirmationDialogFragment fragment = new ConfirmationDialogFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_MESSAGE, message);
            args.putStringArray(ARG_PERMISSIONS, permissions);
            args.putInt(ARG_REQUEST_CODE, requestCode);
            args.putInt(ARG_NOT_GRANTED_MESSAGE, notGrantedMessage);
            fragment.setArguments(args);
            return fragment;
        }

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
// set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? "TAG" : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag("TAG");
        getRequestQueue().add(req);
    }

    private CountDownTimer mSignupCounter = new CountDownTimer(11000, 1000) {
        public void onTick(long millisUntilFinished) {
            mSingupCounterRunning = true;
            progressBar.setVisibility(View.VISIBLE);
            try {
                mCameraSource.takePicture(mShutterCallback, mPictureCallback);
            } catch (RuntimeException e) {
                e.printStackTrace();
                initCamera();
            } catch (Exception e) {
                e.printStackTrace();
                initCamera();
            }
            Log.e("CountDownTimer", "call");
        }

        public void onFinish() {

            progressBar.setVisibility(View.GONE);
            mSingupCounterRunning = false;
            mBtnCapture.setEnabled(true);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            alertDialogBuilder.setMessage(getString(R.string.click_next_for_proceed));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();

                    submitdata();
                }
            });

            alertDialogBuilder.setNegativeButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();

                }
            });


            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    };

    private CountDownTimer mLoginCounter = new CountDownTimer(4000, 1000) {
        public void onTick(long millisUntilFinished) {
            mLoginCounterRunning = true;
            progressBar.setVisibility(View.VISIBLE);
            try {
                mCameraSource.takePicture(mShutterCallback, mPictureCallback);
            } catch (RuntimeException e) {
                initCamera();
                e.printStackTrace();
            } catch (Exception e) {
                initCamera();
                e.printStackTrace();
            }

        }

        public void onFinish() {
            mLoginCounterRunning = false;
            progressBar.setVisibility(View.GONE);

            mBtnCapture.setEnabled(true);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
            alertDialogBuilder.setMessage(getString(R.string.click_next_for_proceed));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();

                    submitdataLogin();
                }
            });

            alertDialogBuilder.setNegativeButton(getString(R.string.try_again), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
    };

}