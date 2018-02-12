package com.vedicfashions.shopping;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Utils;

/*
 * Created by welcome on 13-10-2016.
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edtUserName, edtMobile, edtPassword, edtEmail, etRefer;
    String StredtUserName = "", StredtMobile = "", StredtPassword = "", StredtEmail = "", txtRefer = "";
    TextView txtConditionRed, txtCreateAccount, txtsignin, skip, showHide;
    CheckBox checkTurmCondition;

    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    ProgressDialog loading;
    RelativeLayout relativeMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        // getWindow().setBackgroundDrawableResource(R.drawable.login_bg);
        FetchXmlID();
        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        txtCreateAccount.setOnClickListener(this);
        txtsignin.setOnClickListener(this);
        txtConditionRed.setOnClickListener(this);
        skip.setOnClickListener(this);
        showHide.setOnClickListener(this);
    }

    private void FetchXmlID() {
        edtUserName = (EditText) findViewById(R.id.edtUserName);
        edtMobile = (EditText) findViewById(R.id.edtMobile);
        etRefer = (EditText) findViewById(R.id.etRefer);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        txtConditionRed = (TextView) findViewById(R.id.txtConditionRed);
        txtCreateAccount = (TextView) findViewById(R.id.txtCreateAccount);
        skip = (TextView) findViewById(R.id.skip);
        showHide = (TextView) findViewById(R.id.showhide);
        txtsignin = (TextView) findViewById(R.id.txtsignin);
        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);
        checkTurmCondition = (CheckBox) findViewById(R.id.checkTurmCondition);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.txtConditionRed:

                startActivity(new Intent(RegisterActivity.this, TermCondition.class));
                break;


            case R.id.txtCreateAccount:
                StredtUserName = edtUserName.getText().toString();
                StredtMobile = edtMobile.getText().toString();
                StredtPassword = edtPassword.getText().toString();
                StredtEmail = edtEmail.getText().toString();
                txtRefer = etRefer.getText().toString();
                if (Utils.isNetworkAvailable(RegisterActivity.this)) {
                    if (TextUtils.isEmpty(StredtUserName)) {
                        Utils.ShowSnakBar("Please enter Name", relativeMain, RegisterActivity.this);
                    } else if (TextUtils.isEmpty(StredtMobile)) {
                        Utils.ShowSnakBar("Please enter Mobile", relativeMain, RegisterActivity.this);
                    } else if (StredtMobile.length() != 10) {
                        Utils.ShowSnakBar("Invalid Mobile", relativeMain, RegisterActivity.this);
                    } else if (TextUtils.isEmpty(StredtPassword)) {
                        Utils.ShowSnakBar("Please enter Password", relativeMain, RegisterActivity.this);
                    } else if (StredtPassword.length() < 4) {
                        Utils.ShowSnakBar("Password is short!", relativeMain, RegisterActivity.this);
                    } else if (!checkTurmCondition.isChecked()) {
                        Utils.ShowSnakBar("Select " + getResources().getString(R.string.tandc), relativeMain, RegisterActivity.this);
                    } else {
                        Utils.hideKeyboard(RegisterActivity.this);
                        RegisterAsynTask mRegisterAsynTask = new RegisterAsynTask();
                        mRegisterAsynTask.execute();
                    }
                }
                break;
            case R.id.txtsignin:

                Intent i = new Intent(RegisterActivity.this, Login.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;

            case R.id.skip:

                editorLogin.putString("userID", "");
                editorLogin.putString("name", "Guest");
                editorLogin.putString("email", "");
                editorLogin.putString("phone", "");
                editorLogin.putString("userimage", "");
                editorLogin.commit();
                Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                break;
            case R.id.showhide:
                if (showHide.getText().equals("Hide")) {
                    showHide.setText("Show");
                    edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else if (showHide.getText().equals("Show")) {
                    showHide.setText("Hide");
                    edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @SuppressLint("StaticFieldLeak")
    private class RegisterAsynTask extends AsyncTask<Void, Void, Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            loading = ProgressDialog.show(RegisterActivity.this, "", "Please wait...", false, false);
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {

                IApiMethods methods = restAdapter.create(IApiMethods.class);
                return methods.GetSignUp("register", StredtUserName, StredtEmail, StredtPassword, StredtMobile, txtRefer);
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }


        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators != null) {
                if (curators.msgcode.equals("0")) {
                    try {
                        for (Api_Model.customer_detail dataset : curators.customer_detail) {

                            editorLogin.putString("userID", dataset.ID);
                            editorLogin.putString("name", dataset.name);
                            editorLogin.putString("email", dataset.email);
                            editorLogin.putString("phone", dataset.phone);
                            editorLogin.putString("userimage", dataset.image);
                            editorLogin.commit();

                            Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    } catch (Exception e) {
                        Log.e("Register", "ActivityEx__" + e.getMessage());

                    }

                } else {
                    Utils.showToastShort(curators.message, RegisterActivity.this);
                }
            }
        }
    }
}

