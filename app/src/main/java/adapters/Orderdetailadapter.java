package adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.vedicfashions.shopping.ProductDetailActivity;
import com.vedicfashions.shopping.ProductListActivity;
import com.vedicfashions.shopping.R;
import com.vedicfashions.shopping.ReturnExchActivity;

import java.util.ArrayList;

import models.OrderdetailModel;


public class Orderdetailadapter extends RecyclerView.Adapter<Orderdetailadapter.DataObjectHolder> {

    private ArrayList<OrderdetailModel> bean;
    private Activity context;
    private LayoutInflater inflater;
    String orderDetailId;

    public Orderdetailadapter(Activity context, ArrayList<OrderdetailModel> bean, String orderDetailId) {
        this.context = context;
        this.bean = bean;
        this.inflater = (LayoutInflater.from(context));
        this.orderDetailId = orderDetailId;
    }


    static class DataObjectHolder extends RecyclerView.ViewHolder {
        ImageView ivPImg;
        TextView tvName, tvSize, tvPrice, tvReturn, tvExchange, tvMassage;
        LinearLayout btnLayout, llMain;

        DataObjectHolder(View itemView) {
            super(itemView);

            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvSize = (TextView) itemView.findViewById(R.id.tvSize);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvReturn = (TextView) itemView.findViewById(R.id.tvReturn);
            tvExchange = (TextView) itemView.findViewById(R.id.tvExchange);
            tvMassage = (TextView) itemView.findViewById(R.id.tvMassage);
            btnLayout = (LinearLayout) itemView.findViewById(R.id.btnLayout);
            llMain = (LinearLayout) itemView.findViewById(R.id.llMain);
            ivPImg = (ImageView) itemView.findViewById(R.id.ivPImg);
        }
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_orderdetail, parent, false);
        return new DataObjectHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final OrderdetailModel mOrderdetailModel = bean.get(position);

        holder.tvName.setText(mOrderdetailModel.getName());
        holder.tvSize.setText("Size: " + String.valueOf(mOrderdetailModel.getColor()) + " | Qty: " + String.valueOf(mOrderdetailModel.getQuantity()));
        holder.tvMassage.setText(String.valueOf(mOrderdetailModel.getExchange_msg()));
        holder.tvPrice.setText(String.valueOf(context.getResources().getString(R.string.rs) + " " + mOrderdetailModel.getPrice()));

        Glide.with(context)
                .load(mOrderdetailModel.getImage())
                .asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.default_gray_image)
                .into(holder.ivPImg);

        if (mOrderdetailModel.getExchange().equals("No") && mOrderdetailModel.getReturn_btn().equals("No")) {
            holder.btnLayout.setVisibility(View.GONE);
        }

        if (mOrderdetailModel.getExchange().equals("No")) {
            holder.tvExchange.setVisibility(View.GONE);
        }

        if (mOrderdetailModel.getReturn_btn().equals("No")) {
            holder.tvReturn.setVisibility(View.GONE);
        }

        if (mOrderdetailModel.getExchange_msg().equals("")) {
            holder.tvMassage.setVisibility(View.GONE);
        }

        if (mOrderdetailModel.getExchange_msg().contains("Exchange")) {
            holder.tvMassage.setTextColor(Color.parseColor("#1bac94"));
        }

        if (mOrderdetailModel.getExchange_msg().contains("Return")) {
            holder.tvMassage.setTextColor(Color.RED);
        }

        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(context, ProductDetailActivity.class);
                it.putExtra("subCatId", mOrderdetailModel.getProductID());
                context.startActivity(it);
                context.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
            }
        });

        holder.tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(context, ReturnExchActivity.class);
                it.putExtra("subCatId", mOrderdetailModel.getProductID());
                it.putExtra("page", "return");
                it.putExtra("orderId", orderDetailId);
                it.putExtra("size", String.valueOf(mOrderdetailModel.getColor()));
                it.putExtra("SR", mOrderdetailModel.getSR());
                context.startActivity(it);
                context.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                context.finish();
            }
        });

        holder.tvExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(context, ReturnExchActivity.class);
                it.putExtra("subCatId", mOrderdetailModel.getProductID());
                it.putExtra("page", "exchange");
                it.putExtra("orderId", orderDetailId);
                it.putExtra("size", String.valueOf(mOrderdetailModel.getColor()));
                it.putExtra("SR", mOrderdetailModel.getSR());
                context.startActivity(it);
                context.overridePendingTransition(R.anim.slide_in_right,
                        R.anim.slide_out_left);
                context.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bean.size();
    }

}