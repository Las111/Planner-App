package com.example.eptplanner.utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.eptplanner.mODEL.TODO_model;
import com.example.eptplanner.mODEL.cat_model;

import java.util.ArrayList;
import java.util.List;

public class DB_cat extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String NAME="CATDatabase";
    private static  final String C_TABLE="cat";
    private static  final String ID="C_id";
    private static  final String CNAME="C_name";
    private static  final String DESCRI="C_desc";
    private static  final String CREATE_C_TABLE="CREATE TABLE "+C_TABLE+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
            CNAME +" TEXT,"+ DESCRI + " INTEGER ) " ;

    private SQLiteDatabase db;

    public DB_cat(Context context){
        super(context,NAME,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_C_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db,int oldVersion , int newVersion){
        //Drop the older tables
        db.execSQL("DROP TABLE IF EXISTS "+ C_TABLE);
        //Create tables again
        onCreate(db);
    }

    public void openDatabase(){
        db=this.getWritableDatabase();
    }

    public void Insert (cat_model cat){
        ContentValues cv= new ContentValues();
        cv.put(CNAME,cat.getC_name());
        cv.put(DESCRI,cat.getC_desc());
        db.insert(C_TABLE,null,cv);

    }

    @SuppressLint("Range")
    public List<cat_model> getAllCats(){
        List<cat_model> catList = new ArrayList<>();
        Cursor cur=null;
        db.beginTransaction();
        try{
            cur=db.query(C_TABLE,null,null,null,null,null,null,null);
            if(cur!=null){
                if(cur.moveToFirst()){
                    do{
                        cat_model cat = new cat_model();
                        cat.setC_id(cur.getInt(cur.getColumnIndex(ID)));
                        cat.setC_name(cur.getString(cur.getColumnIndex(CNAME)));
                        cat.setC_desc(cur.getString(cur.getColumnIndex(DESCRI)));

                        catList.add(cat);
                    }while(cur.moveToNext());

                }
            }
        }
        finally{
            db.endTransaction();
            cur.close();
        }
        return catList;

    }

    public void deleteCat(int id){
        db.delete(C_TABLE,ID+"=?",new String[]{String.valueOf(id)});
    }

    public void updatename(int id,String name){
        ContentValues cv = new ContentValues();
        cv.put(CNAME,name);
        db.update(C_TABLE,cv,ID+"=?",new String[]{String.valueOf(id)});
    }

    public void updatedesc(int id,String desc){
        ContentValues cv = new ContentValues();
        cv.put(DESCRI,desc);
        db.update(C_TABLE,cv,ID+"=?",new String[]{String.valueOf(id)});
    }


}
