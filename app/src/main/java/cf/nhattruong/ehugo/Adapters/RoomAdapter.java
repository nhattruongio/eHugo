package cf.nhattruong.ehugo.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import org.apache.commons.lang3.StringUtils;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cf.nhattruong.ehugo.R;
import cf.nhattruong.ehugo.Models.RoomModel;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {

    ArrayList<RoomModel> roomArrayList;
    private OnRoomListener mOnRoomListener;

    public RoomAdapter(ArrayList arrayList, OnRoomListener onItemClickListener) {
        this.roomArrayList = arrayList;
        this.mOnRoomListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.show_room_adapter, viewGroup, false);
        return new RoomAdapter.MyViewHolder(itemView, mOnRoomListener);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        setFadeAnimation(holder.itemView);
        RoomModel room = roomArrayList.get(position);

        String name_room = StringUtils.abbreviate(room.getName_room(), 20);

        holder.txtNameRoom.setText("Tên:"+" "+ name_room);
        holder.txtStatus.setText("Trạng thái:"+" "+ room.getStatus_room());
        DecimalFormat df = new DecimalFormat("#,##0");
        df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.US));
        holder.txtPrice.setText("Giá:"+" "+ df.format(room.getPrice_room()) + " đ");
    }
    @Override
    public int getItemCount() {
        return roomArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtNameRoom, txtPrice,txtStatus;
        OnRoomListener onRoomListener;

        MyViewHolder(View itemView, OnRoomListener onRoomListener) {
            super(itemView);
            this.txtNameRoom=itemView.findViewById(R.id.txtRoom_Name);
            this.txtPrice = itemView.findViewById(R.id.txtPrice_Room);
            this.txtStatus = itemView.findViewById(R.id.txtStatus_Room);
            this.onRoomListener = onRoomListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRoomListener.onRoomClick(getAdapterPosition());
        }
    }
    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }

    public interface OnRoomListener {
        void onRoomClick(int position);
    }

}
