package com.vedicfashions.shopping;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Tools;
import utils.Utils;

public class ForgotActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog progressDialog;
    TextView tvSubmitnewpassword;
    EditText etforgotemailphone;
    String StrUserNmae = "";

    RelativeLayout relativeMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgorpwd);
        //getWindow().setBackgroundDrawableResource(R.drawable.login_bg);
        FetchXMLID();
        tvSubmitnewpassword.setOnClickListener(this);


        Tools.systemBarLolipop(this);
    }

    private void FetchXMLID() {

        tvSubmitnewpassword = (TextView) findViewById(R.id.tvSubmitnewpassword);
        etforgotemailphone = (EditText) findViewById(R.id.etforgotemailphone);
        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);

    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (Utils.isNetworkAvailable(ForgotActivity.this)) {
            Log.i("Checking Url Connection", "Checking");

            if (!etforgotemailphone.getText().toString().equalsIgnoreCase("")) {
                StrUserNmae = etforgotemailphone.getText().toString();
                Utils.hideKeyboard(ForgotActivity.this);
                ForgotPassTask mForgotPassTask = new ForgotPassTask();
                mForgotPassTask.execute();
            } else {
                Utils.ShowSnakBar("Please enter Mobile/Phone", relativeMain, ForgotActivity.this);
            }
        } else {
            Utils.ShowSnakBar("Please Check Your Internet Connection", relativeMain, ForgotActivity.this);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right);
    }


    private class ForgotPassTask extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;
        final ProgressDialog loading = ProgressDialog.show(ForgotActivity.this, "", "Please wait...", false, false);

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                Api_Model curators = methods.postForgot("forget", "forget", StrUserNmae);

                return curators;
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ForgotActivity.this);
                alertDialogBuilder.setMessage("Unable to connect to server");

                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        // TODO Auto-generated method stub
                        Intent startMain = new Intent(Intent.ACTION_MAIN);
                        startMain.addCategory(Intent.CATEGORY_HOME);
                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(startMain);

                    }
                });
            } else {
                if (curators.msgcode.toString().equals("0")) {
                    Intent mainIntent = new Intent(ForgotActivity.this, Login.class);
                    startActivity(mainIntent);
                    finish();
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                } else if (curators.msgcode.toString().equals("1")) {
                    for (Api_Model.fuser_detail dataset : curators.fuser_detail) {
                        Intent mainIntent = new Intent(ForgotActivity.this, OtpForForgotPassword.class);
                        mainIntent.putExtra("ID", dataset.ID);
                        mainIntent.putExtra("phone", dataset.phone);
                        mainIntent.putExtra("otp", dataset.otp);
                        startActivity(mainIntent);
                        finish();
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                } else {
                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, ForgotActivity.this);
                }
            }
        }
    }
}
