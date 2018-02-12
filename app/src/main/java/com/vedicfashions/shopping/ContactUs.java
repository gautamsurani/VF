package com.vedicfashions.shopping;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Tools;
import utils.Utils;
/**
 * Created by welcome on 15-12-2016.
 */
public class ContactUs extends AppCompatActivity {
    TextView txtContactMail, txtWebMail, txtCall, txtAddress, txtBelowName;
    ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term);
        FetchXMLId();
        initToolbar();
        initToolbar();
        Tools.systemBarLolipop(this);

        if (Utils.isNetworkAvailable(getBaseContext()))
        {
            loading = ProgressDialog.show(ContactUs.this, "", "Please wait...", false, false);
            GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
            mGetNoticeListArrayForUpdates.execute();
        }
        else
            {
            Utils.showToastShort("Please Check Your Internet Connection", ContactUs.this);
        }

    }

    private void FetchXMLId() {

        txtContactMail = (TextView) findViewById(R.id.txtContactMail);
        txtWebMail = (TextView) findViewById(R.id.txtWebMail);
        txtCall = (TextView) findViewById(R.id.txtCall);
        txtAddress = (TextView) findViewById(R.id.txtAddress);
        txtBelowName = (TextView) findViewById(R.id.txtBelowName);

    }
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("Contact Us");
        setSupportActionBar(toolbar);


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
                Api_Model curators = methods.GEtContactUsAPI("contact");

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
                    for (Api_Model.contact contacaxat : curators.contact) {
                        txtContactMail.setText(contacaxat.email);
                        txtWebMail.setText(contacaxat.website);
                        txtCall.setText(contacaxat.phone);
                        txtAddress.setText(contacaxat.address_1);
                        txtBelowName.setText(contacaxat.message);
                    }
                } else {

                    Utils.showToastShort(curators.message.toString(), ContactUs.this);
                }
            }
        }
    }

    private void ShowRetryDialog(final String WhichAPILoad) {
        try {
            final Dialog dialog = new Dialog(ContactUs.this);
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
                    loading = ProgressDialog.show(ContactUs.this, "", "Please wait...", false, false);

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
