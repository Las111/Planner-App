package com.example.eptplanner;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.eptplanner.mODEL.TODO_model;
import com.example.eptplanner.mODEL.cat_model;
import com.example.eptplanner.utils.DB_cat;
import com.example.eptplanner.utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AddNewTask extends BottomSheetDialogFragment  {

    public static final String TAG ="ActionBottomDialog";
    private EditText newTaskText,newTaskDate;
    private Spinner spin ;
    private Button newTaskSaveButton;
    private DatabaseHandler db;
    private DB_cat dbc;
    private List<cat_model> catList;

    public static  AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }
    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup contrainer ,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.newtask,contrainer,false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view ;
    }

    @Override
    public  void onViewCreated(View view , Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
        newTaskText=getView().findViewById(R.id.text);
        newTaskSaveButton=getView().findViewById(R.id.tb);
        newTaskDate=getView().findViewById(R.id.text2);
        newTaskSaveButton.setEnabled(false);
        Calendar calendar= Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);

                String cd= DateFormat.getDateInstance().format(calendar.getTime());
                newTaskDate.setText(cd);
            }

        };
        newTaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = AddNewTask.this.getContext();
                new DatePickerDialog(context,date,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        spin=getView().findViewById(R.id.text3);
        final String[] itemValue = new String[1];

        List<String> val = new ArrayList<>();

        //3abi il val bil names mta3 cat :
        dbc=new DB_cat(getActivity());
        dbc.openDatabase();
        catList=dbc.getAllCats();
        for(int i=0;i< catList.size();i++){
            val.add(catList.get(i).getC_name());
        }

        ArrayAdapter<String> spinAdapter =new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,val);
        spin.setAdapter(spinAdapter);


        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemValue[0] = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(), itemValue[0],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        db=new DatabaseHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle !=null){
            isUpdate=true;
            String task = bundle.getString("task");
            String dat = bundle.getString("date");

            newTaskText.setText(task);
            newTaskDate.setText(dat);


            if((task.length()>0)&(dat.length()>0)&(itemValue[0].length()>0)) {
                newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.back));
                newTaskSaveButton.setEnabled(true);
            }
        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if ((s.length()>0)&(newTaskDate.getText().length()>0)){
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.back));
                }else{
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        newTaskDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if ((s.length()>0)&(newTaskText.getText().length()>0)){
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(),R.color.back));
                }else{
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String text= newTaskText.getText().toString();
                String date= newTaskDate.getText().toString();
                String category=itemValue[0];
                if(finalIsUpdate){
                    db.updateTask(bundle.getInt("id"), text);
                }else{
                    TODO_model task = new TODO_model();
                    task.setTask(text);
                    task.setDate(date);
                    task.setCategory(category);
                    db.InsertTask(task);

                }
                dismiss();
            }
        } );



    }
  @Override
    public void onDismiss(@NonNull DialogInterface dialog){
      Activity activity = getActivity();
      if(activity instanceof DialogCloseListener){
          ((DialogCloseListener)activity).handleDialogClose(dialog);

      }
  }



}
