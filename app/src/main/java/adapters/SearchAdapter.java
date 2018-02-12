package adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.vedicfashions.shopping.R;

import java.util.ArrayList;

import models.SearchModel;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    Context context;
    ArrayList<SearchModel> listData;
    LayoutInflater inflater;
    Typeface fonts1, fonts2;
    RatingBar ratingbar;


    public SearchAdapter(Context context, ArrayList<SearchModel> bean) {

        this.listData = bean;
        this.context = context;
        this.inflater = (LayoutInflater.from(context));
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        ImageView image;
        TextView productSearchtitle, productLISTSearchtitle;

        public ViewHolder(View v) {
            super(v);

            productSearchtitle = (TextView) v.findViewById(R.id.productSearchtitle);
            productLISTSearchtitle = (TextView) v.findViewById(R.id.productLISTSearchtitle);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_productlist, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final SearchModel bean = listData.get(position);


        if (bean.getType().equalsIgnoreCase("tag")){
            holder.productSearchtitle.setTypeface(holder.productSearchtitle.getTypeface(),Typeface.BOLD);
        }else {
           // holder.productSearchtitle.setTypeface(null,Typeface.NORMAL);
        }

        holder.productSearchtitle.setText(bean.getName());


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}







