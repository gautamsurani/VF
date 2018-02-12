package com.vedicfashions.shopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapters.WishListAdapter;
import dbhelper.LocalVedicDB;
import models.WishListData;
import utils.Tools;
import utils.Utils;

/**
 * Created by welcome on 18-10-2016.
 */
public class MyWishList extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;

    String StrUserId, StrCartCounter;

    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
    private List<WishListData> WishListList = new ArrayList<>();
    public WishListAdapter mWishListAdapter;

    private ProgressBar progressBar1;
    private LinearLayout lyt_not_found;
    LinearLayout liCateGory, liSale, liWishList, liCart, liNotification;

    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    LocalVedicDB mLocalVedicDB;

    TextView tvitemCounter, txtCartCounter;

    TextView tvmytotalitems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wishlist);
        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);

        prefCartCounter = MyWishList.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");


        String LocalDbUserSocityIdName = StrUserId + "Vedic.db";
        String FinalLocalDBName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + LocalDbUserSocityIdName;
        mLocalVedicDB = new LocalVedicDB(MyWishList.this, FinalLocalDBName);
        mLocalVedicDB.OpenDB();


        FetchXmlID();
        initToolbar();

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(MyWishList.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacesItemDecoration());


        mWishListAdapter = new WishListAdapter(MyWishList.this, WishListList);
        recyclerView.setAdapter(mWishListAdapter);


        mWishListAdapter.setOnItemClickListener(new WishListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {
                WishListData mWishListData = WishListList.get(position);
                if (which == 0) {
                    Intent it = new Intent(MyWishList.this, ProductDetailActivity.class);
                    it.putExtra("subCatId", mWishListData.getProductID());
                    it.putExtra("page", "WishList");
                    startActivity(it);
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                } else {

                    mLocalVedicDB.DeleteWishListByID(mWishListData.getProductID());
                    DisplyLocalDB();

                }

            }
        });

        DisplyLocalDB();
        liCateGory.setOnClickListener(this);
        liSale.setOnClickListener(this);
        liWishList.setOnClickListener(this);
        liCart.setOnClickListener(this);
        liNotification.setOnClickListener(this);
        Tools.systemBarLolipop(this);

        txtCartCounter.setText(StrCartCounter);
    }

    private void DisplyLocalDB() {


        if (WishListList.size() > 0) {
            WishListList.clear();
        }

        try {

            Cursor c = mLocalVedicDB.ShowTableWishList();
            tvitemCounter.setText(c.getCount() + "");
            if (c.getCount() > 0) {

                lyt_not_found.setVisibility(View.GONE);
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {

                    WishListData mWishListData;
                    mWishListData = new WishListData(c.getString(1), c.getString(2), c.getString(3),
                            c.getString(4), c.getString(5));
                    WishListList.add(mWishListData);

                }
                mWishListAdapter.notifyDataSetChanged();

            } else {
                lyt_not_found.setVisibility(View.VISIBLE);
                mWishListAdapter.notifyDataSetChanged();

            }

        } catch (Exception e) {
            Utils.showToastShort(e.getMessage().toString(), MyWishList.this);
            mWishListAdapter.notifyDataSetChanged();
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        WishListList.clear();
        DisplyLocalDB();
        prefCartCounter = MyWishList.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");
        initToolbar();

        txtCartCounter.setText(StrCartCounter);

    }

    private void FetchXmlID() {
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        lyt_not_found = (LinearLayout) findViewById(R.id.lyt_not_found);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvitemCounter = (TextView) findViewById(R.id.tvitemCounter);
        txtCartCounter = (TextView) findViewById(R.id.txtCartCounter);

        liCateGory = (LinearLayout) findViewById(R.id.liCateGory);
        liSale = (LinearLayout) findViewById(R.id.liSale);
        liWishList = (LinearLayout) findViewById(R.id.liWishList);
        liCart = (LinearLayout) findViewById(R.id.liCart);
        liNotification = (LinearLayout) findViewById(R.id.liNotification);


    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        ImageView searchclick = (ImageView) toolbar.findViewById(R.id.searchclick);
        RelativeLayout relMyCart = (RelativeLayout) toolbar.findViewById(R.id.relMyCart);
        tvmytotalitems = (TextView) toolbar.findViewById(R.id.tvmytotalitems);


        textView.setText("My WishList");
        tvmytotalitems.setText(StrCartCounter);
        relMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyWishList.this, MyCart.class);
                intent.putExtra("CartPage", "wishlist");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        searchclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MyWishList.this, SearchActivity.class);
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
        switch (v.getId()) {
            case R.id.liCateGory:
                Intent intent = new Intent(MyWishList.this, NewCategoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.liSale:
                Intent sale = new Intent(MyWishList.this, SaleActivity.class);
                sale.putExtra("PageTypeForSale", "NotPush");
                startActivity(sale);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.liWishList:
                /*finish();
                Intent intentsas = new Intent(MyWishList.this, MyWishList.class);

                startActivity(intentsas);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);*/
                break;
            case R.id.liCart:
                Intent intentdsasdsd = new Intent(MyWishList.this, MyCart.class);
                startActivity(intentdsasdsd);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.liNotification:
                Intent offer = new Intent(MyWishList.this, Updates.class);
                offer.putExtra("PageTypeForPush", "NotPush");
                startActivity(offer);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

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

    private class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration() {
            this.space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15,
                    getResources().getDisplayMetrics()); //8dp as px, value might be obtained e.g. from dimen resources...
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = 0;
                outRect.bottom = 0; //dont forget about recycling...
            }
            if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
                outRect.bottom = space;
                outRect.top = 0;
            }
        }
    }

}
