package adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.vedicfashions.shopping.ChangeAddress;
import com.vedicfashions.shopping.VedicFashions;
import com.vedicfashions.shopping.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import models.MyAddressdData;

/**
 * Created by welcome on 25-10-2016.
 */
public class ChangeAddressAdapter extends RecyclerView.Adapter<ChangeAddressAdapter.ViewHolder> {

    private List<MyAddressdData> items = new ArrayList<>();

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
        RadioButton rdButton;
        TextView txtName, txtAdsdrerss, txthone;
        Button btnEdit;
        LinearLayout asmak;

        public ViewHolder(View rowView) {
            super(rowView);
            txtName = (TextView) rowView.findViewById(R.id.txtName);
            txtAdsdrerss = (TextView) rowView.findViewById(R.id.txtAdsdrerss);
            txthone = (TextView) rowView.findViewById(R.id.txthone);
            rdButton = (RadioButton) rowView.findViewById(R.id.rdButton);
            btnEdit = (Button) rowView.findViewById(R.id.btnEdit);
            asmak = (LinearLayout) rowView.findViewById(R.id.asmak);

        }

    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChangeAddressAdapter(Context ctx, List<MyAddressdData> items) {
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_changeaddress, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.setIsRecyclable(true);
        final MyAddressdData p = items.get(position);

        holder.txtName.setText(p.getName());
        holder.txtAdsdrerss.setText(p.getAddress1() + ", " + p.getAddress2() + "\n" + p.getArea() + ", " + p.getCity() + ", " + p.getState() + " " + p.getPincode());
        holder.txthone.setText(p.getPhone());


        if (ChangeAddress.ThisRadioCount == position) {

            holder.rdButton.setChecked(true);
            holder.btnEdit.setVisibility(View.VISIBLE);
        } else {

            holder.rdButton.setChecked(false);
            holder.btnEdit.setVisibility(View.GONE);
        }

        holder.asmak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.rdButton.setChecked(true);
                mOnItemClickListener.onItemClick(position, view, 0);
                holder.btnEdit.setVisibility(View.VISIBLE);
            }
        });

        holder.rdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.rdButton.setChecked(true);
                mOnItemClickListener.onItemClick(position, v, 0);
                holder.btnEdit.setVisibility(View.VISIBLE);
            }
        });
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemClickListener.onItemClick(position, view, 1);

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
