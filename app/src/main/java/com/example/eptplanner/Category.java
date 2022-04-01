package com.example.eptplanner;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.eptplanner.Adapter.TODOadapter;
import com.example.eptplanner.mODEL.TODO_model;
import com.example.eptplanner.mODEL.cat_model;
import com.example.eptplanner.utils.DB_cat;
import com.example.eptplanner.utils.DatabaseHandler;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class Category extends Myclass {
    private TextView date,name,description,prod;
    private TODOadapter tasksAdapter,tasksAdapter1,tasksAdapter2;
    private RecyclerView taskstoday,taskslater,test;
    private List<TODO_model> taskListtoday,taskListlater;
    private DatabaseHandler db;
    private DB_cat dbc;
    private List<cat_model> catList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category);
        getSupportActionBar().hide();
        name=findViewById(R.id.hello);
        prod=findViewById(R.id.productivity);


        Calendar calendrier = Calendar.getInstance();
        String cd= DateFormat.getDateInstance().format(calendrier.getTime());
        date=findViewById(R.id.titre);
        date.setText(cd);

        Bundle bundle= getIntent().getExtras();

        String k= (String)bundle.get("key");
        name.setText(k);

        dbc=new DB_cat(this);
        dbc.openDatabase();
        catList=dbc.getAllCats();
        description=findViewById(R.id.description);
        for(int i=0;i< catList.size();i++){
            if(k.equals(catList.get(i).getC_name())) description.setText(catList.get(i).getC_desc());
        }


        db= new DatabaseHandler(this);
        db.openDatabase();

        taskstoday=findViewById(R.id.tasks0);
        taskstoday.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter=new TODOadapter(this,db);
        taskstoday.setAdapter(tasksAdapter);

        taskslater=findViewById(R.id.tasks);
        taskslater.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter1=new TODOadapter(this,db);
        taskslater.setAdapter(tasksAdapter1);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(taskstoday);

        ItemTouchHelper itemTouchHelper1 = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter1));
        itemTouchHelper1.attachToRecyclerView(taskslater);

        taskListtoday=new ArrayList<TODO_model>();
        taskListlater=new ArrayList<TODO_model>();

        List<TODO_model> l = db.getAllTasks();

        for(int i=0;i<l.size();i++){
            if(k.equals(l.get(i).getCategory())&(cd.equals(l.get(i).getDate())))
                taskListtoday.add(l.get(i));
            if (k.equals(l.get(i).getCategory())& (cd.equals(l.get(i).getDate())==false))
                taskListlater.add(l.get(i));
        }
        Collections.reverse(taskListtoday);
        tasksAdapter.setTasks(taskListtoday);

        Collections.reverse(taskListlater);
        tasksAdapter1.setTasks(taskListlater);

        tasksAdapter2=new TODOadapter(this,db);
        test=new RecyclerView(this);
        test.setAdapter(tasksAdapter2);
        Collections.reverse(l);
        tasksAdapter2.setTasks(l);

        float n;
        if (l.size() != 0)
            n = (tasksAdapter2.nbTicC(k) / tasksAdapter2.nbC(k)) * 100;
        else n = 0;
        int s = Math.round(n);
        prod.setText(Integer.toString(s) + "%");


    }
    public void ma(){
        Calendar calendrier = Calendar.getInstance();
        String cd= DateFormat.getDateInstance().format(calendrier.getTime());
        taskListtoday=new ArrayList<TODO_model>();
        taskListlater=new ArrayList<TODO_model>();
        Bundle bundle= getIntent().getExtras();
        String k= (String)bundle.get("key");
        List<TODO_model> l = db.getAllTasks();

        for(int i=0;i<l.size();i++){
            if(k.equals(l.get(i).getCategory())&(cd.equals(l.get(i).getDate())))
                taskListtoday.add(l.get(i));
            if (k.equals(l.get(i).getCategory())& (cd.equals(l.get(i).getDate())==false))
                taskListlater.add(l.get(i));
        }
        Collections.reverse(taskListtoday);
        tasksAdapter.setTasks(taskListtoday);

        Collections.reverse(taskListlater);
        tasksAdapter1.setTasks(taskListlater);

        test.setAdapter(tasksAdapter2);
        Collections.reverse(l);
        tasksAdapter2.setTasks(l);

        float n;
        if (l.size() != 0)
            n = (tasksAdapter2.nbTicC(k) / tasksAdapter2.nbC(k)) * 100;
        else n = 0;
        int s = Math.round(n);
        prod.setText(Integer.toString(s) + "%");
    }
}