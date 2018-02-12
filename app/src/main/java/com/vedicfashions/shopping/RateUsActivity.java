package com.vedicfashions.shopping;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * Created by welcome on 14-10-2016.
 */

public class RateUsActivity extends AppCompatActivity {
    Button btnRateUS;
    TextView txtSignUp;
    LinearLayout fivestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_us);


        initToolbar();
        FetchXMLID();
        btnRateUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(i);
            }
        });
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RateUsActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        fivestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("market://details?id=" + getPackageName()));
                startActivity(i);
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("FeedBack");
        setSupportActionBar(toolbar);
    }
    private void FetchXMLID() {
        btnRateUS = (Button) findViewById(R.id.btnRateUS);
        txtSignUp = (TextView) findViewById(R.id.txtSignUp);
        fivestart = (LinearLayout) findViewById(R.id.fivestart);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                Intent intent = new Intent(RateUsActivity.this, Login.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

