package com.example.spiteful_reminder;


import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.Date;

public class NewReminder extends AppCompatActivity {

    public static String memo,date,time;
    public static String status = "pending";
    public static int hrs,min;
    public static FirebaseDatabase db;
    public static DatabaseReference reference;

    final int PERMISSION_REQUEST_CODE = 112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reminder);

        if (Build.VERSION.SDK_INT > 32) {
            if(!shouldShowRequestPermissionRationale("112")){
                getNotificationPermission();
            }
        }

        //Code to make the status bar black (Start)
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));
        //Code to make the status bar black (End)

        db = FirebaseDatabase.getInstance();
        reference = db.getReference("helper");


    }

        public void getNotificationPermission(){
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        }catch (Exception e){
            Toast.makeText(this, "Error!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void time (View view){
        Switch sw1 = (Switch) findViewById(R.id.sw1);
        if(sw1.isChecked()){
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(NewReminder.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    hrs = selectedHour;
                    min = selectedMinute;
                    time = selectedHour + ":" + selectedMinute;
                }
            }, hour, minute, true);//Yes 24 hour time
            mTimePicker.show();
        }
    }

    public void date (View view){
        Switch sw2 = (Switch) findViewById(R.id.sw2);
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
                date=selectedday + "/" + selectedmonth + "/" + selectedyear;
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
//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS)== PackageManager.PERMISSION_GRANTED){
//            Toast.makeText(this, "Permission Success", Toast.LENGTH_SHORT).show();
//        }
        //Toast.makeText(this, "Check", Toast.LENGTH_SHORT).show();
        EditText ed1 = (EditText) findViewById(R.id.ed1);
        memo = ed1.getText().toString();
        if(memo.length()==0){
            ed1.requestFocus();
            ed1.setError("This field cannot be empty");
        }
        else {
            helper uh = new helper(memo, time, date, status);

            reference.child(memo).setValue(uh).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    scheduleNotification(memo, time, date);
                    Toast.makeText(NewReminder.this, "Reminder Added!", Toast.LENGTH_SHORT).show();
                    memo = "";
                    time = "";
                    date = "";
                    Intent i = new Intent(NewReminder.this, MainActivity.class);
                    startActivity(i);
                }
            });
        }
    }

    private void scheduleNotification(String memo, String time, String date) {
        // Parse date and time strings to create a Calendar object
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date dateTime = dateFormat.parse(date + " " + time);
            calendar.setTime(dateTime);
            long wait = calendar.getTimeInMillis();
//            Toast.makeText(this, ""+wait, Toast.LENGTH_SHORT).show();

            // Use AlarmManager to schedule the notification

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent notificationIntent = new Intent(this,NotificationReceiver.class);
            notificationIntent.putExtra("memo", memo); // Pass the memo to the receiver
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wait , pendingIntent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

//    private void scheduleNotification(String memo, String date, String time) {
//        // Parse date and time strings to create a Calendar object
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
//        try {
//            Date dateTime = dateFormat.parse(NewReminder.date + " " + NewReminder.time);
//            calendar.setTime(dateTime);
////            calendar.set(Calendar.HOUR_OF_DAY,hrs);
////            calendar.set(Calendar.MINUTE,min);
////            calendar.set(Calendar.HOUR_OF_DAY,19);
//
//            // Use AlarmManager to schedule the notification
//            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//            // Generate a unique request code based on the memo
//            int requestCode = memo.hashCode(); // You can use a better way to generate a unique code
//
//            Intent notificationIntent = new Intent(this, NotificationReceiver.class);
//            notificationIntent.putExtra("memo", memo);
//
//            // Use a unique request code for PendingIntent
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
}