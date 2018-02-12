package com.vedicfashions.shopping;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.BankAdapter;
import models.BankDetailsModel;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Tools;
import utils.Utils;

public class BankDetailsActivity extends AppCompatActivity {


    Context context;
    ProgressDialog progressDialog;
    BankAdapter mBankAdapter;
    ArrayList<BankDetailsModel> bankDetailsArrayList = new ArrayList<BankDetailsModel>();
    LinearLayoutManager mLayoutManager;
    RecyclerView recyclerView;
    private LinearLayout lyt_not_found;
    ProgressDialog loading;
    String StrMsg = "";
    TextView txtBankMsg;

    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_bankdetails);




        prefCartCounter =getApplicationContext().getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();

        context = this;


        editorCartCounter.putString("CartCounter", "0");
        editorCartCounter.commit();
        Intent in = getIntent();
        if (in != null) {
            StrMsg = in.getStringExtra("Msg");
        }
        initToolbar();
        initComp();
        Tools.systemBarLolipop(this);

        txtBankMsg.setText(StrMsg + " ");
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacesItemDecoration());


        mBankAdapter = new BankAdapter(BankDetailsActivity.this, bankDetailsArrayList);
        recyclerView.setAdapter(mBankAdapter);

        if (Utils.isNetworkAvailable(getBaseContext())) {
            loading = ProgressDialog.show(BankDetailsActivity.this, "", "Please wait...", false, false);
            GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
            mGetNoticeListArrayForUpdates.execute();
        } else {


        }


    }

    private void initToolbar() {


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("Bank Details");
        setSupportActionBar(toolbar);


    }

    private void initComp() {
        recyclerView = (RecyclerView) findViewById(R.id.rvBankDetails);
        lyt_not_found = (LinearLayout) findViewById(R.id.lyt_not_found);
        txtBankMsg = (TextView) findViewById(R.id.txtBankMsg);
    }


    private class GetNoticeListArrayForUpdates extends AsyncTask<Void, Void,
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
                Api_Model curators = methods.GetBankDetails("bank_data");

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

                ShowRetryDialog("GetNoticeListArrayForUpdates");
            } else {
                if (curators.msgcode.toString().equals("0")) {
                    for (Api_Model.bank_data dataset : curators.bank_data) {
                        BankDetailsModel mBankDetailsModel;
                        mBankDetailsModel = new BankDetailsModel(dataset.name, dataset.account_no,
                                dataset.branch, dataset.ifsc_code, dataset.branch_code, dataset.account_type, dataset.address);
                        bankDetailsArrayList.add(mBankDetailsModel);


                    }
                    mBankAdapter.notifyDataSetChanged();
                } else {
                }


            }


        }

    }

    private void ShowRetryDialog(final String WhichAPILoad) {
        try {
            final Dialog dialog = new Dialog(BankDetailsActivity.this);
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
                    dialog.dismiss();
                }
            });
            txtDialogRetryOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loading = ProgressDialog.show(BankDetailsActivity.this, "", "Please wait...", false, false);

                    GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
                    mGetNoticeListArrayForUpdates.execute();

                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {
        }
    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration() {
            this.space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()); //8dp as px, value might be obtained e.g. from dimen resources...
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = 0;
                outRect.bottom = 0; //dont forget about recycling...
            }
            if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
                outRect.bottom = space;
                outRect.top = 0;
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
        BankDetailsActivity.this.finish();
        Intent i = new Intent(getApplicationContext(), MyOrder.class);
        i.putExtra("PageType", "MainActivity");
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

}

