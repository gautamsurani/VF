package com.vedicfashions.shopping;

import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import adapters.ChangeAddressAdapter;
import models.MyAddressdData;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Utils;

/**
 * Created by welcome on 25-10-2016.
 */
public class ChangeAddress extends AppCompatActivity implements View.OnClickListener {

    TextView nodata, txtAddAddress;
Button btnSendData;
    private ArrayList<MyAddressdData> MyAddressList = new ArrayList<>();
    public ChangeAddressAdapter mChangeAddressAdapter;
    RecyclerView recyclerView;
    ProgressDialog loading;
public  static int ThisRadioCount=0;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    String StrUserId;
    MyAddressdData mMyAddressdDataThyis;

    String StrThisSubTotal="";
    String StrThisItems="";
    int ThisPOs=0;
    LinearLayout relativeMain;
    String StrAdddesId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeaddress);

        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();

        StrUserId = prefLogin.getString("userID", null);

         ChangeAddress.ThisRadioCount=0;
        Intent in = getIntent();
        StrThisSubTotal= in.getStringExtra("subtotal");
        StrThisItems= in.getStringExtra("item");
        StrAdddesId= in.getStringExtra("Pos");


        MyAddressList = (ArrayList<MyAddressdData>) getIntent().getSerializableExtra("ArrModel");


        FetchXMLId();
        initToolbar(MyAddressList.size() + " Addresses");

        if(MyAddressList.size()!=0){

            for (int i=0;i<MyAddressList.size();i++){
                MyAddressdData mMyAddressdData=MyAddressList.get(i);
                if(mMyAddressdData.getAddID().equalsIgnoreCase(StrAdddesId)){
                   ChangeAddress.ThisRadioCount=i;
                    break;
                }
            }
            mMyAddressdDataThyis= MyAddressList.get(ChangeAddress.ThisRadioCount);
            btnSendData.setVisibility(View.VISIBLE);
        }else{
            btnSendData.setVisibility(View.GONE);
        }
        final GridLayoutManager mLayoutManager = new GridLayoutManager(ChangeAddress.this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration());

        mChangeAddressAdapter = new ChangeAddressAdapter(ChangeAddress.this, MyAddressList);
        recyclerView.setAdapter(mChangeAddressAdapter);


        mChangeAddressAdapter.setOnItemClickListener(new ChangeAddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {

                MyAddressdData mMyAddressdData = MyAddressList.get(position);
                if (which == 0) {
                     ThisPOs=position;
                     mMyAddressdDataThyis=mMyAddressdData;
                   ChangeAddress.ThisRadioCount=position;
                    mChangeAddressAdapter.notifyDataSetChanged();

                } else {
                    Intent in = new Intent(ChangeAddress.this, AddEditAddres.class);
                    in.putExtra("Page", "EditAddress");
                    in.putExtra("Model", mMyAddressdData);
                    startActivity(in);
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                }
            }
        });
        txtAddAddress.setOnClickListener(this);
        btnSendData.setOnClickListener(this);
    }

    private void initToolbar(String s) {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText(s);
        setSupportActionBar(toolbar);


    }


    private void FetchXMLId() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        nodata = (TextView) findViewById(R.id.nodata);
        txtAddAddress = (TextView) findViewById(R.id.txtAddAddress);

        relativeMain = (LinearLayout) findViewById(R.id.relativeMain);
        btnSendData= (Button) findViewById(R.id.btnSendData);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        //   getMenuInflater().inflate(R.menu.menu_discussion_filter, menu);
        return true;

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        if (Utils.isNetworkAvailable(getBaseContext())) {
            Log.d("NotAddressRadio", "NotAddressRadio");
            loading = ProgressDialog.show(ChangeAddress.this, "", "Please wait...", false, false);
            GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
            mGetNoticeListArrayForUpdates.execute();
        } else {
            Utils.ShowSnakBar("Please Check Your Internet Connection", relativeMain, ChangeAddress.this);

        }


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
        switch (v.getId()){
            case R.id.btnSendData:

                finish();
                Intent in = new Intent(ChangeAddress.this, DeleveryClass.class);

              /*  in.putExtra("list", List < MyAddressdData > MyAddressList);
                in.putExtra("QuestionListExtra", ArrayList<MyAddressdData>MyAddressList);*/



                Bundle args = new Bundle();
                args.putSerializable("ARRAYLIST", MyAddressList);

                in.putExtra("BUNDLE", args);
                in.putExtra("Page", "AddressRadio");
                in.putExtra("Model", mMyAddressdDataThyis);

                in.putExtra("item", StrThisItems);
                in.putExtra("subtotal", StrThisSubTotal);
                startActivity(in);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);

                break;

            case R.id.txtAddAddress:
                Intent zdxcdxin = new Intent(ChangeAddress.this, AddEditAddres.class);
                zdxcdxin.putExtra("Page", "AddAddress");
                startActivity(zdxcdxin);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        Intent in = new Intent(ChangeAddress.this, DeleveryClass.class);

        Bundle args = new Bundle();

        if(MyAddressList.size()!=0){
            args.putSerializable("ARRAYLIST", MyAddressList);
        }else{
            args.putSerializable("ARRAYLIST", null);
        }
        in.putExtra("BUNDLE", args);
        in.putExtra("Page", "AddressRadio");
        in.putExtra("Model", mMyAddressdDataThyis);

        in.putExtra("item", StrThisItems);
        in.putExtra("subtotal", StrThisSubTotal);
        startActivity(in);
        overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
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
                if (curators.msgcode.toString().equals("0")) {
                    if (!curators.flag.toString().equals("no_address")) {
                        int in = 0;
                        MyAddressList.clear();
                        for (Api_Model.address_list dataset : curators.address_list) {


                            MyAddressdData mMyAddressdData;
                            mMyAddressdData = new MyAddressdData(dataset.SR, dataset.addID,
                                    dataset.name, dataset.phone, dataset.address1,
                                    dataset.address2, dataset.area, dataset.city,
                                    dataset.state, dataset.pincode);
                            MyAddressList.add(mMyAddressdData);
                            mChangeAddressAdapter.notifyDataSetChanged();
                        }

                        if(MyAddressList.size()!=0){

                           // mMyAddressdDataThyis= MyAddressList.get(ThisPOs);
                            mMyAddressdDataThyis= MyAddressList.get(ChangeAddress.ThisRadioCount);
                        }
                    } else {


                    }


                }


            }

        }

    }

    private void ShowRetryDialog() {
        try {
            final Dialog dialog = new Dialog(ChangeAddress.this);
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
                    loading = ProgressDialog.show(ChangeAddress.this, "", "Please wait...", false, false);
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
