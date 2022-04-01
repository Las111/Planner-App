package com.example.eptplanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.icu.number.Precision;
import android.os.Bundle;

import android.content.Intent;
import android.text.Layout;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.eptplanner.Adapter.TODOadapter;
import com.example.eptplanner.Adapter.cat_adapter;
import com.example.eptplanner.mODEL.TODO_model;
import com.example.eptplanner.mODEL.cat_model;
import com.example.eptplanner.utils.DB_cat;
import com.example.eptplanner.utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Myclass implements DialogCloseListener {

    private RecyclerView tasksRecyclerView,CatRecyclerView;
    private TODOadapter tasksAdapter;
    private cat_adapter CatAdapter;
    private FloatingActionButton fab,tab;
    private TextView txt,date,cab;
    private List<TODO_model> taskList;
    private List<cat_model> catList;
    private DatabaseHandler db;
    private DB_cat DBC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        db= new DatabaseHandler(this);
        db.openDatabase();

        DBC=new DB_cat(this);
        DBC.openDatabase();


        tasksRecyclerView=findViewById(R.id.tasks);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter=new TODOadapter(this,db);
        tasksRecyclerView.setAdapter(tasksAdapter);

        CatRecyclerView=findViewById(R.id.cat);
        CatRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, true));
        CatAdapter= new cat_adapter(this,DBC);
        CatRecyclerView.setAdapter(CatAdapter);


        fab=findViewById(R.id.btn);
        tab=findViewById(R.id.tout);
        txt=findViewById(R.id.productivity);
        cab=findViewById(R.id.newC);



        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        ItemTouchHelper itemTouchHelper1 = new
                ItemTouchHelper(new RecyclerItemToucherHelperCat(CatAdapter,tasksAdapter));
        itemTouchHelper1.attachToRecyclerView(CatRecyclerView);

        taskList= db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);

        catList=DBC.getAllCats();
        Collections.reverse(catList);
        CatAdapter.setCat(catList);

        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        cab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewC.newInstance().show(getSupportFragmentManager(), AddNewC.TAG);
            }
        });

        tab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCal();
            }
        });




        float n;
        if (tasksAdapter.getItemCount() != 0)
            n = (tasksAdapter.nbTic() / (float)tasksAdapter.getItemCount()) * 100;
        else n = 0;
        int s = Math.round(n);
        txt.setText(Integer.toString(s) + "%");


        Calendar calendrier = Calendar.getInstance();
        String cd= DateFormat.getDateInstance().format(calendrier.getTime());
        date=findViewById(R.id.titre);
        date.setText(cd);

    }


    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
        float n;
        if (tasksAdapter.getItemCount() != 0)
            n = (tasksAdapter.nbTic() / (float)tasksAdapter.getItemCount()) * 100;
        else n = 0;
        int s = Math.round(n);
        txt.setText(Integer.toString(s) + "%");
    }


    @Override
    public void handleDialogCloseC(DialogInterface dialog) {
        catList=DBC.getAllCats();
        Collections.reverse(catList);
        CatAdapter.setCat(catList);
        CatAdapter.notifyDataSetChanged();
    }

    public void OpenCal (){
        Intent intent = new Intent(this, Calendrier.class);
        startActivity(intent);
    }


    public void ma (){
        List<TODO_model> l = db.getAllTasks();

        int sa=0;
        for ( int i=0;i<l.size();i++){
            if (l.get(i).getStatus()==1) sa++;
        }

        float n;
        if (l.size() != 0)
            n = (sa / (float)l.size()) * 100;
        else n = 0;
        int s = Math.round(n);
        txt.setText(Integer.toString(s) + "%");
    }

    public void mlc(){
        catList=DBC.getAllCats();
        Collections.reverse(catList);
        CatAdapter.setCat(catList);
        CatAdapter.notifyDataSetChanged();
    }
    @Override
   protected void onResume() {
        super.onResume();
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
        float n;
        if (tasksAdapter.getItemCount() != 0)
            n = (tasksAdapter.nbTic() / (float)tasksAdapter.getItemCount()) * 100;
        else n = 0;
        int s = Math.round(n);
        txt.setText(Integer.toString(s) + "%");
    }
}