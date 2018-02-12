package com.vedicfashions.shopping;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import utils.Tools;

/**
 * Created by welcome on 15-12-2016.
 */
public class TermCondition extends AppCompatActivity{
    WebView webForTermCondition;

    String url = "http://www.vedicfashion.com/terms-and-conditions.html";
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_condition);



        FetchXMLId();
        initToolbar();


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCancelable(true);
        progressDialog.show();

        initToolbar();
        Tools.systemBarLolipop(this);

        webForTermCondition.setClickable(true);
        webForTermCondition.setFocusableInTouchMode(true);
        WebSettings settings = webForTermCondition.getSettings();
        settings.setJavaScriptEnabled(true);
        webForTermCondition.setInitialScale(1);
        webForTermCondition.getSettings().setJavaScriptEnabled(true);
        webForTermCondition.getSettings().setUseWideViewPort(true);
        webForTermCondition.getSettings().setBuiltInZoomControls(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setDomStorageEnabled(true);
        webForTermCondition.requestFocus();
        webForTermCondition.requestFocusFromTouch();
        webForTermCondition.setWebViewClient(new WebViewClient()
        {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);

                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                Log.d("MyCurrentUrl", url);
                if (!progressDialog.isShowing()) {
                    progressDialog.show();
                    view.loadUrl(url);
                }

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
        webForTermCondition.loadUrl(url);
    }

    private void FetchXMLId() {

         webForTermCondition=(WebView)findViewById(R.id.webForTermCondition);
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("Terms & Conditions");
        setSupportActionBar(toolbar);


    }
    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch(keyCode){
                case KeyEvent.KEYCODE_BACK:
                    if(webForTermCondition.canGoBack()){
                        webForTermCondition.goBack();
                    }
                    else {
                        onBackPressed();
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

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
