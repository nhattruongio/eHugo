package cf.nhattruong.ehugo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cf.nhattruong.ehugo.R;
import cf.nhattruong.ehugo.Models.HouseModel;


public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.MyViewHolder> {

    private ArrayList<HouseModel> houseArrayList;
    private OnHouseListener mOnHouseListener;

    public HouseAdapter(ArrayList arrayList, OnHouseListener onItemClickListener) {
        this.houseArrayList = arrayList;
        this.mOnHouseListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.show_house_adapter, viewGroup, false);
        return new MyViewHolder(itemView, mOnHouseListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseAdapter.MyViewHolder holder, int position) {
        setFadeAnimation(holder.itemView);
        HouseModel house = houseArrayList.get(position);

        String name_house = StringUtils.abbreviate(house.getName_house(), 20);
        String quantity_room = StringUtils.abbreviate(house.getQuantity_house(), 20);
        String address_house = StringUtils.abbreviate(house.getAddress_house(), 20);

        holder.txtNameHouse.setText(name_house);
        holder.txtQuantity_room.setText(quantity_room);
        holder.txtAddress.setText(address_house);
        holder.txtCre_at.setText(house.getCreated_at());
    }

    @Override
    public int getItemCount() {
        return houseArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtNameHouse, txtAddress, txtQuantity_room, txtCre_at;
        HouseAdapter.OnHouseListener onHouseListener;
        ImageView imageView;

        MyViewHolder(View itemView, HouseAdapter.OnHouseListener onHouseListener) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.img_house);
            this.txtNameHouse = itemView.findViewById(R.id.txtHouse_Name);
            this.txtAddress = itemView.findViewById(R.id.txtAddress);
            this.txtQuantity_room = itemView.findViewById(R.id.txtQuantity_Room);
            this.txtCre_at = itemView.findViewById(R.id.txtCre_House);
            this.onHouseListener = onHouseListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onHouseListener.onHouseClick(getAdapterPosition());
        }
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    public interface OnHouseListener {
        void onHouseClick(int position);
    }
}
