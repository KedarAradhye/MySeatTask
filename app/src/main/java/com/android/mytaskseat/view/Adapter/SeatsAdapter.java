package com.android.mytaskseat.view.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.mytaskseat.Model.SeatMap;
import com.android.mytaskseat.R;
import com.android.mytaskseat.view.Activity.MainActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SeatsAdapter extends RecyclerView.Adapter<SeatsAdapter.ViewHolder> {

    private Context mContext;
    private List<SeatMap> mSeatMap = new ArrayList<>();
    private String mSeats;
    private String[] mSeat1;
    List<String> mAllSeats = new ArrayList<String>();

    public SeatsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_seat_layout, parent, false);
        SeatsAdapter.ViewHolder viewHolder = new SeatsAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Glide.with(mContext).load(mSeatMap.get(position).getSeatRow1()).into(holder.poster);
        mSeats = mSeatMap.get(position).getSeatRow();
        mAllSeats = Arrays.asList(mSeats.split(","));
        for(int i=0;i<mAllSeats.size();i++) {
            if (!mAllSeats.equals(0)) {
                String[] parts = mAllSeats.get(i).split("-");
                holder.mSeatName.setText(parts[2]);
            }else {
                holder.mSeatName.setText("");
            }
        }

    }

    @Override
    public int getItemCount() {
        return mSeatMap.size();
    }

    public void setSeats(List<SeatMap> seatMap) {
        mSeatMap = seatMap;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mSeatsImage;
        public TextView mSeatName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mSeatsImage = itemView.findViewById(R.id.iv_seat);
            mSeatName = itemView.findViewById(R.id.tv_seat_name);

        }
    }

}
