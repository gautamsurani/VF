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
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import adapters.MyAddressAdapter;
import models.MyAddressdData;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Utils;

/**
 * Created by welcome on 24-10-2016.
 */
public class MyAddress extends AppCompatActivity implements View.OnClickListener {
    private List<MyAddressdData> MyAddressList = new ArrayList<>();
    public MyAddressAdapter mMyAddressAdapter;
    public int pagecode = 0;
    TextView nodata, txtSavedAdress, txtAddAddress;


    private View parent_view;
    RecyclerView recyclerView;
    RelativeLayout relativeLoader;

    String StrAddressID = "";
    ProgressDialog loading;

    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    VedicFashions application;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    String StrUserId, StrUserName, StrUserEmail, StrUserPhone, StrUserImage;
LinearLayout lyt_not_found;

    LinearLayout relativeMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaddress);

        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();

        StrUserId = prefLogin.getString("userID", null);
        StrUserName = prefLogin.getString("name", null);
        StrUserEmail = prefLogin.getString("email", null);
        StrUserPhone = prefLogin.getString("phone", null);
        StrUserImage = prefLogin.getString("userimage", null);

        application = (VedicFashions) getApplicationContext();
        options = getImageOptions();
        imageLoader = application.getImageLoader();




        FetchXMLId();
        initToolbar();

        final GridLayoutManager mLayoutManager = new GridLayoutManager(MyAddress.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration());


        mMyAddressAdapter = new MyAddressAdapter(MyAddress.this, MyAddressList);
        recyclerView.setAdapter(mMyAddressAdapter);

        mMyAddressAdapter.setOnMoreButtonClickListener(new MyAddressAdapter.OnMoreButtonClickListener() {
            @Override
            public void onItemClick(View v, MyAddressdData obj, int actionId) {

                switch (actionId) {
                    case R.id.action_edit:
                        Intent in = new Intent(MyAddress.this, AddEditAddres.class);
                        in.putExtra("Page", "EditAddress");
                        in.putExtra("Model", obj);
                        startActivity(in);
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left);
                        break;
                    case R.id.action_remove:
                        DeleteAddress(obj.getAddID());
                        break;
                }
            }
        });

        if (Utils.isNetworkAvailable(getBaseContext())) {
            loading = ProgressDialog.show(MyAddress.this, "", "Please wait...", false, false);
            GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
            mGetNoticeListArrayForUpdates.execute();
        } else {

            Utils.ShowSnakBar("Please Check Your Internet Connection", relativeMain, MyAddress.this);

        }
        txtAddAddress.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Utils.isNetworkAvailable(getBaseContext())) {

            loading = ProgressDialog.show(MyAddress.this, "", "Please wait...", false, false);
            GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
            mGetNoticeListArrayForUpdates.execute();
        } else {

            Utils.ShowSnakBar("Please Check Your Internet Connection", relativeMain, MyAddress.this);

        }
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("My Addresses");
        setSupportActionBar(toolbar);


    }

    private void DeleteAddress(final String addID) {

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MyAddress.this);
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
        nodata = (TextView) findViewById(R.id.nodata);
        txtSavedAdress = (TextView) findViewById(R.id.txtSavedAdress);
        txtAddAddress = (TextView) findViewById(R.id.txtAddAddress);
         lyt_not_found= (LinearLayout) findViewById(R.id.lyt_not_found);

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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        Intent in = new Intent(MyAddress.this, AddEditAddres.class);
        in.putExtra("Page", "AddAddress");
        startActivity(in);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
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

                ShowRetryDialog("GetNoticeListArrayForUpdates");
            } else {
                if (curators.msgcode.toString().equals("0")) {
                    MyAddressList.clear();
                    if (!curators.flag.toString().equals("no_address")) {

                        lyt_not_found.setVisibility(View.GONE);
                        for (Api_Model.address_list dataset : curators.address_list) {

                            MyAddressdData mMyAddressdData;
                            mMyAddressdData = new MyAddressdData(dataset.SR, dataset.addID,
                                    dataset.name, dataset.phone, dataset.address1,
                                    dataset.address2, dataset.area, dataset.city,
                                    dataset.state, dataset.pincode);
                            MyAddressList.add(mMyAddressdData);


                        }
                        mMyAddressAdapter.notifyDataSetChanged();
                        txtSavedAdress.setText(MyAddressList.size() + " SAVED ADDRESSES");
                    }else{
                        lyt_not_found.setVisibility(View.VISIBLE);
                    }

                } else {


                }


            }


        }

    }

    private void ShowRetryDialog(final String WhichAPILoad) {
        try {
            final Dialog dialog = new Dialog(MyAddress.this);
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
                    loading = ProgressDialog.show(MyAddress.this, "", "Please wait...", false, false);

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
            super.onBackPressed();
            overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right);

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
        ProgressDialog loading = ProgressDialog.show(MyAddress.this, "", "Please wait...", false, false);

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
                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(MyAddress.this);
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
                    startActivity(new Intent(MyAddress.this, MyAddress.class));
                    overridePendingTransition(
                            R.anim.slide_in_left,
                            R.anim.slide_out_right);

                } else {

                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, MyAddress.this);


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
