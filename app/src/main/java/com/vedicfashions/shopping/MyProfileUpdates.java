package com.vedicfashions.shopping;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import customclass.MyRoundImageview;
import utils.Tools;
import utils.Utils;

/*
 * Created by welcome on 24-10-2016.
 */
public class MyProfileUpdates extends AppCompatActivity {
    String Str_Imag_GaleeryUrl;
    private static final int CAMERA_CAPTURE = 1;
    private static final int GALLERY_CAPTURE = 2;
    ProgressDialog progressDialog;
    Context context;

    ProgressDialog loading;
    String outPath_Camera;
    Boolean IsFileMAke = false;
    private MyRoundImageview imgUpdateimage;
    ImageView imgChangeImage;
    EditText etUname, etUemail, etUphone;
    Button btnUpdateData;


    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    VedicFashions application;
    String StrUserId, StrUserName, StrUserEmail, StrUserPhone, StrUserImage;


    boolean IsAllowAgain = true;
    final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 007;
    String uploadFilePath = "";
    String file_data = "no";
    String upLoadServerUri = "http://www.vedicfashion.com/mapp/index.php?view=profile";

    @SuppressLint("CommitPrefEdits")
    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_updateprofile);

        context = this;

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        inotComp();
        initToolbar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            insertDummyContactWrapper();
        }

        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);
        StrUserName = prefLogin.getString("name", null);
        StrUserEmail = prefLogin.getString("email", null);
        StrUserPhone = prefLogin.getString("phone", null);
        StrUserImage = prefLogin.getString("userimage", null);

        application = (VedicFashions) getApplicationContext();
        DisplayImageOptions options = getImageOptions();
        ImageLoader imageLoader = application.getImageLoader();

        etUname.setText(StrUserName);
        etUemail.setText(StrUserEmail);
        etUphone.setText(StrUserPhone);

        imageLoader.displayImage(StrUserImage, imgUpdateimage, options);
        Tools.systemBarLolipop(this);

        File createFolder = new File("sdcard/VedicFashions/Temp");
        if (!createFolder.exists()) {
            createFolder.mkdirs();
        }

        File createFolder2 = new File("sdcard/VedicFashions/Temp/Camera");
        if (!createFolder2.exists()) {
            createFolder2.mkdirs();
        }
    }

    @SuppressLint("SetTextI18n")
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        TextView textView = toolbar.findViewById(R.id.eshop);
        textView.setText("My Profile");
        setSupportActionBar(toolbar);
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void insertDummyContactWrapper() {

        List<String> permissionsNeeded = new ArrayList<>();
        final List<String> permissionsList = new ArrayList<>();
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Read Storage");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Write Storage");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");
        if (permissionsList.size() > 0) {
            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                }
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        || perms.get(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(MyProfileUpdates.this, Manifest.permission.CAMERA)) {
                        IsAllowAgain = false;
                        CRateWhyDialog();

                    }
                    if (IsAllowAgain) {
                        insertDummyContactWrapper();
                    }
                }
            }
            break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void CRateWhyDialog() {
        try {
            final Dialog dialog = new Dialog(MyProfileUpdates.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_new_login_no_permission);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setCanceledOnTouchOutside(true);

            TextView txtDialogBottomText = dialog.findViewById(R.id.txtDialogBottomText);

            txtDialogBottomText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + MyProfileUpdates.this.getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    MyProfileUpdates.this.startActivity(intent);
                    dialog.dismiss();

                    MyProfileUpdates.this.finish();

                }
            });
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inotComp() {
        imgUpdateimage = findViewById(R.id.imgUpdateimage);
        imgChangeImage = findViewById(R.id.imgChangeImage);
        etUname = findViewById(R.id.etUname);
        etUemail = findViewById(R.id.etUemail);
        etUphone = findViewById(R.id.etUphone);
        btnUpdateData = findViewById(R.id.btnUpdateData);

        btnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isNetworkAvailable(MyProfileUpdates.this)) {

                    StrUserName = etUname.getText().toString();
                    StrUserEmail = etUemail.getText().toString();
                    StrUserPhone = etUphone.getText().toString();
                    if (StrUserPhone.equalsIgnoreCase("")) {
                        Utils.showToastShort("Enter Mobile !", MyProfileUpdates.this);
                    } else {
                        loading = ProgressDialog.show(MyProfileUpdates.this, "", "Please wait...", false, false);
                        new Thread(new Runnable() {
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {

                                    }
                                });
                                Utils.hideKeyboard(MyProfileUpdates.this);
                                UploadFileToServer task = new UploadFileToServer();
                                task.execute();
                            }
                        }).start();
                    }
                }
            }
        });
        imgChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogForImage();
            }
        });
    }

    private void ShowDialogForImage() {

        android.support.v7.app.AlertDialog.Builder Dilaog_3DMethod = new android.support.v7.app.AlertDialog.Builder(MyProfileUpdates.this);
        Dilaog_3DMethod.setCancelable(true);
        final CharSequence[] items = {getResources().getString(R.string.str_common_TakeCamera), getResources().getString(R.string.str_common_TakeGallery)};

        Dilaog_3DMethod.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    Random as = new Random();
                    int a = as.nextInt();

                    outPath_Camera = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VedicFashions/Temp/Camera/" + a + ".jpg";
                    File outFile = new File(outPath_Camera);
                    Uri outuri = Uri.fromFile(outFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, outuri);
                    startActivityForResult(intent, CAMERA_CAPTURE);

                } else {
                    Intent captureIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(captureIntent, GALLERY_CAPTURE);
                }

            }
        });


        final android.support.v7.app.AlertDialog asd_Dialog = Dilaog_3DMethod.create();
        asd_Dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA_CAPTURE:
                if (resultCode == RESULT_OK) {
                    try {
                        Str_Imag_GaleeryUrl = outPath_Camera;
                        File f = new File(Str_Imag_GaleeryUrl);
                        String path = f.getAbsolutePath();
                        try {
                            IsFileMAke = true;
                            Bitmap bmp = BitmapFactory.decodeFile(path);
                            imgUpdateimage.setImageBitmap(bmp);
                            uploadFilePath = path;
                            file_data = "yes";
                        } catch (Exception e) {
                            IsFileMAke = false;
                            Utils.showToastShort("Allow Beutopian to access Storage", MyProfileUpdates.this);
                            file_data = "no";
                        }

                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
                break;
            case GALLERY_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d("File Uri:", "File Uri: " + uri.toString());
                    String path;
                    try {
                        IsFileMAke = true;
                        path = getPath(MyProfileUpdates.this, uri);
                        Str_Imag_GaleeryUrl = path;
                        Bitmap bmp = BitmapFactory.decodeFile(path);
                        imgUpdateimage.setImageBitmap(bmp);
                        uploadFilePath = path;
                        file_data = "yes";
                    } catch (Exception e) {
                        Utils.showToastShort("Allow Beutopian to access Storage", MyProfileUpdates.this);
                        file_data = "no";
                        IsFileMAke = false;
                        e.printStackTrace();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public DisplayImageOptions getImageOptions() {

        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true).showImageOnLoading(R.drawable.default_gray_image)
                .showImageForEmptyUri(R.drawable.default_gray_image).showImageOnFail(R.drawable.default_gray_image)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }


    @SuppressLint("StaticFieldLeak")
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero

            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible

        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {


            if (IsFileMAke) {
                Log.e("Str_Imag_GaleeryUrl", Str_Imag_GaleeryUrl);


                File imgfilek = new File(Str_Imag_GaleeryUrl);
                File from = new File(imgfilek.getParent(), imgfilek.getName());
                File to = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VedicFashions/Temp/", imgfilek.getName());

                try {
                    InputStream in = new FileInputStream(from);
                    OutputStream out = new FileOutputStream(to);
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                Random as = new Random();
                int a = as.nextInt();

                File beforname = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VedicFashions/Temp/", imgfilek.getName());
                File aftername = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VedicFashions/Temp/", a + ".jpg");
                beforname.renameTo(aftername);


                int MAX_IMAGE_SIZE = 200 * 1024; // max final file size
                Bitmap bmpPic = BitmapFactory.decodeFile(aftername.getPath());

                if ((bmpPic.getWidth() >= 1024) && (bmpPic.getHeight() >= 1024)) {
                    BitmapFactory.Options bmpOptions = new BitmapFactory.Options();
                    bmpOptions.inSampleSize = 1;
                    while ((bmpPic.getWidth() >= 1024) && (bmpPic.getHeight() >= 1024)) {
                        bmpOptions.inSampleSize++;
                        bmpPic = BitmapFactory.decodeFile(aftername.getPath(), bmpOptions);
                    }

                }
                int compressQuality = 104; // quality decreasing by 5 every loop. (start from 99)
                int streamLength = MAX_IMAGE_SIZE;
                while (streamLength >= MAX_IMAGE_SIZE) {
                    ByteArrayOutputStream bmpStream = new ByteArrayOutputStream();
                    compressQuality -= 5;
                    //   sucsesstoast("Quality: " + compressQuality);
                    bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream);
                    byte[] bmpPicByteArray = bmpStream.toByteArray();
                    streamLength = bmpPicByteArray.length;

                }
                try {
                    FileOutputStream bmpFile = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VedicFashions/Temp/" + a + ".jpg");
                    bmpPic.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpFile);
                    bmpFile.flush();
                    bmpFile.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                uploadFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VedicFashions/Temp/" + a + ".jpg";

            }


            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(upLoadServerUri);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        new AndroidMultiPartEntity.ProgressListener() {

                            @Override
                            public void transferred(long num) {

                            }
                        });
                if (file_data.equals("yes")) {
                    File sourceFile = new File(uploadFilePath);

                    // Adding file data to http body
                    entity.addPart("FILE", new FileBody(sourceFile));
                }
                // Extra parameters if you want to pass to server
             /*   entity.addPart("view",
                        new StringBody("profile"));*/
                entity.addPart("custID",
                        new StringBody(StrUserId));
                entity.addPart("NAME",
                        new StringBody(StrUserName));
                entity.addPart("EMAIL",
                        new StringBody(StrUserEmail));
                entity.addPart("PHONE",
                        new StringBody(StrUserPhone));

                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }
                if (responseString != null) {
                    // Getting JSON Array node
                    Log.e("response", responseString);
                }
            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String temp) {
            //  Log.e("temp", temp);
            IsFileMAke = false;
            loading.dismiss();
            file_data = "no";


            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(temp);

                String resMessage = jsonObject.getString("message");
                String resCode = jsonObject.getString("msgcode");
                if (resCode.equalsIgnoreCase("0")) {

                    JSONObject NewJsonObj = jsonObject.getJSONObject("customer_detail");

                    Log.d("D", "Done");
                    editorLogin.putString("name", NewJsonObj.getString("name"));
                    editorLogin.putString("email", NewJsonObj.getString("email"));
                    editorLogin.putString("phone", NewJsonObj.getString("phone"));
                    editorLogin.putString("userimage", NewJsonObj.getString("image"));
                    Log.e("Image XXX", NewJsonObj.getString("image"));
                    editorLogin.commit();

                    startActivity(new Intent(MyProfileUpdates.this, MyAccountActivity.class));
                    overridePendingTransition(
                            R.anim.slide_in_left,
                            R.anim.slide_out_right);


                } else {
                    Utils.showToastShort(resMessage, MyProfileUpdates.this);

                }

            } catch (JSONException e) {
                Log.e("AA", e.getMessage());
            }


            finish();
            startActivity(new Intent(MyProfileUpdates.this, MyAccountActivity.class));
            overridePendingTransition(
                    R.anim.slide_in_left,
                    R.anim.slide_out_right);
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

