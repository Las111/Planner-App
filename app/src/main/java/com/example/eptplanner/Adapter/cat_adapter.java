package com.example.eptplanner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eptplanner.MainActivity;
import com.example.eptplanner.R;
import com.example.eptplanner.mODEL.TODO_model;
import com.example.eptplanner.mODEL.cat_model;
import com.example.eptplanner.utils.DB_cat;
import com.example.eptplanner.utils.DatabaseHandler;

import java.util.List;

public class cat_adapter extends RecyclerView.Adapter<cat_adapter.ViewHolder> {
    public List<cat_model> CATlist;
    public MainActivity activity;
    private DB_cat db;

    public cat_adapter(MainActivity activity,DB_cat db){
        this.activity=activity;
        this.db=db;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder{
         TextView task;
        ViewHolder(View view){
            super(view);
            task=view.findViewById(R.id.cat);
        }
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType){
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.cat,parent,false);
        return new cat_adapter.ViewHolder(itemView);
    }

    public void onBindViewHolder(cat_adapter.ViewHolder holder, int position){
        db.openDatabase();
        cat_model item = CATlist.get(position);
        holder.task.setText(item.getC_name());

    }

    @Override
    public int getItemCount() {
        return CATlist.size();
    }

    public Context getContext(){
        return activity;
    }

    public void deleteItem(int position) {
        cat_model item = CATlist.get(position);
        db.deleteCat(item.getC_id());
        CATlist.remove(position);
        notifyItemRemoved(position);
    }

    public void setCat (List<cat_model> CATlist){
        this.CATlist=CATlist;
        notifyDataSetChanged();
    }


}
