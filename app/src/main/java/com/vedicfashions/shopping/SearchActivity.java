package com.vedicfashions.shopping;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import adapters.SearchAdapter;
import dbhelper.LocalVedicDB;
import fragments.Dashboard;
import models.SearchModel;
import utils.Tools;
import utils.Utils;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private EditText searchresult;
    LinearLayout liCateGory, liSale, liWishList, liCart, liNotification;
    ArrayList<SearchModel> listuserresults = new ArrayList<SearchModel>();
    SharedPreferences mprefs;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    LinearLayout lyt_not_found, liserach;
    LinearLayoutManager mLayoutManager;
    ProgressBar progressBar;
    SharedPreferences prefCartCounter;
    SharedPreferences.Editor editorCartCounter;
    LocalVedicDB mLocalVedicDB;
    TextView tvitemCounter, txtCartCounter;
    SharedPreferences prefLogin;
    SharedPreferences.Editor editorLogin;
    String StrUserId, StrCartCounter;
    ImageView imgSend;
    String StrEdtTEext = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        context = this;

        prefLogin = getApplicationContext().getSharedPreferences("LogInPrefVedic", 0); // 0 - for private mode
        editorLogin = prefLogin.edit();
        StrUserId = prefLogin.getString("userID", null);

        prefCartCounter = getApplicationContext().getSharedPreferences("DataCartCounter", 0); // 0 - for private mode
        editorCartCounter = prefCartCounter.edit();
        StrCartCounter = prefCartCounter.getString("CartCounter", "0");


        String LocalDbUserSocityIdName = StrUserId + "Vedic.db";
        String FinalLocalDBName = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/" + LocalDbUserSocityIdName;
        mLocalVedicDB = new LocalVedicDB(SearchActivity.this, FinalLocalDBName);
        mLocalVedicDB.OpenDB();
        initComp();

        Tools.systemBarLolipop(this);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        try {
            if (Dashboard.searchlist.size() == 0) {
                lyt_not_found.setVisibility(View.GONE);
            } else {
                lyt_not_found.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        liCateGory.setOnClickListener(this);
        liSale.setOnClickListener(this);
        liWishList.setOnClickListener(this);
        liCart.setOnClickListener(this);
        liNotification.setOnClickListener(this);
        Tools.systemBarLolipop(this);

        Cursor c = mLocalVedicDB.ShowTableWishList();
        tvitemCounter.setText(c.getCount() + "");
        txtCartCounter.setText(StrCartCounter + "");

    }

    public void initComp() {
        searchresult = (EditText) findViewById(R.id.searchresult);
        imgSend = (ImageView) findViewById(R.id.imgSend);
        requestFocus(searchresult);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewSearch);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        lyt_not_found = (LinearLayout) findViewById(R.id.lyt_not_found);
        liserach = (LinearLayout) findViewById(R.id.liserach);


        tvitemCounter = (TextView) findViewById(R.id.tvitemCounter);
        liCateGory = (LinearLayout) findViewById(R.id.liCateGory);
        liSale = (LinearLayout) findViewById(R.id.liSale);
        liWishList = (LinearLayout) findViewById(R.id.liWishList);
        liCart = (LinearLayout) findViewById(R.id.liCart);
        liNotification = (LinearLayout) findViewById(R.id.liNotification);
        txtCartCounter = (TextView) findViewById(R.id.txtCartCounter);
        searchresult.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listuserresults.clear();
                listuserresults = new ArrayList<SearchModel>();
                try {
                    for (SearchModel c : Dashboard.searchlist) {
                        if (c.getName().toLowerCase().contains(s.toString().toLowerCase())) {
                            listuserresults.add(c);
                        }

                    }


                    searchAdapter = new SearchAdapter(context, listuserresults);
                    recyclerView.setAdapter(searchAdapter);


                    if (searchresult.length() == 0) {
                        listuserresults.clear();
                    }


                } catch (NullPointerException ne) {
                    ne.getMessage();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        searchresult.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    StrEdtTEext = searchresult.getText().toString();
                    Utils.hideKeyboard(SearchActivity.this);
                    Intent it = new Intent(SearchActivity.this, ProductListFromSearch.class);
                    it.putExtra("SerchText", StrEdtTEext);
                    startActivity(it);
                    overridePendingTransition(R.anim.slide_in_right,
                            R.anim.slide_out_left);
                    return true;
                }
                return false;
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Utils.hideKeyboard(SearchActivity.this);

                if (listuserresults.get(position).getType().equalsIgnoreCase("tag")) {

                    Intent it = new Intent(SearchActivity.this, ProductListFromSearch.class);
                    it.putExtra("SerchText", listuserresults.get(position).getName().trim());
                    startActivity(it);

                } else {
                    Intent it = new Intent(SearchActivity.this, ProductDetailActivity.class);
                    it.putExtra("subCatId", listuserresults.get(position).getProductID());
                    startActivity(it);
                }
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                //Toast.makeText(getApplicationContext(), catarraylist.get(position).getCatSubcat(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        liserach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrEdtTEext = searchresult.getText().toString();
                Utils.hideKeyboard(SearchActivity.this);
                Intent it = new Intent(SearchActivity.this, ProductListFromSearch.class);
                it.putExtra("SerchText", StrEdtTEext);
                startActivity(it);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.liCateGory:
                Intent intent = new Intent(SearchActivity.this, NewCategoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.liSale:
                break;
            case R.id.liWishList:
                finish();
                Intent inwsdtent = new Intent(SearchActivity.this, MyWishList.class);
                inwsdtent.putExtra("WishPage", "main");
                startActivity(inwsdtent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.liCart:
                Intent intxent = new Intent(SearchActivity.this, MyCart.class);
                startActivity(intxent);
                overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case R.id.liNotification:
                Intent offer = new Intent(SearchActivity.this, Updates.class);
                offer.putExtra("PageTypeForPush", "NotPush");
                startActivity(offer);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;


        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
