package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.vedicfashions.shopping.ProductDetailActivity;
import com.vedicfashions.shopping.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by welcome on 26-10-2016.
 */
public class ProductDetailSizeAdapter extends RecyclerView.Adapter<ProductDetailSizeAdapter.ViewHolder> {

    Typeface fonts1;
    private List<String> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view, int i);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtSize;
        LinearLayout ly_parent;


        public customclass.Font_RobotoLight reply_count;

        public ViewHolder(View v) {
            super(v);
            txtSize = (TextView) v.findViewById(R.id.txtSize);
            ly_parent = (LinearLayout) v.findViewById(R.id.ly_parent);


        }

    }

    public ProductDetailSizeAdapter(Context ctx, List<String> items) {
        this.ctx = ctx;
        this.items = items;


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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_proctdetailsize, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(true);


        holder.txtSize.setText(items.get(position));

        if (ProductDetailActivity.INTChangeForSIze == position) {
            holder.txtSize.setBackgroundResource(R.drawable.sizeround2);
            holder.txtSize.setPadding(30, 5, 30, 5);
            holder.txtSize.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.txtSize.setBackgroundResource(R.drawable.sizeround1);
            holder.txtSize.setPadding(30, 5, 30, 5);
            holder.txtSize.setTextColor(Color.parseColor("#585858"));
        }
        holder.ly_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, view, 0);
                }
            }
        });

    }

    private int lastPosition = -1;

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}