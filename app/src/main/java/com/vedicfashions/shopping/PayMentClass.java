package com.vedicfashions.shopping;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.payu.india.Model.PaymentParams;
import com.payu.india.Model.PayuConfig;
import com.payu.india.Model.PayuHashes;
import com.payu.india.Payu.Payu;
import com.payu.india.Payu.PayuConstants;
import com.payu.payuui.Activity.PayUBaseActivity;

import java.text.DecimalFormat;

import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Utils;

/*
 * Created by welcome on 16-11-2016.
 */
public class PayMentClass extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    TextView txtPriceTotal, txtShipingChargeRs, txtFinalMrp, txtMrpBottom, txtPricetime, txtGreenText, WalleteRS, walletpaytxt, Superwalletetxt, txtSuperwalletRs;
    Button btnSendData;
    CheckBox Walletcheckb;
    String StrUserId;
    LinearLayout paytowalletlayout;
    String Strshipping = "", AddressId = "", TotalSubTotal = "", item = "";
    String flag = "cod";
    String FinalTotal = "";
    String walletFinal = "";
    String VedicWallet_used = "";
    String FinalTotalWithDiscount = "";
    String FinalTotalWithDiscountAndWallet = "";
    String StrtxtGreenLine = "";
    LinearLayout checkCouponLayout, couponLayout;
    TextView orderHideCoupon, txtCouponMsg, wallateMsgTv;
    EditText couponcodeEdt;
    Button applycouponBtn;
    String StrCoupenText = "", strCoupenCode = "", strCoupenValue = "";
    PayuHashes payuHashes;
    String strWalletmony = "";
    String super_use_amount = "";
    String user_super_wallet = "";
    RelativeLayout relativeMain;
    LinearLayout linerCoupenLayout;
    TextView txtCoupenvalue;
    String subtotle = "";
    String StrCodAvilable = "Yes";
    String Strbank_trans = "No";
    boolean IsFromPayYou = false;
    boolean IsUseCouponecode = false;
    String mainTotalRS = "";
    String mainTotalRSwithsuper = "";
    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
    SharedPreferences prefSuper;
    SharedPreferences.Editor editorSuperInfo;
    RadioButton radioBankTransfer;
    View viewBankTransfer;
    TextView ordersuperTitletxt;
    Context context;
    Dialog dialog;
    String cart_total = "";
    String StrVedicAmaunt = "";
    double SubTotal;
    double Walletmony;
    double Supermony;
    String strSuperMsg = "";
    double sipingcharge;
    double cpvalue;
    String wallet_used = "";
    String super_msg = "";

    @SuppressLint({"SetTextI18n", "CommitPrefEdits", "ClickableViewAccessibility"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_payment);
        Payu.setInstance(PayMentClass.this);
        Intent in = getIntent();
        prefCartCounter = PayMentClass.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();

        prefSuper = getApplicationContext().getSharedPreferences("DataSuperInfo", 0); // 0 - for private mode
        editorSuperInfo = prefSuper.edit();
        strSuperMsg = prefSuper.getString("SuperInfoMsg", null);
        if (in != null) {
            Strshipping = in.getStringExtra("shipping");
            AddressId = in.getStringExtra("AddressId");
            TotalSubTotal = in.getStringExtra("TotalSubTotal");
            subtotle = in.getStringExtra("TotalSubTotal");
            item = in.getStringExtra("item");
            cart_total = in.getStringExtra("TotalSubTotal");
            StrtxtGreenLine = in.getStringExtra("result");
            StrCodAvilable = in.getStringExtra("codAvailable");
            Strbank_trans = in.getStringExtra("bank_trans");
            strWalletmony = in.getStringExtra("wallet");
            user_super_wallet = in.getStringExtra("user_super_wallet");
            super_use_amount = in.getStringExtra("super_use_amount");
            super_msg = in.getStringExtra("super_wallet_msg");
            Log.e("supermsg", "Super" + super_msg);
        }
        FetchXmlID();
        initToolbar();
        prefLogin = PayMentClass.this.getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);
        RadioButton RadioCod = (RadioButton) findViewById(R.id.radioCOD);
        RadioButton radioATM = (RadioButton) findViewById(R.id.radioATM);
        final View Vi = findViewById(R.id.CodLine);
        try {
            WalleteRS.setText(getResources().getString(R.string.rs) + " " + strWalletmony);
            Superwalletetxt.setText(getResources().getString(R.string.rs) + " " + user_super_wallet);
            SubTotal = Double.valueOf(TotalSubTotal);
            Walletmony = Double.valueOf(strWalletmony);
            Supermony = Double.valueOf(super_use_amount);
            sipingcharge = Double.valueOf(Strshipping);
            if (super_msg.equalsIgnoreCase("")) {
                wallateMsgTv.setVisibility(View.GONE);
            } else {
                wallateMsgTv.setVisibility(View.VISIBLE);
                wallateMsgTv.setText(super_msg);
            }
        } catch (Exception ex) {
            Log.e("Payment", "Activity" + ex.getMessage());
        }
        if (StrCodAvilable.equalsIgnoreCase("Yes")) {
            RadioCod.setVisibility(View.VISIBLE);
            Vi.setVisibility(View.VISIBLE);
            flag = "cod";
            RadioCod.setChecked(true);
        } else {
            RadioCod.setVisibility(View.GONE);
            Vi.setVisibility(View.GONE);
            radioATM.setChecked(true);
            flag = "online";
        }

        if (Strbank_trans.equalsIgnoreCase("Yes")) {
            radioBankTransfer.setVisibility(View.VISIBLE);
            viewBankTransfer.setVisibility(View.VISIBLE);
        } else {
            radioBankTransfer.setVisibility(View.GONE);
            viewBankTransfer.setVisibility(View.GONE);
        }
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radiogroupMainPayment);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId) {
                    case R.id.radioCOD:
                        flag = "cod";
                        break;
                    case R.id.radioATM:
                        flag = "online";
                        break;
                    case R.id.radioBankTransfer:
                        flag = "bank";
                        break;
                }
            }
        });

        ordersuperTitletxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(PayMentClass.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                dialog.setContentView(R.layout.supermony_info_dialog);
                ImageView imgClose = (ImageView) dialog.findViewById(R.id.imgClose);
                TextView Cashbackmsg = (TextView) dialog.findViewById(R.id.Cashbackmsg);
                Cashbackmsg.setText(strSuperMsg);
                TextView user_super_walletTv = (TextView) dialog.findViewById(R.id.user_super_walletTv);

                try {
                    user_super_walletTv.setText(getResources().getString(R.string.rs) + " " + user_super_wallet);
                } catch (Exception ex) {
                    Log.e("Payment", "Exe__" + ex.getMessage());
                }
                imgClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        Walletcheckb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Walletcheckb.isChecked()) {
                        paytowalletlayout.setVisibility(View.VISIBLE);
                        if (SubTotal > Walletmony) {
                            Log.e("", "");
                            double result = SubTotal - Walletmony - Supermony + sipingcharge - cpvalue;
                            mainTotalRS = Double.toString(result);
                            txtPriceTotal.setText(getResources().getString(R.string.rs) + " " + TotalSubTotal);
                            txtSuperwalletRs.setText("- " + getResources().getString(R.string.rs) + " " + Supermony);
                            txtShipingChargeRs.setText("+ " + getResources().getString(R.string.rs) + " " + Strshipping);
                            txtPricetime.setText("Price (" + item + " items)");
                            txtGreenText.setText(StrtxtGreenLine);
                            double DouubleFinalTotal = Double.parseDouble(mainTotalRS);
                            DecimalFormat df = new DecimalFormat("#.00"); // df.format(LastCount)
                            walletFinal = df.format(DouubleFinalTotal) + "";
                            FinalTotalWithDiscount = df.format(DouubleFinalTotal) + "";
                            paytowalletlayout.setVisibility(View.VISIBLE);
                            walletpaytxt.setText("- " + getResources().getString(R.string.rs) + " " + strWalletmony);
                            StrVedicAmaunt = strWalletmony;
                            txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + walletFinal);
                            txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + walletFinal);
                            FinalTotalWithDiscountAndWallet = walletFinal;
                            VedicWallet_used = strWalletmony;

                            Log.e("Vedic Wallet ", "Wallet Used" + strWalletmony);
                        } else {
                            double result = SubTotal - Supermony + sipingcharge - cpvalue;
                            String p = Double.toString(result);
                            double main = Walletmony - result;
                            mainTotalRS = Double.toString(main);
                            txtPriceTotal.setText(getResources().getString(R.string.rs) + " " + TotalSubTotal);
                            txtSuperwalletRs.setText("- " + getResources().getString(R.string.rs) + " " + Supermony);
                            txtShipingChargeRs.setText("+ " + getResources().getString(R.string.rs) + " " + Strshipping);
                            txtPricetime.setText("Price (" + item + " items)");
                            txtGreenText.setText(StrtxtGreenLine);
                            double DouubleFinalTotal = Double.parseDouble(mainTotalRS);
                            DecimalFormat df = new DecimalFormat("#.00"); // df.format(LastCount)
                            walletFinal = df.format(DouubleFinalTotal) + "";
                            FinalTotalWithDiscount = df.format(DouubleFinalTotal) + "";
                            walletpaytxt.setText("- " + getResources().getString(R.string.rs) + " " + p);
                            StrVedicAmaunt = strWalletmony;
                            txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + "00.00");
                            txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + "00.00");
                            FinalTotalWithDiscount = "00.00";
                            VedicWallet_used = p;
                            Log.e("Decorent wallet", "wallet Used" + p);
                        }
                    } else {
                        SubTotal = Double.valueOf(TotalSubTotal);
                        Supermony = Double.valueOf(super_use_amount);
                        double result = SubTotal - Supermony - cpvalue + sipingcharge;
                        StrVedicAmaunt = "";
                        paytowalletlayout.setVisibility(View.GONE);
                        mainTotalRSwithsuper = Double.toString(result);
                        txtPriceTotal.setText(getResources().getString(R.string.rs) + " " + TotalSubTotal);
                        txtShipingChargeRs.setText("+ " + getResources().getString(R.string.rs) + " " + Strshipping);
                        txtPricetime.setText("Price (" + item + " items)");
                        txtGreenText.setText(StrtxtGreenLine);
                        txtSuperwalletRs.setText("- " + getResources().getString(R.string.rs) + " " + Supermony);
                        double DouubleFinalTotal = Double.parseDouble(mainTotalRSwithsuper);
                        DecimalFormat df = new DecimalFormat("#.00"); // df.format(LastCount)
                        FinalTotal = df.format(DouubleFinalTotal) + "";
                        FinalTotalWithDiscount = df.format(DouubleFinalTotal) + "";
                        txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + FinalTotal);
                        txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + FinalTotal);
                        FinalTotalWithDiscountAndWallet = FinalTotal;
                    }
                } catch (Exception ex) {
                    Log.e("paymentActivity", "Ex___" + ex.getMessage());
                }
            }
        });
        try {
            double SubTotal = Double.valueOf(TotalSubTotal);
            double Supermony = Double.valueOf(super_use_amount);
            double result = SubTotal - Supermony + sipingcharge;
            mainTotalRSwithsuper = Double.toString(result);
            paytowalletlayout.setVisibility(View.GONE);
            StrVedicAmaunt = "";
            txtPriceTotal.setText(getResources().getString(R.string.rs) + " " + TotalSubTotal);
            txtShipingChargeRs.setText("+ " + getResources().getString(R.string.rs) + " " + Strshipping);
            txtPricetime.setText("Price (" + item + " items)");
            txtGreenText.setText(StrtxtGreenLine);
            txtSuperwalletRs.setText("- " + getResources().getString(R.string.rs) + " " + Supermony);
            double DouubleFinalTotal = Double.parseDouble(mainTotalRSwithsuper);
            DecimalFormat df = new DecimalFormat("#.00"); // df.format(LastCount)
            FinalTotal = df.format(DouubleFinalTotal) + "";
            FinalTotalWithDiscount = df.format(DouubleFinalTotal) + "";
            txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + FinalTotal);
            txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + FinalTotal);
            FinalTotalWithDiscountAndWallet = FinalTotal;
        } catch (Exception ex) {
            Log.e("PaymentActivity", "Ex___" + ex.getMessage());
        }
        btnSendData.setOnClickListener(this);
        applycouponBtn.setOnClickListener(this);
        couponcodeEdt.addTextChangedListener(txwatcher);
        checkCouponLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        if (couponLayout.getVisibility() == View.VISIBLE) {
                            couponLayout.setVisibility(View.GONE);
                            orderHideCoupon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.bottom_arrow, 0);
                        } else {
                            couponLayout.setVisibility(View.VISIBLE);
                            orderHideCoupon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.top_arrow, 0);
                        }
                        break;
                }
                return false;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("Payments");
        setSupportActionBar(toolbar);

    }

    final TextWatcher txwatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 0) {
                txtCouponMsg.setVisibility(View.GONE);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    private void FetchXmlID() {
        ordersuperTitletxt = (TextView) findViewById(R.id.ordersuperTitletxt);
        txtSuperwalletRs = (TextView) findViewById(R.id.txtSuperwalletRs);
        Superwalletetxt = (TextView) findViewById(R.id.SuperwalleteRS);
        paytowalletlayout = (LinearLayout) findViewById(R.id.paytowalletlayout);
        walletpaytxt = (TextView) findViewById(R.id.walletpaytxt);
        Walletcheckb = (CheckBox) findViewById(R.id.Walletcheckb);
        btnSendData = (Button) findViewById(R.id.btnSendData);
        txtPriceTotal = (TextView) findViewById(R.id.txtPriceTotal);
        txtShipingChargeRs = (TextView) findViewById(R.id.txtShipingChargeRs);
        txtFinalMrp = (TextView) findViewById(R.id.txtFinalMrp);
        txtMrpBottom = (TextView) findViewById(R.id.txtMrpBottom);
        txtPricetime = (TextView) findViewById(R.id.txtPricetime);
        txtGreenText = (TextView) findViewById(R.id.txtGreenText);
        txtCouponMsg = (TextView) findViewById(R.id.txtCouponMsg);
        WalleteRS = (TextView) findViewById(R.id.walleteRS);
        checkCouponLayout = (LinearLayout) findViewById(R.id.checkCouponLayout);
        couponLayout = (LinearLayout) findViewById(R.id.couponLayout);
        orderHideCoupon = (TextView) findViewById(R.id.orderHideCoupon);
        couponcodeEdt = (EditText) findViewById(R.id.couponcodeEdt);
        applycouponBtn = (Button) findViewById(R.id.applycouponBtn);
        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);
        linerCoupenLayout = (LinearLayout) findViewById(R.id.linerCoupenLayout);
        txtCoupenvalue = (TextView) findViewById(R.id.txtCoupenvalue);
        radioBankTransfer = (RadioButton) findViewById(R.id.radioBankTransfer);
        viewBankTransfer = findViewById(R.id.viewBankTransfer);
        wallateMsgTv = (TextView) findViewById(R.id.wallateMsgTv);
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
    protected void onResume() {
        super.onResume();
        if (IsFromPayYou) {
            finish();
            Intent intent = new Intent(PayMentClass.this, TransactionSuccessFailed.class);
            intent.putExtra("message", "Your payment has been cancelled !");
            intent.putExtra("message1", "Please try again later.");
            intent.putExtra("msgCode", 1);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {

        if (IsFromPayYou) {
            finish();
            Intent intent = new Intent(PayMentClass.this, TransactionSuccessFailed.class);
            intent.putExtra("message", "Your payment has been cancelled !");
            intent.putExtra("message1", "Please try again later.");
            intent.putExtra("msgCode", 1);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left,
                    R.anim.slide_out_right);
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
            case R.id.applycouponBtn:
                StrCoupenText = couponcodeEdt.getText().toString();
                if (StrCoupenText.equalsIgnoreCase("")) {
                    Utils.ShowSnakBar("Enter Coupon Code First !", relativeMain, PayMentClass.this);
                } else {
                    Utils.hideKeyboard(PayMentClass.this);
                    double DouubleFinalTotal = Double.parseDouble(TotalSubTotal) + Double.parseDouble(Strshipping);
                    DecimalFormat df = new DecimalFormat("#.00"); // df.format(LastCount)
                    FinalTotalWithDiscount = df.format(DouubleFinalTotal) + "";
                    strCoupenCode = "";
                    strCoupenValue = "";
                    GetCouponCodePriceTask mGetCouponCodePriceTask = new GetCouponCodePriceTask();
                    mGetCouponCodePriceTask.execute();
                }
                break;
            case R.id.btnSendData:
                if (Utils.isNetworkAvailable(PayMentClass.this)) {
                    Utils.hideKeyboard(PayMentClass.this);
                    GetPayMentDetails task = new GetPayMentDetails();
                    task.execute();
                } else {
                    Utils.showToastShort("Check your Internet connection !", PayMentClass.this);
                }
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetCouponCodePriceTask extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;
        final ProgressDialog loading = ProgressDialog.show(PayMentClass.this, "", "Please wait...", false, false);

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
                return methods.GetSearchDataApi("discount", StrCoupenText, FinalTotal, StrUserId);
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PayMentClass.this);
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setMessage("Unable to connect to server");
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        GetCouponCodePriceTask mGetCouponCodePriceTask = new GetCouponCodePriceTask();
                        mGetCouponCodePriceTask.execute();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        GetCouponCodePriceTask mGetCouponCodePriceTask = new GetCouponCodePriceTask();
                        mGetCouponCodePriceTask.execute();

                    }
                });

            } else {
                if (curators.msgcode.equals("0")) {
                    Walletcheckb.setChecked(false);
                    paytowalletlayout.setVisibility(View.GONE);
                    txtCouponMsg.setVisibility(View.VISIBLE);
                    txtCouponMsg.setBackgroundColor(Color.parseColor("#1bac94"));
                    txtCouponMsg.setText(curators.message);
                    double DouubleFinalTotal = Double.parseDouble(TotalSubTotal) - Double.parseDouble(curators.Discount) - Double.parseDouble(super_use_amount) + sipingcharge;
                    DecimalFormat df = new DecimalFormat("#.00"); // df.format(LastCount)
                    FinalTotalWithDiscount = df.format(DouubleFinalTotal) + "";
                    txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + FinalTotalWithDiscount);
                    txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + FinalTotalWithDiscount);
                    strCoupenCode = curators.disID;
                    strCoupenValue = curators.Discount;
                    linerCoupenLayout.setVisibility(View.VISIBLE);
                    double DouubleFinalTotalAAAAA = Double.parseDouble(curators.Discount);
                    txtCoupenvalue.setText("-" + getResources().getString(R.string.rs) + " " + df.format(DouubleFinalTotalAAAAA) + "");
                    String p = curators.Discount;
                    cpvalue = Double.parseDouble(p);
                } else {
                    cpvalue = 00.00;
                    Walletcheckb.setChecked(false);
                    double DouubleFinalTotal = Double.parseDouble(FinalTotalWithDiscount) - Supermony + sipingcharge;
                    DecimalFormat df = new DecimalFormat("#.00");
                    FinalTotalWithDiscount = df.format(DouubleFinalTotal) + "";
                    txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + FinalTotalWithDiscount);
                    txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + FinalTotalWithDiscount);
                    txtCouponMsg.setVisibility(View.VISIBLE);
                    txtCouponMsg.setBackgroundColor(Color.parseColor("#720808"));
                    txtCouponMsg.setText(curators.message);
                    linerCoupenLayout.setVisibility(View.GONE);
                    txtCoupenvalue.setText("");
                    if (Walletcheckb.isChecked()) {
                        cpvalue = 0.0;
                        if (SubTotal > Walletmony) {
                            IsUseCouponecode = false;
                            double result = SubTotal - Walletmony - Supermony - sipingcharge - cpvalue;
                            mainTotalRS = Double.toString(result);
                            txtPriceTotal.setText(getResources().getString(R.string.rs) + " " + subtotle);
                            txtSuperwalletRs.setText("- " + getResources().getString(R.string.rs) + " " + Supermony);
                            txtShipingChargeRs.setText("+ " + getResources().getString(R.string.rs) + " " + Strshipping);
                            txtPricetime.setText("Price (" + item + " items)");
                            txtGreenText.setText(StrtxtGreenLine);
                            double DouubleFinalTotal1 = Double.parseDouble(mainTotalRS);
                            DecimalFormat dff = new DecimalFormat("#.00"); // df.format(LastCount)
                            walletFinal = dff.format(DouubleFinalTotal1) + "";
                            FinalTotalWithDiscount = dff.format(DouubleFinalTotal1) + "";
                            paytowalletlayout.setVisibility(View.VISIBLE);
                            walletpaytxt.setText("- " + getResources().getString(R.string.rs) + " " + strWalletmony);
                            txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + walletFinal);
                            txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + walletFinal);
                            FinalTotalWithDiscountAndWallet = walletFinal;
                            Log.e("VedicFashions", "Wallet used" + strWalletmony);
                            VedicWallet_used = strWalletmony;

                        } else {
                            IsUseCouponecode = false;
                            double result = SubTotal - Supermony + sipingcharge - cpvalue;
                            String p = Double.toString(result);
                            double main = Walletmony - result;
                            mainTotalRS = Double.toString(main);
                            txtPriceTotal.setText(getResources().getString(R.string.rs) + " " + subtotle);
                            txtSuperwalletRs.setText("- " + getResources().getString(R.string.rs) + " " + Supermony);
                            txtShipingChargeRs.setText("+ " + getResources().getString(R.string.rs) + " " + Strshipping);
                            txtPricetime.setText("Price (" + item + " items)");
                            txtGreenText.setText(StrtxtGreenLine);
                            double DouubleFinalTotal2 = Double.parseDouble(mainTotalRS);
                            DecimalFormat dff = new DecimalFormat("#.00"); // df.format(LastCount)
                            walletFinal = dff.format(DouubleFinalTotal2) + "";
                            FinalTotalWithDiscount = dff.format(DouubleFinalTotal2) + "";
                            paytowalletlayout.setVisibility(View.VISIBLE);
                            walletpaytxt.setText("- " + getResources().getString(R.string.rs) + " " + p);
                            StrVedicAmaunt = strWalletmony;
                            txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + "00.00");
                            txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + "00.00");
                            FinalTotalWithDiscount = "00.00";
                            Log.e("VedicFashions", "Wallet used" + p);
                            VedicWallet_used = p;

                        }
                    } else {
                        double SubTotal = Double.valueOf(subtotle);
                        double Supermony = Double.valueOf(super_use_amount);
                        double result = SubTotal - Supermony + sipingcharge;
                        StrVedicAmaunt = "";
                        mainTotalRSwithsuper = Double.toString(result);
                        paytowalletlayout.setVisibility(View.GONE);
                        txtPriceTotal.setText(getResources().getString(R.string.rs) + " " + TotalSubTotal);
                        txtShipingChargeRs.setText("+ " + getResources().getString(R.string.rs) + " " + Strshipping);
                        txtPricetime.setText("Price (" + item + " items)");
                        txtGreenText.setText(StrtxtGreenLine);
                        txtSuperwalletRs.setText("- " + getResources().getString(R.string.rs) + " " + Supermony);
                        // FinalTotal=Double.parseDouble(TotalSubTotal)+Double.parseDouble(Strshipping)+"";
                        double DouubleFinalTotal3 = Double.parseDouble(mainTotalRSwithsuper);
                        DecimalFormat dff = new DecimalFormat("#.00"); // df.format(LastCount)
                        FinalTotal = dff.format(DouubleFinalTotal3) + "";
                        FinalTotalWithDiscount = dff.format(DouubleFinalTotal3) + "";
                        txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + FinalTotal);
                        txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + FinalTotal);
                        FinalTotalWithDiscountAndWallet = FinalTotal;
                    }
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetPayMentDetails extends AsyncTask<Void, Void, Api_Model> {
        RestAdapter restAdapter;
        final ProgressDialog loading = ProgressDialog.show(PayMentClass.this, "", "Please wait...", false, false);

        @Override
        protected void onPreExecute() {
            Log.e("StrUserId", StrUserId);
            Log.e("AddressId", AddressId);
            Log.e("item", item);
            Log.e("FinalTotalWithDiscount", FinalTotalWithDiscount);
            Log.e("F", FinalTotalWithDiscountAndWallet);
            Log.e("Strshipping", Strshipping);
            Log.e("flag", flag);
            Log.e("strCoupenCode", strCoupenCode + " ");
            Log.e("strCoupenValue", strCoupenValue + " ");
            if (Walletcheckb.isChecked() && !strWalletmony.equalsIgnoreCase("")) {
                wallet_used = "Yes";
            } else {
                wallet_used = "No";
                VedicWallet_used = "00.00";
            }
            Log.e("VedicWallet_used", VedicWallet_used);
            Log.e("wallet_used", wallet_used + " ");
            restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.API_URL).build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                return methods.GetPaymentDetails("order_place_wallet", StrUserId, AddressId, item, FinalTotalWithDiscount, Strshipping, flag, strCoupenCode, strCoupenValue, wallet_used, VedicWallet_used, super_use_amount, cart_total);
            } catch (Exception E) {
                Log.e("exception e AAA", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PayMentClass.this);
                alertDialogBuilder.setCancelable(true);
                alertDialogBuilder.setMessage("Unable to connect to server");
                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        GetPayMentDetails task = new GetPayMentDetails();
                        task.execute();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
            } else {
                switch (curators.msgcode) {
                    case "0": {
                        editorCartCounter.putString("CartCounter", "0");
                        editorCartCounter.commit();
                        finish();
                        Intent intent = new Intent(PayMentClass.this, TransactionSuccessFailed.class);
                        intent.putExtra("message", curators.message);
                        intent.putExtra("message1", curators.message1);
                        intent.putExtra("msgCode", 0);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;
                    }
                    case "1":
                        Utils.showToastShort(curators.message, PayMentClass.this);
                        break;
                    case "7": {

                        /*editorCartCounter.putString("CartCounter", "0");
                        editorCartCounter.commit();
                        finish();

                        Intent intent = new Intent(PayMentClass.this, BankDetailsActivity.class);
                        intent.putExtra("Msg", curators.message);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);*/

                        break;
                    }
                    case "9":
                        editorCartCounter.putString("CartCounter", "0");
                        editorCartCounter.commit();
                        for (Api_Model.online_payment_data dataset : curators.online_payment_data) {
                            PaymentParams mPaymentParams = new PaymentParams();
                            PayuConfig payuConfig = new PayuConfig();
                            payuHashes = new PayuHashes();
                            mPaymentParams.setKey(dataset.payu_key);
                            mPaymentParams.setAmount(dataset.amount);
                            Log.e("dataset", "amount" + dataset.amount);
                            mPaymentParams.setProductInfo(dataset.product_info);
                            mPaymentParams.setFirstName(dataset.first_name);
                            mPaymentParams.setEmail(dataset.email);
                            mPaymentParams.setTxnId(dataset.trans_id);
                            mPaymentParams.setSurl("http://www.vedicfashion.com/mapp/index.php?view=order_result");
                            mPaymentParams.setFurl("http://www.vedicfashion.com/mapp/index.php?view=order_result");
                            mPaymentParams.setUdf1(dataset.udf1);
                            mPaymentParams.setUdf2(dataset.udf2);
                            mPaymentParams.setUdf3("");
                            mPaymentParams.setUdf4("");
                            mPaymentParams.setUdf5("");
                            //mPaymentParams.setCardBin("");
                            mPaymentParams.setUserCredentials(dataset.payu_key + ":" + dataset.email);
                            payuConfig.setEnvironment(PayuConstants.PRODUCTION_ENV);
                            payuHashes.setPaymentHash(dataset.payment_hash);
                            payuHashes.setPaymentRelatedDetailsForMobileSdkHash(dataset.payment_related_details_for_mobile_sdk_hash);
                            payuHashes.setVasForMobileSdkHash(dataset.vas_for_mobile_sdk_hash);
                            mPaymentParams.setHash(dataset.payment_hash);
                            IsFromPayYou = true;
                            Intent intent = new Intent(PayMentClass.this, PayUBaseActivity.class);
                            intent.putExtra(PayuConstants.PAYU_CONFIG, payuConfig);
                            intent.putExtra(PayuConstants.PAYMENT_PARAMS, mPaymentParams);
                            intent.putExtra(PayuConstants.PAYU_HASHES, payuHashes);
                            // intent.putExtra(PayuConstants.SALT, STrSlast);
                            try {

                                startActivityForResult(intent, PayuConstants.PAYU_REQUEST_CODE);
                                Log.e("Ok", "Done");

                            } catch (Exception e) {
                                Log.e("e", e.getMessage());
                            }
                        }
                        break;
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Control will come back to this  place when transaction completed(for both fail and success)
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String[] itemsDate = data.getStringExtra("result").split("_");
                    finish();
                    Intent intent = new Intent(PayMentClass.this, TransactionSuccessFailed.class);
                    intent.putExtra("message", itemsDate[0] + "");
                    intent.putExtra("message1", itemsDate[1] + "");
                    intent.putExtra("msgCode", 0);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
            if (resultCode == RESULT_CANCELED) {
                if (data != null) {
                    String[] itemsDate = data.getStringExtra("result").split("_");
                    finish();
                    Intent sdsd = new Intent(PayMentClass.this, TransactionSuccessFailed.class);
                    sdsd.putExtra("message", itemsDate[0] + "");
                    sdsd.putExtra("message1", itemsDate[1] + "");
                    sdsd.putExtra("msgCode", 1);
                    startActivity(sdsd);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        }
    }
}
