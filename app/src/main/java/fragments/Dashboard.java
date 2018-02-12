package fragments;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.vedicfashions.shopping.Api_Model;
import com.vedicfashions.shopping.BuildConfig;
import com.vedicfashions.shopping.CategoryExpandableListView;
import com.vedicfashions.shopping.MyCart;
import com.vedicfashions.shopping.MyWishList;
import com.vedicfashions.shopping.NewCategoryActivity;
import com.vedicfashions.shopping.ProductDetailActivity;
import com.vedicfashions.shopping.ProductListActivity;
import com.vedicfashions.shopping.ProductListActivityBrand;
import com.vedicfashions.shopping.R;
import com.vedicfashions.shopping.ReferActivity;
import com.vedicfashions.shopping.SaleActivity;
import com.vedicfashions.shopping.SearchActivity;
import com.vedicfashions.shopping.SplashScreen;
import com.vedicfashions.shopping.Updates;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.DashBoardBestSellingAdapter;
import adapters.DashBoardBrandAdapter;
import adapters.DashBoardNewProductAdapter;
import adapters.DashBoardOffersAdapter;
import dbhelper.LocalVedicDB;
import models.DashBoardBannersData;
import models.DashBoardBestSellingData;
import models.DashBoardBrandData;
import models.DashBoardNewProductData;
import models.DashBoardOffersData;
import models.SearchModel;
import retrofit.RestAdapter;
import utils.AppConstant;
import utils.IApiMethods;
import utils.Utils;

/*
 * Created by welcome on 01-08-2016.
 */
public class Dashboard extends Fragment implements View.OnClickListener {

    private List<DashBoardNewProductData> DashBoardNewProductList = new ArrayList<>();
    public DashBoardNewProductAdapter mDashBoardNewProductAdapter;

    private List<DashBoardBestSellingData> DashBoardBestSellingList = new ArrayList<>();
    public DashBoardBestSellingAdapter mDashBoardBestSellingAdapter;

    private List<DashBoardOffersData> DashBoardOffersList = new ArrayList<>();
    public DashBoardOffersAdapter mDashBoardOffersAdapter;

    private List<DashBoardBrandData> DashBoardBrandList = new ArrayList<>();
    public DashBoardBrandAdapter mDashBoardBrandAdapter;

    private List<DashBoardNewProductData> DashBoardCatONEList = new ArrayList<>();
    public DashBoardNewProductAdapter mDashBoardCatONEAdapter;

    private List<DashBoardNewProductData> DashBoardCatTWOList = new ArrayList<>();
    public DashBoardNewProductAdapter mDashBoardCatTWOAdapter;

    private List<DashBoardNewProductData> DashBoardCatTHREEList = new ArrayList<>();
    public DashBoardNewProductAdapter mDashBoardCatTHREEAdapter;

    private List<DashBoardNewProductData> DashBoardCatFIVEList = new ArrayList<>();
    public DashBoardNewProductAdapter mDashBoardCatFIVEAdapter;

    private List<DashBoardNewProductData> DashBoardCatSIXList = new ArrayList<>();
    public DashBoardNewProductAdapter mDashBoardCatSIXAdapter;

    private List<DashBoardNewProductData> DashBoardCatFOURList = new ArrayList<>();
    public DashBoardNewProductAdapter mDashBoardCatFOURAdapter;

    private List<DashBoardBannersData> DashBoardBannersList = new ArrayList<>();
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;

    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
    View view;
    private RecyclerView recycleNewProduct, recycleOffers, recycleBestProducts, recycleOurBrands;

    private RecyclerView recyclecatOne, recyclecatTwo, recyclecatThree, recyclecatFour, recyclecatFive, recyclecatSix;
    TextView txtCatOne, txtCatTwo, txtCatThree, txtCatFour, txtCatFive, txtCatSix;
    TextView txtViewMoreCatOne, txtViewMoreCatTwo, txtViewMoreCatThree, txtViewMoreCatFour, txtViewMoreCatFive, txtViewMoreCatSix;
    RelativeLayout linearCatOne, linearCatTwo, linearCatThree, linearCatFour, linearCatFive, linearCatSix;
    TextView txtNewArrival, txtBestSelling, txtTopBrands;

    public static ArrayList<SearchModel> searchlist;
    SliderLayout mDemoSlider;
    ArrayList bannercontent;

    ProgressDialog loading;
    private Context context;
    Typeface fonts1;
    TextView searchtext;
    CardView categoryCard;
    public TextView tvitemCounter, txtCartCounter;
    LinearLayout liCateGory, liSale, liWishList, liCart, liNotification;
    SharedPreferences prefSuper;
    SharedPreferences.Editor editorSuperInfo;
    LocalVedicDB mLocalVedicDB;
    String StrUserId, StrCartCounter;

    SharedPreferences prefToken;
    SharedPreferences.Editor editorToken;

