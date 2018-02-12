package com.vedicfashions.shopping;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import adapters.MyOrderAdapter;
import models.MyOrderdData;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Utils;

/**
 * Created by welcome on 24-10-2016.
 */
public class MyOrder extends AppCompatActivity {
    private List<MyOrderdData> MyOrderList = new ArrayList<>();
    public MyOrderAdapter mMyOrderAdapter;

    String ThisPageType = "MainActivity";

    boolean IsLAstLoading = true;
    ProgressBar progressBar1;
    private View parent_view;
    RecyclerView recyclerView;
    RelativeLayout relativeLoader;

    public int pagecode = 0;
    String StrAddressID = "";
    ProgressDialog loading;
    TextView nodata;

    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    VedicFashions application;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    String StrUserId, StrUserName, StrUserEmail, StrUserPhone, StrUserImage;


    LinearLayout relativeMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);

        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();

        StrUserId = prefLogin.getString("userID", null);
        StrUserName = prefLogin.getString("name", null);
        StrUserEmail = prefLogin.getString("email", null);
        StrUserPhone = prefLogin.getString("phone", null);
        StrUserImage = prefLogin.getString("userimage", null);

        nodata = (TextView) findViewById(R.id.nodata);
        application = (VedicFashions) getApplicationContext();
        options = getImageOptions();
        imageLoader = application.getImageLoader();


        Intent in = this.getIntent();
        if (in != null) {
            ThisPageType = in.getStringExtra("PageType");
        }


        FetchXMLId();
        initToolbar();

        final GridLayoutManager mLayoutManager = new GridLayoutManager(MyOrder.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration());


        mMyOrderAdapter = new MyOrderAdapter(MyOrder.this, MyOrderList);
        recyclerView.setAdapter(mMyOrderAdapter);


        mMyOrderAdapter.setOnItemClickListener(new MyOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {

                MyOrderdData mMyOrderdData = MyOrderList.get(position);

                Intent it = new Intent(MyOrder.this, OrderDetailsActivity.class);
                it.putExtra("OrderId", mMyOrderdData.getOrderID());
                startActivity(it);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);

            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (IsLAstLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount &&
                                recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom() <= recyclerView.getHeight()) {
                            IsLAstLoading = false;
                            progressBar1.setVisibility(View.VISIBLE);
                            pagecode++;


                            GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
                            mGetNoticeListArrayForUpdates.execute();
                            Log.e("...", "Last Item Wow !");
                            //Do pagination.. i.e. fetch new data
                        }
                    }
                }
            }
        });

        if (Utils.isNetworkAvailable(getBaseContext())) {
            loading = ProgressDialog.show(MyOrder.this, "", "Please wait...", false, false);
            GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
            mGetNoticeListArrayForUpdates.execute();
        } else {

            Utils.ShowSnakBar("No Internet Connection..!!", relativeMain, MyOrder.this);

        }

    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("My Order");
        setSupportActionBar(toolbar);


    }

    private void DeleteAddress(final String addID) {

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MyOrder.this);
        alertDialogBuilder.setMessage("Remove Address");

        alertDialogBuilder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                StrAddressID = addID;
                DeleteStaff asd = new DeleteStaff();
                asd.execute();
                arg0.dismiss();
            }
        });

        alertDialogBuilder.setNegativeButton("CENCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int which) {
                arg0.dismiss();
              /*  DeleteAlbumId="";*/
            }
        });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void FetchXMLId() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);

        relativeMain = (LinearLayout) findViewById(R.id.relativeMain);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        //   getMenuInflater().inflate(R.menu.menu_discussion_filter, menu);
        return true;

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public DisplayImageOptions getImageOptions() {

        final DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true).showImageOnLoading(R.drawable.default_gray_image)
                .showImageForEmptyUri(R.drawable.default_gray_image).showImageOnFail(R.drawable.default_gray_image)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        return imageOptions;
    }


    private class GetNoticeListArrayForUpdates extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            Log.e("StrUserId", StrUserId + "");
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                Api_Model curators = methods.GEtMyOrdersAPI("orders", "list", StrUserId, Integer.toString(pagecode));

                return curators;
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }


        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            IsLAstLoading = true;
            progressBar1.setVisibility(View.GONE);
            if (curators == null) {

                ShowRetryDialog("GetNoticeListArrayForUpdates");
            } else {
                if (curators.msgcode.toString().equals("0")) {
                    for (Api_Model.order_list dataset : curators.order_list) {
                        MyOrderdData mMyOrderdData;
                        mMyOrderdData = new MyOrderdData(dataset.SR, dataset.orderID,
                                dataset.date, dataset.status, dataset.amount);
                        MyOrderList.add(mMyOrderdData);


                    }
                    mMyOrderAdapter.notifyDataSetChanged();
                } else if (curators.msgcode.toString().equals("1")) {
                    nodata.setText(curators.message.toString());
                   // nodata.setVisibility(View.VISIBLE);
                    Utils.ShowSnakBar(curators.message.toString(),relativeMain,MyOrder.this);
                } else {

                }


            }


        }

    }

    private void ShowRetryDialog(final String WhichAPILoad) {
        try {
            final Dialog dialog = new Dialog(MyOrder.this);
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
                    loading = ProgressDialog.show(MyOrder.this, "", "Please wait...", false, false);

                    GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
                    mGetNoticeListArrayForUpdates.execute();

                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {
        }
    }


    @Override
    public void onBackPressed() {


        if (ThisPageType.equalsIgnoreCase("MainActivity")) {
            Intent intedcsdsnt = new Intent(MyOrder.this, MainActivity.class);
            startActivity(intedcsdsnt);
            overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right);
        } else {
            Intent intedcsdsnt = new Intent(MyOrder.this, MyAccountActivity.class);
            startActivity(intedcsdsnt);
            overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right);
        }


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

    private class DeleteStaff extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;
        ProgressDialog loading = ProgressDialog.show(MyOrder.this, "", "Please wait...", false, false);

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
                Api_Model curators = methods.DeteAddressAPI("addresses", "remove", StrUserId, StrAddressID);

                return curators;
            } catch (Exception E) {
                Log.i("exceptio e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MyOrder.this);
                alertDialogBuilder.setMessage("Unable to connect to server");

                alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                    }
                });
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
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
                Log.i("Curator", curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {

                    finish();
                    startActivity(new Intent(MyOrder.this, MyOrder.class));
                    overridePendingTransition(
                            R.anim.slide_in_left,
                            R.anim.slide_out_right);

                } else {

                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, MyOrder.this);


                }


            }

        }

    }

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration() {
            this.space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                    getResources().getDisplayMetrics()); //8dp as px, value might be obtained e.g. from dimen resources...
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
}
