package com.vedicfashions.shopping;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import adapters.CategoryAdapter;
import dbhelper.LocalVedicDB;
import models.CategoryData;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Tools;
import utils.Utils;

/**
 * Created by welcome on 14-10-2016.
 */
public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    private List<CategoryData> CategoryList = new ArrayList<>();
    public CategoryAdapter mCategoryAdapter;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    String StrUserId,StrCartCounter;
    TextView searchtext;
    private LinearLayout lyt_not_found;
    LinearLayout liCateGory, liSale, liWishList, liCart, liNotification;

    ProgressDialog loading;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
     LocalVedicDB mLocalVedicDB;
    TextView tvitemCounter,txtCartCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);


        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);

        prefCartCounter =getApplicationContext().getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");

        String LocalDbUserSocityIdName = StrUserId + "Vedic.db";
        String FinalLocalDBName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + LocalDbUserSocityIdName;
        mLocalVedicDB = new LocalVedicDB(CategoryActivity.this, FinalLocalDBName);
        mLocalVedicDB.OpenDB();
        FetchXmlID();
        initToolbar();
        LinearLayoutManager mLayoutManager = new GridLayoutManager(CategoryActivity.this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacesItemDecoration());

        mCategoryAdapter = new CategoryAdapter(CategoryActivity.this, CategoryList);
        recyclerView.setAdapter(mCategoryAdapter);


        mCategoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {
                CategoryData mCategoryData = CategoryList.get(position);
                if (mCategoryData.getSubcat().equalsIgnoreCase("Yes")) {

                    Intent intent = new Intent(CategoryActivity.this, CategoryExpandableListView.class);
                    intent.putExtra("catID", mCategoryData.getCatID());
                    intent.putExtra("catName", mCategoryData.getName());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                } else {
                    Intent intent = new Intent(CategoryActivity.this, ProductListActivity.class);
                    intent.putExtra("catID", mCategoryData.getCatID());
                    intent.putExtra("catName", mCategoryData.getName());
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        });


        loading = ProgressDialog.show(CategoryActivity.this, "", "Please wait...", false, false);
        GetCategoryDataTask mGetCategoryDataTask = new GetCategoryDataTask();
        mGetCategoryDataTask.execute();


        liCateGory.setOnClickListener(this);
        liSale.setOnClickListener(this);
        liWishList.setOnClickListener(this);
        liCart.setOnClickListener(this);
        liNotification.setOnClickListener(this);
        searchtext.setOnClickListener(this);
        Tools.systemBarLolipop(this);


    }

    private void FetchXmlID() {
        searchtext = (TextView) findViewById(R.id.searchtext);
        lyt_not_found = (LinearLayout) findViewById(R.id.lyt_not_found);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        tvitemCounter = (TextView) findViewById(R.id.tvitemCounter);
        liCateGory = (LinearLayout) findViewById(R.id.liCateGory);
        liSale = (LinearLayout) findViewById(R.id.liSale);
        liWishList = (LinearLayout) findViewById(R.id.liWishList);
        liCart = (LinearLayout) findViewById(R.id.liCart);
        liNotification = (LinearLayout) findViewById(R.id.liNotification);
        txtCartCounter = (TextView) findViewById(R.id.txtCartCounter);
        Cursor c = mLocalVedicDB.ShowTableWishList();
        tvitemCounter.setText(c.getCount() + "");
        txtCartCounter.setText(StrCartCounter + "");
    }

    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("Categories");
        setSupportActionBar(toolbar);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        loading = ProgressDialog.show(CategoryActivity.this, "", "Please wait...", false, false);
        GetCategoryDataTask mGetCategoryDataTask = new GetCategoryDataTask();
        mGetCategoryDataTask.execute();

        prefCartCounter =getApplicationContext().getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");
        initToolbar();

        Cursor c = mLocalVedicDB.ShowTableWishList();
        tvitemCounter.setText(c.getCount() + "");
        txtCartCounter.setText(StrCartCounter + "");

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
              /*  Intent intent = new Intent(CategoryActivity.this, CategoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);*/
                break;
            case R.id.liSale:
                break;
            case R.id.liWishList:
                Intent inwsdtent = new Intent(CategoryActivity.this, MyWishList.class);
                inwsdtent.putExtra("WishPage","category");
                startActivity(inwsdtent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.liCart:
                Intent intxent = new Intent(CategoryActivity.this, MyCart.class);
                startActivity(intxent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.liNotification:
                Intent offer = new Intent(CategoryActivity.this, Updates.class);
                offer.putExtra("PageTypeForPush","NotPush");
                startActivity(offer);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.searchtext:

                Intent it = new Intent(CategoryActivity.this, SearchActivity.class);
                startActivity(it);
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

    private class GetCategoryDataTask extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            CategoryList.clear();
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                Api_Model curators = methods.GetCategoryAPI("category");

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

            } else {

                Log.i("Curator", curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {
                    try {
                        for (Api_Model.category_list dataset : curators.category_list) {
                            CategoryData mCategoryData;
                            mCategoryData = new CategoryData(dataset.catID, dataset.name, dataset.icon, dataset.subcat);
                            CategoryList.add(mCategoryData);

                        }

                        mCategoryAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("Exception e", e.toString());
                    }

                } else {
                    Utils.showToastShort(curators.message.toString(), CategoryActivity.this);
                }
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
