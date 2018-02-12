package com.vedicfashions.shopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Tools;
import utils.Utils;

/**
 * Created by welcome on 07-01-2017.
 */
public class SellUsActivity extends AppCompatActivity implements View.OnClickListener {


    ProgressDialog progressDialog;
    private EditText edtName, edtComlpanyname, edtEmail, edtPhone,edtVatNo,edtPanNo,edtProductDetails;
    String StredtName="", StredtComlpanyname="", StredtEmail="",
            StredtPhone="",StredtVatNo="",StredtPanNo="",StredtProductDetails="";

    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    String StrUserId, StrUserName, StrUserEmail, StrUserPhone, StrUserImage;
    ProgressDialog loading;
    RelativeLayout relativeMain;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_us);


        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();


        StrUserId = prefLogin.getString("userID", null);
        StrUserName = prefLogin.getString("name", null);
        StrUserEmail = prefLogin.getString("email", null);
        StrUserPhone = prefLogin.getString("phone", null);
        StrUserImage = prefLogin.getString("userimage", null);

        FEtchXmlId();
        initToolbar();

        btnSubmit.setOnClickListener(this);
        Tools.systemBarLolipop(this);

        edtName.setText(StrUserName);
        edtEmail.setText(StrUserEmail);
        edtPhone.setText(StrUserPhone);

    }

    public void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("Sell With Us");
        setSupportActionBar(toolbar);


    }

    public void FEtchXmlId() {
        edtName = (EditText) findViewById(R.id.edtName);
        edtComlpanyname = (EditText) findViewById(R.id.edtComlpanyname);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtVatNo = (EditText) findViewById(R.id.edtVatNo);
        edtPanNo = (EditText) findViewById(R.id.edtPanNo);
        edtProductDetails = (EditText) findViewById(R.id.edtProductDetails);




        btnSubmit = (Button) findViewById(R.id.btnSubmitComplaint);
        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        StredtName = edtName.getText().toString();
        StredtComlpanyname = edtComlpanyname.getText().toString();
        StredtEmail=edtEmail.getText().toString();
        StredtPhone = edtPhone.getText().toString();


        StredtVatNo = edtVatNo.getText().toString();
        StredtPanNo=edtPanNo.getText().toString();
        StredtProductDetails = edtProductDetails.getText().toString();


        if (TextUtils.isEmpty(StredtName)) {
            Utils.ShowSnakBar("Enter Name*", relativeMain, SellUsActivity.this);
        } else    if (TextUtils.isEmpty(StredtComlpanyname)) {
            Utils.ShowSnakBar("Enter Company Name*", relativeMain, SellUsActivity.this);
        }else if (TextUtils.isEmpty(StredtEmail)) {
            Utils.ShowSnakBar("Enter Email*", relativeMain, SellUsActivity.this);
        } else if (!Utils.isValidEmail(StredtEmail)) {
            Utils.ShowSnakBar("Invalid  Email*", relativeMain, SellUsActivity.this);
        } else if (TextUtils.isEmpty(StredtPhone)) {
            Utils.ShowSnakBar("Enter Phone*", relativeMain, SellUsActivity.this);
        } else if (StredtPhone.length() != 10) {
            Utils.ShowSnakBar("Invalid Phone*", relativeMain, SellUsActivity.this);
        }
        /*else if (TextUtils.isEmpty(StredtPanNo)) {
            Utils.ShowSnakBar("Enter Pan No.*", relativeMain, SellUsActivity.this);
        }*/

        else {
            Utils.hideKeyboard(SellUsActivity.this);
            loading = ProgressDialog.show(SellUsActivity.this, "", "Please wait...", false, false);
            AddDiscussion task = new AddDiscussion();
            task.execute();

        }
    }

    private class AddDiscussion extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;

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
                Api_Model curators = methods.SellUs("sell", StredtName, StredtEmail,StredtComlpanyname, StredtPhone, StredtVatNo,"",StredtProductDetails);

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
                Utils.showToastShort("Something try agin later", SellUsActivity.this);
                finish();
                startActivity(new Intent(SellUsActivity.this, MainActivity.class));
            } else {

                Log.i("Curator", curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {
                    try {
                        Utils.showToastShort(curators.message.toString()   , SellUsActivity.this);
                        onBackPressed();
                    } catch (Exception e) {
                        Log.e("Exception e", e.toString());
                    }

                } else {

                    Utils.showToastShort(curators.message.toString(), SellUsActivity.this);
                }
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);

    }


}
