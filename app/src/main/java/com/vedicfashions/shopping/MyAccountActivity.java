package com.vedicfashions.shopping;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import customclass.MyRoundImageview;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Tools;
import utils.Utils;
public class MyAccountActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog progressDialog;
    LinearLayout liDashboarde, liMyWishList, liMyAddress, liMyOffers, liDetacive;

    ProgressDialog loading;

    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    VedicFashions application;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    String StrUserName, StrUserEmail, StrUserPhone, StrUserImage;

    private MyRoundImageview imgMyUpdateimage;
    TextView tvMyuname, tvMyuphone;
    LinearLayout liMyprofile, liMyOrders, liMyWallet, liMyNotification, liChangepwd, lideactiveAct, liHome;
    RelativeLayout relbasicdetail;
    private Button btnLogout;

    String StrUserId, StrCartCounter;

    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
    TextView tvmytotalitems;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_myaccount);
        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();


        prefCartCounter = MyAccountActivity.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefLogin.getString("CartCounter", "0");


        StrUserId = prefLogin.getString("userID", null);
        StrUserName = prefLogin.getString("name", null);
        StrUserEmail = prefLogin.getString("email", null);
        StrUserPhone = prefLogin.getString("phone", null);
        StrUserImage = prefLogin.getString("userimage", null);

        application = (VedicFashions) getApplicationContext();
        options = getImageOptions();
        imageLoader = application.getImageLoader();
        inotComp();
        initToolbar();

        Log.e("StrUserName", StrUserName);

        tvMyuname.setText(StrUserName + "");
        tvMyuphone.setText(StrUserPhone);
        imageLoader.displayImage(StrUserImage, imgMyUpdateimage, options);
        liDashboarde.setOnClickListener(this);
        liMyprofile.setOnClickListener(this);
        liMyOrders.setOnClickListener(this);
        liMyWallet.setOnClickListener(this);
        liMyWishList.setOnClickListener(this);
        liMyAddress.setOnClickListener(this);
        liMyOffers.setOnClickListener(this);
        liDetacive.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        Tools.systemBarLolipop(this);


    }

    public void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("My Account");
        setSupportActionBar(toolbar);


    }

    public void inotComp() {
        btnLogout = (Button) findViewById(R.id.btnLogout);
        imgMyUpdateimage = (MyRoundImageview) findViewById(R.id.imgMyUpdateimage);
        tvMyuname = (TextView) findViewById(R.id.tvMyuname);
        tvMyuphone = (TextView) findViewById(R.id.tvMyuphone);


        liDashboarde = (LinearLayout) findViewById(R.id.liDashboarde);
        liMyprofile = (LinearLayout) findViewById(R.id.liMyprofile);
        liMyOrders = (LinearLayout) findViewById(R.id.liMyOrders);
        liMyWallet = (LinearLayout) findViewById(R.id.liMyWallet);
        liMyWishList = (LinearLayout) findViewById(R.id.liMyWishList);
        liMyAddress = (LinearLayout) findViewById(R.id.liMyAddress);
        liMyOffers = (LinearLayout) findViewById(R.id.liMyOffers);
        liDetacive = (LinearLayout) findViewById(R.id.liMyOffers);
        liDetacive = (LinearLayout) findViewById(R.id.liDetacive);


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
        switch (v.getId()) {

            case R.id.liDashboarde:
                Intent intent = new Intent(MyAccountActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;

            case R.id.liMyprofile:
                Intent intesdcsnt = new Intent(MyAccountActivity.this, MyProfile.class);
                startActivity(intesdcsnt);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;

            case R.id.liMyOrders:
                Intent intedcsdsnt = new Intent(MyAccountActivity.this, MyOrder.class);
                intedcsdsnt.putExtra("PageType", "MyAccount");
                startActivity(intedcsdsnt);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.liMyWallet:
                Intent intedcsdsnte = new Intent(MyAccountActivity.this, WalletHistory.class);
                //intedcsdsnte.putExtra("PageType", "MyAccount");
                startActivity(intedcsdsnte);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;

            case R.id.liMyWishList:
                Intent intesdsdnt = new Intent(MyAccountActivity.this, MyWishList.class);
                startActivity(intesdsdnt);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;

            case R.id.liMyAddress:
                Intent indsdwdtent = new Intent(MyAccountActivity.this, MyAddress.class);
                startActivity(indsdwdtent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.liDetacive:


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyAccountActivity.this);
                alertDialogBuilder.setMessage("Are you sure deactive account ?");

                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        loading = ProgressDialog.show(MyAccountActivity.this, "", "Please wait...", false, false);
                        AccountDeactive mAccountDeactive = new AccountDeactive();
                        mAccountDeactive.execute();


                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int which) {
                        arg0.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();


                break;

            case R.id.liMyOffers:
                Intent intenddt = new Intent(MyAccountActivity.this, Updates.class);
                intenddt.putExtra("PageTypeForPush", "NotPush");
                startActivity(intenddt);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;

            case R.id.btnLogout:
                LogOUtCall();
                break;


        }
    }

    private void LogOUtCall() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MyAccountActivity.this);
        alertDialogBuilder.setMessage(getResources().getString(R.string.str_common_LogOut));

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                finish();


                SharedPreferences prefThisLogin = getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
                SharedPreferences.Editor editorThisLogin = prefThisLogin.edit();
                editorThisLogin.putString("userID", null);
                editorThisLogin.commit();
                editorThisLogin.clear();

                SharedPreferences prefThisCounter = getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
                SharedPreferences.Editor editorThisCounter = prefThisCounter.edit();
                editorThisCounter.putString("CartCounter", "0");
                editorThisCounter.commit();
                editorThisCounter.clear();


                Intent i = new Intent(getApplicationContext(), Login.class);
                editorLogin.putString("userID", null);
                editorLogin.commit();
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int which) {
                arg0.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
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
        Intent intent = new Intent(MyAccountActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }


    private class AccountDeactive extends AsyncTask<Void, Void,
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
                Api_Model curators = methods.AccountDeactiveAPI("deactive", StrUserId);

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

            } else {

                Log.i("Curator", curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {

                    finish();

                    try {
                        LoginManager.getInstance().logOut();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SharedPreferences prefThisLogin = getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
                    SharedPreferences.Editor editorThisLogin = prefThisLogin.edit();
                    editorThisLogin.putString("userID", null);
                    editorThisLogin.commit();
                    editorThisLogin.clear();

                    SharedPreferences prefThisCounter = getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
                    SharedPreferences.Editor editorThisCounter = prefThisCounter.edit();
                    editorThisCounter.putString("CartCounter", "0");
                    editorThisCounter.commit();
                    editorThisCounter.clear();


                    Intent i = new Intent(getApplicationContext(), Login.class);
                    editorLogin.putString("userID", null);
                    editorLogin.commit();
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);


                    Utils.showToastShort(curators.message.toString(), MyAccountActivity.this);
                } else {
                    Utils.showToastShort(curators.message.toString(), MyAccountActivity.this);
                }
            }
        }
    }
}
