package com.orioton.pickacar.admin.adapters;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.orioton.pickacar.R;
public class AdminPackageListViewHolder extends RecyclerView.ViewHolder {


    View packageItemView;

    public AdminPackageListViewHolder(@NonNull View itemView) {
        super(itemView);
        packageItemView = itemView;
    }


    public void setDetails(Context context, String packageName, Double packagePrice, Double packagePricePerKm, Double kilometers) {

        TextView packageNameView = packageItemView.findViewById(R.id.admin_plan1);
        TextView packagePriceView = packageItemView.findViewById(R.id.admin_plan1_price);
        TextView packagePerPriceView = packageItemView.findViewById(R.id.admin_plan1_km_price);
        TextView packageKmView = packageItemView.findViewById(R.id.admin_plan1_km);


        // set data to the views
        packageNameView.setText(packageName);
        packagePriceView.setText(String.valueOf(packagePrice));
        packagePerPriceView.setText(String.valueOf(packagePricePerKm));
        packageKmView.setText(String.valueOf(kilometers));


        // item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packageItemClickListener.onItemClick(v, getAdapterPosition());

            }
        });

        // item long click
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                packageItemClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            }
        });


    }

    private AdminPackageListViewHolder.ClickListener packageItemClickListener;


    // interface to click
    public interface ClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }


    public void setOnClickListener(AdminPackageListViewHolder.ClickListener clickListener) {

        packageItemClickListener = clickListener;

    }
}
