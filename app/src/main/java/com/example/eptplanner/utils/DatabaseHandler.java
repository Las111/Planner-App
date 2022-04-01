package com.example.eptplanner.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.eptplanner.mODEL.TODO_model;

import java.sql.SQLClientInfoException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class  DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION=1;
    private static final String NAME="TODODatabase";
    private static  final String TODO_TABLE="task";
    private static  final String ID="id";
    private static  final String TASK="task";
    private static  final String STATUS="status";
    private static final String CATEGORY="category";
    private static  final String DATE="date";
    private static  final String CREATE_TODO_TABLE="CREATE TABLE "+TODO_TABLE+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
            +TASK+" TEXT,"+STATUS + " INTEGER ,"+CATEGORY+" TEXT, "+DATE+" TEXT) " ;

    private SQLiteDatabase db;

    public DatabaseHandler(Context context){
        super(context,NAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TODO_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion , int newVersion){
        //Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS "+ TODO_TABLE);
        //Create tables again
        onCreate(db);
    }

    public void openDatabase(){
        db=this.getWritableDatabase();
    }

    public void InsertTask (TODO_model task){
        String tt =task.getTask(),b=task.getCategory(),d= task.getDate();
        //db.execSQL("INSERT INTO task(task,status,category,date) VALUES ('tt',0,'b','d')");
        db.execSQL("INSERT INTO task(task,status,category,date) VALUES ('"+tt+"',0,'"+b+"','"+d+"')");


    }

    @SuppressLint("Range")
    public List<TODO_model> getAllTasks(){
        List<TODO_model> taskList = new ArrayList<>();
        Cursor cur=null;
        db.beginTransaction();
        try{
            cur=db.query(TODO_TABLE,null,null,null,null,null,null,null);
            if(cur!=null){
                if(cur.moveToFirst()){
                    do{
                        TODO_model task = new TODO_model();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        task.setDate(cur.getString(cur.getColumnIndex(DATE)));
                        task.setCategory(cur.getString(cur.getColumnIndex(CATEGORY)));
                        taskList.add(task);
                    }while(cur.moveToNext());

                }
            }
        }
        finally{
            db.endTransaction();
            cur.close();
        }
        return taskList;

    }
    public void updateStatus(int id, int status){
        ContentValues cv= new ContentValues();
        cv.put(STATUS,status);
        db.update(TODO_TABLE,cv, ID+ "=?",new String[] {String.valueOf(id)} );
    }


    public void updateDate(int id, String date ){
        ContentValues cv= new ContentValues();
        cv.put(DATE,date);
        db.update(TODO_TABLE,cv, ID+ "=?",new String[] {String.valueOf(id)} );
    }
    public void updateCategory(int id, int category){
        ContentValues cv= new ContentValues();
        cv.put(CATEGORY,category);
        db.update(TODO_TABLE,cv, ID+ "=?",new String[] {String.valueOf(id)} );
    }

    public void updateTask(int id,String task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task);
        db.update(TODO_TABLE,cv,ID+"=?",new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE,ID+"=?",new String[]{String.valueOf(id)});
    }

}
