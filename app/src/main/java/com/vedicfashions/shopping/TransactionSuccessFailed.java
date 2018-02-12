package com.vedicfashions.shopping;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * Created by welcome on 21-11-2016.
 */
public class TransactionSuccessFailed extends AppCompatActivity implements View.OnClickListener {
    TextView txtBack, txtohh, txtMsg;
    ImageView imgTopImage;
    String StrMsg = "", StrMsg1 = "";
    int intCode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transaction_succsess_failed_screen);
        FetchXMLId();


        Intent in = getIntent();
        if (in != null) {
            StrMsg = in.getStringExtra("message");
            StrMsg1 = in.getStringExtra("message1");
            intCode = in.getIntExtra("msgCode", 0);
        }

        if (intCode == 0) {
            imgTopImage.setImageResource(R.drawable.img_done);
        } else {
            imgTopImage.setImageResource(R.drawable.img_fail);
        }

        txtohh.setText(StrMsg1);
        txtMsg.setText(StrMsg);
        txtBack.setOnClickListener(this);
    }

    private void FetchXMLId() {
        txtBack = (TextView) findViewById(R.id.txtBack);
        txtohh = (TextView) findViewById(R.id.txtohh);
        txtMsg = (TextView) findViewById(R.id.txtMsg);
        imgTopImage = (ImageView) findViewById(R.id.imgTopImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtBack:
                onBackPressed();
                break;
        }

    }

    @Override
    public void onBackPressed() {

        TransactionSuccessFailed.this.finish();
        Intent i = new Intent(getApplicationContext(), MyOrder.class);
        i.putExtra("PageType", "MainActivity");
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}