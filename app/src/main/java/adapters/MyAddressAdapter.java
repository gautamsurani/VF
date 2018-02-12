package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.vedicfashions.shopping.VedicFashions;
import com.vedicfashions.shopping.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import models.MyAddressdData;

/**
 * Created by welcome on 24-10-2016.
 */
public class MyAddressAdapter extends RecyclerView.Adapter<MyAddressAdapter.ViewHolder> {

    private List<MyAddressdData> items = new ArrayList<>();

    private Context ctx;
    VedicFashions application;
    private DisplayImageOptions options;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private OnMoreButtonClickListener onMoreButtonClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View view, int i);
    }


    public interface OnMoreButtonClickListener {
        void onItemClick(View view, MyAddressdData obj, int actionId);
    }

    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView txtName, txtMobileNumber, txtAddress;
        ImageView imgMore;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            txtMobileNumber = (TextView) rowView.findViewById(R.id.txtMobileNumber);
            txtAddress = (TextView) rowView.findViewById(R.id.txtAddress);
            imgMore = (ImageView) rowView.findViewById(R.id.imgMore);

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAddressAdapter(Context ctx, List<MyAddressdData> items) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_myaddress, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.setIsRecyclable(true);
        final MyAddressdData p = items.get(position);

        holder.txtName.setText(p.getName());
        holder.txtMobileNumber.setText(p.getPhone());
        holder.txtAddress.setText(p.getAddress1() + ", " + p.getAddress2() + "\n" + p.getArea() + ", " + p.getCity() + ", " + p.getState() + " -" + p.getPincode());
        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMoreButtonClick(view, p);


            }
        });


    }

    private void onMoreButtonClick(final View view, final MyAddressdData p) {
        PopupMenu popupMenu = new PopupMenu(ctx, view);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onMoreButtonClickListener.onItemClick(view, p, item.getItemId());
                return true;
            }
        });
        popupMenu.inflate(R.menu.menu_popup_address);
        popupMenu.show();
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
