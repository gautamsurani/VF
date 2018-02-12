package com.vedicfashions.shopping;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.SubCategoryAdapter;
import models.SubCategoryModel;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Tools;
import utils.Utils;

/**
 * Created by welcome on 14-10-2016.
 */
public class CategoryExpandableListView extends AppCompatActivity {

    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    String StrUserId, StrCartCounter;
    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
    Context context;
    SliderLayout mDemoSlider;
    public static ExpandableListView expListView;
    ProgressDialog progressDialog;
    List<SubCategoryModel> listDataHeader = new ArrayList<SubCategoryModel>();
    List<SubCategoryModel> chilement = new ArrayList<SubCategoryModel>();
    ArrayList<String> bannerImage = new ArrayList<String>();
    SubCategoryModel myOrderModelParent;
    SubCategoryModel myOrderModelChild;
    HashMap<SubCategoryModel, List<SubCategoryModel>> listDataChild = new HashMap<SubCategoryModel, List<SubCategoryModel>>();
    SubCategoryAdapter subCategoryAdapter;
    String subCatId = "";
    String subCatName = "";
    int dbWishSize = 0;
    TextView tvmytotalitems;
    ProgressDialog loading;
    private LinearLayout lyt_not_found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_expandable);
        FetchXMLID();

        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);
        prefCartCounter = CategoryExpandableListView.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");

        Log.e("StrCartCounter", StrCartCounter + "    2");
        context = this;
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            subCatId = getIntent().getExtras().getString("catID");
            subCatName = getIntent().getExtras().getString("catName");
        }
        initToolbar();
        loading = ProgressDialog.show(CategoryExpandableListView.this, "", "Please wait...", false, false);
        GetSubCategoryDataTask mGetSubCategoryDataTask = new GetSubCategoryDataTask();
        mGetSubCategoryDataTask.execute();
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (subCategoryAdapter.getChildrenCount(groupPosition) == 0) {
                    Intent it = new Intent(context, ProductListActivity.class);
                    it.putExtra("catID", listDataHeader.get(groupPosition).getCatId());
                    it.putExtra("catName", listDataHeader.get(groupPosition).getCatName());
                    startActivity(it);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

                return false;
            }
        });
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent it = new Intent(context, ProductListActivity.class);
                it.putExtra("subCatId", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getChildcatId());
                it.putExtra("subCatName", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getChildcatName());
                startActivity(it);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            }
        });
        Tools.systemBarLolipop(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        prefCartCounter = CategoryExpandableListView.this.getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");
        initToolbar();
    }

    private void FetchXMLID() {
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        expListView.setGroupIndicator(null);
        mDemoSlider = (SliderLayout) findViewById(R.id.mDemoSliderHome);
        lyt_not_found = (LinearLayout) findViewById(R.id.lyt_not_found);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        TextView textView = (TextView) toolbar.findViewById(R.id.eshop);
        ImageView searchclick = (ImageView) toolbar.findViewById(R.id.searchclick);
        // ImageView imgAppLogo = (ImageView) toolbar.findViewById(R.id.imgAppLogo);
        RelativeLayout relMyCart = (RelativeLayout) toolbar.findViewById(R.id.relMyCart);
        tvmytotalitems = (TextView) toolbar.findViewById(R.id.tvmytotalitems);
        textView.setText(subCatName);
        tvmytotalitems.setText(StrCartCounter);
        relMyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryExpandableListView.this, MyCart.class);

                intent.putExtra("CartPage", "categoryExpandable");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
        searchclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(CategoryExpandableListView.this, SearchActivity.class);
                startActivity(it);
            }
        });
        /*imgAppLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(CategoryExpandableListView.this, MainActivity.class);
                startActivity(it);
            }
        });*/


        setSupportActionBar(toolbar);

    }

    private class GetSubCategoryDataTask extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder().setEndpoint(AppConstant.API_URL).build();
        }
        @Override
        protected Api_Model doInBackground(Void... params) {
            try {
                IApiMethods methods = restAdapter.create(IApiMethods.class);
                Api_Model curators = methods.SubGetCategoryAPI("subcategory", subCatId);
                return curators;
            }
            catch (Exception E) {
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
                        for (Api_Model.image_list dataset : curators.image_list) {

                            bannerImage.add(dataset.image);
                        }
                        SetBanner(bannerImage);


                        for (Api_Model.subcategory_list dataset : curators.subcategory_list) {

                            myOrderModelParent = new SubCategoryModel();
                            chilement = new ArrayList<SubCategoryModel>();

                            myOrderModelParent.setCatId(dataset.catID);
                            myOrderModelParent.setCatName(dataset.name);

                            listDataHeader.add(myOrderModelParent);
                            listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), chilement);

                            try {

                                for (Api_Model.subcategory_list.subsubcategory_list datasetsSecond : dataset.subsubcategory_list) {
                                    if (datasetsSecond != null) {

                                        myOrderModelChild = new SubCategoryModel();
                                        myOrderModelChild.setChildcatId(datasetsSecond.Sr);
                                        myOrderModelChild.setChildcatName(datasetsSecond.agenda);
                                        chilement.add(myOrderModelChild);
                                    }
                                }
                            } catch (Exception e) {

                                Log.e("OK e", e.getMessage().toString());
                            }
                        }
                        subCategoryAdapter = new SubCategoryAdapter(CategoryExpandableListView.this, listDataHeader, listDataChild);
                        expListView.setAdapter(subCategoryAdapter);
                        subCategoryAdapter.notifyDataSetChanged();


                    } catch (Exception e) {
                        Log.e("Exception e", e.toString());
                    }

                } else {
                    Utils.showToastShort(curators.message.toString(), CategoryExpandableListView.this);
                }
            }
        }
    }


    public void SetBanner(ArrayList bannercontent) {
        HashMap<String, String> file_maps = new HashMap<String, String>();
        for (int i = 0; i < bannercontent.size(); i++) {
            file_maps.put(Integer.toString(i), bannercontent.get(i).toString());
        }

        for (String name : file_maps.keySet()) {
            DefaultSliderView textSliderView = new DefaultSliderView(CategoryExpandableListView.this);
            textSliderView.image(file_maps.get(name)).setScaleType(BaseSliderView.ScaleType.Fit);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addSlider(textSliderView);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
            startActivity(new Intent(CategoryExpandableListView.this, NewCategoryActivity.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        finish();
        startActivity(new Intent(CategoryExpandableListView.this, NewCategoryActivity.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}