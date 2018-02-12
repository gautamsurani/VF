package com.vedicfashions.shopping;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


public class MyfireDemoMessageService extends FirebaseMessagingService {
    android.support.v4.app.NotificationCompat.Builder builder;
    NotificationManager notificationManager;
    public static int count = 0;


    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    String StrUserId;


    @SuppressLint("CommitPrefEdits")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e("QQQQQQQQ", "Done");


        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);
        Map<String, String> data = remoteMessage.getData();
        ShowAllDashboardModuleNotification(data);


    }


    private void ShowAllDashboardModuleNotification(Map<String, String> data) {
        final Intent gotoIntent = new Intent();

        gotoIntent.putExtra("title", data.get("offer_title"));
        gotoIntent.putExtra("content", data.get("offer_desc"));
        gotoIntent.putExtra("date", data.get("offer_added_on"));
        gotoIntent.putExtra("IMgMain", data.get("imgUrl"));
        gotoIntent.putExtra("sharemsg", data.get("shre_msg"));
        gotoIntent.putExtra("PButton", data.get("offer_product_button"));
        gotoIntent.putExtra("CBuuton", data.get("offer_cat_button"));
        gotoIntent.putExtra("SubCat", data.get("offer_subcat"));
        gotoIntent.putExtra("ButtonID", data.get("offer_buttonID"));
        gotoIntent.putExtra("Name", data.get("name"));
        gotoIntent.putExtra("Type", data.get("type"));
        Log.e("Type", "fcmType" + data.get("type"));
        gotoIntent.putExtra("PageThis", "Push");
        if (StrUserId == null) {
            gotoIntent.setClassName(getApplicationContext(), "com.vedicfashions.shopping.Login");//Start activity when user taps on notification.
        } else {
            if (data.get("type").equalsIgnoreCase("sale")) {
                gotoIntent.setClassName(getApplicationContext(), "com.vedicfashions.shopping.SaleViewActivity");//Start activity when user taps on notification.

            } else {
                gotoIntent.setClassName(getApplicationContext(), "com.vedicfashions.shopping.UpdateViewActivity");//Start activity when user taps on notification.

            }
        }


        if (data.get("display_img").equalsIgnoreCase("Yes")) {
            ShowCommonNotificationWithImage(data.get("imgUrl"), gotoIntent, data.get("offer_title"), data.get("offer_desc"));

        } else {
            ShowCommonNotification(gotoIntent, data.get("offer_title"), data.get("offer_desc"));

        }


    }

    private void ShowCommonNotificationWithImage(String img, Intent gotoIntent, String StrTitle, String StrDescip) {

        Log.e("Ohk 1", "ShowCommonNotificationWithImage");
        Bitmap remote_picture;
        try {
            NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
            notiStyle.setSummaryText(StrDescip);
            remote_picture = getBitmapFromURL(img);


            int imageWidth = remote_picture.getWidth();
            int imageHeight = remote_picture.getHeight();

            DisplayMetrics metrics = this.getResources().getDisplayMetrics();

            int newWidth = metrics.widthPixels;
            float scaleFactor = (float) newWidth / (float) imageWidth;
            int newHeight = (int) (imageHeight * scaleFactor);

            remote_picture = Bitmap.createScaledBitmap(remote_picture, newWidth, newHeight, true);
            notiStyle.bigPicture(remote_picture);
            notificationManager = (NotificationManager) getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent;

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            contentIntent = PendingIntent.getActivity(getApplicationContext(),
                    (int) (Math.random() * 100), gotoIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
            android.app.Notification notification = mBuilder.setSmallIcon(R.drawable.notification_icon)./*setTicker(strMsgTitle).*/setWhen(0)
                    .setColor(getResources().getColor(R.color.colorPrimary)).setAutoCancel(true).setContentTitle(StrTitle)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(StrDescip))
                    .setContentIntent(contentIntent)
                    .setVibrate(new long[]{100, 250})
                    // .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setSound(defaultSoundUri)
                    .setContentText(StrDescip).setStyle(notiStyle).build();
            count++;
            notificationManager.notify(count, notification);//This will generate seperate notification each time server sends.

        } catch (Exception e) {
            Log.e("This", e.getMessage());

        }
    }

    private void ShowCommonNotification(Intent gotoIntent, String StrTitle, String StrDescip) {
        Log.e("Ohk 1", "ShowCommonNotification");
        try {
            NotificationCompat.BigPictureStyle notiStyle = new NotificationCompat.BigPictureStyle();
            notiStyle.setSummaryText(StrDescip);
            notificationManager = (NotificationManager) getApplicationContext()
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent contentIntent;

            contentIntent = PendingIntent.getActivity(getApplicationContext(),
                    (int) (Math.random() * 100), gotoIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimary));
            notificationBuilder.setSmallIcon(R.drawable.notification_icon);

            notificationBuilder.setContentTitle(StrTitle);
            notificationBuilder.setContentText(StrDescip);

            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setContentIntent(contentIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            count++;
            notificationManager.notify(count, notificationBuilder.build());

        } catch (Exception e) {
            Log.e("This", e.getMessage());
        }
    }


    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            Log.e("This Second", e.getMessage());
            return null;
        }
    }


}



