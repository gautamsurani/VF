package com.vedicfashions.shopping;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.ProductListAdapter;
import adapters.StateAdapter;
import models.MyAddressdData;
import models.StatelistModel;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Utils;

/*
 * Created by welcome on 24-10-2016.
 */
public class AddEditAddres extends AppCompatActivity implements View.OnClickListener {

    EditText etuserfullname, etuserphonnu, etuseraddressone, etuseraddresstwo, etdArea, etuseraddresscity,
            etuseraddresszipcode;
    String Stretuserfullname = "", Stretuserphonnu = "", Stretuseraddressone = "", Stretuseraddresstwo = "",
            StretdArea = "", Stretuseraddresscity = "", Stretuseraddressstate = "", Stretuseraddresszipcode = "";
    String StrUserId, StrUserName;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    ProgressDialog loading;
    RelativeLayout relativeMain;
    Button btnPlaceorder;
    MyAddressdData mMyAddressdData;
    String STrAddressID = "";
    TextView tvSpinnerState;
    Dialog dialog;
    RecyclerView rvSelectState;
    ArrayList<StatelistModel> statelistList = new ArrayList<>();
    public static ArrayList<String> ArrStateList = new ArrayList<>();
    String ThisPage = "";
    String Stritem = "", Strsubtotal = "";

    @SuppressLint({"SetTextI18n", "CommitPrefEdits"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_editaddress);
        FetchXmlID();
        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);
        StrUserName = prefLogin.getString("name", null);
        btnPlaceorder.setOnClickListener(this);
        Intent in = getIntent();
        ThisPage = in.getStringExtra("Page");
        if (ThisPage.equalsIgnoreCase("AddAddress")) {
            btnPlaceorder.setText("Add Address");
        } else if (ThisPage.equalsIgnoreCase("AddAddressFromDeleveryClass")) {
            Stritem = in.getStringExtra("item");
            Strsubtotal = in.getStringExtra("subtotal");
        } else {
            btnPlaceorder.setText("Update Address");
            mMyAddressdData = (MyAddressdData) in.getExtras().getSerializable("Model");
            etuserfullname.setText(mMyAddressdData.getName());
            etuserphonnu.setText(mMyAddressdData.getPhone());
            etuseraddressone.setText(mMyAddressdData.getAddress1());
            etuseraddresstwo.setText(mMyAddressdData.getAddress2());
            etdArea.setText(mMyAddressdData.getArea());
            etuseraddresscity.setText(mMyAddressdData.getCity());
            tvSpinnerState.setText(mMyAddressdData.getState());
            etuseraddresszipcode.setText(mMyAddressdData.getPincode());
            STrAddressID = mMyAddressdData.getAddID();
        }

