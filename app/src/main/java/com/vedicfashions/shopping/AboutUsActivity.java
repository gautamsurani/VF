package com.vedicfashions.shopping;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener {
    String web[] = {"Visit site", "Terms and Condition", "Privact Policy", "Disclaimer"};
    ListView list;
    ImageView aboutus_icon;
    HtmlTextView aboutus_content;
    ConnectivityManager conMgr;
    NetworkInfo mWifi;
    ImageView facebookshare, twittershare, googleplusshare, instagramshare;
    RelativeLayout retry;
    String facebook_link = "";
    String google_link = "";
    String linkdin_link = "";
    String twitter_link = "";
    String insta_link = "";
    VedicFashions application;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus_view);
        application = (VedicFashions) getApplicationContext();
        options = getImageOptions();
        imageLoader = application.getImageLoader();
        list = (ListView) findViewById(R.id.aboutus_list);
        aboutus_content = (HtmlTextView) findViewById(R.id.aboutus_content);
        facebookshare = (ImageView) findViewById(R.id.about_us_facebookshare);
        twittershare = (ImageView) findViewById(R.id.about_us_twittershare);
        googleplusshare = (ImageView) findViewById(R.id.about_us_googleplusshare);
        instagramshare = (ImageView) findViewById(R.id.about_us_instagramshare);
        aboutus_icon = (ImageView) findViewById(R.id.aboutus_icon);
        retry = (RelativeLayout) findViewById(R.id.retry_layout);
        facebookshare.setOnClickListener(this);
        twittershare.setOnClickListener(this);
        googleplusshare.setOnClickListener(this);
        instagramshare.setOnClickListener(this);
        initToolbar();
        if (CheckConnection().equals("true")) {
            Log.i("Checking Url Connection", "Checking");
            AboutUsDetail task = new AboutUsDetail();
            task.execute();
        } else {
            NetworkToast("Please Check Your Internet Connection");
        }
        try {
            String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            String version = String.format(getString(R.string.version_code),
                    versionName);
            String a = String.format(getString(R.string.version_code), versionName);


            TextView versionView = (TextView) findViewById(R.id.aboutus_version);
            versionView.setText(version);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("About VedicFashions");
        setSupportActionBar(toolbar);
    }

    @Override
    public void onClick(View v) {
        if (v == facebookshare) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook_link));
            startActivity(browserIntent);
        } else if (v == twittershare) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitter_link));
            startActivity(browserIntent);
        } else if (v == googleplusshare) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(google_link));
            startActivity(browserIntent);
        } else if (v == instagramshare) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(insta_link));
            startActivity(browserIntent);
        }
    }

    public DisplayImageOptions getImageOptions() {

        final DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        return imageOptions;
    }

    private class AboutUsDetail extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;
        final ProgressDialog loading = ProgressDialog.show(AboutUsActivity.this, "", "Please wait...", false, false);

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
                Api_Model curators = methods.GetStartScreen("about");

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

                retry.setVisibility(View.VISIBLE);
                retry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (CheckConnection().equals("true")) {
                            Log.i("Checking Url Connection", "Checking");
                            retry.setVisibility(View.GONE);
                            AboutUsDetail task = new AboutUsDetail();
                            task.execute();
                        } else {
                            NetworkToast("Please Check Your Internet Connection");
                        }
                    }
                });
            } else {
                Log.i("Curator", curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {

                    for (Api_Model.about dataset : curators.about) {

                        String number = dataset.text.toString().toString().replaceAll("\t", "");
                        aboutus_content.setHtml(number);
                        imageLoader.displayImage(dataset.image.toString(), aboutus_icon, options);


                        facebook_link = dataset.facebook_link;
                        google_link = dataset.google_link;
                        linkdin_link = dataset.linkdin_link;
                        twitter_link = dataset.twitter_link;
                        insta_link = dataset.insta_link;
                    }
                    CustomAdapter cstm = new CustomAdapter(AboutUsActivity.this, web);
                    list.setAdapter(cstm);

                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                String url = "http://www.vedicfashion.com";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            } else if (position == 1) {
                                String url = "http://www.vedicfashion.com/terms-and-conditions.html";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            } else if (position == 2) {
                                String url = "http://www.vedicfashion.com/privacy-policy.html";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            } else {
                                String url = "http://www.vedicfashion.com/disclaimer.html";
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                startActivity(i);
                            }
                        }
                    });
                    setListViewHeightBasedOnChildren(list);
                } else {
                    NetworkToast(curators.message.toString());
                }
            }
        }
    }

    public class CustomAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private final String[] web;

        public CustomAdapter(Activity context, String[] web) {
            super(context, R.layout.account_list_textview, web);
            this.context = context;
            this.web = web;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.account_list_textview, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.myprofiletext);
            ImageView icoimg = (ImageView) rowView.findViewById(R.id.iconimg);
            icoimg.setVisibility(View.GONE);
            txtTitle.setText(web[position]);
            return rowView;
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) {
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));
            }
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
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

    public void NetworkToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        v.setTextColor(Color.WHITE);
        toast.show();
    }

    public String CheckConnection() {
        String Status = "";
        try {
            conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            mWifi = conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWifi.isConnected() || conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING) {
                Status = "true";
            } else if (mWifi.isFailover() || conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
                Status = "false";
            }

        } catch (Exception E) {
            Status = "false";
            NetworkToast("Please Check Your Internet Connection");
        }
        return Status;
    }
}
