package com.example.spiteful_reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class Navigation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        //Code to make the status bar black (Start)
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.black));
        //Code to make the status bar black (End)
    }

    public void All (View view){
        Intent i = new Intent(this, All_Notifications.class);
        startActivity(i);
    }

    public void Reminders (View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    public void Completed (View view){
        Intent i = new Intent(this, Completed.class);
        startActivity(i);
    }

    public void Signin (View view){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}