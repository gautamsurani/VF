package com.vedicfashions.shopping;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import adapters.ProductListAdapter;
import dbhelper.LocalVedicDB;
import models.ProductlistModel;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Tools;
import utils.Utils;

/**
 * Created by welcome on 15-12-2016.
 */
public class ProductListActivityBrand extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    String StrUserId, StrCartCounter;

    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
    ProgressDialog progressDialog;
    ArrayList<ProductlistModel> productList = new ArrayList<ProductlistModel>();
    private ProductListAdapter mProductListAdapter;
    LinearLayout lyt_not_found;
    GridLayoutManager gridLayoutManager;
    RecyclerView recyclerView;
    ProgressBar progressBar1;
    TextView tvSortbyPrice, tvReSet;
    int current_page = 0;
    LocalVedicDB mLocalVedicDB;
    boolean IsLAstLoading = true;
    ProgressDialog loading;
    LinearLayout botbar;
    String subCatId = "";
    String subCatName = "";

    public int pagecode = 0;
    int dbWishSize = 0;
    Dialog dialog;
    RadioButton rblowtohigh, rbhightolow, rbnameatoz, rbnameztoa;
    String filtertype = "";

    TextView tvmytotalitems, txtToastCountMsg;
    LinearLayout linearShowToastMsg;
    String StrProductCount="0";

    RelativeLayout relativeMain,relListGrid;
    ArrayList<String> WishLocalArrFOrStartView = new ArrayList<String>();

    boolean isLayout = false;
    LinearLayoutManager mLayoutManager;
    int ThisvisibleItemCount = 0;
    int visibleItemCount, totalItemCount, pastVisiblesItems;
    ImageView imglistgridview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productlist);
        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);


        String LocalDbUserSocityIdName = StrUserId + "Vedic.db";
        String FinalLocalDBName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + LocalDbUserSocityIdName;
        mLocalVedicDB = new LocalVedicDB(ProductListActivityBrand.this, FinalLocalDBName);
        mLocalVedicDB.OpenDB();


        prefCartCounter = ProductListActivityBrand.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            subCatId = getIntent().getExtras().getString("catID");
            subCatName = getIntent().getExtras().getString("catName");
        }

        FetchXmlID();
        initToolbar();
        Tools.systemBarLolipop(this);

        ProductListAdapter.viewFormatProductList=1;
        gridLayoutManager = new GridLayoutManager(ProductListActivityBrand.this, 2);
        mLayoutManager = new LinearLayoutManager(ProductListActivityBrand.this);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        //   recyclerView.setItemAnimator(new DefaultItemAnimator());
        //  recyclerView.addItemDecoration(new SpacesItemDecoration());



        Cursor c = mLocalVedicDB.ShowTableWishList();
        if (c.getCount() > 0) {
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                WishLocalArrFOrStartView.add(c.getString(1));
            }
        }

        mProductListAdapter = new ProductListAdapter(ProductListActivityBrand.this, productList, mLocalVedicDB, WishLocalArrFOrStartView);
        recyclerView.setAdapter(mProductListAdapter);


        mProductListAdapter.setOnItemClickListener(new ProductListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {

                ProductlistModel mProductlistModel = productList.get(position);
                if (which == 0) {

                    Cursor c = mLocalVedicDB.ShowTableWishList();
                    //   Utils.showToastShort(c.getCount()+"",ProductListActivityBrand.this);
                } else {
                    Intent it = new Intent(ProductListActivityBrand.this, ProductDetailActivity.class);
                    it.putExtra("subCatId", mProductlistModel.getProductID());
                    startActivity(it);
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);

                }


            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                linearShowToastMsg.setVisibility(View.VISIBLE);

               /* int ThisvisibleItemCount = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (ThisvisibleItemCount != -1) {
                    txtToastCountMsg.setText("Showing " + String.valueOf(ThisvisibleItemCount + "/" +StrProductCount + " items"));
                }


                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        linearShowToastMsg.setVisibility(View.GONE);
                    }
                }, 3000);*/

                if (dy > 0) {

                    if (ProductListAdapter.viewFormatProductList == 0) {
                        ThisvisibleItemCount = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                        if (ThisvisibleItemCount != -1) {
                            txtToastCountMsg.setText("Showing " + String.valueOf(ThisvisibleItemCount + "/" + StrProductCount + " items"));
                        }

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                linearShowToastMsg.setVisibility(View.GONE);
                            }
                        }, 3000);

                        visibleItemCount = mLayoutManager.getChildCount();
                        totalItemCount = mLayoutManager.getItemCount();
                        pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    } else {

                        ThisvisibleItemCount = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                        if (ThisvisibleItemCount != -1) {
                            txtToastCountMsg.setText("Showing " + String.valueOf(ThisvisibleItemCount + "/" + StrProductCount + " items"));
                        }

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                linearShowToastMsg.setVisibility(View.GONE);
                            }
                        }, 3000);

                        visibleItemCount = gridLayoutManager.getChildCount();
                        totalItemCount = gridLayoutManager.getItemCount();
                        pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
                    }


                    if (IsLAstLoading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount &&
                                recyclerView.getChildAt(recyclerView.getChildCount() - 1).getBottom() <= recyclerView.getHeight()) {
                            IsLAstLoading = false;

                            progressBar1.setVisibility(View.VISIBLE);
                            pagecode++;
                            GetProductDetailDataTask mGetProductDetailDataTask = new GetProductDetailDataTask();
                            mGetProductDetailDataTask.execute();

                        }
                    }
                } else {
                    if (ProductListAdapter.viewFormatProductList == 0) {
                        ThisvisibleItemCount = mLayoutManager.findFirstCompletelyVisibleItemPosition();
                        if (ThisvisibleItemCount != -1) {
                            txtToastCountMsg.setText("Showing " + String.valueOf(ThisvisibleItemCount + "/" + StrProductCount + " items"));
                        }

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                linearShowToastMsg.setVisibility(View.GONE);
                            }
                        }, 3000);


                    } else {
                        ThisvisibleItemCount = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
                        if (ThisvisibleItemCount != -1) {
                            txtToastCountMsg.setText("Showing " + String.valueOf(ThisvisibleItemCount + "/" + StrProductCount + " items"));
                        }

                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                linearShowToastMsg.setVisibility(View.GONE);
                            }
                        }, 3000);

                    }
                }
            }
        });

        loading = ProgressDialog.show(ProductListActivityBrand.this, "", "Please wait...", false, false);
        GetProductDetailDataTask mGetProductDetailDataTask = new GetProductDetailDataTask();
        mGetProductDetailDataTask.execute();

        tvReSet.setOnClickListener(this);
        tvSortbyPrice.setOnClickListener(this);
        linearShowToastMsg.setOnClickListener(this);


        relListGrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ProductListAdapter.viewFormatProductList == 1) {
                    ProductListAdapter.viewFormatProductList = 0;
                    recyclerView.setLayoutManager(mLayoutManager);
                    imglistgridview.setImageDrawable(getResources().getDrawable(R.drawable.ic_grid_item_small));
                    mProductListAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(ThisvisibleItemCount);
                    isLayout = false;
                } else {
                    ProductListAdapter.viewFormatProductList = 1;
                    recyclerView.setLayoutManager(gridLayoutManager);
                    imglistgridview.setImageDrawable(getResources().getDrawable(R.drawable.ic_listitemlist));

                    mProductListAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(ThisvisibleItemCount);
                    isLayout = true;
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (Utils.isNetworkAvailable(getBaseContext())) {
            WishLocalArrFOrStartView.clear();

            Cursor c = mLocalVedicDB.ShowTableWishList();
            if (c.getCount() > 0) {
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    WishLocalArrFOrStartView.add(c.getString(1));
                }
            }

            if (ProductListAdapter.viewFormatProductList ==0){
                recyclerView.setLayoutManager(mLayoutManager);
            }else {
                recyclerView.setLayoutManager(gridLayoutManager);
            }


            mProductListAdapter = new ProductListAdapter(ProductListActivityBrand.this, productList, mLocalVedicDB, WishLocalArrFOrStartView);
            recyclerView.setAdapter(mProductListAdapter);


            mProductListAdapter.setOnItemClickListener(new ProductListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position, View view, int which) {

                    ProductlistModel mProductlistModel = productList.get(position);
                    if (which == 0) {

                        Cursor c = mLocalVedicDB.ShowTableWishList();
                        //   Utils.showToastShort(c.getCount()+"",ProductListActivityBrand.this);
                    } else {
                        Intent it = new Intent(ProductListActivityBrand.this, ProductDetailActivity.class);
                        it.putExtra("subCatId", mProductlistModel.getProductID());
                        startActivity(it);
                        overridePendingTransition(R.anim.slide_in_right,
                                R.anim.slide_out_left);

                    }


                }
            });


            loading = ProgressDialog.show(ProductListActivityBrand.this, "", "Please wait...", false, false);
            GetProductDetailDataTask mGetProductDetailDataTask = new GetProductDetailDataTask();
            mGetProductDetailDataTask.execute();

            prefCartCounter = ProductListActivityBrand.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
            editorCartCounter = prefCartCounter.edit();
            StrCartCounter = prefCartCounter.getString("CartCounter", "0");
            initToolbar();
        } else {
            Utils.ShowSnakBar("Please Check Your Internet Connection", relativeMain, ProductListActivityBrand.this);

        }

    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        ImageView searchclick = (ImageView) toolbar.findViewById(R.id.searchclick);
        RelativeLayout relMyCart = (RelativeLayout) toolbar.findViewById(R.id.relMyCart);
        tvmytotalitems = (TextView) toolbar.findViewById(R.id.tvmytotalitems);


        textView.setText(subCatName);
        tvmytotalitems.setText(StrCartCounter);
        relMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductListActivityBrand.this, MyCart.class);
                intent.putExtra("CartPage", "MyCArt");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        searchclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ProductListActivityBrand.this, SearchActivity.class);
                startActivity(it);
            }
        });

        setSupportActionBar(toolbar);

    }

    private void FetchXmlID() {
        tvSortbyPrice = (TextView) findViewById(R.id.tvSortbyPrice);
        tvReSet = (TextView) findViewById(R.id.tvReSet);
        txtToastCountMsg = (TextView) findViewById(R.id.txtToastCountMsg);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerProductList);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        lyt_not_found = (LinearLayout) findViewById(R.id.lyt_not_found);


        relativeMain = (RelativeLayout) findViewById(R.id.relativeMain);
        relListGrid = (RelativeLayout) findViewById(R.id.relListGrid);
        imglistgridview = (ImageView) findViewById(R.id.imglistgridview);
        linearShowToastMsg = (LinearLayout) findViewById(R.id.linearShowToastMsg);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.tvReSet:

                if (ProductListAdapter.viewFormatProductList == 0) {
                    filtertype = "";
                    visibleItemCount = mLayoutManager.getChildCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= 0) {
                        filtertype = "";
                        pagecode = 0;
                        //  productList.clear();
                        //   recyclerView.scrollToPosition(1);

                        recyclerView.scrollToPosition(0);
                        loading = ProgressDialog.show(ProductListActivityBrand.this, "", "Please wait...", false, false);
                        GetProductDetailDataTask mGetProductDetailDataTask = new GetProductDetailDataTask();
                        mGetProductDetailDataTask.execute();
                    }
                } else {
                    filtertype = "";
                    visibleItemCount = gridLayoutManager.getChildCount();
                    pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= 0) {
                        filtertype = "";
                        pagecode = 0;
                        // productList.clear();
                        //  recyclerView.scrollToPosition(1);

                        recyclerView.scrollToPosition(0);
                        loading = ProgressDialog.show(ProductListActivityBrand.this, "", "Please wait...", false, false);
                        GetProductDetailDataTask mGetProductDetailDataTask = new GetProductDetailDataTask();
                        mGetProductDetailDataTask.execute();

                    }
                }

                /*pagecode=0;
                loading = ProgressDialog.show(ProductListActivityBrand.this, "", "Please wait...", false, false);
                GetProductDetailDataTask mGetProductDetailDataTask = new GetProductDetailDataTask();
                mGetProductDetailDataTask.execute();*/
                break;
            case R.id.tvSortbyPrice:
                SortByPriceDialog();
                break;
            case R.id.linearShowToastMsg:
                recyclerView.smoothScrollToPosition(0);
                linearShowToastMsg.setVisibility(View.GONE);
                break;


        }
    }

    private void SortByPriceDialog() {

        if (productList.size() == 0) {
            Toast.makeText(ProductListActivityBrand.this, "No Products Found", Toast.LENGTH_SHORT).show();
        } else {
            dialog = new BottomSheetDialog(ProductListActivityBrand.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_filter);

            dialog.show();

            rblowtohigh = (RadioButton) dialog.findViewById(R.id.rblowtohigh);
            rbhightolow = (RadioButton) dialog.findViewById(R.id.rbhightolow);
            rbnameatoz = (RadioButton) dialog.findViewById(R.id.rbnameatoz);
            rbnameztoa = (RadioButton) dialog.findViewById(R.id.rbnameztoa);

            rblowtohigh.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    filtertype = "price_l_h";
                    pagecode = 0;
                    recyclerView.scrollToPosition(0);

                    loading = ProgressDialog.show(ProductListActivityBrand.this, "", "Please wait...", false, false);
                    GetProductDetailDataTask mGetProductDetailDataTask = new GetProductDetailDataTask();
                    mGetProductDetailDataTask.execute();

                    dialog.dismiss();
                }
            });

            rbhightolow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    filtertype = "price_h_l";
                    pagecode = 0;
                    recyclerView.scrollToPosition(0);

                    loading = ProgressDialog.show(ProductListActivityBrand.this, "", "Please wait...", false, false);
                    GetProductDetailDataTask mGetProductDetailDataTask = new GetProductDetailDataTask();
                    mGetProductDetailDataTask.execute();

                    dialog.dismiss();
                }
            });


            rbnameatoz.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    filtertype = "name_a_z";
                    pagecode = 0;
                    recyclerView.scrollToPosition(0);

                    loading = ProgressDialog.show(ProductListActivityBrand.this, "", "Please wait...", false, false);
                    GetProductDetailDataTask mGetProductDetailDataTask = new GetProductDetailDataTask();
                    mGetProductDetailDataTask.execute();

                    dialog.dismiss();
                }
            });

            rbnameztoa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    filtertype = "name_z_a";
                    pagecode = 0;
                    recyclerView.scrollToPosition(0);

                    loading = ProgressDialog.show(ProductListActivityBrand.this, "", "Please wait...", false, false);
                    GetProductDetailDataTask mGetProductDetailDataTask = new GetProductDetailDataTask();
                    mGetProductDetailDataTask.execute();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

    }

    private class GetProductDetailDataTask extends AsyncTask<Void, Void,
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
                Api_Model curators = methods.GetProductListAPIBrand("product", subCatId, Integer.toString(pagecode),filtertype);

                return curators;
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            IsLAstLoading = true;
            progressBar1.setVisibility(View.GONE);
            if (curators == null) {

            } else {

                Log.i("Curator", curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {
                    productList.clear();


                    try {
                        for (Api_Model.product_list dataset : curators.product_list) {
                            ProductlistModel mProductlistModel;
                            mProductlistModel = new ProductlistModel(dataset.productID, dataset.name, dataset.image,
                                    dataset.discount, dataset.price, dataset.mrp, dataset.sold_out,dataset.qty_message);
                            productList.add(mProductlistModel);

                        }

                        mProductListAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("Exception e", e.toString());
                    }
                    StrProductCount=""+curators.product_count;


                } else {

                    Utils.ShowSnakBar(curators.message.toString(), relativeMain, ProductListActivityBrand.this);
                }
            }


            if (productList.size() > 0) {

                lyt_not_found.setVisibility(View.GONE);

            } else {
                lyt_not_found.setVisibility(View.VISIBLE);
            }
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
                outRect.top = 0;
                outRect.bottom = 0; //dont forget about recycling...
            }
            if (parent.getChildAdapterPosition(view) == state.getItemCount() - 1) {
                outRect.bottom = space;
                outRect.top = 0;
            }
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
        ProductListAdapter.viewFormatProductList = 0;
        overridePendingTransition(
                R.anim.slide_in_left,
                R.anim.slide_out_right);
    }

}

