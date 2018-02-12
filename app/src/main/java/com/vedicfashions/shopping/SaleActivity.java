package com.vedicfashions.shopping;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapters.UpdatesAdapter;
import models.UpdatestData;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.DividerItemDecoration;
import utils.IApiMethods;
import utils.Utils;

/**
 * Created by welcome on 04-08-2016.
 */
public class SaleActivity extends AppCompatActivity {
    private List<UpdatestData> UpdateNotificationList = new ArrayList<>();
    public UpdatesAdapter mUpdatesAdapter;
    // ProgressBar progressBar1;
    TextView nodata;
    SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;

    String StrUserId;

    public int pagecode = 0;
    private View parent_view;
    RecyclerView recyclerView;
    ProgressDialog loading;
    SwipeRefreshLayout swiperefresh;
    String StrPage = "Push";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        FetchXMLId();
        initToolbar();

        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);

        Intent in = getIntent();
        if (in != null) {
            StrPage = in.getStringExtra("PageTypeForSale");
        }
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(SaleActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(SaleActivity.this, DividerItemDecoration.VERTICAL_LIST));


        mUpdatesAdapter = new UpdatesAdapter(SaleActivity.this, UpdateNotificationList);
        recyclerView.setAdapter(mUpdatesAdapter);


        mUpdatesAdapter.setOnItemClickListener(new UpdatesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {

                finish();
                UpdatestData mUpdatestData = UpdateNotificationList.get(position);
                Intent intent = new Intent(SaleActivity.this, SaleViewActivity.class);
                intent.putExtra("title", mUpdatestData.getTitle());
                intent.putExtra("content", mUpdatestData.getMessage());
                intent.putExtra("date", mUpdatestData.getAdded_on());
                intent.putExtra("IMgMain", mUpdatestData.getImage());
                intent.putExtra("sharemsg", mUpdatestData.getShre_msg());
                intent.putExtra("PButton", mUpdatestData.getProduct_button());
                intent.putExtra("CBuuton", mUpdatestData.getCat_button());
                intent.putExtra("SubCat", mUpdatestData.getSubcat());
                intent.putExtra("ButtonID", mUpdatestData.getButtonID());
                intent.putExtra("Name", mUpdatestData.getName());
                intent.putExtra("PageThis", StrPage);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        if (Utils.isNetworkAvailable(getBaseContext())) {
            loading = ProgressDialog.show(SaleActivity.this, "", "Please wait...", false, false);

            mSwipeRefreshLayout.setVisibility(View.GONE);
            GetNotificationDetalil task = new GetNotificationDetalil();
            task.execute();
        } else {
            Utils.showToastShort("Please Check Your Internet Connection", SaleActivity.this);

        }

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Utils.hideKeyboard(SaleActivity.this);
                mSwipeRefreshLayout.setRefreshing(true);
                (new Handler()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (Utils.isNetworkAvailable(getBaseContext())) {
                            pagecode = 0;
                            GetNotificationDetalil task = new GetNotificationDetalil();
                            task.execute();
                        } else {
                            Utils.showToastShort("Please Check Your Internet Connection", SaleActivity.this);

                        }
                    }
                });
            }
        });

    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("Offers");
        setSupportActionBar(toolbar);
    }


    private void FetchXMLId() {
        //   progressBar1=(ProgressBar)findViewById(R.id.progressBar1);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        nodata = (TextView) findViewById(R.id.nodata);

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the men
        return true;

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class GetNotificationDetalil extends AsyncTask<Void, Void,
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
                Api_Model curators = methods.GetAllNOtification("sale_offer_list", StrUserId);
                Log.e("uid", "uid:=" + StrUserId);
                return curators;
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setVisibility(View.VISIBLE);


            if (curators == null) {
                ShowRetryDialog();
            } else {

                if (curators.msgcode.toString().equals("0")) {
                    try {

                        nodata.setVisibility(View.GONE);
                        if (pagecode == 0) {
                            UpdateNotificationList.clear();
                        }
                        for (Api_Model.sale_offer_list dataset : curators.sale_offer_list) {
                            UpdatestData mUpdatestData;
                            mUpdatestData = new UpdatestData(dataset.offer_ID,
                                    dataset.name,
                                    dataset.buttonID,
                                    dataset.subcat, dataset.title, dataset.image, dataset.message, dataset.added_on,
                                    dataset.product_button, dataset.cat_button, dataset.shre_msg);
                            UpdateNotificationList.add(mUpdatestData);


                        }


                        mUpdatesAdapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        Utils.showToastShort(e.getMessage().toString(), SaleActivity.this);
                        nodata.setVisibility(View.VISIBLE);

                    }

                } else {
                    nodata.setVisibility(View.VISIBLE);

                    nodata.setText(curators.message.toString());

                }


            }


        }


    }

    private void ShowRetryDialog() {
        try {
            final Dialog dialog = new Dialog(SaleActivity.this);
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
                    mSwipeRefreshLayout.setVisibility(View.GONE);

                    GetNotificationDetalil mGetNotificationDetalil = new GetNotificationDetalil();
                    mGetNotificationDetalil.execute();


                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {

        if (StrPage.equalsIgnoreCase("Push")) {
            Intent innew = new Intent(SaleActivity.this, MainActivity.class);
            startActivity(innew);
            overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right);
        } else {
            super.onBackPressed();
            overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }

    }


}
