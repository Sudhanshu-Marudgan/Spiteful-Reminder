package com.example.spiteful_reminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class NewReminder extends AppCompatActivity {

    public static String memo,date,time;
    Switch sw1 = (Switch) findViewById(R.id.sw1);
    Switch sw2 = (Switch) findViewById(R.id.sw2);
    FirebaseDatabase db;
    DatabaseReference reference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        //Code to make the status bar black (Start)
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));
        //Code to make the status bar black (End)

    }

    public void time (View view){
        if(sw1.isChecked()){
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(NewReminder.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    time = selectedHour + ":" + selectedMinute;
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.show();
        }
    }

    public void date (View view){
        if(sw2.isChecked()){
            Calendar mcurrentDate = Calendar.getInstance();
            int mYear = mcurrentDate.get(Calendar.YEAR);
            int mMonth = mcurrentDate.get(Calendar.MONTH);
            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker;
            mDatePicker = new DatePickerDialog(NewReminder.this, new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                    /*      Your code   to get date and time    */
                    selectedmonth = selectedmonth + 1;
                date="" + selectedday + "/" + selectedmonth + "/" + selectedyear;
                }
            }, mYear, mMonth, mDay);
            mDatePicker.show();
        }
    }

    public void category (View view){
        Intent i = new Intent(NewReminder.this, Category.class);
        startActivity(i);
    }

    public void cancel (View view) {
        Intent i = new Intent(NewReminder.this,MainActivity.class);
        startActivity(i);
    }

    public void save (View view){
        EditText ed1 = (EditText) findViewById(R.id.ed1);
        memo = ed1.getText().toString();
        if(memo.length()==0){
            ed1.requestFocus();
            ed1.setError("This field cannot be empty1");
        }
        if(time.length()==0){
            sw1.requestFocus();
            sw1.setError("This field cannot be empty1");
        }
        if(date.length()==0){
            sw2.requestFocus();
            sw2.setError("This field cannot be empty1");
        }
        else{
            helper uh = new helper(memo,time,date);
            db=FirebaseDatabase.getInstance();
            reference = db.getReference("helper");
            reference.child(memo).setValue(uh).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    memo = "";
                    time = "";
                    date = "";
                }
            });
            Toast.makeText(this, "Reminder Added!", Toast.LENGTH_SHORT).show();
        }
    }
}