    String StrCatID1 = "", StrCatID2 = "", StrCatID3 = "", StrCatID4 = "", StrCatID5 = "", StrCatID6 = "";
    String StrCatNAME1 = "", StrCatNAME2 = "", StrCatNAME3 = "", StrCatNAME4 = "", StrCatNAME5 = "", StrCatNAME6 = "";

    String StrUserBeutopianCommitteToken = "", StrUserBeutopianCommitteDeviceID = "";

    Button btnFB, btnRefer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_homepage, container, false);
        FetchXmlID(view);
        bannercontent = new ArrayList();

        prefLogin = getActivity().getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);

        prefCartCounter = getActivity().getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");


        prefToken = getActivity().getSharedPreferences("TokenPref", 0); // 0 - for private mode
        editorToken = prefToken.edit();

        prefSuper = getActivity().getSharedPreferences("DataSuperInfo", 0); // 0 - for private mode
        editorSuperInfo = prefSuper.edit();

        if (prefToken.getBoolean("IsFromSplash", true) == true) {


            try {
                StrUserBeutopianCommitteToken = FirebaseInstanceId.getInstance().getToken();
                StrUserBeutopianCommitteDeviceID = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                Log.e("Refreshed token: ", StrUserBeutopianCommitteToken + "");
                Log.e("Refreshed  : DeviceId", StrUserBeutopianCommitteDeviceID + "");

                editorToken.putBoolean("IsFromSplash", false);
                editorToken.commit();

                Log.e("Token ", StrUserBeutopianCommitteToken + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Utils.showToastShort(StrUserBeutopianCommitteToken,getActivity());
        } else {
            StrUserBeutopianCommitteToken = "";
            StrUserBeutopianCommitteDeviceID = "";

            Log.e("Refreshed token: ", StrUserBeutopianCommitteToken + "");
            Log.e("Refreshed  : DeviceId", StrUserBeutopianCommitteDeviceID + "");
        }


        String LocalDbUserSocityIdName = StrUserId + "Vedic.db";
        String FinalLocalDBName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + LocalDbUserSocityIdName;
        mLocalVedicDB = new LocalVedicDB(getActivity(), FinalLocalDBName);
        mLocalVedicDB.OpenDB();
        searchlist = new ArrayList<>();

        ArrayList<String> WishLocalArrFOrStartView = new ArrayList<String>();

        Cursor cas = mLocalVedicDB.ShowTableWishList();
        if (cas.getCount() > 0) {
            for (cas.moveToFirst(); !cas.isAfterLast(); cas.moveToNext()) {
                WishLocalArrFOrStartView.add(cas.getString(1));
            }
        }

        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycleNewProduct.setLayoutManager(mLayoutManager);
        recycleNewProduct.setHasFixedSize(true);
        mDashBoardNewProductAdapter = new DashBoardNewProductAdapter(getActivity(), DashBoardNewProductList, mLocalVedicDB, WishLocalArrFOrStartView);
        recycleNewProduct.setAdapter(mDashBoardNewProductAdapter);
        mDashBoardNewProductAdapter.setOnItemClickListener(new DashBoardNewProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {
                DashBoardNewProductData mDashBoardNewProductData = DashBoardNewProductList.get(position);
                if (which == 0) {
                    Cursor c = mLocalVedicDB.ShowTableWishList();
                    tvitemCounter.setText(c.getCount() + "");
                } else {
                    Intent it = new Intent(getActivity(), ProductDetailActivity.class);
                    it.putExtra("subCatId", mDashBoardNewProductData.getProductID());
                    it.putExtra("page", "main");
                    startActivity(it);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });


        final LinearLayoutManager mLayoutManagerCatOne = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclecatOne.setLayoutManager(mLayoutManagerCatOne);
        recyclecatOne.setHasFixedSize(true);
        mDashBoardCatONEAdapter = new DashBoardNewProductAdapter(getActivity(), DashBoardCatONEList, mLocalVedicDB, WishLocalArrFOrStartView);
        recyclecatOne.setAdapter(mDashBoardCatONEAdapter);
        mDashBoardCatONEAdapter.setOnItemClickListener(new DashBoardNewProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {
                DashBoardNewProductData mDashBoardNewProductData = DashBoardCatONEList.get(position);
                if (which == 0) {
                    Cursor c = mLocalVedicDB.ShowTableWishList();
                    tvitemCounter.setText(c.getCount() + "");
                } else {

                    Intent it = new Intent(getActivity(), ProductDetailActivity.class);
                    it.putExtra("subCatId", mDashBoardNewProductData.getProductID());
                    it.putExtra("page", "main");
                    startActivity(it);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        final LinearLayoutManager mLayoutManagerCatTwo = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclecatTwo.setLayoutManager(mLayoutManagerCatTwo);
        recyclecatTwo.setHasFixedSize(true);
        mDashBoardCatTWOAdapter = new DashBoardNewProductAdapter(getActivity(), DashBoardCatTWOList, mLocalVedicDB, WishLocalArrFOrStartView);
        recyclecatTwo.setAdapter(mDashBoardCatTWOAdapter);
        mDashBoardCatTWOAdapter.setOnItemClickListener(new DashBoardNewProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {

                DashBoardNewProductData mDashBoardNewProductData = DashBoardCatTWOList.get(position);
                if (which == 0) {
                    Cursor c = mLocalVedicDB.ShowTableWishList();
                    tvitemCounter.setText(c.getCount() + "");

                } else {
                    Intent it = new Intent(getActivity(), ProductDetailActivity.class);
                    it.putExtra("subCatId", mDashBoardNewProductData.getProductID());
                    it.putExtra("page", "main");
                    startActivity(it);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                }
            }
        });

        final LinearLayoutManager mLayoutManagerCatThree = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclecatThree.setLayoutManager(mLayoutManagerCatThree);
        recyclecatThree.setHasFixedSize(true);
        mDashBoardCatTHREEAdapter = new DashBoardNewProductAdapter(getActivity(), DashBoardCatTHREEList, mLocalVedicDB, WishLocalArrFOrStartView);
        recyclecatThree.setAdapter(mDashBoardCatTHREEAdapter);
        mDashBoardCatTHREEAdapter.setOnItemClickListener(new DashBoardNewProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {

                DashBoardNewProductData mDashBoardNewProductData = DashBoardCatTHREEList.get(position);
                if (which == 0) {
                    Cursor c = mLocalVedicDB.ShowTableWishList();
                    tvitemCounter.setText(c.getCount() + "");

                } else {
                    Intent it = new Intent(getActivity(), ProductDetailActivity.class);
                    it.putExtra("subCatId", mDashBoardNewProductData.getProductID());
                    it.putExtra("page", "main");
                    startActivity(it);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                }
            }
        });


        final LinearLayoutManager mLayoutManagerCatFour = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclecatFour.setLayoutManager(mLayoutManagerCatFour);
        recyclecatFour.setHasFixedSize(true);
        mDashBoardCatFOURAdapter = new DashBoardNewProductAdapter(getActivity(), DashBoardCatFOURList, mLocalVedicDB, WishLocalArrFOrStartView);
        recyclecatFour.setAdapter(mDashBoardCatFOURAdapter);
        mDashBoardCatFOURAdapter.setOnItemClickListener(new DashBoardNewProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {

                DashBoardNewProductData mDashBoardNewProductData = DashBoardCatFOURList.get(position);
                if (which == 0) {
                    Cursor c = mLocalVedicDB.ShowTableWishList();
                    tvitemCounter.setText(c.getCount() + "");

                } else {
                    Intent it = new Intent(getActivity(), ProductDetailActivity.class);
                    it.putExtra("subCatId", mDashBoardNewProductData.getProductID());
                    it.putExtra("page", "main");
                    startActivity(it);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                }
            }
        });

        final LinearLayoutManager mLayoutManagerCatFive = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclecatFive.setLayoutManager(mLayoutManagerCatFive);
        recyclecatFive.setHasFixedSize(true);
        mDashBoardCatFIVEAdapter = new DashBoardNewProductAdapter(getActivity(), DashBoardCatFIVEList, mLocalVedicDB, WishLocalArrFOrStartView);
        recyclecatFive.setAdapter(mDashBoardCatFIVEAdapter);
        mDashBoardCatFIVEAdapter.setOnItemClickListener(new DashBoardNewProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {

                DashBoardNewProductData mDashBoardNewProductData = DashBoardCatFIVEList.get(position);
                if (which == 0) {
                    Cursor c = mLocalVedicDB.ShowTableWishList();
                    tvitemCounter.setText(c.getCount() + "");

                } else {
                    Intent it = new Intent(getActivity(), ProductDetailActivity.class);
                    it.putExtra("subCatId", mDashBoardNewProductData.getProductID());
                    it.putExtra("page", "main");
                    startActivity(it);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                }
            }
        });


        final LinearLayoutManager mLayoutManagerCatSix = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclecatSix.setLayoutManager(mLayoutManagerCatSix);
        recyclecatSix.setHasFixedSize(true);
        mDashBoardCatSIXAdapter = new DashBoardNewProductAdapter(getActivity(), DashBoardCatSIXList, mLocalVedicDB, WishLocalArrFOrStartView);
        recyclecatSix.setAdapter(mDashBoardCatSIXAdapter);
        mDashBoardCatSIXAdapter.setOnItemClickListener(new DashBoardNewProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {

                DashBoardNewProductData mDashBoardNewProductData = DashBoardCatSIXList.get(position);
                if (which == 0) {
                    Cursor c = mLocalVedicDB.ShowTableWishList();
                    tvitemCounter.setText(c.getCount() + "");

                } else {
                    Intent it = new Intent(getActivity(), ProductDetailActivity.class);
                    it.putExtra("subCatId", mDashBoardNewProductData.getProductID());
                    it.putExtra("page", "main");
                    startActivity(it);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                }
            }
        });

        final LinearLayoutManager mLayoutManagerSecond = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycleBestProducts.setLayoutManager(mLayoutManagerSecond);
        recycleBestProducts.setHasFixedSize(true);
        mDashBoardBestSellingAdapter = new DashBoardBestSellingAdapter(getActivity(), DashBoardBestSellingList, mLocalVedicDB, WishLocalArrFOrStartView);
        recycleBestProducts.setAdapter(mDashBoardBestSellingAdapter);
        mDashBoardBestSellingAdapter.setOnItemClickListener(new DashBoardBestSellingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {

                DashBoardBestSellingData mDashBoardBestSellingData = DashBoardBestSellingList.get(position);
                if (which == 0) {
                    Cursor c = mLocalVedicDB.ShowTableWishList();
                    tvitemCounter.setText(c.getCount() + "");
                } else {
                    Intent it = new Intent(getActivity(), ProductDetailActivity.class);
                    it.putExtra("subCatId", mDashBoardBestSellingData.getProductID());
                    it.putExtra("page", "main");
                    startActivity(it);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                }

            }
        });


        final LinearLayoutManager mLayoutManagerBrand = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycleOurBrands.setLayoutManager(mLayoutManagerBrand);
        recycleOurBrands.setHasFixedSize(true);
        mDashBoardBrandAdapter = new DashBoardBrandAdapter(getActivity(), DashBoardBrandList);
        recycleOurBrands.setAdapter(mDashBoardBrandAdapter);
        mDashBoardBrandAdapter.setOnItemClickListener(new DashBoardBrandAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {

                DashBoardBrandData mDashBoardBrandData = DashBoardBrandList.get(position);
                Intent intent = new Intent(getActivity(), ProductListActivityBrand.class);
                intent.putExtra("catID", mDashBoardBrandData.getBrandID());
                intent.putExtra("catName", mDashBoardBrandData.getName());
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });


        final LinearLayoutManager mLayoutManagerOffers = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycleOffers.setLayoutManager(mLayoutManagerOffers);
        recycleOffers.setHasFixedSize(true);
        mDashBoardOffersAdapter = new DashBoardOffersAdapter(getActivity(), DashBoardOffersList);
        recycleOffers.setAdapter(mDashBoardOffersAdapter);
        mDashBoardOffersAdapter.setOnItemClickListener(new DashBoardOffersAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view, int which) {

                DashBoardOffersData mDashBoardOffersData = DashBoardOffersList.get(position);
                if (mDashBoardOffersData.getSubcat().equalsIgnoreCase("Yes")) {

                    Intent intent = new Intent(getActivity(), CategoryExpandableListView.class);
                    intent.putExtra("catID", mDashBoardOffersData.getCatID());
                    intent.putExtra("catName", mDashBoardOffersData.getName());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                } else {
                    Intent intent = new Intent(getActivity(), ProductListActivity.class);
                    intent.putExtra("catID", mDashBoardOffersData.getCatID());
                    intent.putExtra("catName", mDashBoardOffersData.getName());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

            }
        });


        if (Utils.isNetworkAvailable(getActivity())) {
            loading = ProgressDialog.show(getActivity(), "", "Please wait...", false, false);
            GetDashBoardData mGetDashBoardData = new GetDashBoardData();
            mGetDashBoardData.execute();


        }

        btnFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(Utils.getOpenFacebookIntent(context));
                launchFacebook();
            }
        });

        btnRefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ReferActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        liCateGory.setOnClickListener(this);
        liSale.setOnClickListener(this);
        liWishList.setOnClickListener(this);
        liCart.setOnClickListener(this);
        liNotification.setOnClickListener(this);


        txtViewMoreCatOne.setOnClickListener(this);
        txtViewMoreCatTwo.setOnClickListener(this);
        txtViewMoreCatThree.setOnClickListener(this);
        txtViewMoreCatFour.setOnClickListener(this);
        txtViewMoreCatFive.setOnClickListener(this);
        txtViewMoreCatSix.setOnClickListener(this);

        searchtext.setOnClickListener(this);
        categoryCard.setOnClickListener(this);


        Cursor c = mLocalVedicDB.ShowTableWishList();
        tvitemCounter.setText(c.getCount() + "");
        Log.d("StrCartCounter", StrCartCounter);
        txtCartCounter.setText(StrCartCounter + "");
        return view;
    }

    public final void launchFacebook() {
        String YourPageURL = "https://www.facebook.com/n/?vedicfashion";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YourPageURL));
        startActivity(browserIntent);
    }

    public void FetchXmlID(View view) {

        mDemoSlider = (SliderLayout) view.findViewById(R.id.mDemoSliderHome);

        recycleNewProduct = (RecyclerView) view.findViewById(R.id.recycleNewProduct);
        recycleOffers = (RecyclerView) view.findViewById(R.id.recycleOffers);
        recycleBestProducts = (RecyclerView) view.findViewById(R.id.recycleBestProducts);
        recycleOurBrands = (RecyclerView) view.findViewById(R.id.recycleOurBrands);


        recyclecatOne = (RecyclerView) view.findViewById(R.id.recyclecatOne);
        recyclecatTwo = (RecyclerView) view.findViewById(R.id.recyclecatTwo);
        recyclecatThree = (RecyclerView) view.findViewById(R.id.recyclecatThree);
        recyclecatFour = (RecyclerView) view.findViewById(R.id.recyclecatFour);
        recyclecatFive = (RecyclerView) view.findViewById(R.id.recyclecatFive);
        recyclecatSix = (RecyclerView) view.findViewById(R.id.recyclecatSix);


        txtCatOne = (TextView) view.findViewById(R.id.txtCatOne);
        txtCatTwo = (TextView) view.findViewById(R.id.txtCatTwo);
        txtCatThree = (TextView) view.findViewById(R.id.txtCatThree);
        txtCatFour = (TextView) view.findViewById(R.id.txtCatFour);
        txtCatFive = (TextView) view.findViewById(R.id.txtCatFive);
        txtCatSix = (TextView) view.findViewById(R.id.txtCatSix);

        txtViewMoreCatOne = (TextView) view.findViewById(R.id.txtViewMoreCatOne);
        txtViewMoreCatTwo = (TextView) view.findViewById(R.id.txtViewMoreCatTwo);
        txtViewMoreCatThree = (TextView) view.findViewById(R.id.txtViewMoreCatThree);
        txtViewMoreCatFour = (TextView) view.findViewById(R.id.txtViewMoreCatFour);
        txtViewMoreCatFive = (TextView) view.findViewById(R.id.txtViewMoreCatFive);
        txtViewMoreCatSix = (TextView) view.findViewById(R.id.txtViewMoreCatSix);


        linearCatOne = (RelativeLayout) view.findViewById(R.id.linearCatOne);
        linearCatTwo = (RelativeLayout) view.findViewById(R.id.linearCatTwo);
        linearCatThree = (RelativeLayout) view.findViewById(R.id.linearCatThree);
        linearCatFour = (RelativeLayout) view.findViewById(R.id.linearCatFour);
        linearCatFive = (RelativeLayout) view.findViewById(R.id.linearCatFive);
        linearCatSix = (RelativeLayout) view.findViewById(R.id.linearCatSix);


        txtNewArrival = (TextView) view.findViewById(R.id.txtNewArrival);
        txtBestSelling = (TextView) view.findViewById(R.id.txtBestSelling);
        txtTopBrands = (TextView) view.findViewById(R.id.txtTopBrands);


        liCateGory = (LinearLayout) view.findViewById(R.id.liCateGory);
        liSale = (LinearLayout) view.findViewById(R.id.liSale);
        liWishList = (LinearLayout) view.findViewById(R.id.liWishList);
        liCart = (LinearLayout) view.findViewById(R.id.liCart);
        liNotification = (LinearLayout) view.findViewById(R.id.liNotification);

        tvitemCounter = (TextView) view.findViewById(R.id.tvitemCounter);
        txtCartCounter = (TextView) view.findViewById(R.id.txtCartCounter);

        searchtext = (TextView) view.findViewById(R.id.searchtext);
        categoryCard = (CardView) view.findViewById(R.id.categoryCard);

        btnFB = (Button) view.findViewById(R.id.btnFB);
        btnRefer = (Button) view.findViewById(R.id.btnRefer);
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
                Intent intent = new Intent(getActivity(), NewCategoryActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.txtViewMoreCatOne:
                Intent intentsd = new Intent(getActivity(), ProductListActivity.class);
                intentsd.putExtra("catID", StrCatID1);
                intentsd.putExtra("catName", StrCatNAME1);
                startActivity(intentsd);
                break;
            case R.id.txtViewMoreCatTwo:
                Intent intentsdxx = new Intent(getActivity(), ProductListActivity.class);
                intentsdxx.putExtra("catID", StrCatID2);
                intentsdxx.putExtra("catName", StrCatNAME2);
                startActivity(intentsdxx);
                break;
            case R.id.txtViewMoreCatThree:
                Intent intentssdd = new Intent(getActivity(), ProductListActivity.class);
                intentssdd.putExtra("catID", StrCatID3);
                intentssdd.putExtra("catName", StrCatNAME3);
                startActivity(intentssdd);
                break;
            case R.id.txtViewMoreCatFour:
                Intent intentssddaa = new Intent(getActivity(), ProductListActivity.class);
                intentssddaa.putExtra("catID", StrCatID4);
                intentssddaa.putExtra("catName", StrCatNAME4);
                startActivity(intentssddaa);
                break;
            case R.id.txtViewMoreCatFive:
                Intent intentfive = new Intent(getActivity(), ProductListActivity.class);
                intentfive.putExtra("catID", StrCatID5);
                intentfive.putExtra("catName", StrCatNAME5);
                startActivity(intentfive);
                break;
            case R.id.txtViewMoreCatSix:
                Intent intentsix = new Intent(getActivity(), ProductListActivity.class);
                intentsix.putExtra("catID", StrCatID6);
                intentsix.putExtra("catName", StrCatNAME6);
                startActivity(intentsix);
                break;
            case R.id.liWishList:
                if (StrUserId.isEmpty()) {
                    Utils.loginDialog(getActivity(), "You need to be signed in to create and save your own wishlist.");
                } else {
                    Intent intenscdst = new Intent(getActivity(), MyWishList.class);
                    intenscdst.putExtra("WishPage", "main");
                    startActivity(intenscdst);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                }
                break;
            case R.id.liCart:
                Intent intdswdent = new Intent(getActivity(), MyCart.class);
                intdswdent.putExtra("CartPage", "main");
                startActivity(intdswdent);
                getActivity().overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.liNotification:
                Intent offer = new Intent(getActivity(), Updates.class);
                offer.putExtra("PageTypeForPush", "NotPush");
                startActivity(offer);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.liSale:
                Intent sale = new Intent(getActivity(), SaleActivity.class);
                sale.putExtra("PageTypeForSale", "NotPush");
                startActivity(sale);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;

            case R.id.searchtext:

                Intent it = new Intent(getActivity(), SearchActivity.class);
                startActivity(it);
                break;

            case R.id.categoryCard:

                Intent itn = new Intent(getActivity(), NewCategoryActivity.class);
                startActivity(itn);
                break;


        }
    }


    private class GetDashBoardData extends AsyncTask<Void, Void,
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


                Api_Model curators = methods.GetDashBoardData("dashboard", StrUserId, StrUserBeutopianCommitteToken, StrUserBeutopianCommitteDeviceID);

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
                Log.i("Curator", curators.message.toString());
                if (curators.msgcode.toString().equals("0")) {

                    Log.e("super_wallet_info", "super_wallet_info" + curators.super_wallet_info);
                    txtNewArrival.setText("New Arrivals");
                    txtBestSelling.setText("Best Selling");
                    txtTopBrands.setText("Top Brands");
                    editorSuperInfo.putString("SuperInfoMsg", curators.super_wallet_info);
                    editorSuperInfo.commit();

                    //Baneers
                    for (Api_Model.banners dataset : curators.banners) {
                        bannercontent.add(dataset.image);

                        DashBoardBannersData
                                mDashBoardBannersData;
                        mDashBoardBannersData = new DashBoardBannersData(dataset.sr, dataset.image, dataset.catID,
                                dataset.subcat, dataset.name);
                        DashBoardBannersList.add(mDashBoardBannersData);

                    }
                    SetBanner(bannercontent);


                    //New Product List
                    for (Api_Model.new_product dataset : curators.new_product) {
                        DashBoardNewProductData
                                mDashBoardNewProductData;
                        mDashBoardNewProductData = new DashBoardNewProductData(dataset.productID, dataset.name, dataset.image,
                                dataset.discount, dataset.price, dataset.mrp, dataset.sold_out);
                        DashBoardNewProductList.add(mDashBoardNewProductData);

                    }
                    mDashBoardNewProductAdapter.notifyDataSetChanged();


                    //Best Selling

                    for (Api_Model.best_product dataset : curators.best_product) {
                        DashBoardBestSellingData
                                mDashBoardBestSellingData;
                        mDashBoardBestSellingData = new DashBoardBestSellingData(dataset.productID, dataset.name, dataset.image,
                                dataset.discount, dataset.price, dataset.mrp, dataset.sold_out);
                        DashBoardBestSellingList.add(mDashBoardBestSellingData);

                    }
                    mDashBoardBestSellingAdapter.notifyDataSetChanged();

                    //Offers
                    if (curators.offer_show.equalsIgnoreCase("Yes")) {
                        recycleOffers.setVisibility(View.VISIBLE);
                        for (Api_Model.offers dataset : curators.offers) {
                            DashBoardOffersData
                                    mDashBoardOffersData;
                            mDashBoardOffersData = new DashBoardOffersData(dataset.sr, dataset.image, dataset.catID,
                                    dataset.subcat, dataset.name);
                            DashBoardOffersList.add(mDashBoardOffersData);

                        }
                    } else {
                        recycleOffers.setVisibility(View.GONE);
                    }


                    mDashBoardOffersAdapter.notifyDataSetChanged();


                    String versionCode = BuildConfig.VERSION_NAME;
                    //  Log.i("Versioncode", versionCode);
                    if (Float.parseFloat(curators.version.toString()) > Float.parseFloat(versionCode)) {

                        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                        alertDialogBuilder.setMessage(curators.msg.toString());
                        alertDialogBuilder.setCancelable(false);

                        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                            }
                        });
                        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                getActivity().finish();
                                getActivity().startActivity(new Intent(getActivity(), SplashScreen.class));
                            }
                        });
                        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                getActivity().finish();
                                getActivity().startActivity(new Intent(getActivity(), SplashScreen.class));

                            }
                        });
                    }
                } else {
                    Utils.showToastShort(curators.message.toString(), getActivity());


                }


            }

            LoadBootomOfArrayData mLoadBootomOfArrayData = new LoadBootomOfArrayData();
            mLoadBootomOfArrayData.execute();
            //please share your experince with sare us rate us


        }


    }

    private void ShowRetryDialog() {
        try {
            final Dialog dialog = new Dialog(getActivity());
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

                    loading = ProgressDialog.show(getActivity(), "", "Please wait...", false, false);
                    GetDashBoardData mGetDashBoardData = new GetDashBoardData();
                    mGetDashBoardData.execute();

                    dialog.dismiss();
                }
            });


            dialog.show();
        } catch (Exception e) {

        }
    }

    public void SetBanner(ArrayList bannercontent) {
        HashMap<String, String> file_maps = new HashMap<String, String>();


        for (int i = 0; i < bannercontent.size(); i++) {
            DefaultSliderView textSliderView = new DefaultSliderView(getActivity());
            // initialize a SliderLayout
            final int finalI = i;
            textSliderView

                    .image(bannercontent.get(i).toString())
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(BaseSliderView slider) {
                            DashBoardBannersData mDashBoardBannersData = DashBoardBannersList.get(finalI);
                            //  Utils.showToastShort(mDashBoardBannersData.getSubcat() + " " + finalI, getActivity());
                            if (mDashBoardBannersData.getSubcat().equalsIgnoreCase("Yes")) {

                                Intent intent = new Intent(getActivity(), CategoryExpandableListView.class);
                                intent.putExtra("catID", mDashBoardBannersData.getCatID());
                                intent.putExtra("catName", mDashBoardBannersData.getName());
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                            } else {
                                Intent intent = new Intent(getActivity(), ProductListActivity.class);
                                intent.putExtra("catID", mDashBoardBannersData.getCatID());
                                intent.putExtra("catName", mDashBoardBannersData.getName());
                                startActivity(intent);
                                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                            }


                        }
                    });


            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addSlider(textSliderView);
        }


    }


    private class LoadBootomOfArrayData extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            //  loading = ProgressDialog.show(getActivity(), "", "Please wait...", false, false);
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {

                IApiMethods methods = restAdapter.create(IApiMethods.class);
                Api_Model curators = methods.GetBootomData("dashboard_bottom", StrUserId);

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
                LoadBootomOfArrayData mLoadBootomOfArrayData = new LoadBootomOfArrayData();
                mLoadBootomOfArrayData.execute();
                Log.i("hiiiiii0", "hiiiiii1");

            } else {
                Log.i("hiiiiii1", "hiiiiii1");

                if (curators.msgcode.toString().equals("0")) {
                    try {
                        //Brands
                        Log.i("hiiiiii2", "hiiiiii1");
                        for (Api_Model.brands dataset : curators.brands) {
                            DashBoardBrandData
                                    mDashBoardBrandData;
                            mDashBoardBrandData = new DashBoardBrandData(dataset.sr, dataset.image, dataset.brandID,
                                    dataset.name);
                            DashBoardBrandList.add(mDashBoardBrandData);

                        }
                        mDashBoardBrandAdapter.notifyDataSetChanged();

                        Log.i("hiiiiii3", "hiiiiii1");

                        if (!curators.cat1.equalsIgnoreCase("")) {
                            Log.i("hiiiiii4", "hiiiiii1");

                            linearCatOne.setVisibility(View.VISIBLE);
                            txtCatOne.setText(curators.cat1);

                            StrCatID1 = curators.cat1_id;
                            StrCatNAME1 = curators.cat1;
                            for (Api_Model.cat1_product dataset : curators.cat1_product) {
                                DashBoardNewProductData
                                        mDashBoardNewProductData;
                                mDashBoardNewProductData = new DashBoardNewProductData(dataset.productID, dataset.name, dataset.image,
                                        dataset.discount, dataset.price, dataset.mrp, dataset.sold_out);
                                DashBoardCatONEList.add(mDashBoardNewProductData);

                            }
                            mDashBoardCatONEAdapter.notifyDataSetChanged();


                        } else {
                            linearCatOne.setVisibility(View.GONE);
                        }


                        if (!curators.cat2.equalsIgnoreCase("")) {
                            linearCatTwo.setVisibility(View.VISIBLE);
                            txtCatTwo.setText(curators.cat2);

                            StrCatID2 = curators.cat2_id;
                            StrCatNAME2 = curators.cat2;

                            for (Api_Model.cat2_product dataset : curators.cat2_product) {
                                DashBoardNewProductData
                                        mDashBoardNewProductData;
                                mDashBoardNewProductData = new DashBoardNewProductData(dataset.productID, dataset.name, dataset.image,
                                        dataset.discount, dataset.price, dataset.mrp, dataset.sold_out);
                                DashBoardCatTWOList.add(mDashBoardNewProductData);

                            }
                            mDashBoardCatTWOAdapter.notifyDataSetChanged();

                        } else {
                            linearCatTwo.setVisibility(View.GONE);
                        }


                        if (!curators.cat3.equalsIgnoreCase("")) {
                            linearCatThree.setVisibility(View.VISIBLE);
                            txtCatThree.setText(curators.cat3);

                            StrCatID3 = curators.cat3_id;
                            StrCatNAME3 = curators.cat3;
                            for (Api_Model.cat3_product dataset : curators.cat3_product) {
                                DashBoardNewProductData
                                        mDashBoardNewProductData;
                                mDashBoardNewProductData = new DashBoardNewProductData(dataset.productID, dataset.name, dataset.image,
                                        dataset.discount, dataset.price, dataset.mrp, dataset.sold_out);
                                DashBoardCatTHREEList.add(mDashBoardNewProductData);

                            }
                            mDashBoardCatTHREEAdapter.notifyDataSetChanged();


                        } else {
                            linearCatThree.setVisibility(View.GONE);
                        }


                        if (!curators.cat4.equalsIgnoreCase("")) {
                            linearCatFour.setVisibility(View.VISIBLE);
                            txtCatFour.setText(curators.cat4);

                            StrCatID4 = curators.cat4_id;
                            StrCatNAME4 = curators.cat4;
                            for (Api_Model.cat4_product dataset : curators.cat4_product) {
                                DashBoardNewProductData
                                        mDashBoardNewProductData;
                                mDashBoardNewProductData = new DashBoardNewProductData(dataset.productID, dataset.name, dataset.image,
                                        dataset.discount, dataset.price, dataset.mrp, dataset.sold_out);
                                DashBoardCatFOURList.add(mDashBoardNewProductData);

                            }
                            Log.e("DashBoardCatFOURList", DashBoardCatFOURList.size() + "   99");
                            mDashBoardCatFOURAdapter.notifyDataSetChanged();


                        } else {
                            linearCatFour.setVisibility(View.GONE);
                        }

                        if (!curators.cat5.equalsIgnoreCase("")) {
                            linearCatFive.setVisibility(View.VISIBLE);
                            txtCatFive.setText(curators.cat5);

                            StrCatID5 = curators.cat5_id;
                            StrCatNAME5 = curators.cat5;
                            Log.e("StrCatID5", "StrCatID5" + StrCatID5);
                            for (Api_Model.cat5_product dataset : curators.cat5_product) {
                                DashBoardNewProductData
                                        mDashBoardNewProductData;
                                mDashBoardNewProductData = new DashBoardNewProductData(dataset.productID, dataset.name, dataset.image,
                                        dataset.discount, dataset.price, dataset.mrp, dataset.sold_out);
                                DashBoardCatFIVEList.add(mDashBoardNewProductData);

                            }
                            Log.e("DashBoardCatFIVEList", DashBoardCatFIVEList.size() + "   99");
                            mDashBoardCatFIVEAdapter.notifyDataSetChanged();


                        } else {
                            linearCatFive.setVisibility(View.GONE);
                        }

                        if (!curators.cat6.equalsIgnoreCase("")) {
                            linearCatSix.setVisibility(View.VISIBLE);
                            txtCatSix.setText(curators.cat6);

                            StrCatID6 = curators.cat6_id;
                            StrCatNAME6 = curators.cat6;
                            for (Api_Model.cat6_product dataset : curators.cat6_product) {
                                DashBoardNewProductData
                                        mDashBoardNewProductData;
                                mDashBoardNewProductData = new DashBoardNewProductData(dataset.productID, dataset.name, dataset.image,
                                        dataset.discount, dataset.price, dataset.mrp, dataset.sold_out);
                                DashBoardCatSIXList.add(mDashBoardNewProductData);

                            }
                            Log.e("DashBoardCatFIVEList", DashBoardCatSIXList.size() + "   99");
                            mDashBoardCatSIXAdapter.notifyDataSetChanged();


                        } else {
                            linearCatSix.setVisibility(View.GONE);
                        }


                    } catch (Exception e) {


                    }

                }
            }


            loadSearchAsync mloadSearchAsync = new loadSearchAsync();
            mloadSearchAsync.execute();
        }
    }


    private class loadSearchAsync extends AsyncTask<Void, Void,
            Api_Model> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            //  loading = ProgressDialog.show(getActivity(), "", "Please wait...", false, false);
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint(AppConstant.API_URL)
                    .build();
        }

        @Override
        protected Api_Model doInBackground(Void... params) {
            try {

                IApiMethods methods = restAdapter.create(IApiMethods.class);
                Api_Model curators = methods.GetSearchDataApi("search");

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
                if (curators.msgcode.equals("0")) {
                    try {
                        searchlist.clear();
                        for (Api_Model.search_list dataset : curators.search_list) {

                            SearchModel myOrderModel;
                            myOrderModel = new SearchModel(dataset.productID, dataset.name, dataset.type);

                            searchlist.add(myOrderModel);

                        }


                    } catch (Exception e) {


                    }

                } else {


                }


            }


        }


    }


}