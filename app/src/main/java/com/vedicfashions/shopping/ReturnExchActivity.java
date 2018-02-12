package com.vedicfashions.shopping;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.util.ArrayList;

import adapters.ProductDetailSizeAdapter;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.RequestMethod;
import utils.RestClient;

public class ReturnExchActivity extends AppCompatActivity {

    String CatId = "", page = "", size = "", orderId = "", SR = "", Remark = "";

    private ArrayList<String> type = new ArrayList<>();

    private ArrayList<String> PriceId = new ArrayList<>();

    String subCatId = "";

    ProgressDialog loading;

    SharedPreferences prefLogin;

    SharedPreferences.Editor editorLogin;

    ArrayList<String> ArrImgListSmall, ArrImgListLarge;

    String StrUserId;

    ImageView ivPImg;

    TextView tvName, tvSize;

    RecyclerView recycleSize;

    ProductDetailSizeAdapter mProductDetailSizeAdapter;

    LinearLayout llSize;

    String selectedSize = "", selectedSizeId = "", selectedPage = "";

    EditText etRemark;

    Button btnSubmit;

    ProgressDialog progressDialog;

    String resMessage = "", resCode = "";

    Context context;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_exch);

        initComp();

        context = this;

        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);

        Intent in = getIntent();
        if (in != null) {
            CatId = in.getExtras().getString("subCatId");
            page = in.getExtras().getString("page");
            size = in.getExtras().getString("size");
            orderId = in.getExtras().getString("orderId");
            SR = in.getExtras().getString("SR");
        }

        initToolbar();

        if (page.equals("return")) {
            llSize.setVisibility(View.GONE);
            selectedPage = "Return";
        } else {
            llSize.setVisibility(View.VISIBLE);
            selectedPage = "Exchange";
        }

        final LinearLayoutManager mLayoutManagerSecond = new LinearLayoutManager(ReturnExchActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recycleSize.setLayoutManager(mLayoutManagerSecond);
        recycleSize.setHasFixedSize(true);
        mProductDetailSizeAdapter = new ProductDetailSizeAdapter(ReturnExchActivity.this, type);
        recycleSize.setAdapter(mProductDetailSizeAdapter);
        mProductDetailSizeAdapter.setOnItemClickListener(new ProductDetailSizeAdapter.OnItemClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemClick(int position, View view, int which) {

                ProductDetailActivity.INTChangeForSIze = position;

                selectedSize = type.get(position);

                selectedSizeId = PriceId.get(position);

                mProductDetailSizeAdapter.notifyDataSetChanged();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Remark = etRemark.getText().toString();

                if (Remark.equals("")) {
                    Toast.makeText(context, "Please Enter Remark", Toast.LENGTH_SHORT).show();
                } else {
                    if (page.equals("return")) {
                        new ApiCall().execute();
                    } else {
                        if (selectedSize.equals("")) {
                            Toast.makeText(context, "Please Select Size", Toast.LENGTH_SHORT).show();
                        } else {
                            new ApiCall().execute();
                        }
                    }
                }
            }
        });

        loading = ProgressDialog.show(this, "", "Please wait...", false, false);
        GetProductDetail getProductDetail = new GetProductDetail();
        getProductDetail.execute();

    }

    @SuppressLint("StaticFieldLeak")
    private class ApiCall extends AsyncTask<String, Void, String> {
        JSONObject jsonObjectList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.show();
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {

            String strAPI = "http://www.vedicfashion.com/mapp/index.php?view=order_product_exchange" +
                    "&custID=" + StrUserId +
                    "&page=exchange_product" +
                    "&order_id=" + orderId +
                    "&order_exchange_type=" + selectedPage +
                    "&order_detail_id1=" + SR +
                    "&order_detail_product_id=" + CatId +
                    "&order_detail_size_id=" + selectedSizeId +
                    "&order_detail_size=" + selectedSize +
                    "&remark=" + Remark;

            String strAPITrim = strAPI.replaceAll(" ", "%20");
            Log.d("strAPI", strAPITrim);
            try {
                RestClient restClient = new RestClient(strAPITrim);
                try {
                    restClient.Execute(RequestMethod.POST);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String Register = restClient.getResponse();
                Log.e("API", Register);

                if (Register != null && Register.length() != 0) {
                    jsonObjectList = new JSONObject(Register);
                    if (jsonObjectList.length() != 0) {
                        resMessage = jsonObjectList.getString("message");
                        resCode = jsonObjectList.getString("msgcode");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if (resCode.equalsIgnoreCase("0")) {
                Toast.makeText(context, resMessage, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, resMessage, Toast.LENGTH_SHORT).show();
            }
            onBackPressed();
        }
    }

    @SuppressLint("SetTextI18n")
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_ID);
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        TextView textView = toolbar.findViewById(R.id.eshop);
        if (page.equals("return")) {
            textView.setText("Return");
        } else {
            textView.setText("Exchange");
        }
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent(ReturnExchActivity.this, OrderDetailsActivity.class);
        it.putExtra("OrderId", orderId);
        startActivity(it);
        finish();
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

    private void initComp() {
        ivPImg = findViewById(R.id.ivPImg);
        tvName = findViewById(R.id.tvName);
        tvSize = findViewById(R.id.tvSize);
        recycleSize = findViewById(R.id.recycleSize);
        llSize = findViewById(R.id.size);
        etRemark = findViewById(R.id.etRemark);
        btnSubmit = findViewById(R.id.btnSubmit);
    }

    @SuppressLint("StaticFieldLeak")
    private class GetProductDetail extends AsyncTask<Void, Void, Api_Model> {
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
                return methods.GetProductDetails("product_detail", CatId, StrUserId);
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
            type.clear();

            if (curators != null) {
                if (curators.msgcode.equals("0")) {
                    try {
                        for (Api_Model.product dataset : curators.product) {

                            for (Api_Model.product.image_list datasetnew : dataset.image_list) {
                                ArrImgListSmall.add(datasetnew.small_image);
                                ArrImgListLarge.add(datasetnew.large_image);
                            }

                            for (Api_Model.product.price_list datasetnews : dataset.price_list) {
                                PriceId.add(datasetnews.priceID);
                                type.add(datasetnews.type);
                            }

                            tvName.setText(dataset.name);
                            tvSize.setText("Size: " + size);
                        }

                        Glide.with(ReturnExchActivity.this)
                                .load(ArrImgListSmall.get(0))
                                .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                                .placeholder(R.drawable.default_gray_image)
                                .into(ivPImg);

                        mProductDetailSizeAdapter.notifyDataSetChanged();

                    } catch (Exception ignored) {
                    }

                } else {
                    Toast.makeText(ReturnExchActivity.this, "" + curators.message, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
