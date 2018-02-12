package com.vedicfashions.shopping;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import models.MyAddressdData;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Tools;
import utils.Utils;

/**
 * Created by welcome on 14-10-2016.
 */
public class DeleveryClass extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<MyAddressdData> MyAddressList = new ArrayList<>();
    private LinearLayout lyt_not_found;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    ProgressDialog progressDialog;
    ProgressDialog loading;
    TextView txtInvoiceEmail, txtInvoiceEmailNew;
    String StrUserId;
    String super_use_amount;
    String wallet;
    String user_super_wallet;
    String StrInvoiceEmail = "";
    String StrAddressId = "";
    String StrThisSubTotal = "";
    String StrThisItems = "";
    TextView txtPricetime, txtPriceTotal, txtDelevery, txtDeleveryType, txtFinalMrp, txtGreenText, txtMrpBottom;
    Button btnSendData;
    CardView ThisCsacffdswfard;
    TextView txtName, txtAddress, txtMobile, txtChangeAddress, txtAddAddress, txtNoAddressFound;
    String StrThisPAge = "";
    boolean IsAddressAvailbale = true;
    LinearLayout lienrAddInvoiceEmail;
    RelativeLayout relativeMain;

    // TextView txtDelveryNotAvilbale,txtDelevenotChangeAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_delevery);

        FetchXmlID();
        initToolbar();

        prefLogin = DeleveryClass.this.getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);


        Tools.systemBarLolipop(this);
        //  txtDelevenotChangeAddress.setOnClickListener(this);

        Intent in = getIntent();
        if (in != null) {
            StrThisPAge = in.getStringExtra("Page");
            StrThisSubTotal = in.getStringExtra("subtotal");
            StrThisItems = in.getStringExtra("item");

        }

        txtPricetime.setText("Price (" + StrThisItems + " items)");
        txtPriceTotal.setText(getResources().getString(R.string.rs) + " " + StrThisSubTotal);
        txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + StrThisSubTotal);
        txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + StrThisSubTotal);


        if (StrThisPAge.equalsIgnoreCase("AddressRadio")) {
            Log.d("AddressRadio", "AddressRadio");


            Intent intent = getIntent();
            Bundle args = intent.getBundleExtra("BUNDLE");
            ArrayList<MyAddressdData> object = (ArrayList<MyAddressdData>) args.getSerializable("ARRAYLIST");


            MyAddressdData p;
            p = (MyAddressdData) in.getExtras().getSerializable("Model");
            if (p != null) {
                txtName.setText(p.getName());
                txtMobile.setText(p.getPhone());
                txtAddress.setText(p.getAddress1() + ", " + p.getAddress2() + ", " + p.getArea() + ", " + p.getCity() + ", " + p.getState() + ", " + p.getPincode());

                MyAddressList = object;
                StrAddressId = p.getAddID();
                if (p.getName().equalsIgnoreCase("")) {
                    txtNoAddressFound.setVisibility(View.VISIBLE);
                } else {

                    txtNoAddressFound.setVisibility(View.GONE);
                }
            }

        } else {
            if (Utils.isNetworkAvailable(getBaseContext())) {
                Log.d("NotAddressRadio", "NotAddressRadio");
                loading = ProgressDialog.show(DeleveryClass.this, "", "Please wait...", false, false);
                GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
                mGetNoticeListArrayForUpdates.execute();
            } else {
                Utils.ShowSnakBar("Please Check Your Internet Connection", relativeMain, DeleveryClass.this);
            }
        }

        btnSendData.setOnClickListener(this);
        lienrAddInvoiceEmail.setOnClickListener(this);
        txtChangeAddress.setOnClickListener(this);
        txtAddAddress.setOnClickListener(this);
    }

    private void CreateDelevryNOtAvailbleAlertDialog(String dd) {

        LayoutInflater lf = DeleveryClass.this.getLayoutInflater();
        View myview = lf.inflate(R.layout.dialog_delverynotavailable, null);
        AlertDialog.Builder Dilaog_Sound = new AlertDialog.Builder(DeleveryClass.this);
        Dilaog_Sound.setView(myview);
        TextView txtDelevenotChangeAddress = (TextView) myview.findViewById(R.id.txtDelevenotChangeAddress);
        TextView txtDelveryNotAvilbale = (TextView) myview.findViewById(R.id.txtDelveryNotAvilbale);
        txtDelveryNotAvilbale.setText(dd);
        final AlertDialog asd_Dialog = Dilaog_Sound.create();
        txtDelevenotChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asd_Dialog.dismiss();
                Intent in = new Intent(DeleveryClass.this, ChangeAddress.class);
                in.putExtra("ArrModel", MyAddressList);
                in.putExtra("Pos", StrAddressId);

                in.putExtra("item", StrThisItems);
                in.putExtra("subtotal", StrThisSubTotal);
                startActivity(in);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                finish();
            }
        });
        asd_Dialog.setCancelable(false);
        asd_Dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        asd_Dialog.show();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Utils.isNetworkAvailable(getBaseContext())) {

            loading = ProgressDialog.show(DeleveryClass.this, "", "Please wait...", false, false);
            GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
            mGetNoticeListArrayForUpdates.execute();
        } else {

            Utils.ShowSnakBar("Please Check Your Internet Connection", relativeMain, DeleveryClass.this);

        }
    }

    private void FetchXmlID() {
        lyt_not_found = (LinearLayout) findViewById(R.id.lyt_not_found);

        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);
        btnSendData = (Button) findViewById(R.id.btnSendData);
        ThisCsacffdswfard = (CardView) findViewById(R.id.ThisCsacffdswfard);



    /*    txtDelveryNotAvilbale = (TextView) findViewById(R.id.txtDelveryNotAvilbale);
        txtDelevenotChangeAddress = (TextView) findViewById(R.id.txtDelevenotChangeAddress);
*/
        txtPricetime = (TextView) findViewById(R.id.txtPricetime);
        txtInvoiceEmail = (TextView) findViewById(R.id.txtInvoiceEmail);
        txtInvoiceEmailNew = (TextView) findViewById(R.id.txtInvoiceEmailNew);
        lienrAddInvoiceEmail = (LinearLayout) findViewById(R.id.lienrAddInvoiceEmail);

        txtPriceTotal = (TextView) findViewById(R.id.txtPriceTotal);
        txtDelevery = (TextView) findViewById(R.id.txtDelevery);
        txtDeleveryType = (TextView) findViewById(R.id.txtDeleveryType);
        txtFinalMrp = (TextView) findViewById(R.id.txtFinalMrp);
        txtGreenText = (TextView) findViewById(R.id.txtGreenText);
        txtMrpBottom = (TextView) findViewById(R.id.txtMrpBottom);
        txtNoAddressFound = (TextView) findViewById(R.id.txtNoAddressFound);
        txtNoAddressFound.setVisibility(View.GONE);
        txtName = (TextView) findViewById(R.id.txtName);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtMobile = (TextView) findViewById(R.id.txtMobile);
        txtChangeAddress = (TextView) findViewById(R.id.txtChangeAddress);
        txtAddAddress = (TextView) findViewById(R.id.txtAddAddress);

    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("Delivery");
        setSupportActionBar(toolbar);


    }


    @Override
    public void onResume() {

        super.onResume();

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lienrAddInvoiceEmail:
                final Dialog dialog = new Dialog(DeleveryClass.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_changeemail);
                LayoutInflater fullmylf5595 = getLayoutInflater();
                final View mv559512 = fullmylf5595.inflate(R.layout.popup_changeemail, null);


                dialog.setCancelable(true);

                final EditText text = (EditText) dialog.findViewById(R.id.etPromocode);
                if (!StrInvoiceEmail.equalsIgnoreCase("")) {
                    text.setText(StrInvoiceEmail);
                }
                TextView dialogButton = (TextView) dialog.findViewById(R.id.btnEnterpromo);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StrInvoiceEmail = text.getText().toString();
                        if (StrInvoiceEmail.isEmpty()) {
                            Toast.makeText(DeleveryClass.this, "Enter PinCode", Toast.LENGTH_SHORT).show();
                        } else {
                            loading = ProgressDialog.show(DeleveryClass.this, "", "Please wait...", false, false);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mv559512.getWindowToken(), 0);
                            ChangeEmailAsynTask mChangePinAsynTask = new ChangeEmailAsynTask();
                            mChangePinAsynTask.execute();
                            dialog.dismiss();
                        }

                    }
                });

                dialog.show();

                break;
            case R.id.txtChangeAddress:
                Intent in = new Intent(DeleveryClass.this, ChangeAddress.class);

                in.putExtra("ArrModel", MyAddressList);
                in.putExtra("Pos", StrAddressId);

                in.putExtra("item", StrThisItems);
                in.putExtra("subtotal", StrThisSubTotal);
                startActivity(in);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                finish();
                break;
            case R.id.txtAddAddress:
                Intent iaccn = new Intent(DeleveryClass.this, AddEditAddres.class);
                iaccn.putExtra("Page", "AddAddressFromDeleveryClass");

                iaccn.putExtra("item", StrThisItems);
                iaccn.putExtra("subtotal", StrThisSubTotal);
                startActivity(iaccn);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;

            case R.id.btnSendData:

                if (IsAddressAvailbale) {
                    loading = ProgressDialog.show(DeleveryClass.this, "", "Please wait...", false, false);


                    OrderShipingAsyTask mRomoveToCartAsyTask = new OrderShipingAsyTask();
                    mRomoveToCartAsyTask.execute();
                } else {

                    Utils.ShowSnakBar("Please Add Any One Address !", relativeMain, DeleveryClass.this);
                }


                break;

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
        // MainActivity.drawer.closeDrawer(Gravity.LEFT);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);

    }


    private class GetNoticeListArrayForUpdates extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            MyAddressList.clear();
            restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.API_URL).build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                Api_Model curators = methods.GEtAddressAPI("addresses", "list", StrUserId);

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

                ShowRetryDialog();
            } else {
                if (curators.msgcode.equals("0")) {

                    StrInvoiceEmail = curators.email;
                    if (!StrInvoiceEmail.equalsIgnoreCase("")) {
                        txtInvoiceEmail.setText("Edit Email");
                        txtInvoiceEmailNew.setVisibility(View.VISIBLE);
                        txtInvoiceEmailNew.setText(StrInvoiceEmail);
                    } else {
                        txtInvoiceEmailNew.setVisibility(View.GONE);
                        txtInvoiceEmail.setText("Add Email ?");
                    }
                    if (!curators.flag.equals("no_address")) {
                        IsAddressAvailbale = true;
                        MyAddressList.clear();
                        txtNoAddressFound.setVisibility(View.GONE);
                        ThisCsacffdswfard.setVisibility(View.VISIBLE);
                        int in = 0;
                        for (Api_Model.address_list dataset : curators.address_list) {

                            if (in == 0) {
                                txtName.setText(dataset.name);
                                txtMobile.setText(dataset.phone);
                                txtAddress.setText(dataset.address1 + ", " + dataset.address2 + "\n" +
                                        dataset.area + ", " + dataset.city + ", " + dataset.state + ", " +
                                        dataset.pincode);
                                StrAddressId = dataset.addID;

                            }
                            in++;
                            MyAddressdData mMyAddressdData;
                            mMyAddressdData = new MyAddressdData(dataset.SR, dataset.addID,
                                    dataset.name, dataset.phone, dataset.address1,
                                    dataset.address2, dataset.area, dataset.city,
                                    dataset.state, dataset.pincode);
                            MyAddressList.add(mMyAddressdData);
                        }
                    } else {
                        txtName.setText("");
                        txtMobile.setText("");
                        txtAddress.setText("");
                        txtNoAddressFound.setVisibility(View.VISIBLE);
                        ThisCsacffdswfard.setVisibility(View.GONE);
                        IsAddressAvailbale = false;
                    }

                } else {

                }
            }
        }
    }

    private void ShowRetryDialog() {
        try {
            final Dialog dialog = new Dialog(DeleveryClass.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_internet_not_found);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            Window window = dialog.getWindow();
            WindowManager.LayoutParams param = window.getAttributes();

            dialog.setCanceledOnTouchOutside(true);


            TextView txtDialogRetryOk = (TextView) dialog.findViewById(R.id.txtDialogRetryOk);
            TextView txtDialogRetryCancel = (TextView) dialog.findViewById(R.id.txtDialogRetryCancel);

            txtDialogRetryCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loading = ProgressDialog.show(DeleveryClass.this, "", "Please wait...", false, false);
                    GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
                    mGetNoticeListArrayForUpdates.execute();
                    dialog.dismiss();
                }
            });
            txtDialogRetryOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {

        }
    }

    private class OrderShipingAsyTask extends AsyncTask<Void, Void,
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
                return methods.OrderShippingApi("orders", "shipping", StrUserId, StrAddressId, StrThisItems, StrThisSubTotal);
            } catch (Exception E) {
                Log.i("exception", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {
                ShowRetryDialog();
            } else {
                Log.i("Curator", curators.message);

                if (curators.delivery.equals("No")) {
                    CreateDelevryNOtAvailbleAlertDialog(curators.result);
                }
                if (curators.msgcode.equals("0")) {
                    Intent in = new Intent(DeleveryClass.this, PayMentClass.class);
                    in.putExtra("shipping", curators.shipping);
                    in.putExtra("AddressId", StrAddressId);
                    in.putExtra("TotalSubTotal", StrThisSubTotal);
                    in.putExtra("item", StrThisItems);
                    in.putExtra("codAvailable", curators.cod);
                    in.putExtra("result", curators.result);
                    in.putExtra("bank_trans", curators.bank_trans);
                    in.putExtra("wallet", curators.wallet);
                    in.putExtra("user_super_wallet", curators.user_super_wallet);
                    in.putExtra("super_use_amount", curators.super_use_amount);
                    in.putExtra("super_wallet_msg", curators.super_wallet_msg);
                    startActivity(in);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }

            }


        }

    }

    private class ChangeEmailAsynTask extends AsyncTask<Void, Void, Api_Model> {
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
                Api_Model curators = methods.ChangeEmailAsyntask("email_checkout", StrUserId, StrInvoiceEmail);//StrPInCode

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

                ShowRetryDialog();
            } else {
                Log.i("Curator", curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {
                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, DeleveryClass.this);
                    txtInvoiceEmailNew.setVisibility(View.VISIBLE);
                    txtInvoiceEmailNew.setText(StrInvoiceEmail);
                    txtInvoiceEmail.setText("Edit Email");
                } else {
                    /*txtInvoiceEmailNew.setText("");
                    txtInvoiceEmailNew.setVisibility(View.GONE);
                    txtInvoiceEmail.setText("Add Email");*/
                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, DeleveryClass.this);
                }
            }
        }
    }
}
