package com.vedicfashions.shopping;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/*
 * Created by welcome on 14-10-2016.
 */
public class IncomigSmsForForgotPassword extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    String message = currentMessage.getDisplayMessageBody();
                    Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
                    String substr = message.substring(31, 35).trim();
                    Log.d("currentMessage", senderNum);
                    Log.d("message", message);
                    Log.i("substr", substr);
                    try {
                        if (senderNum.contains("VEDICF")) {
                            OtpForForgotPassword SmsA = new OtpForForgotPassword();
                            SmsA.recivedSms(substr);
                            Log.d("otpmessage", substr);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}