package com.orioton.pickacar.admin.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.orioton.pickacar.R;
import com.orioton.pickacar.admin.fragments.AdminUsersFragment;
import com.orioton.pickacar.admin.model.AdminUserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdminUsersViewHolder extends RecyclerView.Adapter<AdminUsersViewHolder.MyHolder> {

    Context context;
    List<AdminUserModel> userList;

    // constructor


    public AdminUsersViewHolder(Context context, List<AdminUserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.layout_admin_user_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        // get data
        String image = userList.get(position).getImage();
        String name = userList.get(position).getName();
        final String email = userList.get(position).getEmail();

        // setData
        holder.users_name.setText(name);
        holder.users_email.setText(email);


        try{

            Picasso.get().load(image).placeholder(R.drawable.ic_action_face_yellow).into(holder.users_image);



        }
        catch (Exception e){


        }

        // handle item click

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, ""+ email, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // view holder class
    class MyHolder extends RecyclerView.ViewHolder{

        ImageView users_image;
        TextView users_name, users_email;




        public MyHolder(@NonNull View itemView) {
            super(itemView);


            // init views
            users_image = itemView.findViewById(R.id.user_list_image);
            users_name = itemView.findViewById(R.id.user_list_name);
            users_email = itemView.findViewById(R.id.user_list_email);

        }
    }
}
