package utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.vedicfashions.shopping.Login;
import com.vedicfashions.shopping.R;

import java.math.BigDecimal;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Locale;


public class Utils {
    private Context _context;

    // constructor
    public Utils(Context context) {
        this._context = context;

    }

    public static String getUserId(Context context) {
        SharedPreferences prefLogin;
        SharedPreferences.Editor editorLogin;
        prefLogin = context.getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        return prefLogin.getString("userID", null);
    }

    public static void loginDialog(final Context context, String Message) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context, R.style.CustomAlertDialog);
            AlertDialog alertDialog = alertDialogBuilder.create();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") final View alertlayout = inflater.inflate(R.layout.pop_login, null);
            Button btnNO = (Button) alertlayout.findViewById(R.id.btn_popup_NO);
            Button btnYES = (Button) alertlayout.findViewById(R.id.btn_popup_YES);
            TextView tvMessage = (TextView) alertlayout.findViewById(R.id.tvMessage);

            tvMessage.setText(Message);
            alertDialogBuilder.setView(alertlayout);

            alertDialog = alertDialogBuilder.create();

            final AlertDialog finalAlertDialog = alertDialog;
            btnNO.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    finalAlertDialog.dismiss();


                }
            });

            btnYES.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    finalAlertDialog.dismiss();
                    context.startActivity(new Intent(context, Login.class));
                    ((Activity) context).finish();
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                }
            });
            alertDialog.show();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alertDialog.getWindow().getAttributes());
            lp.width = convertDpToPixel(280, context);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            alertDialog.getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void loginDialogForCart(final Context context, String Message) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context, R.style.CustomAlertDialog);
            AlertDialog alertDialog = alertDialogBuilder.create();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") final View alertlayout = inflater.inflate(R.layout.pop_login, null);
            Button btnNO = (Button) alertlayout.findViewById(R.id.btn_popup_NO);
            Button btnYES = (Button) alertlayout.findViewById(R.id.btn_popup_YES);
            TextView tvMessage = (TextView) alertlayout.findViewById(R.id.tvMessage);

            tvMessage.setText(Message);
            alertDialogBuilder.setView(alertlayout);

            alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            final AlertDialog finalAlertDialog = alertDialog;
            btnNO.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    finalAlertDialog.dismiss();
                    ((Activity) context).finish();
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                }
            });

            btnYES.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    finalAlertDialog.dismiss();
                    context.startActivity(new Intent(context, Login.class));
                    ((Activity) context).finish();
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                }
            });
            alertDialog.show();

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alertDialog.getWindow().getAttributes());
            lp.width = convertDpToPixel(280, context);
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.CENTER;

            alertDialog.getWindow().setAttributes(lp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        String sPX;

        if (String.valueOf(px).contains(".")) {
            sPX = String.valueOf(px).substring(0, String.valueOf(px).indexOf("."));
        } else {
            sPX = String.valueOf(px);
        }

        return Integer.parseInt(sPX);
    }

    public static boolean isValidEmail(String strEmail) {
        if (strEmail == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches();
        }
    }

    public static boolean isNetworkAvailable(Context activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/vedicfashion"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/vedicfashion/"));
        }
    }

    public static String rupeeFormet() {
        Format format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        return format.format(new BigDecimal("100000000"));
    }

    public static void showToastShort(String message, Activity noticeBoard) {
        // TODO Auto-generated method stub
        LayoutInflater mylf = noticeBoard.getLayoutInflater();
        View myview = mylf.inflate(R.layout.toast_layout, null);
        TextView text_data = (TextView) myview.findViewById(R.id.toast_text);

        Typeface fonts1 = Typeface.createFromAsset(noticeBoard.getAssets(), "fonts/MavenPro-Regular.ttf");

        text_data.setTypeface(fonts1);
        text_data.setText(message);
        Toast mytoast = new Toast(noticeBoard);
        mytoast.setDuration(Toast.LENGTH_SHORT);
        mytoast.setView(myview);
        mytoast.show();
    }

    public static void showToastLong(String message, Activity noticeBoard) {
        // TODO Auto-generated method stub
        LayoutInflater mylf = noticeBoard.getLayoutInflater();
        View myview = mylf.inflate(R.layout.toast_layout, null);
        TextView text_data = (TextView) myview.findViewById(R.id.toast_text);

        Typeface fonts1 = Typeface.createFromAsset(noticeBoard.getAssets(), "fonts/MavenPro-Regular.ttf");

        text_data.setTypeface(fonts1);

        text_data.setText(message);
        Toast mytoast = new Toast(noticeBoard);
        mytoast.setDuration(Toast.LENGTH_LONG);
        mytoast.setView(myview);
        mytoast.show();
    }

    public static void hideKeyboard(Activity noticeBoard) {
        View view = noticeBoard.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) noticeBoard.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void ShowSnakBar(String s, View linearLayout, Activity login) {

        Snackbar snackbar = Snackbar.make(linearLayout, s, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(login, R.color.colorPrimary));
        snackbar.show();

    }

    public static void displayDialog(String title, String msg,
                                     final Context context, final boolean isFinish) {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(false);
        alertDialog.setMessage(msg);
        alertDialog.setNeutralButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (isFinish)
                            ((Activity) context).finish();
                        ((Activity) context).overridePendingTransition(
                                R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                });
        final AlertDialog dialog = alertDialog.create();
        if (!((Activity) context).isFinishing()) {
            if (!dialog.isShowing()) {
                alertDialog.show();
            }
        }
    }
}
