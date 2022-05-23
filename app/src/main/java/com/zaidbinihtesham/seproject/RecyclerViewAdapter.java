package com.zaidbinihtesham.seproject;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;



public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>  {


    List<Upload> ls;
    Context c;


    public RecyclerViewAdapter(List<Upload> ls, Context c) {
        this.ls = ls;
        this.c = c;
    }


    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview= LayoutInflater.from(c).inflate(R.layout.row,parent,false);
        return new MyViewHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final String[] get_title_from_recycler = {null};
        final String[] get_description_from_recycler = {null};
        final String[] get_category_from_recyler = {null};
        final String[] get_target_amount_from_recycler = {null};
        final String[] get_status_from_recycler={null};
        final String[] get_city_from_recycler={null};

        Upload uploadCurrent=ls.get(position);
        Picasso.with(c).load(uploadCurrent.getmImageUrl()).centerCrop().fit().into(holder.image_to_show);
        holder.Goal_title.setText(ls.get(position).getmName());
        holder.Goal_targetamount.setText(ls.get(position).getmTargetAmount()+" Rs/-");
        holder.Goal_category.setText(ls.get(position).getmCategory());
        holder.Goal_status.setText(ls.get(position).getmStatus());
        holder.Goal_city.setText(ls.get(position).getmCity());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = holder.getAdapterPosition();
                if (pos!=RecyclerView.NO_POSITION){
                    get_title_from_recycler[0] = ls.get(pos).getmName();
                    get_description_from_recycler[0] =ls.get(pos).getmDescription();
                    get_category_from_recyler[0] =ls.get(pos).getmCategory();
                    get_target_amount_from_recycler[0] =ls.get(pos).getmTargetAmount();
                    get_status_from_recycler[0]=ls.get(pos).getmStatus();
                    get_city_from_recycler[0]=ls.get(pos).getmCity();

                    Intent intent=new Intent(c,RecyclerViewSpecificItem.class);
                    intent.putExtra("Title",get_title_from_recycler[0]);
                    intent.putExtra("Description",get_description_from_recycler[0]);
                    intent.putExtra("Category",get_category_from_recyler[0]);
                    intent.putExtra("TargetAmount",get_target_amount_from_recycler[0]);
                    intent.putExtra("Status",get_status_from_recycler[0]);
                    intent.putExtra("City", get_city_from_recycler[0]);

                    v.getContext().startActivity(intent);
                }



            }
        });


    }



    @Override
    public int getItemCount() {
        return ls.size();
    }

    public void filterList(ArrayList<Upload> filteredList) {
        ls = filteredList;
        notifyDataSetChanged();
    }


    public class MyViewHolder<V> extends RecyclerView.ViewHolder  {
        de.hdodenhof.circleimageview.CircleImageView image_to_show;
        TextView Goal_title;
        TextView Goal_targetamount;
        TextView Goal_category;
        TextView Goal_status;
        TextView Goal_city;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image_to_show=itemView.findViewById(R.id.image_to_show);
            Goal_title=itemView.findViewById(R.id.title);
            Goal_targetamount=itemView.findViewById(R.id.targetamount);
            Goal_category=itemView.findViewById(R.id.category);
            Goal_status=itemView.findViewById(R.id.status);
            Goal_city=itemView.findViewById(R.id.city);

        }



    }
}