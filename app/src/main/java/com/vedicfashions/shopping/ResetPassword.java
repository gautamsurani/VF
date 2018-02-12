package com.vedicfashions.shopping;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Utils;

/**
 * Created by welcome on 09-08-2016.
 */
public class ResetPassword extends AppCompatActivity {

    EditText edtPassword_One, edtPassword_Two;
    ConnectivityManager conMgr;
    NetworkInfo mWifi;
    TextView txtResetPassword;
    String FinalPassword, StrPhone, joinID;

    RelativeLayout relativeMain;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        //getWindow().setBackgroundDrawableResource(R.drawable.login_bg);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();

        if (b != null) {
            StrPhone = (String) b.get("phone");
            joinID = (String) b.get("ID");
        }
        edtPassword_One = (EditText) findViewById(R.id.edtPasssword);
        edtPassword_Two = (EditText) findViewById(R.id.edtCPasssword);
        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);
        txtResetPassword = (TextView) findViewById(R.id.txtSignIn);
        txtResetPassword.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String StrPass_1 = edtPassword_One.getText().toString();
                final String StrPass_2 = edtPassword_Two.getText().toString();

                if (StrPass_1.toString().trim().equals("")) {
                    Utils.ShowSnakBar("Please enter password", relativeMain, ResetPassword.this);

                } else if (StrPass_2.toString().trim().equals("")) {
                    Utils.ShowSnakBar("Please enter confirm password", relativeMain, ResetPassword.this);

                } else if (!StrPass_1.equalsIgnoreCase(StrPass_2)) {
                    Utils.ShowSnakBar("Password must be same !", relativeMain, ResetPassword.this);

                } else {
                    FinalPassword = StrPass_2;


                    if (Utils.isNetworkAvailable(ResetPassword.this)) {
                        Log.i("Checking Url Connection", "Checking");
                        Utils.hideKeyboard(ResetPassword.this);
                        ResetNewPassword task = new ResetNewPassword();
                        task.execute();
                    } else {
                        Utils.ShowSnakBar("Please Check Your Internet Connection", relativeMain, ResetPassword.this);
                    }
                }

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                this.overridePendingTransition(
                        R.anim.slide_in_left, R.anim.slide_out_right);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ResetNewPassword extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;
        final ProgressDialog loading = ProgressDialog.show(ResetPassword.this, "", "Please wait...", false, false);

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
                Api_Model curators = methods.postRestPasswor("forget", "verify", StrPhone, joinID, FinalPassword);

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
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResetPassword.this);
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
                    Utils.showToastShort(curators.message.toString(), ResetPassword.this);
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    startActivity(i);

                } else {
                    Utils.showToastShort(curators.message.toString(), ResetPassword.this);
                }


            }


        }

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
        overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right);
    }
}

