package com.orioton.pickacar.admin;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orioton.pickacar.R;
import com.squareup.picasso.Picasso;

public class CarListViewHolder extends RecyclerView.ViewHolder {

    View carItemView;

    public CarListViewHolder(@NonNull View itemView) {
        super(itemView);

        carItemView = itemView;

    }

    // set details to recycler view item
    public void setDetails(Context context, String brand, String model, String image) {


        TextView modelView = carItemView.findViewById(R.id.car_list_model_txt);
        TextView brandView = carItemView.findViewById(R.id.car_list_brand_txt);
        ImageView imageView = carItemView.findViewById(R.id.car_list_image);


        // set data to the views
        modelView.setText(model);
        brandView.setText(brand);
        Picasso.get().load(image).into(imageView);


    }
}
