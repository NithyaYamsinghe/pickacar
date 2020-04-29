package com.orioton.pickacar.driver;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orioton.pickacar.R;

class JourneyLisitViewHolder extends RecyclerView.ViewHolder  {


    View journeyItemView;

    public JourneyLisitViewHolder(@NonNull View itemView) {
        super(itemView);

        journeyItemView = itemView;

    }

    // set details to recycler view item
    public void setDetails(Context context, String userId, String username, String phone,  String date,  String location, String destination, String passengers,String status,
                           String price) {

        // TextView userIdView = journeyItemView.findViewById(R.id.dj_tv_driver_userId);
        TextView usernameView = journeyItemView.findViewById(R.id.dj_tv_driver_username);
        TextView phoneView = journeyItemView.findViewById(R.id.dj_tv_driver_phone);
        TextView dateView = journeyItemView.findViewById(R.id.dj_tv_driver_date);
        TextView locationView = journeyItemView.findViewById(R.id.dj_tv_driver_location);
        TextView destinationView = journeyItemView.findViewById(R.id.dj_tv_driver_destination);
        TextView passengerView = journeyItemView.findViewById(R.id.dj_tv_driver_passengers);
        TextView statusView = journeyItemView.findViewById(R.id.dj_tv_driver_status);
        TextView priceView = journeyItemView.findViewById(R.id.dj_tv_driver_price);



        // set data to the views
        //userIdView.setText(userId);
        usernameView.setText(username);
        phoneView.setText(phone);
        dateView.setText(date);
        locationView.setText(location);
        destinationView.setText(destination);
        passengerView.setText(passengers);
        statusView.setText(status);
        priceView.setText(price);


        // item click
//        itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                carItemClickListener.onItemClick(v, getAdapterPosition());
//
//            }
//        });

        // item long click
//        itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                carItemClickListener.onItemLongClick(v, getAdapterPosition());
//                return true;
//            }
//        });


    }

    //  private  JourneyListViewHolder.ClickListener carItemClickListener;

    // interface to click
//    public interface ClickListener {
//        void onItemClick(View view, int position);
//        void onItemLongClick(View view, int position);
//    }
//
//    public void setOnClickListener(JourneyLisitVewHolder.ClickListener clickListener){
//
//        journeyItemClickListener = clickListener;
//
//    }
}


