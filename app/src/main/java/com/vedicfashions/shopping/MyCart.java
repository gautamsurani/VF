package com.vedicfashions.shopping;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import adapters.CartAdapter;
import dbhelper.LocalVedicDB;
import models.CartData;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Tools;
import utils.Utils;

/**
 * Created by welcome on 14-10-2016.
 */
public class MyCart extends AppCompatActivity implements View.OnClickListener {

    private List<CartData> CartList = new ArrayList<>();
    public CartAdapter mCartAdapter;

    private ProgressBar progressBar1;
    private LinearLayout lyt_not_found;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    RecyclerView recyclerView;
    LinearLayout MAkeBottomView;
    LocalVedicDB mLocalVedicDB;
    String StrUserId, StrCartCounter;

    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
    String StrMOveWishId, StrDeleteCartItemId;
    int IntModelPosition;
    TextView txtPricetime, txtPriceTotal, txtDelevery, txtDeleveryType, txtFinalMrp, txtGreenText, txtMrpBottom, txtViewPrice;
    Button btnSendData;
    String StrProductId, StrPRiceId, productQuantity;
    String StrPrice = "", StrProductName = "", StrImageName = "", StrMRP = "";
    ProgressDialog loading;
    double IntTotalCount = 0;
    NestedScrollView nestedScollView;
    TextView tvmytotalitems;
    String StrCartId = "";

