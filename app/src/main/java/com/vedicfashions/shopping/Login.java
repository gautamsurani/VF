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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.util.Arrays;

import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Utils;

/*
 * Created by welcome on 13-10-2016.
 */
public class Login extends AppCompatActivity implements View.OnClickListener {
    ImageView imgLogo;
    EditText edtEmil, edtPasssword;
    TextView txtForgotPassword, txtSignIn, txtNotMember, txtSignUp, skip, showHide;
    String StredtEmil, StredtPasssword;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    RelativeLayout relativeMain;
    ProgressDialog loading;
    private CallbackManager callbackManager;
    LoginButton loginButton;
    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
    ProgressDialog progressDialog;
    String StrFbNane = "", STrFbEmail = "", StrID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.signin);
        FetChXmlId();
        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        prefCartCounter = getApplicationContext().getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        loginButton = (LoginButton) findViewById(R.id.button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (Utils.isNetworkAvailable(Login.this)) {
                    if (loginResult.getAccessToken() != null) {
                        loginButton.setText("Sign in with Facebook");
                        GetFbData();
                    }
                } else {
                    Utils.ShowSnakBar("No Internet Connection..!!", relativeMain, Login.this);
                }
            }

            @Override
            public void onCancel() {
                //  Toast.makeText(LoginActivity.this,"Login has been cancelled..!!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("HelloUsers", error.getMessage());
                Toast.makeText(Login.this, "Something wrong happen..!!", Toast.LENGTH_SHORT).show();
            }
        });


        txtForgotPassword.setOnClickListener(this);
        txtSignIn.setOnClickListener(this);
        txtNotMember.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        skip.setOnClickListener(this);
        showHide.setOnClickListener(this);
    }

    public void GetFbData() {
        loginButton.setText("Sign in with Facebook");
//        progressDialog.show();
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                if (jsonObject != null) {
                    loginButton.setText("Sign in with Facebook");
                    try {
                        new LogInFaceBookAsynTask(jsonObject).execute("");
                    } catch (Exception e) {
                        Log.e("OK2", e.getMessage());
                    }
                } else if (graphResponse.getError() != null) {
                    //   progressDialog.dismiss();
                    switch (graphResponse.getError().getCategory()) {
                        case LOGIN_RECOVERABLE:
                            break;
                        case TRANSIENT:
                            break;
                        case OTHER:
                            break;
                    }
                }
            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void FetChXmlId() {
        skip = (TextView) findViewById(R.id.skip);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        edtEmil = (EditText) findViewById(R.id.edtEmil);
        edtPasssword = (EditText) findViewById(R.id.edtPasssword);
        txtForgotPassword = (TextView) findViewById(R.id.txtForgotPassword);
        txtSignIn = (TextView) findViewById(R.id.txtSignIn);
        txtNotMember = (TextView) findViewById(R.id.txtNotMember);
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);
        showHide = (TextView) findViewById(R.id.showhide);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.txtForgotPassword:
                Intent idd = new Intent(Login.this, ForgotActivity.class);
                startActivity(idd);
                overridePendingTransition(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left);

                break;


            case R.id.txtSignIn:
                StredtEmil = edtEmil.getText().toString();
                StredtPasssword = edtPasssword.getText().toString();

                if (Utils.isNetworkAvailable(Login.this)) {
                    if (TextUtils.isEmpty(StredtEmil)) {

                        Utils.ShowSnakBar("Please enter Email / Name", relativeMain, Login.this);
                    } else if (TextUtils.isEmpty(StredtPasssword)) {

                        Utils.ShowSnakBar("Please enter Password", relativeMain, Login.this);
                    } else {
                        Utils.hideKeyboard(Login.this);
                        LogInAsynTask mLogInAsynTask = new LogInAsynTask();
                        mLogInAsynTask.execute();
                    }

                }
                break;


            case R.id.txtNotMember:
                break;
            case R.id.txtSignUp:

                Intent i = new Intent(Login.this, RegisterActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.skip:
                editorLogin.putString("userID", "");
                editorLogin.putString("name", "Guest");
                editorLogin.putString("email", "");
                editorLogin.putString("phone", "");
                editorLogin.putString("userimage", "");
                editorLogin.commit();
                Intent mainIntent = new Intent(Login.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.showhide:
                if (showHide.getText().equals("Hide")) {
                    showHide.setText("Show");
                    edtPasssword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else if (showHide.getText().equals("Show")) {
                    showHide.setText("Hide");
                    edtPasssword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LogInFaceBookAsynTask extends AsyncTask<String, Void, Api_Model> {
        RestAdapter restAdapter;
        JSONObject objUser;

        LogInFaceBookAsynTask(JSONObject jsonObject) {
            objUser = jsonObject;
            Log.e("objUser", objUser.toString());
        }

        @Override
        protected void onPreExecute() {
            try {
                StrFbNane = objUser.getString("name");
                STrFbEmail = objUser.getString("email");
                StrID = objUser.getString("id");
                loading = ProgressDialog.show(Login.this, "", "Please wait...", false, false);
                restAdapter = new RestAdapter.Builder()
                        .setEndpoint(AppConstant.API_URL)
                        .build();
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
        }

        @Override
        protected Api_Model doInBackground(String... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                return methods.GetSocialLogin("login", "social", STrFbEmail, StrID);
            } catch (Exception E) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();

            //  Log.e("  AAsss  e",curators.msgcode.toString()+" ");
            if (curators != null) {
                Log.e("curators.message", "curators.message" + curators.message);
                if (curators.msgcode.equals("0")) {
                    editorCartCounter.putString("CartCounter", curators.cart_count);
                    editorCartCounter.commit();
                    try {
                        for (Api_Model.customer_detail dataset : curators.customer_detail) {
                            editorLogin.putString("userID", dataset.ID);
                            editorLogin.putString("name", dataset.name);
                            editorLogin.putString("email", dataset.email);
                            editorLogin.putString("phone", dataset.phone);
                            editorLogin.putString("userimage", dataset.image);
                            editorLogin.commit();
                            Intent mainIntent = new Intent(Login.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    } catch (Exception e) {
                        Log.e("Fb", "LoginEx__" + e.getMessage());
                    }
                } else {
                    Utils.ShowSnakBar(curators.message, relativeMain, Login.this);
                    loginButton.setText("Sign in with Facebook");
                    try {
                        LoginManager.getInstance().logOut();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent i = new Intent(Login.this, RegisterActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class LogInAsynTask extends AsyncTask<Void, Void, Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            loading = ProgressDialog.show(Login.this, "", "Please wait...", false, false);
            restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.API_URL).build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            Api_Model curators = null;
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                curators = methods.GetLogin("login", "signin", StredtEmil, StredtPasssword);
                return curators;
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return curators;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators != null) {
                if (curators.msgcode.equals("0")) {
                    editorCartCounter.putString("CartCounter", curators.cart_count);
                    editorCartCounter.commit();
                    try {
                        for (Api_Model.customer_detail dataset : curators.customer_detail) {

                            editorLogin.putString("userID", dataset.ID);
                            editorLogin.putString("name", dataset.name);
                            editorLogin.putString("email", dataset.email);
                            editorLogin.putString("phone", dataset.phone);
                            editorLogin.putString("userimage", dataset.image);
                            editorLogin.commit();
                            Intent mainIntent = new Intent(Login.this, MainActivity.class);
                            startActivity(mainIntent);
                            finish();
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, Login.this);
                }
            }
        }
    }
}
