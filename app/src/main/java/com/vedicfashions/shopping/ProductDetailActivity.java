package com.vedicfashions.shopping;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import junit.framework.Assert;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;

import adapters.ProductDetailCOLORAdapter;
import adapters.ProductDetailSizeAdapter;
import adapters.RelatedProductAdapter;
import dbhelper.LocalVedicDB;
import models.ProductlistModel;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.SmallBang;
import utils.Tools;
import utils.Utils;

public class ProductDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<String> type, colors;
    private ArrayList<String> Subcolors;
    public ProductDetailSizeAdapter mProductDetailSizeAdapter;
    public static int INTChangeForColor = 0;
    public static int INTChangeForSIze = 100;
    public ProductDetailCOLORAdapter mProductDetailCOLORAdapter;

    String subCatId = "";
    TextView productDetailstitle, changePinCode;
    HtmlTextView tvProductdesc;
    LocalVedicDB mLocalVedicDB;
    Button btnBuyNow, btnAddtoCArt;
    Boolean isSoldOut = false;
    ProgressDialog loading;
    VedicFashions application;
    TextView txtPriceTop, txtMrpTop, txtChangePincode, txtQtyMsg;
    String StrMrp = "";
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;

    ArrayList<String> ArrImgListSmall, ArrImgListLarge;
    ArrayList<ProductlistModel> relatedProductList = new ArrayList<>();
    ArrayList<String> priceID, price, mrp, discount;
    RecyclerView recycleSize, recycleCOlor;
    ScrollView detailscrollview;
    String StrUserId;
    ViewPager pager;
    LinearLayout thumbnails, qty_linear;
    private GalleryPagerAdapter adapter;
    RecyclerView rvRelatedProduct;
    private RelatedProductAdapter mRelatedProductAdapter;
    String ThisPriceId = "", ThisPrice = "", ThisCOlor = "";
    TextView tvmytotalitems, tvDiscount;
    String StrCartCounter;
    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
    LinearLayout size, colour;
    String StrPInCode = "";
    LinearLayout linerDelveryCharge1, linerDelveryCharge2, linerDelveryCharge3;
    TextView ShipingMsg1, ShipingMsg2, ShipingMsg3;
    ImageView imgDelevery1, imgDelevery2, imgDelevery3;
    ImageView imgAddToWishList, imgShare;
    RelativeLayout relativeMain;
    ArrayList<String> WishLocalArrFOrStartView;

    SmallBang mSmallBang;

    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;

    String filename = "", download_url = "";
    boolean IsFromDownload = true;

    final String LOCAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/VedicFashions/ProductImages";

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetail);

        ProductDetailActivity.INTChangeForColor = 0;
        ProductDetailActivity.INTChangeForSIze = 100;

        prefLogin = ProductDetailActivity.this.getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);

        prefCartCounter = ProductDetailActivity.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");

        Intent in = getIntent();
        if (in != null) {
            subCatId = in.getExtras().getString("subCatId");
        }

        File createfolader2 = new File(LOCAL_PATH);
        if (!createfolader2.exists()) {
            createfolader2.mkdirs();
        }
        mSmallBang = SmallBang.attach2Window(ProductDetailActivity.this);
        ProductDetailActivity.INTChangeForColor = 0;
        ProductDetailActivity.INTChangeForSIze = 100;

        String LocalDbUserSocityIdName = StrUserId + "Vedic.db";
        String FinalLocalDBName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + LocalDbUserSocityIdName;
        mLocalVedicDB = new LocalVedicDB(ProductDetailActivity.this, FinalLocalDBName);
        mLocalVedicDB.OpenDB();

        priceID = new ArrayList<>();
        type = new ArrayList<>();
        price = new ArrayList<>();
        mrp = new ArrayList<>();
        discount = new ArrayList<>();
        colors = new ArrayList<>();
        Subcolors = new ArrayList<>();

        application = (VedicFashions) getApplicationContext();

        FetchXmlID();
        initToolbar();
        Tools.systemBarLolipop(this);

        rvRelatedProduct = (RecyclerView) findViewById(R.id.rvBestProduct);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rvRelatedProduct.setLayoutManager(mLayoutManager1);

        WishLocalArrFOrStartView = new ArrayList<>();

        Cursor c = mLocalVedicDB.ShowTableWishList();
        if (c.getCount() > 0) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                WishLocalArrFOrStartView.add(c.getString(1));
            }
        }
        mRelatedProductAdapter = new RelatedProductAdapter(ProductDetailActivity.this, relatedProductList, mLocalVedicDB, WishLocalArrFOrStartView);
        rvRelatedProduct.setAdapter(mRelatedProductAdapter);

        mRelatedProductAdapter.setOnItemClickListener(new RelatedProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {

                ProductlistModel mProductlistModel = relatedProductList.get(position);
                if (which != 0) {
                    finish();
                    Intent intent = new Intent(ProductDetailActivity.this, ProductDetailActivity.class);
                    intent.putExtra("subCatId", mProductlistModel.getProductID());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });
        adapter = new GalleryPagerAdapter(this);


        final LinearLayoutManager mLayoutManagerSecond = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recycleSize.setLayoutManager(mLayoutManagerSecond);
        recycleSize.setHasFixedSize(true);
        mProductDetailSizeAdapter = new ProductDetailSizeAdapter(ProductDetailActivity.this, type);
        recycleSize.setAdapter(mProductDetailSizeAdapter);
        mProductDetailSizeAdapter.setOnItemClickListener(new ProductDetailSizeAdapter.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(int position, View view, int which) {

                ProductDetailActivity.INTChangeForSIze = position;

                txtPriceTop.setText(getResources().getString(R.string.rs) + " " + price.get(position));

                if (mrp.get(position).equals("")) {
                    txtMrpTop.setVisibility(View.GONE);
                }

                if (!discount.get(position).equals("")) {
                    tvDiscount.setVisibility(View.VISIBLE);
                    tvDiscount.setText(discount.get(0));
                }

                txtMrpTop.setText(getResources().getString(R.string.rs) + " " + mrp.get(position));
                txtMrpTop.setPaintFlags(txtMrpTop.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                ThisPrice = price.get(position);
                ThisPriceId = priceID.get(position);
                ThisCOlor = "";
                if (colors.size() - 1 >= position) {
                    colour.setVisibility(View.VISIBLE);
                    Subcolors.clear();
                    String[] parts = colors.get(position).split(",");
                    Collections.addAll(Subcolors, parts);
                    ProductDetailActivity.INTChangeForColor = 0;
                    mProductDetailCOLORAdapter.notifyDataSetChanged();
                    detailscrollview.post(new Runnable() {

                        @Override
                        public void run() {
                            detailscrollview.fullScroll(ScrollView.FOCUS_UP);
                        }
                    });
                    ThisCOlor = Subcolors.get(0);
                } else {
                    colour.setVisibility(View.GONE);
                }
                mProductDetailSizeAdapter.notifyDataSetChanged();
            }
        });

        final LinearLayoutManager mLayoutManagerColor = new LinearLayoutManager(ProductDetailActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recycleCOlor.setLayoutManager(mLayoutManagerColor);
        recycleCOlor.setHasFixedSize(true);
        mProductDetailCOLORAdapter = new ProductDetailCOLORAdapter(ProductDetailActivity.this, Subcolors);
        recycleCOlor.setAdapter(mProductDetailCOLORAdapter);
        mProductDetailCOLORAdapter.setOnItemClickListener(new ProductDetailCOLORAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {
                ProductDetailActivity.INTChangeForColor = position;
                ThisCOlor = Subcolors.get(position);
                mProductDetailCOLORAdapter.notifyDataSetChanged();
            }
        });

        loading = ProgressDialog.show(ProductDetailActivity.this, "", "Please wait...", false, false);
        GetProductDetailDataTask mGetProductDetailDataTask = new GetProductDetailDataTask();
        mGetProductDetailDataTask.execute();
        txtChangePincode.setOnClickListener(this);
        btnBuyNow.setOnClickListener(this);
        btnAddtoCArt.setOnClickListener(this);
        imgAddToWishList.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        boolean baa = WishLocalArrFOrStartView.contains(subCatId);

        if (baa) {
            imgAddToWishList.setImageResource(R.mipmap.fav_selcted);
        } else {
            imgAddToWishList.setImageResource(R.mipmap.fav_nonselcted);
        }
    }

    @SuppressLint("SetTextI18n")
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        ImageView searchClick = (ImageView) toolbar.findViewById(R.id.searchclick);
        RelativeLayout relMyCart = (RelativeLayout) toolbar.findViewById(R.id.relMyCart);
        tvmytotalitems = (TextView) toolbar.findViewById(R.id.tvmytotalitems);

        textView.setText("Product Detail");
        tvmytotalitems.setText(StrCartCounter);
        relMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductDetailActivity.this, MyCart.class);
                intent.putExtra("CartPage", "PropductDetail");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        searchClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ProductDetailActivity.this, SearchActivity.class);
                startActivity(it);
            }
        });
        setSupportActionBar(toolbar);
    }

    private void FetchXmlID() {
        productDetailstitle = (TextView) findViewById(R.id.productDetailstitle);
        tvProductdesc = (HtmlTextView) findViewById(R.id.tvProductdesc);
        txtPriceTop = (TextView) findViewById(R.id.txtPriceTop);
        txtMrpTop = (TextView) findViewById(R.id.txtMrpTop);
        txtChangePincode = (TextView) findViewById(R.id.txtChangePincode);
        txtQtyMsg = (TextView) findViewById(R.id.txtQtyMsg);
        changePinCode = (TextView) findViewById(R.id.changePinCode);
        recycleSize = (RecyclerView) findViewById(R.id.recycleSize);
        recycleCOlor = (RecyclerView) findViewById(R.id.recycleCOlor);
        imgAddToWishList = (ImageView) findViewById(R.id.imgAddToWishList);
        imgShare = (ImageView) findViewById(R.id.imgShare);
        btnBuyNow = (Button) findViewById(R.id.btnBuyNow);
        btnAddtoCArt = (Button) findViewById(R.id.btnAddtoCArt);
        pager = (ViewPager) findViewById(R.id.pager);
        thumbnails = (LinearLayout) findViewById(R.id.thumbnails);
        qty_linear = (LinearLayout) findViewById(R.id.qty_linear);
        size = (LinearLayout) findViewById(R.id.size);
        linerDelveryCharge1 = (LinearLayout) findViewById(R.id.linerDelveryCharge1);
        linerDelveryCharge2 = (LinearLayout) findViewById(R.id.linerDelveryCharge2);
        linerDelveryCharge3 = (LinearLayout) findViewById(R.id.linerDelveryCharge3);
        ShipingMsg1 = (TextView) findViewById(R.id.ShipingMsg1);
        ShipingMsg2 = (TextView) findViewById(R.id.ShipingMsg2);
        ShipingMsg3 = (TextView) findViewById(R.id.ShipingMsg3);
        tvDiscount = (TextView) findViewById(R.id.tvDiscount);
        imgDelevery1 = (ImageView) findViewById(R.id.imgDelevery1);
        imgDelevery2 = (ImageView) findViewById(R.id.imgDelevery2);
        imgDelevery3 = (ImageView) findViewById(R.id.imgDelevery3);
        linerDelveryCharge1.setVisibility(View.GONE);
        linerDelveryCharge2.setVisibility(View.GONE);
        linerDelveryCharge3.setVisibility(View.GONE);
        colour = (LinearLayout) findViewById(R.id.colour);
        detailscrollview = (ScrollView) findViewById(R.id.detailscrollview);
        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onRestart() {
        super.onRestart();
        prefCartCounter = ProductDetailActivity.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");
        initToolbar();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtChangePincode:
                final Dialog dialog = new Dialog(ProductDetailActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_promocode);
                LayoutInflater fullmylf5595 = getLayoutInflater();
                @SuppressLint("InflateParams") final View mv559512 = fullmylf5595.inflate(R.layout.popup_promocode, null);
                dialog.setCancelable(true);

                final EditText text = (EditText) dialog.findViewById(R.id.etPromocode);
                TextView dialogButton = (TextView) dialog.findViewById(R.id.btnEnterpromo);
                //   strPromocode = text.getText().toString()
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StrPInCode = text.getText().toString();
                        if (StrPInCode.isEmpty()) {
                            Toast.makeText(ProductDetailActivity.this, "Enter PinCode", Toast.LENGTH_SHORT).show();
                        } else if (StrPInCode.length() != 6) {
                            Toast.makeText(ProductDetailActivity.this, "Invalid PinCode", Toast.LENGTH_SHORT).show();
                        } else {
                            loading = ProgressDialog.show(ProductDetailActivity.this, "", "Please wait...", false, false);
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mv559512.getWindowToken(), 0);

                            ChangePinAsynTask mChangePinAsynTask = new ChangePinAsynTask();
                            mChangePinAsynTask.execute();
                            dialog.dismiss();
                        }
                    }
                });
                dialog.show();
                break;

            case R.id.btnBuyNow:
                if (ProductDetailActivity.INTChangeForSIze == 100) {
                    Toast.makeText(this, "Please Select Size", Toast.LENGTH_SHORT).show();
                } else {
                    if (isSoldOut) {
                        Utils.showToastShort("Item sold out !", ProductDetailActivity.this);
                        break;
                    } else {
                        loading = ProgressDialog.show(ProductDetailActivity.this, "", "Please wait...", false, false);
                        AddtoCartWithBuyNowAsynTask mAddtoCartWithBuyNowAsynTask = new AddtoCartWithBuyNowAsynTask();
                        mAddtoCartWithBuyNowAsynTask.execute();
                    }
                }
                break;

            case R.id.imgAddToWishList:

                if (StrUserId.isEmpty()) {
                    Utils.loginDialog(ProductDetailActivity.this, "You need to be signed in to create and save your own wishlist.");
                } else {
                    ArrayList<String> WishLocalArr = new ArrayList<>();

                    Cursor c = mLocalVedicDB.ShowTableWishList();
                    if (c.getCount() > 0) {
                        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                            WishLocalArr.add(c.getString(1));
                        }
                        boolean b = WishLocalArr.contains(subCatId);
                        if (b) {
                            imgAddToWishList.setImageResource(R.mipmap.fav_nonselcted);
                            mLocalVedicDB.DeleteWishListByID(subCatId);
                        } else {
                            imgAddToWishList.setImageResource(R.mipmap.fav_selcted);
                            mSmallBang.bang(v);
                            mLocalVedicDB.InsertWishListData(subCatId, productDetailstitle.getText().toString(),
                                    ArrImgListLarge.get(0), ThisPrice, StrMrp);
                        }
                    } else {
                        imgAddToWishList.setImageResource(R.mipmap.fav_selcted);
                        mSmallBang.bang(v);
                        mLocalVedicDB.InsertWishListData(subCatId, productDetailstitle.getText().toString(),
                                ArrImgListLarge.get(0), ThisPrice, StrMrp);
                    }
                }
                break;
            case R.id.imgShare:
                download_url = ArrImgListLarge.get(0);
                if (!download_url.equalsIgnoreCase("") && Utils.isNetworkAvailable(ProductDetailActivity.this)) {
                    String fileName = download_url.substring(download_url.lastIndexOf('/') + 1, download_url.length());
                    filename = fileName.substring(0, fileName.lastIndexOf('.'));
                    download_url = ArrImgListLarge.get(0);
                    File file = new File(LOCAL_PATH + "/" + filename + ".jpg");

                    if (file.exists() && (download_url.toLowerCase().contains(".jpg") || download_url.toLowerCase().contains(".jpeg") ||
                            download_url.toLowerCase().contains(".png") || download_url.toLowerCase().contains(".bmp"))) {
                        try {
                            String StrTopText = productDetailstitle.getText().toString() + "\n\n http://play.google.com/store/apps/details?id=" + ProductDetailActivity.this.getPackageName();
                            String shareBody = StrTopText + "\n";

                            File fileNew = new File(file.getAbsolutePath());
                            Uri imageUri = Uri.fromFile(fileNew);
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("image/*");
                            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                            intent.putExtra(Intent.EXTRA_SUBJECT, "BeUtopian");
                            intent.putExtra(Intent.EXTRA_TEXT, shareBody);

                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(intent, "Share via"));
                        } catch (Exception e) {
                            Utils.showToastShort("Something Wrong !", ProductDetailActivity.this);
                        }
                    } else {
                        IsFromDownload = false;
                        DownloadImage task = new DownloadImage();
                        task.execute(download_url);
                    }
                } else {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String StrTopText = productDetailstitle.getText().toString() + "\n\n http://play.google.com/store/apps/details?id=" + ProductDetailActivity.this.getPackageName();
                    String shareBody = StrTopText + "\n";
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }

                break;
            case R.id.btnAddtoCArt:
                if (StrUserId.isEmpty()) {
                    Utils.loginDialog(ProductDetailActivity.this, "You need to be signed in to add to cart.");
                } else {
                    if (ProductDetailActivity.INTChangeForSIze == 100) {
                        Toast.makeText(this, "Please Select Size", Toast.LENGTH_SHORT).show();
                    } else {
                        loading = ProgressDialog.show(ProductDetailActivity.this, "", "Please wait...", false, false);
                        AddtoCartAsynTask mAddtoCartAsynTask = new AddtoCartAsynTask();
                        mAddtoCartAsynTask.execute();
                    }
                }
                break;
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class GetProductDetailDataTask extends AsyncTask<Void, Void, Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                return methods.GetProductDetails("product_detail", subCatId, StrUserId);
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Api_Model curators) {

            loading.dismiss();
            ArrImgListSmall = new ArrayList<>();
            ArrImgListLarge = new ArrayList<>();
            priceID.clear();
            type.clear();
            price.clear();
            mrp.clear();
            colors.clear();

            if (curators == null) {
                ShowRetryDialog();
            } else {
                if (curators.msgcode.equals("0")) {
                    if (curators.pincode.equalsIgnoreCase("")) {
                        changePinCode.setText("365480");
                    } else {
                        try {
                            int in = 0;
                            for (Api_Model.pincode_msg dataset : curators.pincode_msg) {
                                in++;
                                if (in == 1) {

                                    linerDelveryCharge1.setVisibility(View.VISIBLE);
                                    ShipingMsg1.setText(dataset.msg);

                                    if (dataset.type.equalsIgnoreCase("success")) {
                                        ShipingMsg1.setTextColor(Color.parseColor("#1bac94"));
                                        imgDelevery1.setImageResource(R.mipmap.checked);

                                    } else {
                                        ShipingMsg1.setTextColor(Color.parseColor("#ac0d0d"));
                                        imgDelevery1.setImageResource(R.mipmap.checked_red);
                                    }

                                    linerDelveryCharge2.setVisibility(View.GONE);
                                    linerDelveryCharge3.setVisibility(View.GONE);

                                } else if (in == 2) {
                                    linerDelveryCharge2.setVisibility(View.VISIBLE);
                                    ShipingMsg2.setText(dataset.msg);

                                    if (dataset.type.equalsIgnoreCase("success")) {
                                        ShipingMsg2.setTextColor(Color.parseColor("#1bac94"));
                                        imgDelevery2.setImageResource(R.mipmap.checked);
                                    } else {
                                        ShipingMsg2.setTextColor(Color.parseColor("#ac0d0d"));
                                        imgDelevery2.setImageResource(R.mipmap.checked_red);

                                    }
                                    linerDelveryCharge3.setVisibility(View.GONE);
                                } else if (in == 3) {
                                    linerDelveryCharge3.setVisibility(View.VISIBLE);
                                    ShipingMsg3.setText(dataset.msg);
                                    if (dataset.type.equalsIgnoreCase("success")) {
                                        ShipingMsg3.setTextColor(Color.parseColor("#1bac94"));
                                        imgDelevery3.setImageResource(R.mipmap.checked);
                                    } else {
                                        ShipingMsg3.setTextColor(Color.parseColor("#ac0d0d"));
                                        imgDelevery3.setImageResource(R.mipmap.checked_red);

                                    }
                                }

                            }
                            changePinCode.setText(curators.pincode);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                    try {
                        for (Api_Model.product dataset : curators.product) {
                            productDetailstitle.setText(dataset.name);

                            if (dataset.description.contains("\t")) {
                                String number = dataset.description.replaceAll("\t", "");
                                tvProductdesc.setHtml(number);
                            } else {
                                tvProductdesc.setHtml(dataset.description);
                            }

                            if (!dataset.qty_message.trim().equalsIgnoreCase("")) {

                                txtQtyMsg.setText(dataset.qty_message.trim());
                                qty_linear.setVisibility(View.VISIBLE);
                            } else {
                                qty_linear.setVisibility(View.GONE);
                            }

                            if (dataset.sold_out.equalsIgnoreCase("Yes")) {
                                btnAddtoCArt.setText("Sold out");
                                btnAddtoCArt.setTextColor(Color.parseColor("#FFFFFF"));
                                btnAddtoCArt.setClickable(false);
                                isSoldOut = true;
                            } else {
                                btnAddtoCArt.setClickable(true);
                                isSoldOut = false;
                            }


                            for (Api_Model.product.image_list datasetnew : dataset.image_list) {
                                ArrImgListSmall.add(datasetnew.small_image);
                                ArrImgListLarge.add(datasetnew.large_image);
                            }

                            for (Api_Model.product.price_list datasetnews : dataset.price_list) {
                                priceID.add(datasetnews.priceID);

                                if (datasetnews.size_option.equalsIgnoreCase("Yes")) {
                                    type.add(datasetnews.type);
                                }
                                if (datasetnews.color_option.equalsIgnoreCase("Yes")) {
                                    colors.add(datasetnews.colors);
                                }

                                price.add(datasetnews.price);
                                mrp.add(datasetnews.mrp);
                                discount.add(datasetnews.discount);
                            }
                        }
                        txtPriceTop.setText(getResources().getString(R.string.rs) + " " + price.get(0));
                        ThisPrice = price.get(0);
                        ThisPriceId = priceID.get(0);
                        if (!discount.get(0).equals("")) {
                            tvDiscount.setVisibility(View.VISIBLE);
                            tvDiscount.setText(discount.get(0));
                        }
                        if (mrp.get(0).equals("")) {
                            txtMrpTop.setVisibility(View.GONE);
                        }
                        txtMrpTop.setText(getResources().getString(R.string.rs) + " " + mrp.get(0));
                        txtMrpTop.setPaintFlags(txtMrpTop.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        StrMrp = mrp.get(0);
                        Assert.assertNotNull(ArrImgListSmall);
                        pager.setAdapter(adapter);
                        pager.setOffscreenPageLimit(6);
                        adapter.notifyDataSetChanged();

                        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
                        tabLayout.setupWithViewPager(pager, true);

                        ProductDetailActivity.INTChangeForSIze = 100;

                        if (type.size() > 0) {
                            size.setVisibility(View.VISIBLE);
                            mProductDetailSizeAdapter.notifyDataSetChanged();

                        } else {
                            size.setVisibility(View.GONE);

                        }
                        if (colors.size() > 0) {

                            colour.setVisibility(View.VISIBLE);
                            String[] parts = colors.get(0).split(",");
                            Collections.addAll(Subcolors, parts);
                            ProductDetailActivity.INTChangeForColor = 0;

                            ThisCOlor = Subcolors.get(0);
                            mProductDetailCOLORAdapter.notifyDataSetChanged();
                        } else {

                            colour.setVisibility(View.GONE);
                        }
                    } catch (Exception ignored) {
                    }
                    GetRelatedproductList getRelatedproductList = new GetRelatedproductList();
                    getRelatedproductList.execute();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "" + curators.message, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void ShowRetryDialog() {
        try {
            final Dialog dialog = new Dialog(ProductDetailActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_internet_not_found);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
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
                    loading = ProgressDialog.show(ProductDetailActivity.this, "", "Please wait...", false, false);
                    GetProductDetailDataTask mGetProductDetailDataTask = new GetProductDetailDataTask();
                    mGetProductDetailDataTask.execute();
                    dialog.dismiss();
                }
            });
            dialog.show();
        } catch (Exception ignored) {
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetRelatedproductList extends AsyncTask<Void, Void, Api_Model> {
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
                return methods.GetRelatedProduct("related_product", subCatId);
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {
                ShowRetryDialog();
            } else {
                Log.i("Curator", curators.message);
                if (curators.msgcode.equals("0")) {
                    try {
                        for (Api_Model.product_list dataset : curators.product_list) {
                            ProductlistModel mProductlistModel;
                            mProductlistModel = new ProductlistModel(dataset.productID, dataset.name, dataset.image,
                                    dataset.discount, dataset.price, dataset.mrp, dataset.sold_out);
                            relatedProductList.add(mProductlistModel);
                        }
                        mRelatedProductAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("Exception e", e.toString());
                    }
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AddtoCartWithBuyNowAsynTask extends AsyncTask<Void, Void, Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            Log.e("ThisPriceId", ThisPriceId);
            Log.e("subCatId", subCatId);
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                return methods.AddtoCartApi("cart", "add", StrUserId, subCatId, ThisPriceId, "", ThisCOlor);
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {
                ShowRetryDialog();
            } else {
                Log.i("Curator", curators.message);

                Utils.ShowSnakBar(curators.message, relativeMain, ProductDetailActivity.this);

                editorCartCounter.putString("CartCounter", curators.cart_count);
                editorCartCounter.commit();
                tvmytotalitems.setText(curators.cart_count);

                Intent intent = new Intent(ProductDetailActivity.this, MyCart.class);
                intent.putExtra("CartPage", "PropductDetail");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class AddtoCartAsynTask extends AsyncTask<Void, Void, Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            Log.e("ThisPriceId", ThisPriceId);
            Log.e("subCatId", subCatId);
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                return methods.AddtoCartApi("cart", "add", StrUserId, subCatId, ThisPriceId, "", ThisCOlor);
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();

            if (curators == null) {

                ShowRetryDialog();
            } else {

                Log.i("Curator", curators.message);
                if (curators.msgcode.equals("0")) {

                    Utils.ShowSnakBar(curators.message, relativeMain, ProductDetailActivity.this);
                    editorCartCounter.putString("CartCounter", curators.cart_count);
                    editorCartCounter.commit();
                    tvmytotalitems.setText(curators.cart_count);
                } else {
                    Utils.ShowSnakBar(curators.message, relativeMain, ProductDetailActivity.this);
                }
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class ChangePinAsynTask extends AsyncTask<Void, Void, Api_Model> {
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
                return methods.ChangePIncodeAsyntask("check_ship", StrPInCode, subCatId, StrUserId);
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();

            if (curators == null) {

                ShowRetryDialog();
            } else {
                Log.i("Curator", curators.message);
                if (curators.msgcode.equals("0")) {
                    int in = 0;
                    for (Api_Model.msg_values dataset : curators.msg_values) {
                        in++;
                        if (in == 1) {

                            linerDelveryCharge1.setVisibility(View.VISIBLE);
                            ShipingMsg1.setText(dataset.msg);

                            if (dataset.type.equalsIgnoreCase("success")) {
                                ShipingMsg1.setTextColor(Color.parseColor("#1bac94"));
                                imgDelevery1.setImageResource(R.mipmap.checked);
                            } else {
                                ShipingMsg1.setTextColor(Color.parseColor("#ac0d0d"));
                                imgDelevery1.setImageResource(R.mipmap.checked_red);
                            }
                            linerDelveryCharge2.setVisibility(View.GONE);
                            linerDelveryCharge3.setVisibility(View.GONE);

                        } else if (in == 2) {
                            linerDelveryCharge2.setVisibility(View.VISIBLE);
                            ShipingMsg2.setText(dataset.msg);
                            if (dataset.type.equalsIgnoreCase("success")) {
                                ShipingMsg2.setTextColor(Color.parseColor("#1bac94"));
                                imgDelevery2.setImageResource(R.mipmap.checked);
                            } else {
                                ShipingMsg2.setTextColor(Color.parseColor("#ac0d0d"));
                                imgDelevery2.setImageResource(R.mipmap.checked_red);
                            }
                            linerDelveryCharge3.setVisibility(View.GONE);
                        } else if (in == 3) {
                            linerDelveryCharge3.setVisibility(View.VISIBLE);
                            ShipingMsg3.setText(dataset.msg);
                            if (dataset.type.equalsIgnoreCase("success")) {
                                ShipingMsg3.setTextColor(Color.parseColor("#1bac94"));
                                imgDelevery3.setImageResource(R.mipmap.checked);
                            } else {
                                ShipingMsg3.setTextColor(Color.parseColor("#ac0d0d"));
                                imgDelevery3.setImageResource(R.mipmap.checked_red);

                            }
                        }

                    }
                    changePinCode.setText(StrPInCode);
                } else {
                    linerDelveryCharge1.setVisibility(View.GONE);
                    linerDelveryCharge2.setVisibility(View.GONE);
                    linerDelveryCharge3.setVisibility(View.GONE);
                    Utils.ShowSnakBar(curators.message, relativeMain, ProductDetailActivity.this);
                }
            }
        }
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

    public DisplayImageOptions getImageOptions() {

        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true).showImageOnLoading(R.drawable.default_gray_image)
                .showImageForEmptyUri(R.drawable.default_gray_image).showImageOnFail(R.drawable.default_gray_image)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }


    class GalleryPagerAdapter extends PagerAdapter {

        Context _context;
        LayoutInflater _inflater;

        GalleryPagerAdapter(Context context) {
            _context = context;
            _inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return ArrImgListSmall.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = _inflater.inflate(R.layout.detail_single_image, container, false);
            container.addView(itemView);


            int borderSize = thumbnails.getPaddingTop();


            int thumbnailSize = ((FrameLayout.LayoutParams)
                    pager.getLayoutParams()).bottomMargin - (borderSize * 2);

            // Set the thumbnail layout parameters. Adjust as required
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
            params.setMargins(0, 0, borderSize, 0);


            final ImageView thumbView = new ImageView(_context);
            thumbView.setBackgroundResource(R.drawable.sizeround1);
            thumbView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            thumbView.setLayoutParams(params);
            thumbView.setPadding(5, 5, 5, 5);
            thumbView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CurrentImage", "Thumbnail clicked");
                    pager.setCurrentItem(position);
                }
            });
            thumbnails.addView(thumbView);

            final ImageView imageView = (ImageView) itemView.findViewById(R.id.imagedetail);

            // Asynchronously load the image and set the thumbnail and pager view
            Glide.with(_context)
                    .load(ArrImgListLarge.get(position))
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                            imageView.setImageBitmap(bitmap);
                            thumbView.setImageBitmap(bitmap);
                        }
                    });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent it = new Intent(ProductDetailActivity.this, ImageFinalZommWithThumb.class);
                    it.putExtra("productName", productDetailstitle.getText().toString());
                    it.putStringArrayListExtra("imageListLarge", ArrImgListLarge);
                    it.putStringArrayListExtra("imageListSmall", ArrImgListSmall);
                    startActivity(it);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Loading, please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class DownloadImage extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(download_url);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream

                OutputStream output = new FileOutputStream(LOCAL_PATH + "/" + filename + ".jpg");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }


        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String file_url) {
            try {
                dismissDialog(progress_bar_type);
            } catch (Exception e) {
                Log.e("aaaaaaaa", "9999999999999");
            }
            File file = new File(LOCAL_PATH + "/" + filename + ".jpg");


            try {
                String StrTopText = productDetailstitle.getText().toString() + "\n\n http://play.google.com/store/apps/details?id=" + ProductDetailActivity.this.getPackageName();
                String shareBody = StrTopText + "\n";
                File fileNew = new File(file.getAbsolutePath());
                Uri imageUri = Uri.fromFile(fileNew);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_STREAM, imageUri);
                intent.putExtra(Intent.EXTRA_SUBJECT, "BeUtopian");
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "Share via"));
            } catch (Exception e) {
                Utils.showToastShort("Something Wrong !", ProductDetailActivity.this);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right);
    }


}


