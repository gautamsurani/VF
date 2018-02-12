package adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.vedicfashions.shopping.R;
import com.vedicfashions.shopping.VedicFashions;

import java.util.ArrayList;
import java.util.List;

import dbhelper.LocalVedicDB;
import models.DashBoardNewProductData;
import utils.SmallBang;
import utils.Utils;

/*
 * Created by welcome on 24-10-2016.
 */
public class DashBoardNewProductAdapter extends RecyclerView.Adapter<DashBoardNewProductAdapter.ViewHolder> {

    Typeface fonts1;
    private List<DashBoardNewProductData> items = new ArrayList<>();

    private Context ctx;
    private com.nostra13.universalimageloader.core.ImageLoader imageLoader;
    private DisplayImageOptions options;
    private OnItemClickListener mOnItemClickListener;
    private LocalVedicDB mLocalVedicDB;
    private ArrayList<String> ArrForvIEW = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(int position, View view, int i);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imagFav, imgMainImg, imgSoldOut;
        TextView txtName, txtMrp1, txtMrp2, tvDiscount;
        LinearLayout lv_parent;
        SmallBang mSmallBang;
        public customclass.Font_RobotoLight reply_count;

        public ViewHolder(View v) {
            super(v);
            imagFav = (ImageView) v.findViewById(R.id.imagFav);
            imgMainImg = (ImageView) v.findViewById(R.id.imgMainImg);
            imgSoldOut = (ImageView) v.findViewById(R.id.imgSoldOut);
            txtName = (TextView) v.findViewById(R.id.txtName);
            txtMrp1 = (TextView) v.findViewById(R.id.txtMrp1);
            txtMrp2 = (TextView) v.findViewById(R.id.txtMrp2);
            tvDiscount = (TextView) v.findViewById(R.id.tvDiscount);
            lv_parent = (LinearLayout) v.findViewById(R.id.lv_parent);
        }
    }

    public DashBoardNewProductAdapter(Context ctx, List<DashBoardNewProductData> items, LocalVedicDB AdpDb, ArrayList<String> Arr) {
        this.ctx = ctx;
        this.items = items;
        this.mLocalVedicDB = AdpDb;
        VedicFashions application = (VedicFashions) ctx.getApplicationContext();
        options = getImageOptions();
        imageLoader = application.getImageLoader();
        ArrForvIEW = Arr;
    }

    public DisplayImageOptions getImageOptions() {

        return new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true)
                .considerExifParams(true).showImageOnLoading(R.drawable.default_gray_image)
                .showImageForEmptyUri(R.drawable.default_gray_image).showImageOnFail(R.drawable.default_gray_image)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_productlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.setIsRecyclable(true);
        final DashBoardNewProductData p = items.get(position);
        boolean baa = ArrForvIEW.contains(p.getProductID());
        if (baa) {
            holder.imagFav.setImageResource(R.mipmap.fav_selcted);
        } else {
            holder.imagFav.setImageResource(R.mipmap.fav_nonselcted);
        }

        if (p.getSold_out().equalsIgnoreCase("Yes")) {
            holder.imgSoldOut.setVisibility(View.VISIBLE);
        } else {
            holder.imgSoldOut.setVisibility(View.GONE);
        }

        if (!p.getDiscount().equals("")) {
            holder.tvDiscount.setVisibility(View.VISIBLE);
            holder.tvDiscount.setText(p.getDiscount());
        }

        imageLoader.displayImage(p.getImage(), holder.imgMainImg, options);

        holder.txtName.setText(p.getName());
        holder.txtMrp1.setText(ctx.getResources().getString(R.string.rs) + " " + p.getPrice());
        holder.txtMrp1.setTypeface(holder.txtMrp1.getTypeface(), Typeface.BOLD);


        if (p.getMrp().equals("")) {
            holder.txtMrp2.setVisibility(View.GONE);
        }

        holder.txtMrp2.setText(ctx.getResources().getString(R.string.rs) + " " + p.getMrp());

        holder.txtMrp2.setPaintFlags(holder.txtMrp2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        holder.mSmallBang = SmallBang.attach2Window((Activity) ctx);
        // setAnimation(holder.itemView, position);

        String StrUserId;

        holder.imagFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utils.getUserId(ctx).isEmpty()) {
                    Utils.loginDialog(ctx, "You need to be signed in to create and save your own wishlist.");
                } else {
                    if (mOnItemClickListener != null) {
                        ArrayList<String> WishLocalArr = new ArrayList<String>();

                        Cursor c = mLocalVedicDB.ShowTableWishList();
                        if (c.getCount() > 0) {
                            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                                WishLocalArr.add(c.getString(1));
                            }

                            boolean b = WishLocalArr.contains(p.getProductID());
                            if (b) {
                                holder.imagFav.setImageResource(R.mipmap.fav_nonselcted);
                                mLocalVedicDB.DeleteWishListByID(p.getProductID());
                            } else {
                                holder.imagFav.setImageResource(R.mipmap.fav_selcted);
                                holder.mSmallBang.bang(view);
                                mLocalVedicDB.InsertWishListData(p.getProductID(), p.getName(),
                                        p.getImage(), p.getPrice(), p.getMrp());
                            }
                        } else {
                            holder.imagFav.setImageResource(R.mipmap.fav_selcted);
                            holder.mSmallBang.bang(view);
                            mLocalVedicDB.InsertWishListData(p.getProductID(), p.getName(),
                                    p.getImage(), p.getPrice(), p.getMrp());
                        }
                        mOnItemClickListener.onItemClick(position, view, 0);
                    }
                }
            }
        });

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