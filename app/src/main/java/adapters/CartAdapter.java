package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.vedicfashions.shopping.R;
import com.vedicfashions.shopping.VedicFashions;

import java.util.ArrayList;
import java.util.List;

import models.CartData;

/**
 * Created by welcome on 14-10-2016.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartData> items = new ArrayList<>();

    private Context ctx;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private DisplayImageOptions options;
    private VedicFashions application;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view, int i);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView imgProductImage;
        TextView txtProductName, txtModelName, txtPrice, txtMrp, txtmoveWishList, txtmoveDelete;
        Spinner spinnerQuentity;
        private boolean isSpinnerTouched = false;
        public customclass.Font_RobotoLight reply_count;
        RelativeLayout rl_parent;

        public ViewHolder(View v) {
            super(v);
            imgProductImage = (ImageView) v.findViewById(R.id.imgProductImage);
            txtProductName = (TextView) v.findViewById(R.id.txtProductName);
            txtModelName = (TextView) v.findViewById(R.id.txtModelName);
            txtPrice = (TextView) v.findViewById(R.id.txtPrice);
            txtMrp = (TextView) v.findViewById(R.id.txtMrp);
            txtmoveWishList = (TextView) v.findViewById(R.id.txtmoveWishList);
            txtmoveDelete = (TextView) v.findViewById(R.id.txtmoveDelete);
            spinnerQuentity = (Spinner) v.findViewById(R.id.spinnerQuentity);
            rl_parent = (RelativeLayout) v.findViewById(R.id.rl_parent);


        }

    }

    public CartAdapter(Context ctx, List<CartData> items) {
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

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cart, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(true);

        final CartData mCartData = items.get(position);


        imageLoader.displayImage(mCartData.getImage(), holder.imgProductImage, options);

      /*  holder.txtProductName.setTypeface(EasyFonts.windSong(ctx));
        holder.txtModelName.setTypeface(EasyFonts.windSong(ctx));
        holder.txtPrice.setTypeface(EasyFonts.windSong(ctx));
        holder.txtMrp.setTypeface(EasyFonts.windSong(ctx));*/


        holder.txtProductName.setText(mCartData.getName());
        holder.txtModelName.setText(mCartData.getModel());
        holder.txtPrice.setText(ctx.getResources().getString(R.string.rs) + " " + mCartData.getPrice());
        holder.txtMrp.setText(ctx.getResources().getString(R.string.rs) + " " + mCartData.getMrp());

        if (mCartData.getMrp().equals("")) {
            holder.txtMrp.setVisibility(View.GONE);
        }

        holder.txtMrp.setPaintFlags(holder.txtMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        final ArrayList<String> ArrQuentityList = new ArrayList();

        for (int i = 1; i <= Integer.parseInt(mCartData.maxQty); i++) {
            ArrQuentityList.add("Qty : " + i);
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ctx, R.layout.spinner_item_size, ArrQuentityList);
        dataAdapter.setDropDownViewResource(R.layout.selectedlistitem);
        holder.spinnerQuentity.setAdapter(dataAdapter);

        //  holder.spinnerQuentity.setSelection(0,false);
        int Pos = ArrQuentityList.indexOf("Qty : " + mCartData.getQuantity());
        holder.spinnerQuentity.setSelection(Pos);

        holder.spinnerQuentity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                holder.isSpinnerTouched = true;
                return false;
            }
        });


        holder.spinnerQuentity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                if (holder.isSpinnerTouched) {
                    String StrThis = ArrQuentityList.get(position);
                    String[] Str = StrThis.split(":");
                    String s1 = Str[0];
                    String s2 = Str[1];
                    int Quentity = Integer.parseInt(s2.trim());
                    mCartData.setQuantity(Quentity + "");

                    Log.e("XXXXXXXXXX", "Cal This");
                    Log.e("Quentity", String.valueOf(Str));
                    Log.e("s1", s1);
                    Log.e("s2", s2.trim());

                    mOnItemClickListener.onItemClick(holder.getAdapterPosition(), selectedItemView, Quentity);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        setAnimation(holder.itemView, position);
        holder.txtmoveWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, view, 97);
                }
            }
        });
        holder.txtmoveDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, view, 98);
                }
            }
        });
        holder.rl_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(position, view, 99);
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