    RelativeLayout relativeMain;
    LinearLayout llHideCart;


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);

        FetchXmlID();

        prefLogin = MyCart.this.getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);

        Log.e("StrUserId", StrUserId);

        if (StrUserId.isEmpty()) {
            Utils.loginDialogForCart(MyCart.this, "You need to be signed in to manage your cart.");
            llHideCart.setVisibility(View.VISIBLE);
        } else {
            llHideCart.setVisibility(View.GONE);
            String LocalDbUserSocityIdName = StrUserId + "Vedic.db";
            String FinalLocalDBName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + LocalDbUserSocityIdName;
            mLocalVedicDB = new LocalVedicDB(MyCart.this, FinalLocalDBName);
            mLocalVedicDB.OpenDB();
            prefCartCounter = MyCart.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
            editorCartCounter = prefCartCounter.edit();
            StrCartCounter = prefCartCounter.getString("CartCounter", "0");
            Log.e("StrCartCounter", StrCartCounter);
            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(MyCart.this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setHasFixedSize(true);
            recyclerView.addItemDecoration(new SpacesItemDecoration());
            mCartAdapter = new CartAdapter(MyCart.this, CartList);
            recyclerView.setAdapter(mCartAdapter);
            mCartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view, int which) {

                    CartData mCartData = CartList.get(position);

                    if (which == 97) {
                        IntModelPosition = position;
                        loading = ProgressDialog.show(MyCart.this, "", "Please wait...", false, false);
                        StrProductId = mCartData.getProductID();
                        StrPRiceId = mCartData.getPriceID();
                        StrPrice = mCartData.getPrice();
                        StrProductName = mCartData.getName();
                        StrImageName = mCartData.getImage();
                        StrMRP = mCartData.getMrp();
                        StrCartId = mCartData.getCartID();
                        MoveWishListandDeleteToCartAsyTask mMoveWishListandDeleteToCartAsyTask = new MoveWishListandDeleteToCartAsyTask();
                        mMoveWishListandDeleteToCartAsyTask.execute();
                    } else if (which == 98) {
                        loading = ProgressDialog.show(MyCart.this, "", "Please wait...", false, false);

                        IntModelPosition = position;
                        StrProductId = mCartData.getProductID();
                        StrPRiceId = mCartData.getPriceID();

                        StrCartId = mCartData.getCartID();
                        RomoveToCartAsyTask mRomoveToCartAsyTask = new RomoveToCartAsyTask();
                        mRomoveToCartAsyTask.execute();
                    } else if (which == 99) {
                        Intent it = new Intent(MyCart.this, ProductDetailActivity.class);
                        it.putExtra("subCatId", mCartData.getProductID());
                        it.putExtra("page", "MyCart");
                        startActivity(it);
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left);
                    } else {
                        //  which
                        loading = ProgressDialog.show(MyCart.this, "", "Please wait...", false, false);
                        StrProductId = mCartData.getProductID();
                        StrPRiceId = mCartData.getPriceID();
                        productQuantity = which + "";
                        StrCartId = mCartData.getCartID();
                        ChangeQuentytyAsyTask mChangeQuentytyAsyTask = new ChangeQuentytyAsyTask();
                        mChangeQuentytyAsyTask.execute();
                    }

                }
            });

            if (Utils.isNetworkAvailable(getBaseContext())) {
                loading = ProgressDialog.show(MyCart.this, "", "Please wait...", false, false);
                GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
                mGetNoticeListArrayForUpdates.execute();
            } else {
                Utils.ShowSnakBar("Please Check Your Internet Connection", relativeMain, MyCart.this);
            }
            Tools.systemBarLolipop(this);
            btnSendData.setOnClickListener(this);

            txtViewPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    nestedScollView.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            nestedScollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });

                }
            });
            initToolbar();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Utils.isNetworkAvailable(getBaseContext())) {
            loading = ProgressDialog.show(MyCart.this, "", "Please wait...", false, false);
            GetNoticeListArrayForUpdates mGetNoticeListArrayForUpdates = new GetNoticeListArrayForUpdates();
            mGetNoticeListArrayForUpdates.execute();
        } else {
            Utils.ShowSnakBar("Please Check Your Internet Connection", relativeMain, MyCart.this);

        }

        prefCartCounter = MyCart.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");
        initToolbar();
    }

    private void FetchXmlID() {
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        lyt_not_found = (LinearLayout) findViewById(R.id.lyt_not_found);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btnSendData = (Button) findViewById(R.id.btnSendData);
        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);
        MAkeBottomView = (LinearLayout) findViewById(R.id.MAkeBottomView);
        txtPricetime = (TextView) findViewById(R.id.txtPricetime);
        txtPriceTotal = (TextView) findViewById(R.id.txtPriceTotal);
        txtDelevery = (TextView) findViewById(R.id.txtDelevery);
        txtDeleveryType = (TextView) findViewById(R.id.txtDeleveryType);
        txtFinalMrp = (TextView) findViewById(R.id.txtFinalMrp);
        txtGreenText = (TextView) findViewById(R.id.txtGreenText);
        txtMrpBottom = (TextView) findViewById(R.id.txtMrpBottom);
        txtViewPrice = (TextView) findViewById(R.id.txtViewPrice);
        nestedScollView = (NestedScrollView) findViewById(R.id.nestedScollView);
        llHideCart = (LinearLayout) findViewById(R.id.llHideCart);
    }

    @SuppressLint("SetTextI18n")
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        ImageView searchClick = (ImageView) toolbar.findViewById(R.id.searchclick);
        RelativeLayout relMyCart = (RelativeLayout) toolbar.findViewById(R.id.relMyCart);
        tvmytotalitems = (TextView) toolbar.findViewById(R.id.tvmytotalitems);

        textView.setText("My Cart");
        tvmytotalitems.setText(StrCartCounter);
        /*relMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent = new Intent(MyCart.this, MyCart.class);
                intent.putExtra("CartPage", "MyCArt");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });*/
        searchClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MyCart.this, SearchActivity.class);
                startActivity(it);
            }
        });

        setSupportActionBar(toolbar);

    }


    @Override
    public void onResume() {

        super.onResume();

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */


    @Override
    public void onClick(View v) {
        if (CartList.size() != 0) {
            Intent in = new Intent(MyCart.this, DeleveryClass.class);

            in.putExtra("Page", "Cart");
            in.putExtra("item", CartList.size() + "");

            String string = txtMrpBottom.getText().toString();
            String[] parts = string.split(getResources().getString(R.string.rs) + " ");
            String part1 = parts[1];
            in.putExtra("subtotal", part1);
            startActivity(in);
            overridePendingTransition(R.anim.slide_in_right,
                    R.anim.slide_out_left);
        } else {
            Utils.ShowSnakBar("Cart is Empty !", relativeMain, MyCart.this);
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
        overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right);
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
                Api_Model curators = methods.GetCartDataApi("cart", "list", StrUserId);

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

                ShowRetryDialog();
            } else {


                editorCartCounter.putString("CartCounter", curators.total.toString());
                editorCartCounter.commit();
                StrCartCounter = curators.total.toString();
                initToolbar();


                if (curators.msgcode.toString().equals("0")) {

                    CartList.clear();
                    MAkeBottomView.setVisibility(View.VISIBLE);
                    lyt_not_found.setVisibility(View.GONE);


                    for (Api_Model.cart dataset : curators.cart) {

                        CartData mCartData;
                        mCartData = new CartData(dataset.productID, dataset.priceID,
                                dataset.name, dataset.model, dataset.image,
                                dataset.price, dataset.quantity, dataset.color, dataset.mrp, dataset.cartID, dataset.maxqty);
                        CartList.add(mCartData);


                    }
                    mCartAdapter.notifyDataSetChanged();
                } else {
                    MAkeBottomView.setVisibility(View.GONE);
                    lyt_not_found.setVisibility(View.VISIBLE);
                }


            }


            txtPricetime.setText("Price (" + CartList.size() + " Items)");
            for (int i = 0; i < CartList.size(); i++) {
                CartData mThiscartData = CartList.get(i);
                IntTotalCount = IntTotalCount + (Double.parseDouble(mThiscartData.getPrice()) * Double.parseDouble(mThiscartData.getQuantity()));
            }

            DecimalFormat df = new DecimalFormat("#.00"); // df.format(LastCount)

            txtPriceTotal.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
            txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
            txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
            IntTotalCount = 0.00;
        }

    }

    private class RomoveToCartAsyTask extends AsyncTask<Void, Void,
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
                Api_Model curators = methods.RemoveCartApi("cart", "remove", StrUserId, StrProductId, StrPRiceId, StrCartId);

                return curators;
            } catch (Exception E) {
                Log.i("exceptio e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {

                ShowRetryDialog();
            } else {
                Log.i("Curator", curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {
                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, MyCart.this);

                    editorCartCounter.putString("CartCounter", curators.cart_count.toString());
                    editorCartCounter.commit();
                    editorCartCounter.apply();


                    tvmytotalitems.setText(curators.cart_count.toString());
                    CartList.remove(IntModelPosition);
                    mCartAdapter.notifyDataSetChanged();
                } else {
                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, MyCart.this);


                }


                if (CartList.size() == 0) {
                    MAkeBottomView.setVisibility(View.GONE);
                    lyt_not_found.setVisibility(View.VISIBLE);
                } else {
                    MAkeBottomView.setVisibility(View.VISIBLE);
                    lyt_not_found.setVisibility(View.GONE);

                }


            }

            txtPricetime.setText("Price (" + CartList.size() + " Items)");
            for (int i = 0; i < CartList.size(); i++) {
                CartData mThiscartData = CartList.get(i);
                IntTotalCount = IntTotalCount + (Double.parseDouble(mThiscartData.getPrice()) * Double.parseDouble(mThiscartData.getQuantity()));
            }
            DecimalFormat df = new DecimalFormat("#.00");

            txtPriceTotal.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
            txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
            txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
            IntTotalCount = 0.00;
        }

    }


    private class ChangeQuentytyAsyTask extends AsyncTask<Void, Void,
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
                Api_Model curators = methods.cHANGEqUENTITYCartApi("cart", "update", StrUserId, StrProductId, StrPRiceId, productQuantity, StrCartId);

                return curators;
            } catch (Exception E) {
                Log.i("exceptio e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {

                ShowRetryDialog();
            } else {
                Log.i("Curator", curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {

                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, MyCart.this);
                    txtPricetime.setText("Price (" + CartList.size() + " Items)");
                    for (int i = 0; i < CartList.size(); i++) {
                        CartData mThiscartData = CartList.get(i);
                        IntTotalCount = IntTotalCount + (Double.parseDouble(mThiscartData.getPrice()) * Double.parseDouble(mThiscartData.getQuantity()));
                    }

                    DecimalFormat df = new DecimalFormat("#.00");

                    txtPriceTotal.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
                    txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
                    txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
                    IntTotalCount = 0.00;
                    //    mCartAdapter.notifyDataSetChanged();
                } else {
                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, MyCart.this);


                }


            }

        }


    }


    private class MoveWishListandDeleteToCartAsyTask extends AsyncTask<Void, Void,
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
                Api_Model curators = methods.RemoveCartApi("cart", "remove", StrUserId, StrProductId, StrPRiceId, StrCartId);

                return curators;
            } catch (Exception E) {
                Log.i("exceptio e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {

                ShowRetryDialog();
            } else {
                Log.i("Curator", curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {
                    try {
                        mLocalVedicDB.DeleteWishListByID(StrProductId);
                        mLocalVedicDB.InsertWishListData(StrProductId, StrProductName,
                                StrImageName, StrPrice, StrMRP);

                    } catch (Exception e) {

                    }


                    Utils.ShowSnakBar("Successfully Move To WishList", relativeMain, MyCart.this);
                    editorCartCounter.putString("CartCounter", curators.cart_count.toString());
                    editorCartCounter.commit();
                    tvmytotalitems.setText(curators.cart_count.toString());
                    CartList.remove(IntModelPosition);
                    mCartAdapter.notifyDataSetChanged();

                    if (CartList.size() == 0) {
                        MAkeBottomView.setVisibility(View.GONE);
                        lyt_not_found.setVisibility(View.VISIBLE);
                    } else {
                        MAkeBottomView.setVisibility(View.VISIBLE);
                        lyt_not_found.setVisibility(View.GONE);
                    }

                    txtPricetime.setText("Price (" + CartList.size() + " Items)");
                    for (int i = 0; i < CartList.size(); i++) {
                        CartData mThiscartData = CartList.get(i);
                        IntTotalCount = IntTotalCount + (Double.parseDouble(mThiscartData.getPrice()) * Double.parseDouble(mThiscartData.getQuantity()));
                    }

                    DecimalFormat df = new DecimalFormat("#.00");

                    txtPriceTotal.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
                    txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
                    txtMrpBottom.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
                    IntTotalCount = 0.00;
                } else {

                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, MyCart.this);


                }


            }


            txtPricetime.setText("Price (" + CartList.size() + " Items)");
            for (int i = 0; i < CartList.size(); i++) {
                CartData mThiscartData = CartList.get(i);
                IntTotalCount = IntTotalCount + (Double.parseDouble(mThiscartData.getPrice()) * Double.parseDouble(mThiscartData.getQuantity()));
            }

            DecimalFormat df = new DecimalFormat("#.00");

            txtPriceTotal.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
            txtFinalMrp.setText(getResources().getString(R.string.rs) + " " + df.format(IntTotalCount) + "");
            IntTotalCount = 0.00;


            if (CartList.size() == 0) {
                MAkeBottomView.setVisibility(View.GONE);
                lyt_not_found.setVisibility(View.VISIBLE);
            } else {
                MAkeBottomView.setVisibility(View.VISIBLE);
                lyt_not_found.setVisibility(View.GONE);

            }
        }

    }

    private void ShowRetryDialog() {
        try {
            final Dialog dialog = new Dialog(MyCart.this);
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


                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {

        }
    }


    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration() {
            this.space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15,
                    getResources().getDisplayMetrics()); //8dp as px, value might be obtained e.g. from dimen resources...
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = -15;
                outRect.bottom = 0; //dont forget about recycling...
            }
            if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
                outRect.bottom = space;
                outRect.top = 0;
            }
        }
    }

}
