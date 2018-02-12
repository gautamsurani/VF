package com.vedicfashions.shopping;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import dbhelper.LocalVedicDB;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.ChildModel;
import utils.IApiMethods;
import utils.ItemClickListener;
import utils.Section;
import utils.SectionedExpandableLayoutHelper;
import utils.Tools;
import utils.Utils;


public class NewCategoryActivity extends AppCompatActivity implements ItemClickListener, View.OnClickListener {

    Toolbar toolbar;
    RecyclerView mRecyclerView;
    ProgressDialog loading;
    SectionedExpandableLayoutHelper sectionedExpandableLayoutHelper;
    LinearLayout liCateGory, liSale, liWishList, liCart, liNotification;
    TextView tvitemCounter, txtCartCounter;
    LocalVedicDB mLocalVedicDB;
    String StrUserId, StrCartCounter;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;

    @SuppressLint({"SetTextI18n", "CommitPrefEdits"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcategory);

        toolbar = (Toolbar) findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        Tools.systemBarLolipop(this);
        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        textView.setText("Categories");
        setSupportActionBar(toolbar);

        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);

        prefCartCounter = getApplicationContext().getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");

        String LocalDbUserSocityIdName = StrUserId + "Vedic.db";
        String FinalLocalDBName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + LocalDbUserSocityIdName;

        mLocalVedicDB = new LocalVedicDB(NewCategoryActivity.this, FinalLocalDBName);
        mLocalVedicDB.OpenDB();

        liCateGory = (LinearLayout) findViewById(R.id.liCateGory);
        liSale = (LinearLayout) findViewById(R.id.liSale);
        liWishList = (LinearLayout) findViewById(R.id.liWishList);
        liCart = (LinearLayout) findViewById(R.id.liCart);
        liNotification = (LinearLayout) findViewById(R.id.liNotification);

        tvitemCounter = (TextView) findViewById(R.id.tvitemCounter);
        txtCartCounter = (TextView) findViewById(R.id.txtCartCounter);

        Cursor c = mLocalVedicDB.ShowTableWishList();
        tvitemCounter.setText(c.getCount() + "");
        txtCartCounter.setText(StrCartCounter + "");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        sectionedExpandableLayoutHelper = new SectionedExpandableLayoutHelper(this,
                mRecyclerView, this, 2);

        loading = ProgressDialog.show(NewCategoryActivity.this, "", "Please wait...", false, false);
        GetSubCategoryDataTask mGetSubCategoryDataTask = new GetSubCategoryDataTask();
        mGetSubCategoryDataTask.execute();

        /*ArrayList<ChildModel> arrayList = new ArrayList<>();
        Section sc=new Section("vbnnnn","First Product","ththfhfh","yes");

        arrayList.add(new ChildModel("dccvv","ASD111","any"));
        arrayList.add(new ChildModel("dccvv","ASD222","any"));
        arrayList.add(new ChildModel("dccvv","ASD333","any"));
        arrayList.add(new ChildModel("dccvv","ASD444","any"));
        arrayList.add(new ChildModel("dccvv","ASD555","any"));

        sectionedExpandableLayoutHelper.addSection(sc, arrayList);

        arrayList = new ArrayList<>();
        sc=new Section("vbnnnn","Second Product","ththfhfh","yes");

        arrayList.add(new ChildModel("dccvv","22ASD111","any"));
        arrayList.add(new ChildModel("dccvv","3qASD222","any"));
        arrayList.add(new ChildModel("dccvv","35ASD333","any"));
        arrayList.add(new ChildModel("dccvv","4ASD444","any"));
        arrayList.add(new ChildModel("dccvv","25ASD555","any"));

        sectionedExpandableLayoutHelper.addSection(sc, arrayList);

        sc=new Section("vbnnnn","Third Product","ththfhfh","no");
        sectionedExpandableLayoutHelper.addSection(sc,null);

        sectionedExpandableLayoutHelper.notifyDataSetChanged();*/

        liCateGory.setOnClickListener(this);
        liSale.setOnClickListener(this);
        liWishList.setOnClickListener(this);
        liCart.setOnClickListener(this);
        liNotification.setOnClickListener(this);

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


    @Override
    public void itemClicked(ChildModel item) {
        Intent intent = new Intent(NewCategoryActivity.this, ProductListActivity.class);
        intent.putExtra("catID", item.getCatID());
        intent.putExtra("catName", item.getName());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void itemClicked(Section section) {
        Intent intent = new Intent(NewCategoryActivity.this, ProductListActivity.class);
        intent.putExtra("catID", section.getCatID());
        intent.putExtra("catName", section.getName());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.liCateGory:
              /*  Intent intent = new Intent(CategoryActivity.this, CategoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);*/
                break;

            case R.id.liWishList:
                Intent inwsdtent = new Intent(NewCategoryActivity.this, MyWishList.class);
                inwsdtent.putExtra("WishPage", "category");
                startActivity(inwsdtent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.liCart:
                Intent intxent = new Intent(NewCategoryActivity.this, MyCart.class);
                startActivity(intxent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.liNotification:
                Intent offer = new Intent(NewCategoryActivity.this, Updates.class);
                offer.putExtra("PageTypeForPush", "NotPush");
                startActivity(offer);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.liSale:
                Intent sale = new Intent(NewCategoryActivity.this, SaleActivity.class);
                sale.putExtra("PageTypeForSale", "NotPush");
                startActivity(sale);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
        }
    }


    @SuppressLint("StaticFieldLeak")
    private class GetSubCategoryDataTask extends AsyncTask<Void, Void, Api_Model> {
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
                return methods.getNewCategory("category1");
            } catch (Exception E) {
                Log.i("exception e", E.toString());
                return null;
            }
        }
        @Override
        protected void onPostExecute(Api_Model curators) {
            loading.dismiss();
            if (curators == null) {
                Utils.showToastShort("try again!", NewCategoryActivity.this);
            } else {
                Log.i("Curator", curators.toString());
                if (curators.msgcode.equals("0")) {
                    try {
                        for (int i = 0; i < curators.category_list_new.size(); i++) {
                            Section sc = new Section(curators.category_list_new.get(i).catID,
                                    curators.category_list_new.get(i).name,
                                    curators.category_list_new.get(i).icon,
                                    curators.category_list_new.get(i).subcat
                            );
                            if (curators.category_list_new.get(i).subcat.equalsIgnoreCase("yes")) {
                                try {
                                    ArrayList<ChildModel> arrayList = new ArrayList<>();

                                    for (int j = 0; j < curators.category_list_new.get(i).subcat_list.size(); j++) {

                                        if (curators.category_list_new.get(i).subcat_list != null) {

                                            arrayList.add(new ChildModel(curators.category_list_new.get(i).subcat_list.get(j).catID,
                                                    curators.category_list_new.get(i).subcat_list.get(j).name,
                                                    curators.category_list_new.get(i).subcat_list.get(j).icon
                                            ));
                                        }
                                    }
                                    sectionedExpandableLayoutHelper.addSection(sc, arrayList);
                                } catch (Exception e) {
                                    Log.e("OK e", e.getMessage());
                                }
                            } else {
                                sectionedExpandableLayoutHelper.addSection(sc, null);
                            }
                        }
                        sectionedExpandableLayoutHelper.notifyDataSetChanged();
                    } catch (Exception e) {
                        Log.e("Exception e", e.toString());
                    }
                } else {
                    Utils.showToastShort(curators.message, NewCategoryActivity.this);
                }
            }
        }
    }
}
