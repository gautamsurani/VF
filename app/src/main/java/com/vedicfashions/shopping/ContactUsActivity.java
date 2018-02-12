package com.vedicfashions.shopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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


public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {


    ProgressDialog progressDialog;
    private EditText edtName, edtEmail, edtPhone, edtMsg;
    String StredtName, StredtEmail, StredtPhone, StredtMsg;

    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    String StrUserId, StrUserName, StrUserEmail, StrUserPhone, StrUserImage;
    ProgressDialog loading;
    RelativeLayout relativeMain;
    private Button btnSubmit;
    TextView call_One_Tv, call_Two_Tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_contact);
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

        call_One_Tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + call_One_Tv.getText().toString()));
                startActivity(intent);
            }
        });
    }
    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("Contact Us");
        setSupportActionBar(toolbar);
    }
    public void FEtchXmlId() {
        call_One_Tv = (TextView) findViewById(R.id.call_One_Tv);

        edtName = (EditText) findViewById(R.id.edtName);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        edtMsg = (EditText) findViewById(R.id.edtMsg);
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
        StredtEmail = edtEmail.getText().toString();
        StredtPhone = edtPhone.getText().toString();
        StredtMsg = edtMsg.getText().toString();

        if (TextUtils.isEmpty(StredtName)) {
            Utils.ShowSnakBar("Enter Name*", relativeMain, ContactUsActivity.this);
        } else if (TextUtils.isEmpty(StredtEmail)) {
            Utils.ShowSnakBar("Enter Email*", relativeMain, ContactUsActivity.this);
        } else if (!Utils.isValidEmail(StredtEmail)) {
            Utils.ShowSnakBar("Invalid  Email*", relativeMain, ContactUsActivity.this);
        } else if (TextUtils.isEmpty(StredtPhone)) {
            Utils.ShowSnakBar("Enter Phone*", relativeMain, ContactUsActivity.this);
        } else if (StredtPhone.length() != 10) {
            Utils.ShowSnakBar("Invalid Phone*", relativeMain, ContactUsActivity.this);
        } else if (TextUtils.isEmpty(StredtMsg)) {
            Utils.ShowSnakBar("Enter Message*", relativeMain, ContactUsActivity.this);
        } else {
            Utils.hideKeyboard(ContactUsActivity.this);
            loading = ProgressDialog.show(ContactUsActivity.this, "", "Please wait...", false, false);
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
                Api_Model curators = methods.AddHelp("help", StredtName, StredtEmail, StredtPhone, StredtMsg);

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
                Utils.showToastShort("Something try agin later", ContactUsActivity.this);
                finish();
                startActivity(new Intent(ContactUsActivity.this, MainActivity.class));
            } else {

                Log.i("Curator", curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {
                    try {
                        Utils.ShowSnakBar(curators.message.toString(), relativeMain, ContactUsActivity.this);
                        onBackPressed();
                    } catch (Exception e) {
                        Log.e("Exception e", e.toString());
                    }

                } else {

                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, ContactUsActivity.this);
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
