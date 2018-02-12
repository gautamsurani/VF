package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vedicfashions.shopping.VedicFashions;
import com.vedicfashions.shopping.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import models.MyOrderdData;

/**
 * Created by welcome on 24-10-2016.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {

    private List<MyOrderdData> items = new ArrayList<>();

    private Context ctx;
    VedicFashions application;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();

    public interface OnItemClickListener {
        void onItemClick(int position, View view, int i);
    }


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView txtOrderID, txtStatusValue, txtShipedDate, txtOrderAmopunt;
        LinearLayout lv_parent;

        public ViewHolder(View rowView) {
            super(rowView);
            txtOrderID = (TextView) rowView.findViewById(R.id.txtOrderID);
            txtStatusValue = (TextView) rowView.findViewById(R.id.txtStatusValue);
            txtShipedDate = (TextView) rowView.findViewById(R.id.txtShipedDate);
            txtOrderAmopunt = (TextView) rowView.findViewById(R.id.txtOrderAmopunt);
            lv_parent = (LinearLayout) rowView.findViewById(R.id.lv_parent);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyOrderAdapter(Context ctx, List<MyOrderdData> items) {
        this.ctx = ctx;
        this.items = items;
        application = (VedicFashions) ctx.getApplicationContext();
        options = getImageOptions();
        imageLoader = application.getImageLoader();
    }

    public DisplayImageOptions getImageOptions() {

        final DisplayImageOptions imageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true).showImageOnLoading(R.drawable.default_gray_image)
                .showImageForEmptyUri(R.drawable.default_gray_image).showImageOnFail(R.drawable.default_gray_image)
                .bitmapConfig(Bitmap.Config.RGB_565).build();

        return imageOptions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_myorder, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.setIsRecyclable(true);
        final MyOrderdData p = items.get(position);
        holder.txtOrderID.setText("ORDER  #" + p.getOrderID());
        holder.txtOrderAmopunt.setText(ctx.getResources().getString(R.string.rs) + " " + p.getAmount());
        holder.txtStatusValue.setText(p.getStatus());
        holder.txtShipedDate.setText(p.getDate());

        if (p.getStatus().equalsIgnoreCase("Canceled")) {
            Drawable img = ctx.getResources().getDrawable(R.drawable.oredr_cancel);
            holder.txtStatusValue.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.txtStatusValue.setText(p.getStatus());

        } else {
            Drawable img = ctx.getResources().getDrawable(R.drawable.oredr_success);
            holder.txtStatusValue.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
            holder.txtStatusValue.setText(p.getStatus());
        }

        holder.lv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, view, 1);
                }
            }
        });

    }


    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
