package com.vedicfashions.shopping;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import utils.Tools;

/*
 * Created by welcome on 24-10-2016.
 */
public class MyProfile extends AppCompatActivity {

    private View parent_view;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    public static final String EXTRA_OBJCT = "com.app.sample.chatting";


    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    VedicFashions application;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    String StrUserId, StrUserName, StrUserEmail, StrUserPhone, StrUserImage;

    private ImageView imgeditdetails;
    private ImageView imgUserprofile;
    private TextView etUserProfilename;
    private TextView etProfilenumber;
    private TextView etProfileemail;
    private TextView tvMyAddress;
    private TextView tvUseraddress;
    private RelativeLayout relEdit;

    ProgressDialog progressDialog;
    // give preparation animation activity transition

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_myprofile);
        parent_view = findViewById(android.R.id.content);

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


        // animation transition
        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), EXTRA_OBJCT);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        //   getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor((R.color.toolbarcolor))));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(StrUserName);

        initComp();

        imageLoader.displayImage(StrUserImage, imgUserprofile, options);


        collapsingToolbarLayout.setTitle(StrUserName);
        //  etUserProfilename.setText(StrUserName);
        etProfilenumber.setText(StrUserPhone);
        etProfileemail.setText(StrUserEmail);

        relEdit.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MyProfile.this, MyProfileUpdates.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);

            }
        });
        Tools.systemBarLolipop(this);
    }

    public void initComp() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        imgeditdetails = (ImageView) findViewById(R.id.imgeditdetails);
        imgUserprofile = (ImageView) findViewById(R.id.imgUserprofile);
        etProfilenumber = (TextView) findViewById(R.id.tvProfilenumber);
        etProfileemail = (TextView) findViewById(R.id.tvProfileemail);
        tvUseraddress = (TextView) findViewById(R.id.tvUseraddress);
        relEdit = (RelativeLayout) findViewById(R.id.relEdit);
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
        Intent intent = new Intent(MyProfile.this, MyAccountActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

    public DisplayImageOptions getImageOptions() {

        final DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true).showImageOnLoading(R.drawable.default_gray_image)
                .showImageForEmptyUri(R.drawable.default_gray_image).showImageOnFail(R.drawable.default_gray_image)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        return imageOptions;
    }
}

