package cf.nhattruong.ehugo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cf.nhattruong.ehugo.Models.CustomerModel;
import cf.nhattruong.ehugo.R;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder>{
    private ArrayList<CustomerModel> customerArrayList;
    private CustomerAdapter.OnCustomerListener mOnCustomerListener;

    public CustomerAdapter(ArrayList arrayList,OnCustomerListener onItemClickListener) {
        this.customerArrayList = arrayList;
        this.mOnCustomerListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CustomerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate( R.layout.list_customer_adapter, viewGroup, false);
        return new CustomerAdapter.MyViewHolder(itemView, mOnCustomerListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        setFadeAnimation(holder.itemView);
        CustomerModel customer = customerArrayList.get(position);
        String name = StringUtils.abbreviate(customer.getName(), 20);
        holder.txtNameCustomer.setText(name);
        holder.txtTel_Customer.setText(customer.getTel());
    }

    @Override
    public int getItemCount() {
        return customerArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtNameCustomer, txtTel_Customer;
        OnCustomerListener onCustomerListener;
        MyViewHolder(View itemView,OnCustomerListener onCustomerListener) {
            super(itemView);
            this.txtNameCustomer = itemView.findViewById(R.id.txt_name_customer);
            this.txtTel_Customer = itemView.findViewById(R.id.txt_tel_customer);
            this.onCustomerListener = onCustomerListener;
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            onCustomerListener.onCustomerClick(getAdapterPosition());
        }
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    public interface OnCustomerListener {
        void onCustomerClick(int position);
    }
}
