package adapters;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.vedicfashions.shopping.R;
import java.util.ArrayList;
import models.BankDetailsModel;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.DataObjectHolder> {

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<BankDetailsModel> listData;
    private Context context;
    LayoutInflater inflater;

    public BankAdapter(Context context, ArrayList<BankDetailsModel> bean) {


        this.context = context;
        this.listData = bean;
        this.inflater = (LayoutInflater.from(context));
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView tvBankname, tvAccountnumber, tvBranch, tvIfsccode, tvbranchcode, tvAccounttype, tvbankaddress;

        public DataObjectHolder(View itemView) {
            super(itemView);
            tvBankname = (TextView) itemView.findViewById(R.id.tvBankname);
            tvAccountnumber = (TextView) itemView.findViewById(R.id.tvAccountnumber);
            tvBranch = (TextView) itemView.findViewById(R.id.tvBranch);
            tvIfsccode = (TextView) itemView.findViewById(R.id.tvIfsccode);
            tvbranchcode = (TextView) itemView.findViewById(R.id.tvbranchcode);
            tvAccounttype = (TextView) itemView.findViewById(R.id.tvAccounttype);
            tvbankaddress = (TextView) itemView.findViewById(R.id.tvbankaddress);
        }

        @Override
        public void onClick(View v) {

        }

    }


    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_bankdetails, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        final BankDetailsModel bankDetailsModel = listData.get(position);

        holder.tvBankname.setText(bankDetailsModel.getName());
        holder.tvAccountnumber.setText(bankDetailsModel.getAccount_no());
        holder.tvBranch.setText(bankDetailsModel.getBranch());
        holder.tvIfsccode.setText(bankDetailsModel.getIfsc_code());
        holder.tvbranchcode.setText(bankDetailsModel.getBranch_code());
        holder.tvAccounttype.setText(bankDetailsModel.getAccount_type());
        holder.tvbankaddress.setText(bankDetailsModel.getAddress());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


}