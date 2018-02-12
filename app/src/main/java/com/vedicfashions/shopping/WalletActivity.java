package com.vedicfashions.shopping;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;

import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Utils;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText etAmount;
    Button btnContinueWallet;
    TextView tv100, tv200, tv500, tv1000, tvWalletMoney;
    ImageView imgAddPayUbiz;
    ProgressDialog loading;
    LinearLayout relativeMain;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    String strUserId = "", strMobile = "";
    PayuHashes payuHashes;
    String strWalletBalance = "";
    String payu_wallet_fail = "", payu_wallet_success = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Payu.setInstance(WalletActivity.this);

        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        strUserId = prefLogin.getString("userID", null);
        strMobile = prefLogin.getString("phone", null);


        initToolbar();
        initComponent();

        if (Utils.isNetworkAvailable(WalletActivity.this)) {
            loading = ProgressDialog.show(WalletActivity.this, "", "Please wait...", false, false);
            GetWalletBalance getWalletBalance = new GetWalletBalance();
            getWalletBalance.execute();
        } else {
            retryInternetGetWalletBalance();
        }
    }

    @SuppressLint("SetTextI18n")
    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        TextView toolbartitle = (TextView) toolbar.findViewById(R.id.eshop);
        toolbartitle.setText("Add Money");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void initComponent() {
        tv100 = (TextView) findViewById(R.id.tv100);
        tv200 = (TextView) findViewById(R.id.tv200);
        tv500 = (TextView) findViewById(R.id.tv500);
        tv1000 = (TextView) findViewById(R.id.tv1000);
        tvWalletMoney = (TextView) findViewById(R.id.tvWalletMoney);
        etAmount = (EditText) findViewById(R.id.etAmount);
        imgAddPayUbiz = (ImageView) findViewById(R.id.imgAddPayUbiz);
        //  imgAddPaytm = (ImageView) findViewById(R.id.imgAddPaytm);
        btnContinueWallet = (Button) findViewById(R.id.btnContinueWallet);
        relativeMain = (LinearLayout) findViewById(R.id.relativeMain);
        tv100.setOnClickListener(this);
        tv200.setOnClickListener(this);
        tv500.setOnClickListener(this);
        tv1000.setOnClickListener(this);
        imgAddPayUbiz.setOnClickListener(this);
        // imgAddPaytm.setOnClickListener(this);
        btnContinueWallet.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if (v == tv100) {
            etAmount.setText("100");
        } else if (v == tv200) {
            etAmount.setText("200");
        } else if (v == tv500) {
            etAmount.setText("500");
        } else if (v == tv1000) {
            etAmount.setText("1000");
        } else if (v == imgAddPayUbiz) {
            if (etAmount.getText().toString().equals("")) {
                Toast.makeText(WalletActivity.this, "Please select the amount", Toast.LENGTH_SHORT).show();
            } else {
                if (Utils.isNetworkAvailable(WalletActivity.this)) {
                    loading = ProgressDialog.show(WalletActivity.this, "", "Please wait...", false, false);
                    AddWalletAsync mAddWalletMoney = new AddWalletAsync();
                    mAddWalletMoney.execute();
                } else {
                    retryInternet();
                }
            }
        } else if (v == btnContinueWallet) {
            if (etAmount.getText().toString().equals("")) {
                Toast.makeText(WalletActivity.this, "Please select the amount", Toast.LENGTH_SHORT).show();
            } else {
                if (Utils.isNetworkAvailable(WalletActivity.this)) {
                    loading = ProgressDialog.show(WalletActivity.this, "", "Please wait...", false, false);
                    AddWalletAsync mAddWalletMoney = new AddWalletAsync();
                    mAddWalletMoney.execute();
                } else {
                    retryInternet();
                }
            }
            /*
            Intent intentWallet = new Intent(WalletActivity.this, MainActivity.class);
            startActivity(intentWallet);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();*/
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AddWalletAsync extends AsyncTask<Void, Void, Api_Model> {
        RestAdapter restAdapter;
        String selectedAmount = "";

        @Override
        protected void onPreExecute() {

            restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.API_URL)
                    .build();
            selectedAmount = etAmount.getText().toString().trim();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {

                IApiMethods methods = restAdapter.create(IApiMethods.class);
                return methods.AddWalletMoney("add_money", "add_money", strUserId, strMobile, selectedAmount, "payu");
            } catch (Exception E) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators != null) {
                if (curators.msgcode.equals("0")) {
                    payu_wallet_fail = curators.payu_wallet_fail;
                    payu_wallet_success = curators.payu_wallet_success;
                    try {
                        for (Api_Model.add_money_data dataset : curators.add_money_data) {
                            Intent intent = new Intent(WalletActivity.this, com.payu.payuui.Activity.PayUBaseActivity.class);
                            PaymentParams mPaymentParams = new PaymentParams();
                            PayuConfig payuConfig = new PayuConfig();
                            payuHashes = new PayuHashes();
                            mPaymentParams.setKey(dataset.payu_key);
                            mPaymentParams.setAmount(dataset.amount);
                            mPaymentParams.setProductInfo(dataset.product_info);
                            mPaymentParams.setFirstName(dataset.first_name);
                            mPaymentParams.setEmail(dataset.email);
                            mPaymentParams.setTxnId(dataset.trans_id);
                            mPaymentParams.setSurl("http://www.vedicfashion.com/mapp/index.php?view=wallet_payment_success");
                            mPaymentParams.setFurl("http://www.vedicfashion.com/mapp/index.php?view=wallet_payment_success");
                            mPaymentParams.setUdf2("");
                            mPaymentParams.setUdf3("");
                            mPaymentParams.setUdf4("");
                            mPaymentParams.setUdf5("");
                            mPaymentParams.setUdf1(dataset.udf1);
                            mPaymentParams.setCardBin("");
                            mPaymentParams.setPhone(strMobile);
                            mPaymentParams.setUserCredentials(dataset.payu_key + ":" + dataset.email);
                            payuConfig.setEnvironment(Integer.parseInt("0"));
                            payuHashes.setPaymentHash(dataset.payment_hash);
                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(dataset.payment_related_details_for_mobile_sdk_hash);
                            payuHashes.setVasForMobileSdkHash(dataset.vas_for_mobile_sdk_hash);
                            mPaymentParams.setHash(dataset.payment_hash);
                            intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
                            intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
                            intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
                            startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
                        }

                    } catch (Exception e) {
                        Log.e("msg", "msg" + e.getMessage());
                    }
                } else {
                    Utils.ShowSnakBar(curators.message, relativeMain, WalletActivity.this);
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetWalletBalance extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.API_URL).build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                return methods.getWalletHistory("wallet_transction", strUserId, strMobile, Integer.toString(0));
            } catch (Exception E) {

                return null;
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators != null) {
                if (curators.msgcode.equals("0")) {
                    try {
                        strWalletBalance = curators.walletBal;
                        tvWalletMoney.setText("Vedic Fashion Wallet: " + getResources().getString(R.string.Rs) + " " + strWalletBalance);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    tvWalletMoney.setText("Vedic Fashion Wallet: " + getResources().getString(R.string.Rs) + " " + "0");
                    Utils.ShowSnakBar(curators.message, relativeMain, WalletActivity.this);
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                //  new GetWalletBal();
                if (data.getStringExtra("status").equals("userCancelled")) {
                    Utils.displayDialog(getString(R.string.app_name), "Transaction Cancelled!", WalletActivity.this, false);
                } else if (data.getStringExtra("status").equals("captured")) {
                    Intent intent = new Intent(WalletActivity.this, SuccessWalletActivity.class);
                    intent.putExtra("msg1", data.getStringExtra("msg1"));
                    intent.putExtra("msg2", data.getStringExtra("msg2"));
                    intent.putExtra("wallettype", "PAYTM");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                } else if (data.getStringExtra("status").equals("failed")) {
                    Intent intent = new Intent(WalletActivity.this, FailWalletActivity.class);
                    intent.putExtra("msg1", data.getStringExtra("msg1"));
                    intent.putExtra("msg2", data.getStringExtra("msg2"));
                    intent.putExtra("wallettype", "PAYTM");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();
                }
            }
        }
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (data != null)
                    Log.e("Success", "Success");
                String[] itemsDate = payu_wallet_success.split("_");
                Intent intent = new Intent(WalletActivity.this, SuccessWalletActivity.class);
                intent.putExtra("wallettype", "PAYUWALLET");
                intent.putExtra("msg1", itemsDate[0] + "");
                intent.putExtra("msg2", itemsDate[1] + "");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
            if (resultCode == RESULT_CANCELED) {
                if (data != null)
                    Log.e("Fail", "Fail");
                String[] itemsDate = payu_wallet_fail.split("_");
                Intent intent = new Intent(WalletActivity.this, FailWalletActivity.class);
                intent.putExtra("wallettype", "PAYUWALLET");
                intent.putExtra("msg1", itemsDate[0] + "");
                intent.putExtra("msg2", itemsDate[1] + "");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        }
    }


    public void retryInternet() {

        try {
            final Dialog dialog = new Dialog(WalletActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_internet_not_found);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setCanceledOnTouchOutside(true);


            TextView txtDialogRetryOk = (TextView) dialog.findViewById(R.id.txtDialogRetryOk);
            TextView txtDialogRetryCancel = (TextView) dialog.findViewById(R.id.txtDialogRetryCancel);

            txtDialogRetryCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            txtDialogRetryOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Utils.isNetworkAvailable(WalletActivity.this)) {
                        dialog.dismiss();
                        loading = ProgressDialog.show(WalletActivity.this, "", "Please wait...", false, false);

                    } else {
                        Utils.ShowSnakBar("No internet availbale", relativeMain, WalletActivity.this);
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void retryInternetGetWalletBalance() {

        try {
            final Dialog dialog = new Dialog(WalletActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_internet_not_found);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setCanceledOnTouchOutside(true);
            TextView txtDialogRetryOk = (TextView) dialog.findViewById(R.id.txtDialogRetryOk);
            TextView txtDialogRetryCancel = (TextView) dialog.findViewById(R.id.txtDialogRetryCancel);

            txtDialogRetryCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            txtDialogRetryOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Utils.isNetworkAvailable(WalletActivity.this)) {
                        dialog.dismiss();
                        loading = ProgressDialog.show(WalletActivity.this, "", "Please wait...", false, false);
                        GetWalletBalance getWalletBalance = new GetWalletBalance();
                        getWalletBalance.execute();
                    } else {

                        Utils.ShowSnakBar("No internet availbale", relativeMain, WalletActivity.this);
                    }
                }
            });


            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
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
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}
