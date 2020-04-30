package com.orioton.pickacar.client;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orioton.pickacar.R;

class CarViewHolder extends RecyclerView.ViewHolder {

    ImageView carImage;
    TextView carName;

    public CarViewHolder(@NonNull View itemView) {
        super(itemView);

        carImage = itemView.findViewById(R.id.car_image_single);
        carName = itemView.findViewById(R.id.car_name_single);


    }
}
