package com.vedicfashions.shopping;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SingleOfferImage extends AppCompatActivity {

    VedicFashions application;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();

     Context context;
    String strImage = "", strImageTitle = "";
    ProgressDialog progressDialog;
    ImageView imgSingleImage;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleimage);
        context = this;
        Bundle extras = getIntent().getExtras();

        application = (VedicFashions) getApplicationContext();
        options = getImageOptions();
        imageLoader = application.getImageLoader();
        if(extras!=null)
        {
            strImage = getIntent().getExtras().getString("singleImage");
            strImageTitle = getIntent().getExtras().getString("singleImageTitle");
            Log.d("strImageTitleSingle",strImageTitle);
            Log.d("strImageTitle",strImage);

        }

        initToolbar();
        initComp();


    }
    private void initToolbar()  {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("Notification");
        setSupportActionBar(toolbar);


    }

    private void initComp()
    {
          progressBar = (ProgressBar) findViewById(R.id.pgSingle);
          imgSingleImage= (ImageView) findViewById(R.id.imgSingleImage);



        imageLoader.displayImage(strImage, imgSingleImage, options);


    }


        @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
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
