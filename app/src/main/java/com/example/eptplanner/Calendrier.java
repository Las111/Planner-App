package com.example.eptplanner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.eptplanner.Adapter.TODOadapter;
import com.example.eptplanner.mODEL.TODO_model;
import com.example.eptplanner.utils.DatabaseHandler;

import java.security.AccessControlContext;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static java.security.AccessController.getContext;

public class Calendrier extends Myclass {
    private TextView date;
    private DatePicker datePicker;
    private TODOadapter tasksAdapter;
    private RecyclerView taskstoday;
    private List<TODO_model> taskList;
    private DatabaseHandler db;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendrier);
        getSupportActionBar().hide();

        Calendar calendrier = Calendar.getInstance();
        String cd= DateFormat.getDateInstance().format(calendrier.getTime());
        date=findViewById(R.id.titre);
        date.setText(cd);

        db= new DatabaseHandler(this);
        db.openDatabase();

        taskstoday=findViewById(R.id.tasks);
        taskstoday.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter=new TODOadapter(this,db);
        taskstoday.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(taskstoday);

        taskList=new ArrayList<TODO_model>();
        final String[] td = {cd};
        TextView t = findViewById(R.id.test);


        datePicker=findViewById(R.id.datepicker);
        datePicker.init(calendrier.get(Calendar.YEAR), calendrier.get(Calendar.MONTH), calendrier.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendrier.set(Calendar.YEAR,year);
                calendrier.set(Calendar.MONTH,month);
                calendrier.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                td[0] = DateFormat.getDateInstance().format(calendrier.getTime());
                List<TODO_model> l = db.getAllTasks();
                taskList=new ArrayList<TODO_model>();
                for(int i=0;i<l.size();i++){
                    if(td[0].equals(l.get(i).getDate()))
                        taskList.add(l.get(i));

                }
                Collections.reverse(taskList);
                tasksAdapter.setTasks(taskList);
                tasksAdapter.notifyDataSetChanged();
                t.setText("Tasks for "+td[0]);

            }
        });

        t.setText("Tasks for "+td[0]);



        List<TODO_model> l = db.getAllTasks();

        for(int i=0;i<l.size();i++){
            if(td[0].equals(l.get(i).getDate()))
                taskList.add(l.get(i));

        }
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
    }


}