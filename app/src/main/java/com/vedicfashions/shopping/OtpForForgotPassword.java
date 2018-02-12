package com.vedicfashions.shopping;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Tools;
import utils.Utils;

/*
 * Created by welcome on 14-10-2016.
 */
public class OtpForForgotPassword extends AppCompatActivity implements View.OnClickListener {

    public EditText etOtp;
    TextView txtResendOtp, txtVerifyOTP, txtContactUs;
    ImageView ImgProgressBar;
    ScrollView scrollMainView;
    RelativeLayout relativeImageProgressBar;
    private Animation fab_open, fab_close;
    int ThisCount = 0;
    ProgressDialog loading;
    boolean IsName = true;
    public static String strUserOTPOTP = "";
    private static int OTP_TIME_OUT = 8000;
    RelativeLayout relativeMain;
    String ThisID = "", Thisphone = "", Thisotp = "";
    final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        //getWindow().setBackgroundDrawableResource(R.drawable.login_bg);
        FetchXmlID();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            insertDummyContactWrapper();
        }
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        scrollMainView.setVisibility(View.GONE);
        relativeImageProgressBar.setVisibility(View.VISIBLE);

        ImageProgressBar();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ThisID = getIntent().getExtras().getString("ID");
            Thisphone = getIntent().getExtras().getString("phone");
            Thisotp = getIntent().getExtras().getString("otp");
        }


        Tools.systemBarLolipop(this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollMainView.setVisibility(View.VISIBLE);
                scrollMainView.startAnimation(fab_open);

                relativeImageProgressBar.setVisibility(View.GONE);
                relativeImageProgressBar.startAnimation(fab_close);
                IsName = false;
            }
        }, OTP_TIME_OUT);

        f1();

        txtResendOtp.setOnClickListener(this);
        txtVerifyOTP.setOnClickListener(this);
        txtContactUs.setOnClickListener(this);
    }

    private void ImageProgressBar() {

        final int[] imagarry = {R.drawable.progressbar_indeterminate_holo1, R.drawable.progressbar_indeterminate_holo2, R.drawable.progressbar_indeterminate_holo3,
                R.drawable.progressbar_indeterminate_holo4, R.drawable.progressbar_indeterminate_holo5, R.drawable.progressbar_indeterminate_holo6,
                R.drawable.progressbar_indeterminate_holo7, R.drawable.progressbar_indeterminate_holo8};
        final Handler hand = new Handler();
        Runnable run = new Runnable() {
            int i = 0;

            @Override
            public void run() {
                // TODO Auto-generated method stub
                ImgProgressBar.setImageResource(imagarry[i]);
                i++;
                if (i > (imagarry.length - 1)) {
                    i = 0;
                }
                hand.postDelayed(this, 150);
            }
        };
        hand.postDelayed(run, 150);
    }

    private void FetchXmlID() {

        ImgProgressBar = (ImageView) findViewById(R.id.ImgProgressBar);
        etOtp = (EditText) findViewById(R.id.etOtp);
        txtResendOtp = (TextView) findViewById(R.id.txtResendOtp);
        txtVerifyOTP = (TextView) findViewById(R.id.txtVerifyOTP);
        txtContactUs = (TextView) findViewById(R.id.txtContactUs);

        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);

        scrollMainView = (ScrollView) findViewById(R.id.scrollMainView);
        relativeImageProgressBar = (RelativeLayout) findViewById(R.id.relativeImageProgressBar);

    }


    @TargetApi(Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, android.Manifest.permission.READ_SMS))
            permissionsNeeded.add("Read SMS");
        if (!addPermission(permissionsList, android.Manifest.permission.RECEIVE_SMS))
            permissionsNeeded.add("Receive SMS");


        if (permissionsList.size() > 0) {
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();


                perms.put(android.Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(android.Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                        && perms.get(android.Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Utils.showToastShort("Auto Read SMS Permission is Denied", OtpForForgotPassword.this);
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void f1() {
        f2();
    }

    private void f2() {
        // TODO Auto-generated method stub
        new CountDownTimer(1000, 1000) {
            public void onTick(long arg0) {
                if (Utils.isNetworkAvailable(OtpForForgotPassword.this)) {
                    if (!etOtp.getText().toString().equalsIgnoreCase("")) {

                        if (IsName) {
                            if (etOtp.getText().toString().equalsIgnoreCase(Thisotp)) {
                                IsName = false;
                                Intent mainIntent = new Intent(OtpForForgotPassword.this, ResetPassword.class);
                                mainIntent.putExtra("ID", ThisID);
                                mainIntent.putExtra("phone", Thisphone);
                                startActivity(mainIntent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }
                        }
                    }
                }
            }
            @Override
            public void onFinish() {
                f1();
            }
        }.start();
    }


    public void recivedSms(String message) {
        try {

            scrollMainView.setVisibility(View.VISIBLE);
            relativeImageProgressBar.setVisibility(View.GONE);

            strUserOTPOTP = message;
            etOtp.setText("");
            etOtp.setText(strUserOTPOTP);

            Log.d("strUserOTPOTP", strUserOTPOTP);

        } catch (Exception e) {
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtVerifyOTP:


                if (Utils.isNetworkAvailable(OtpForForgotPassword.this)) {

                    if (etOtp.getText().toString().equalsIgnoreCase(Thisotp)) {
                        IsName = false;

                        Intent mainIntent = new Intent(OtpForForgotPassword.this, ResetPassword.class);
                        mainIntent.putExtra("ID", ThisID);
                        mainIntent.putExtra("phone", Thisphone);
                        startActivity(mainIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left);
                    } else {


                        Utils.ShowSnakBar("Invalid OTP", relativeMain, OtpForForgotPassword.this);
                    }


                }

                break;

            case R.id.txtResendOtp:
                if (Utils.isNetworkAvailable(OtpForForgotPassword.this)) {
                    if (ThisCount < 2) {
                        ResendOTP mResendOTP = new ResendOTP();
                        mResendOTP.execute();
                        ThisCount++;
                    } else {
                        txtResendOtp.setVisibility(View.GONE);
                    }

                } else {

                }
                break;
            case R.id.txtContactUs:
                IsName = false;
                startActivity(new Intent(OtpForForgotPassword.this, ContactUs.class));
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IsName = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        IsName = false;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        IsName = false;
        Intent intent = new Intent(OtpForForgotPassword.this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }


    private class ResendOTP extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {

            loading = ProgressDialog.show(OtpForForgotPassword.this, "", "Please wait...", false, false);
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {

                IApiMethods methods = restAdapter.create(IApiMethods.class);
                Api_Model curators = methods.ResendOTPApi("forget", "resend", "forget", Thisphone);

                return curators;
            } catch (Exception E) {
                return null;
            }
        }


        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {

            } else {
                if (curators.msgcode.toString().equals("0")) {
                    etOtp.setText("");
                    IsName = true;

                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, OtpForForgotPassword.this);

                    Thisotp = curators.otp;

                } else {

                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, OtpForForgotPassword.this);

                }


            }


        }


    }


}
