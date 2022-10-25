package com.example.pochedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.util.ArrayList;

public class poche_RecyclerViewAdapter extends RecyclerView.Adapter<poche_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<pochemon> pochemon;

    public poche_RecyclerViewAdapter(Context context, ArrayList<pochemon> pochemon){
        this.context = context;
        this.pochemon = pochemon;
    }

    @NonNull
    @Override
    public poche_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the layout (give the looks to the rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.pochedex_row, parent, false);
        return new poche_RecyclerViewAdapter.MyViewHolder(view);
    }

    private void setBgdColor(pochemon pochemon, poche_RecyclerViewAdapter.MyViewHolder holder){
        try {
            if(String.valueOf(pochemon.getAttributes().get(0)) == "Grass"){
                holder.rowLayout.setBackgroundResource(R.color.grass);
            } else if (String.valueOf(pochemon.getAttributes().get(0)) == "Fire") {
                holder.rowLayout.setBackgroundResource(R.color.fire);
            } else if(String.valueOf(pochemon.getAttributes().get(0)) == "Water") {
                holder.rowLayout.setBackgroundResource(R.color.water);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull poche_RecyclerViewAdapter.MyViewHolder holder, int position) {
        //Assign values to the views created in pochedex_row based on the position of recycler view
        holder.pocheNumber.setText("#" + Integer.toString(pochemon.get(position).getNumber()));
        holder.pocheName.setText(pochemon.get(position).getName());
        holder.rowImg.setImageResource(pochemon.get(position).getImage());
        setBgdColor(pochemon.get(position), holder);
    }

    @Override
    public int getItemCount() {
        //Return the number of items displayed
        return pochemon.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //Get views form pochedex_row
        //Similar to onCreate

        ImageView rowImg;
        TextView pocheNumber;
        TextView pocheName;
        ConstraintLayout rowLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rowImg = itemView.findViewById(R.id.item_img);
            pocheNumber = itemView.findViewById(R.id.item_number);
            pocheName = itemView.findViewById(R.id.item_name);
            rowLayout = itemView.findViewById(R.id.pocheBgd);
        }
    }
}
