package com.zaidbinihtesham.seproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewContributionAdapter extends RecyclerView.Adapter<RecyclerViewContributionAdapter.MyViewHolder> {


    List<Contribution> ls;
    Context c;


    public RecyclerViewContributionAdapter(List<Contribution> ls, Context c) {
        this.ls = ls;
        this.c = c;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myview= LayoutInflater.from(c).inflate(R.layout.row1,parent,false);
        return new MyViewHolder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.title.setText(ls.get(i).getTitle());
        myViewHolder.contribution.setText(ls.get(i).getContribution());
        myViewHolder.username.setText(ls.get(i).getUsername());
    }



    @Override
    public int getItemCount() {
        return ls.size();
    }

    public void filterList(ArrayList<Contribution> filteredList) {
        ls = filteredList;
        notifyDataSetChanged();
    }


    public class MyViewHolder<V> extends RecyclerView.ViewHolder  {
        de.hdodenhof.circleimageview.CircleImageView image_to_show;
        TextView title;
        TextView contribution;
        TextView username;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title_get);
            contribution=itemView.findViewById(R.id.contribution_get);
            username=itemView.findViewById(R.id.name_get);
            //   date=itemView.findViewById(R.id.date_get);
            //  time=itemView.findViewById(R.id.time_get);

        }



    }
}
