package com.example.eptplanner.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.eptplanner.AddNewTask;
import com.example.eptplanner.Myclass;
import com.example.eptplanner.R;
import com.example.eptplanner.mODEL.TODO_model;
import com.example.eptplanner.utils.DatabaseHandler;

import java.util.Collections;
import java.util.List;

import static java.lang.Thread.sleep;

public class TODOadapter extends RecyclerView.Adapter<TODOadapter.ViewHolder> {

    public List<TODO_model> TODOlist;
    private Myclass activity;
    private DatabaseHandler db;



    public TODOadapter(Myclass cat, DatabaseHandler db){
        this.activity=cat;
        this.db=db;
    }



    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType){
        View itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.todays,parent,false);
        return new ViewHolder(itemView);
    }
    public void onBindViewHolder(ViewHolder holder, int position){
        db.openDatabase();
        TODO_model item = TODOlist.get(position);
        holder.task.setText(item.getTask());
        holder.task.setChecked(tobool(item.getStatus()));
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){

                    db.updateStatus(item.getId(),1);

                }
                else
                {
                    db.updateStatus(item.getId(),0);

                }
                //MA ma = new MA();
                //ma.start();
                activity.ma();



            }
        });


    }

    public int getItemCount(){
        int a=0;
        if (TODOlist!=null)
             a = TODOlist.size();
        return a ;
    }

    public float nbTic(){
        int s=0;
        for ( int i=0;i<this.getItemCount();i++){
            if (TODOlist.get(i).getStatus()==1) s++;
        }
        return s;
    }

    public float nbTicC(String c){
        int s=0;
        for ( int i=0;i<this.getItemCount();i++){
            if ((TODOlist.get(i).getStatus()==1)&(c.equals(TODOlist.get(i).getCategory()))) s++;
        }
        return s;
    }

    public float nbC(String c ){
        int s=0;
        for ( int i=0 ;i<this.getItemCount();i++){
            if (c.equals(TODOlist.get(i).getCategory())) s++;
        }
        return s ;
    }



    private boolean tobool(int n){
        return n!=0;
    }

    public void setTasks(List<TODO_model> todolist){
        this.TODOlist=todolist;
        //notifyDataSetChanged();
    }

    public Context getContext(){
        return activity;
    }


    public void deleteItem(int position) {
        TODO_model item = TODOlist.get(position);
        db.deleteTask(item.getId());
        TODOlist.remove(position);
        notifyItemRemoved(position);
        activity.ma();
    }

    public void editItem(int position){
        TODO_model item = TODOlist.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        bundle.putString("date",item.getDate());
        bundle.putString("category",item.getCategory());
        bundle.putInt("status",item.getStatus());
        AddNewTask fragment = new AddNewTask();
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
        deleteItem(position);
        activity.ma();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view){
            super(view);
            task=view.findViewById(R.id.checkbox);
        }
    }


    /*class MA extends Thread{
        @Override
        public void run() {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //notifyDataSetChanged();
                    activity.ma();
                }
            });

        }
    }*/

}
