package com.orioton.pickacar.admin.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orioton.pickacar.R;
import com.squareup.picasso.Picasso;

public class AdminCarListViewHolder extends RecyclerView.ViewHolder {

    View carItemView;

    public AdminCarListViewHolder(@NonNull View itemView) {
        super(itemView);

        carItemView = itemView;

    }

    // set details to recycler view item
    public void setDetails(Context context, String brand, String model,  String color, String releasedYear, String passengers,String description,
                           String image,
                           String condition) {


        TextView modelView = carItemView.findViewById(R.id.car_list_model_txt);
        TextView brandView = carItemView.findViewById(R.id.car_list_brand_txt);
        TextView colorView = carItemView.findViewById(R.id.car_list_color_txt);
        TextView releasedYearView = carItemView.findViewById(R.id.car_list_released_year_txt);
        TextView passengersView = carItemView.findViewById(R.id.car_list_passengers_txt);
        TextView conditionView = carItemView.findViewById(R.id.car_list_condition_txt);
        TextView descriptionView = carItemView.findViewById(R.id.car_list_description_txt);
        ImageView imageView = carItemView.findViewById(R.id.car_list_image);


        // set data to the views
        modelView.setText(model);
        brandView.setText(brand);
        colorView.setText(color);
        conditionView.setText(condition);
        descriptionView.setText(description);
        releasedYearView.setText(releasedYear);
        passengersView.setText(passengers);
        Picasso.get().load(image).into(imageView);


        // item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carItemClickListener.onItemClick(v, getAdapterPosition());

            }
        });

        // item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                carItemClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });


    }

    private  AdminCarListViewHolder.ClickListener carItemClickListener;

    // interface to click
    public interface ClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(AdminCarListViewHolder.ClickListener clickListener){

      carItemClickListener = clickListener;

    }


}