        tvSpinnerState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(AddEditAddres.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                dialog.setContentView(R.layout.layout_selectstate);
                rvSelectState = dialog.findViewById(R.id.rvSelectState);
                RecyclerView.LayoutManager mLayoutManagermain = new LinearLayoutManager(AddEditAddres.this);
                rvSelectState.setLayoutManager(mLayoutManagermain);
                rvSelectState.setHasFixedSize(true);
                for (int i = 0; i < ArrStateList.size(); i++) {
                    StatelistModel mStatelistModel;
                    mStatelistModel = new StatelistModel(ArrStateList.get(i));
                    statelistList.add(mStatelistModel);
                }
                StateAdapter stateAdapter = new StateAdapter(AddEditAddres.this, statelistList);
                rvSelectState.setAdapter(stateAdapter);
                stateAdapter.setOnItemClickListener(new ProductListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view, int which) {
                        tvSpinnerState.setText(statelistList.get(position).getName());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        initToolbar(ThisPage);

        if (ArrStateList.size() == 0) {
            GetStateArrAsynTask mGetStateArrAsynTask = new GetStateArrAsynTask();
            mGetStateArrAsynTask.execute();
        }
    }

    @SuppressLint("SetTextI18n")
    private void initToolbar(String thisPafe) {
        Toolbar toolbar = findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        TextView textView = toolbar.findViewById(R.id.eshop);
        if (thisPafe.equalsIgnoreCase("AddAddress")) {
            textView.setText("Add Address");
        } else {
            textView.setText("Edit Address");
        }
        setSupportActionBar(toolbar);
    }

    private void FetchXmlID() {
        btnPlaceorder = findViewById(R.id.btnPlaceorder);
        relativeMain = findViewById(R.id.relativeMain);
        tvSpinnerState = findViewById(R.id.tvSpinnerState);
        etuserfullname = findViewById(R.id.etuserfullname);
        etuserphonnu = findViewById(R.id.etuserphonnu);
        etuseraddressone = findViewById(R.id.etuseraddressone);
        etuseraddresstwo = findViewById(R.id.etuseraddresstwo);
        etdArea = findViewById(R.id.etdArea);
        etuseraddresscity = findViewById(R.id.etuseraddresscity);
        etuseraddresszipcode = findViewById(R.id.etuseraddresszipcode);
    }

    @Override
    public void onClick(View v) {

        Stretuserfullname = etuserfullname.getText().toString();
        Stretuserphonnu = etuserphonnu.getText().toString();
        Stretuseraddressone = etuseraddressone.getText().toString();
        Stretuseraddresstwo = etuseraddresstwo.getText().toString();

        StretdArea = etdArea.getText().toString();
        Stretuseraddresscity = etuseraddresscity.getText().toString();
        Stretuseraddressstate = tvSpinnerState.getText().toString();
        Stretuseraddresszipcode = etuseraddresszipcode.getText().toString();


        if (Utils.isNetworkAvailable(AddEditAddres.this)) {
            if (TextUtils.isEmpty(Stretuserfullname)) {
                Utils.ShowSnakBar("Please enter Full Name", relativeMain, AddEditAddres.this);
            } else if (TextUtils.isEmpty(Stretuserphonnu)) {
                Utils.ShowSnakBar("Please enter Phone", relativeMain, AddEditAddres.this);
            } else if (Stretuserphonnu.length() != 10) {
                Utils.ShowSnakBar("Invalid Mobile", relativeMain, AddEditAddres.this);
            } else if (TextUtils.isEmpty(Stretuseraddressone)) {
                Utils.ShowSnakBar("Please enter Address 1", relativeMain, AddEditAddres.this);
            } else if (TextUtils.isEmpty(StretdArea)) {
                Utils.ShowSnakBar("Please enter Area", relativeMain, AddEditAddres.this);
            } else if (TextUtils.isEmpty(Stretuseraddresscity)) {
                Utils.ShowSnakBar("Please enter City", relativeMain, AddEditAddres.this);
            } else if (TextUtils.isEmpty(Stretuseraddressstate)) {
                Utils.ShowSnakBar("Please enter State", relativeMain, AddEditAddres.this);
            } else if (TextUtils.isEmpty(Stretuseraddresszipcode)) {
                Utils.ShowSnakBar("Please enter Zip", relativeMain, AddEditAddres.this);
            } else {
                Utils.hideKeyboard(AddEditAddres.this);
                RegisterAsynTask mRegisterAsynTask = new RegisterAsynTask();
                mRegisterAsynTask.execute();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetStateArrAsynTask extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            loading = ProgressDialog.show(AddEditAddres.this, "", "Please wait...", false, false);
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                return methods.GetStateAPI("state", "list", StrUserId);
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }


        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators != null) {
                if (curators.msgcode.equals("0")) {
                    for (Api_Model.list dataset : curators.list) {
                        ArrStateList.add(dataset.name);
                    }
                } else {
                    Utils.ShowSnakBar(curators.message, relativeMain, AddEditAddres.this);
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class RegisterAsynTask extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            loading = ProgressDialog.show(AddEditAddres.this, "", "Please wait...", false, false);
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                return methods.AddAdrressGEt("addresses", "add", StrUserId, Stretuserfullname, Stretuserphonnu,
                        Stretuseraddressone, Stretuseraddresstwo, StretdArea, Stretuseraddresscity, Stretuseraddressstate,
                        Stretuseraddresszipcode, STrAddressID);
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }


        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators != null) {
                if (curators.msgcode.equals("0")) {
                    onBackPressed();
                } else {
                    Utils.ShowSnakBar(curators.message, relativeMain, AddEditAddres.this);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (ThisPage.equalsIgnoreCase("AddAddressFromDeleveryClass")) {
            finish();
            Intent in = new Intent(AddEditAddres.this, DeleveryClass.class);
            in.putExtra("Page", "Cart");
            in.putExtra("item", Stritem);
            in.putExtra("subtotal", Strsubtotal);
            startActivity(in);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        } else {
            super.onBackPressed();
            Utils.hideKeyboard(AddEditAddres.this);
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
}
