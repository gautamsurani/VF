package com.vedicfashions.shopping;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Tools;
import utils.Utils;

/*
 * Created by welcome on 14-10-2016.
 */

public class ReferActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout sharewp, sharefb, sharemail, sharmore;
    ProgressDialog progressDialog;
    ImageView imgReferfriend;
    Toolbar toolbar;
    TextView tvReferAmount, tvReferlink, tvReferlines;
    VedicFashions application;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    ProgressDialog loading;
    String StrShareMsg = "", StrShreImageUrl, ref_key = "", you_get = "", you_friend_get = "";

    String WhichShareType = "";
    String StrUserId, StrCartCounter;
    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        application = (VedicFashions) getApplicationContext();
        options = getImageOptions();
        imageLoader = application.getImageLoader();

        prefCartCounter = getApplicationContext().getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");


        FetchXmlID();
        initToolbar();

        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", "");

        loading = ProgressDialog.show(ReferActivity.this, "", "Please wait...", false, false);
        LoadReferDetail task = new LoadReferDetail();
        task.execute();

        sharewp.setOnClickListener(this);
        sharefb.setOnClickListener(this);
        sharemail.setOnClickListener(this);
        sharmore.setOnClickListener(this);


        Tools.systemBarLolipop(this);


        tvReferlink.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() <= tvReferlink.getRight()) {
                        Utils.showToastLong("Refer Link Copied",  ReferActivity.this);
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("", StrShareMsg);
                        clipboard.setPrimaryClip(clip);
                        return true;
                    }
                }
                return true;
            }
        });

    }

    private void initToolbar() {


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        TextView txtCartCountersss = (TextView) toolbar.findViewById(R.id.txtCartCounterTopToolBar);
        txtCartCountersss.setText(StrCartCounter);

        textView.setText("Refer Your Friend");
        RelativeLayout relMyCart = (RelativeLayout) toolbar.findViewById(R.id.relMyCart);

        relMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReferActivity.this, MyCart.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });


        setSupportActionBar(toolbar);

    }

    private void FetchXmlID() {
        imgReferfriend = (ImageView) findViewById(R.id.imgReferfriend);
        sharewp = (LinearLayout) findViewById(R.id.sharewp);
        sharefb = (LinearLayout) findViewById(R.id.sharefb);
        sharemail = (LinearLayout) findViewById(R.id.sharemail);
        sharmore = (LinearLayout) findViewById(R.id.sharmore);

        tvReferAmount = (TextView) findViewById(R.id.tvReferAmount);
        tvReferlink = (TextView) findViewById(R.id.tvReferlink);
        tvReferlines = (TextView) findViewById(R.id.tvReferlines);
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

            case R.id.sharewp:

                WhichShareType = "WhatsApp";
                shareMore();

                break;
            case R.id.sharefb:
                WhichShareType = "All";
                shareMore();
                break;
            case R.id.sharemail:

                WhichShareType = "gmail";
                shareMore();
                break;
            case R.id.sharmore:

                WhichShareType = "All";
                shareMore();

                break;
        }
    }

    private class LoadReferDetail extends AsyncTask<Void, Void,
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
                return methods.referFriend("refer_friend_new", StrUserId);
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {
                Utils.showToastShort("Something try agin later", ReferActivity.this);
                finish();
                startActivity(new Intent(ReferActivity.this, MainActivity.class));
            } else {

                Log.i("Curator", curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {
                    try {
                        for (Api_Model.share_data dataset : curators.share_data) {
                            imageLoader.displayImage(dataset.image.toString(), imgReferfriend, options);
                            StrShareMsg = dataset.message;
                            StrShreImageUrl = dataset.share_image;
                            ref_key = dataset.ref_key;
                            you_get = dataset.you_get;
                            you_friend_get = dataset.you_friend_get;
                        }

                        tvReferAmount.setText(you_get + " And " + you_friend_get);
                        tvReferlink.setText(ref_key);

                    } catch (Exception e) {
                        Log.e("Exception e", e.toString());
                    }

                } else {
                    onBackPressed();
                    Utils.showToastShort(curators.message.toString(), ReferActivity.this);
                }
            }
        }
    }

    public void shareMore() {

        String ImgListPath = StrShreImageUrl.toString();
        DownloadSelctedIMG d = new DownloadSelctedIMG();
        d.execute(ImgListPath);

    }

    class DownloadSelctedIMG extends AsyncTask<String, String, Void> {

        String ImgPath = "";
        TextView txtProgressText;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ReferActivity.this);
            progressDialog.setMessage("loading");
            progressDialog.setCancelable(false);
            progressDialog.show();


        }

        protected Void doInBackground(String... arg0) {
            File f = new File(arg0[0]);
            String filename = "VedicFashions.jpg";
            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory().toString() + "/VedicFashions/Event/"
            );
//							  				 + photoList.get(i).get(TAG_PhotoName)
//							  				);

            wallpaperDirectory.mkdirs();
            ImgPath = wallpaperDirectory.getPath().toString() + filename;
            String myu_recivedimage = arg0[0];
            int count;
            try {
                URL myurl = new URL(myu_recivedimage);
                URLConnection conection = myurl.openConnection();

                int lenghtOfFile = conection.getContentLength();
                conection.connect();
                int filelength = conection.getContentLength();
                InputStream inpit = new BufferedInputStream(myurl.openStream());
                OutputStream output = new FileOutputStream(ImgPath);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = inpit.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                inpit.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            try {

                if (WhichShareType.equalsIgnoreCase("WhatsApp")) {
                  /*  String shareBody = "Check out VedicFashions app for Online Shopping. \n" +
                            "Download it today from https://goo.gl/RRjP3y. ";*/

                    //Uri imageUri = Uri.parse(imgPath);
                    File file = new File(ImgPath);
                    Uri imageUri = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setPackage("com.whatsapp");
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "VedicFashions");
                    intent.putExtra(Intent.EXTRA_TEXT, StrShareMsg);

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, "Share via"));

                } else if (WhichShareType.equalsIgnoreCase("gmail")) {

                   /* String shareBody = "Check out VedicFashions app for Online Shopping. \n" +
                            "Download it today from https://goo.gl/RRjP3y. ";*/

                    //Uri imageUri = Uri.parse(imgPath);
                    File file = new File(ImgPath);
                    Uri imageUri = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setData(Uri.parse("mailto:"));
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "VedicFashions");
                    intent.putExtra(Intent.EXTRA_TEXT, StrShareMsg);

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, "Share via"));
                } else {

                    /*String shareBody = "Check out VedicFashions app for Online Shopping. \n" +
                            "Download it today from https://goo.gl/RRjP3y. ";*/

                    //Uri imageUri = Uri.parse(imgPath);
                    File file = new File(ImgPath);
                    Uri imageUri = Uri.fromFile(file);
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                    intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "VedicFashions");
                    intent.putExtra(Intent.EXTRA_TEXT, StrShareMsg);

                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent, "Share via"));
                }


            } catch (Exception e) {

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
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left,
                R.anim.slide_out_right);

    }
}
